package com.abc.exprezo.requirements.service;

import com.abc.exprezo.requirements.domain.Requirement;
import com.abc.exprezo.requirements.domain.RequirementStatus;
import com.abc.exprezo.requirements.repository.RequirementRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class RequirementService {

    private final RequirementRepository repo;

    @Value("${app.storage.root}")
    private String storageRoot;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    public RequirementService(RequirementRepository repo) {
        this.repo = repo;
    }

    // =========================
    // Lecturas
    // =========================

    /** Lista global con filtro opcional por estatus */
    public List<Requirement> list(RequirementStatus status) {
        if (status == null) return repo.findAll();
        return repo.findByStatusOrderByCreatedAtDesc(status);
    }

    /** Obtiene un documento por id */
    public Requirement get(String id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("No existe el requerimiento"));
    }

    /** 🔹 NUEVO: Lista por folio (para la pantalla de revisión) */
    public List<Requirement> listByFolio(String folio) {
        if (!StringUtils.hasText(folio)) throw new IllegalArgumentException("folio requerido");
        return repo.findByFolioOrderByCreatedAtAsc(folio);
    }

    // =========================
    // Creaciones
    // =========================

    /** Alta clásica (sin forzar folio) */
    public Requirement create(String title, String description, MultipartFile file) throws IOException {
        return createWithFolio(title, description, file, null);
    }

    /** 🔹 NUEVO: Alta asignando folio (si no viene, se genera) */
    public Requirement createWithFolio(String title,
                                       String description,
                                       MultipartFile file,
                                       String folio) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Archivo requerido");

        // Dir base de almacenamiento
        Path root = Paths.get(storageRoot).toAbsolutePath().normalize();
        Files.createDirectories(root);

        String ext = getExtension(file.getOriginalFilename());
        String effectiveFolio = (StringUtils.hasText(folio)) ? folio : genFolio(UUID.randomUUID().toString().substring(0, 8));

        // Nombre físico para evitar colisiones: <folio>-<uniq>.<ext>
        String uniq = UUID.randomUUID().toString().substring(0, 8);
        String storedName = effectiveFolio + "-" + uniq + (ext.isBlank() ? "" : "." + ext);

        Path target = root.resolve(storedName).normalize();
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        Requirement req = new Requirement();
        req.setFolio(effectiveFolio);
        req.setTitle(title);
        req.setDescription(description);
        req.setStatus(RequirementStatus.PENDING);
        req.setOriginalFilename(file.getOriginalFilename());
        req.setContentType(file.getContentType());
        req.setSizeBytes(file.getSize());
        req.setStoragePath(root.relativize(target).toString().replace("\\", "/"));
        // reviewDecision / reviewReason inician nulos

        return repo.save(req);
    }

    // =========================
    // Actualizaciones
    // =========================

    /** Patch de campos básicos */
    public Requirement update(String id, String title, String description, RequirementStatus status) {
        Requirement db = get(id);
        if (title != null) db.setTitle(title);
        if (description != null) db.setDescription(description);
        if (status != null) db.setStatus(status);
        return repo.save(db);
    }

    /** 🔹 NUEVO: Registrar decisión por documento (aprobado/rechazado + razón) */
    public Requirement setReview(String id, String decision, String reason) {
        if (!StringUtils.hasText(id)) throw new IllegalArgumentException("id requerido");

        String normalized = null;
        if (decision != null) {
            String d = decision.trim().toLowerCase();
            if (!d.equals("aprobado") && !d.equals("rechazado")) {
                throw new IllegalArgumentException("decision debe ser 'aprobado' o 'rechazado'");
            }
            normalized = d;
        }

        Requirement db = get(id);
        db.setReviewDecision(normalized);                      // "aprobado" | "rechazado" | null
        db.setReviewReason(reason == null ? "" : reason.trim());

        // Nota: aquí NO cambiamos db.setStatus(...) para no romper tu lógica por folio.
        // Si quieres, se puede mapear decisión->status del documento en un paso posterior.

        return repo.save(db);
    }

    // =========================
    // Eliminación / Descarga
    // =========================

    public void delete(String id) throws IOException {
        Requirement db = get(id);
        if (StringUtils.hasText(db.getStoragePath())) {
            Path root = Paths.get(storageRoot).toAbsolutePath().normalize();
            Path file = root.resolve(db.getStoragePath()).normalize();
            Files.deleteIfExists(file);
        }
        repo.deleteById(id);
    }

    public PathResource download(String id) {
        Requirement db = get(id);
        Path root = Paths.get(storageRoot).toAbsolutePath().normalize();
        Path file = root.resolve(db.getStoragePath()).normalize();
        return new PathResource(file);
    }

    // =========================
    // Helpers
    // =========================

    private static String getExtension(String name) {
        if (!StringUtils.hasText(name)) return "";
        int i = name.lastIndexOf('.');
        return (i < 0) ? "" : name.substring(i + 1);
    }

    /** Genera un folio base REQ-YYYYMMDD-HHMMSS-<suffix> único */
    private String genFolio(String suffix) {
        String base = "REQ-" + LocalDateTime.now().format(FMT);
        String folio = base + "-" + (suffix == null ? UUID.randomUUID().toString().substring(0, 8) : suffix);
        // Si ya existiera, regeneramos (muy raro, pero por seguridad)
        while (repo.existsByFolio(folio)) {
            folio = base + "-" + UUID.randomUUID().toString().substring(0, 8);
        }
        return folio;
    }
}