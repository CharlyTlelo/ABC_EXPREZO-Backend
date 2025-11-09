package com.abc.exprezo.repos.api;

import com.abc.exprezo.repos.domain.Repositorio;
import com.abc.exprezo.repos.service.RepositorioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repos")
public class RepositorioController {

    private final RepositorioService service;
    public RepositorioController(RepositorioService service){ this.service = service; }

    @GetMapping public List<Repositorio> list(){ return service.list(); }
    @GetMapping("/{id}") public Repositorio get(@PathVariable String id){ return service.get(id); }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Repositorio create(@Valid @RequestBody Repositorio r){ return service.create(r); }

    @PutMapping("/{id}")
    public Repositorio update(@PathVariable String id, @Valid @RequestBody Repositorio r){ return service.update(id, r); }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id){ service.delete(id); }
}
