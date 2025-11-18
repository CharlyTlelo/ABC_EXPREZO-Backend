package com.abc.exprezo.requirements.service;

import com.abc.exprezo.requirements.model.Requirement;
import com.abc.exprezo.requirements.model.RequirementStatus;
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
import java.time.Instant;

@Service
public class RequirementService {

    private final RequirementRepository repo;

    @Value("${app.storage.root}")
    private String storageRoot;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd");


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

    /**
     * Alta de requerimiento.
     * - Genera folio tipo REQ-YYYYMMDD-LNN (L = letra A..Z, NN = 00..99)
     * - Estatus inicial: PENDING
     * - Archivo es opcional (por si lo mandas desde Postman)
     */
    public Requirement create(String title, String description, MultipartFile file) throws IOException {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("title requerido");
        }

        Requirement r = new Requirement();
        r.setTitle(title);
        r.setDescription(description);
        r.setStatus(RequirementStatus.PENDING);
        r.setCreatedAt(Instant.now());
        r.setUpdatedAt(Instant.now());



        // Folio autogenerado
        String folio = nextFolioForToday();
        int guard = 0;
        while (repo.existsByFolio(folio) && guard++ < 5) {
            folio = nextFolioForToday();
        }
        r.setFolio(folio);

        // Si viene archivo, se guarda y se llenan metadatos (opcional)
        if (file != null && !file.isEmpty()) {
            String originalName = StringUtils.cleanPath(
                    Objects.requireNonNull(file.getOriginalFilename())
            );
            String ext = getExtension(originalName);

            String storageName = folio + (ext.isEmpty() ? "" : "." + ext);

            Path root = Paths.get(storageRoot).toAbsolutePath().normalize();
            Files.createDirectories(root);
            Path dest = root.resolve(storageName).normalize();
            Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

            r.setOriginalFilename(originalName);
            r.setContentType(file.getContentType());
            r.setSizeBytes(file.getSize());
            r.setStoragePath(storageName);
        }

        return repo.save(r);
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

    // Genera el siguiente folio del día con patrón REQ-YYYYMMDD-LNN
    /**
     * Genera el siguiente folio del día actual con formato REQ-YYYYMMDD-LNN
     * L = A..Z, NN = 00..99
     */
    public String nextFolioForToday() {
        String date = LocalDateTime.now().format(FMT);  // 20251114
        String prefix = "REQ-" + date + "-";            // REQ-20251114-

        Optional<Requirement> lastOpt =
                repo.findTopByFolioStartingWithOrderByFolioDesc(prefix);

        char letter = 'A';
        int number = 0;

        if (lastOpt.isPresent()) {
            String last = lastOpt.get().getFolio();  // REQ-20251114-A05
            String suffix = last.substring(prefix.length()); // A05
            if (suffix.length() >= 2) {
                letter = suffix.charAt(0);
                try {
                    number = Integer.parseInt(suffix.substring(1));
                } catch (NumberFormatException ignored) {
                    number = 0;
                }
                number++;
                if (number >= 100) {
                    number = 0;
                    letter = (letter == 'Z') ? 'A' : (char) (letter + 1);
                }
            }
        }

        return String.format("%s%c%02d", prefix, letter, number);
    }

}