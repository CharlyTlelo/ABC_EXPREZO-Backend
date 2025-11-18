package com.abc.exprezo.requirements.repository;

import com.abc.exprezo.requirements.model.Requirement;
import com.abc.exprezo.requirements.model.RequirementStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RequirementRepository extends MongoRepository<Requirement, String> {

    // listar documentos de un folio (para tu pantalla de revisión)
    List<Requirement> findByFolioOrderByCreatedAtAsc(String folio);

    // ya lo usas para listados generales
    List<Requirement> findByStatusOrderByCreatedAtDesc(RequirementStatus status);

    // útil si decides seguir generando folios cuando falten
    boolean existsByFolio(String folio);

    // 👇 NUEVO: último folio del día, para incrementar
    Optional<Requirement> findTopByFolioStartingWithOrderByFolioDesc(String prefix);
}
