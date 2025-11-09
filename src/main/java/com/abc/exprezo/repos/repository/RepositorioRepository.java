package com.abc.exprezo.repos.repository;

import com.abc.exprezo.repos.domain.Repositorio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepositorioRepository extends MongoRepository<Repositorio, String> {
    boolean existsByRepositorioIgnoreCase(String repositorio);
}
