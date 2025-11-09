package com.abc.exprezo.group.api;

import com.abc.exprezo.group.domain.TeamMember;
import com.abc.exprezo.group.service.TeamMemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
public class TeamMemberController {

    private final TeamMemberService service;
    public TeamMemberController(TeamMemberService service){ this.service = service; }

    @GetMapping
    public List<TeamMember> list() { return service.list(); }

    @GetMapping("/{id}")
    public TeamMember get(@PathVariable String id) { return service.get(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamMember create(@Valid @RequestBody TeamMember req) { return service.create(req); }

    @PutMapping("/{id}")
    public TeamMember update(@PathVariable String id, @Valid @RequestBody TeamMember req) { return service.update(id, req); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) { service.delete(id); }
}
