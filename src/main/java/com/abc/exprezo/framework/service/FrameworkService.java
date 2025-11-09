package com.abc.exprezo.framework.service;

import com.abc.exprezo.framework.domain.FrameworkTech;
import com.abc.exprezo.framework.repository.FrameworkRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrameworkService {
    private final FrameworkRepository repo;
    public FrameworkService(FrameworkRepository repo){ this.repo = repo; }

    public List<FrameworkTech> list(){ return repo.findAll(Sort.by("tecnologia").ascending()); }
    public FrameworkTech get(String id){ return repo.findById(id).orElseThrow(() -> new RuntimeException("No existe")); }
    public FrameworkTech create(FrameworkTech f){
        if (repo.existsByTecnologiaIgnoreCase(f.getTecnologia())) throw new RuntimeException("Tecnología ya existe");
        return repo.save(f);
    }
    public FrameworkTech update(String id, FrameworkTech p){
        var db = get(id);
        db.setTecnologia(p.getTecnologia());
        db.setCategoria(p.getCategoria());
        db.setVersionActual(p.getVersionActual());
        db.setProximaVersion(p.getProximaVersion());
        return repo.save(db);
    }
    public void delete(String id){ repo.deleteById(id); }
}
