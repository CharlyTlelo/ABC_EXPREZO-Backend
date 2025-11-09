package com.abc.exprezo.requirements.repository;

import com.abc.exprezo.requirements.domain.Requirement;
import com.abc.exprezo.requirements.domain.RequirementStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RequirementRepository extends MongoRepository<Requirement, String> {

    // listar documentos de un folio (para tu pantalla de revisión)
    List<Requirement> findByFolioOrderByCreatedAtAsc(String folio);

    // ya lo usas para listados generales
    List<Requirement> findByStatusOrderByCreatedAtDesc(RequirementStatus status);

    // útil si decides seguir generando folios cuando falten
    boolean existsByFolio(String folio);
}
