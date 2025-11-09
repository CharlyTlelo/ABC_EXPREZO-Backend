package com.abc.exprezo.framework.repository;

import com.abc.exprezo.framework.domain.FrameworkTech;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FrameworkRepository extends MongoRepository<FrameworkTech, String> {
    boolean existsByTecnologiaIgnoreCase(String tecnologia);
}
