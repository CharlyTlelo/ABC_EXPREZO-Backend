package com.abc.exprezo.repos.service;

import com.abc.exprezo.repos.domain.Repositorio;
import com.abc.exprezo.repos.repository.RepositorioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositorioService {
    private final RepositorioRepository repo;
    private static final String DEFAULT_URL =
            "https://github.com/CharlyTlelo/ABC_EXPREZO-Frontend";

    public RepositorioService(RepositorioRepository repo){ this.repo = repo; }

    public List<Repositorio> list(){
        return repo.findAll(Sort.by("proyecto").ascending());
    }
    public Repositorio get(String id){
        return repo.findById(id).orElseThrow(() -> new RuntimeException("No existe"));
    }
    public Repositorio create(Repositorio r){
        if (repo.existsByRepositorioIgnoreCase(r.getRepositorio()))
            throw new RuntimeException("El repositorio ya existe");
        if (r.getUrl()==null || r.getUrl().isBlank()) r.setUrl(DEFAULT_URL);
        return repo.save(r);
    }
    public Repositorio update(String id, Repositorio p){
        var db = get(id);
        db.setProyecto(p.getProyecto());
        db.setRepositorio(p.getRepositorio());
        db.setUrl((p.getUrl()==null || p.getUrl().isBlank()) ? DEFAULT_URL : p.getUrl());
        return repo.save(db);
    }
    public void delete(String id){ repo.deleteById(id); }
}
