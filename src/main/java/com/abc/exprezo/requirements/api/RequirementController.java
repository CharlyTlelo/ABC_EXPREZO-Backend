package com.abc.exprezo.requirements.api;

import com.abc.exprezo.requirements.api.dto.RequirementUpdateDto;
import com.abc.exprezo.requirements.domain.Requirement;
import com.abc.exprezo.requirements.domain.RequirementStatus;
import com.abc.exprezo.requirements.service.RequirementService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/v1/reqs")
public class RequirementController {

    private final RequirementService service;

    // ⬅️ Constructor explícito (Spring usará este para inyectar el servicio)
    public RequirementController(RequirementService service) {
        this.service = service;
    }

    @GetMapping
    public List<Requirement> list(@RequestParam(required = false) RequirementStatus status) {
        return service.list(status);
    }

    @GetMapping("/{id}")
    public Requirement get(@PathVariable String id) { return service.get(id); }

    @GetMapping("/stats")
    public Map<String, Long> stats() {
        Map<String, Long> m = new LinkedHashMap<>();
        m.put("PENDING",   service.list(RequirementStatus.PENDING).stream().count());
        m.put("IN_REVIEW", service.list(RequirementStatus.IN_REVIEW).stream().count());
        m.put("APPROVED",  service.list(RequirementStatus.APPROVED).stream().count());
        return m;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Requirement create(
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "title", required = false) String title,
            @RequestPart(value = "description", required = false) String description
    ) throws IOException {
        return service.create(title, description, file);
    }

    @PutMapping("/{id}")
    public Requirement update(@PathVariable String id, @RequestBody RequirementUpdateDto body) {
        // Si tu DTO es record: body.title()/body.description()/body.status()
        // Si es class con getters: body.getTitle()...
        return service.update(id, body.title(), body.description(), body.status());
    }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) throws IOException { service.delete(id); }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable String id) {
        var res = service.download(id);
        if (!res.exists()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + res.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(res);
    }
}
