package com.abc.exprezo.framework.api;

import com.abc.exprezo.framework.domain.FrameworkTech;
import com.abc.exprezo.framework.service.FrameworkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/frameworks")
public class FrameworkController {

    private final FrameworkService service;
    public FrameworkController(FrameworkService service){ this.service = service; }

    @GetMapping public List<FrameworkTech> list(){ return service.list(); }
    @GetMapping("/{id}") public FrameworkTech get(@PathVariable String id){ return service.get(id); }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public FrameworkTech create(@Valid @RequestBody FrameworkTech f){ return service.create(f); }

    @PutMapping("/{id}")
    public FrameworkTech update(@PathVariable String id, @Valid @RequestBody FrameworkTech f){ return service.update(id, f); }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id){ service.delete(id); }
}
