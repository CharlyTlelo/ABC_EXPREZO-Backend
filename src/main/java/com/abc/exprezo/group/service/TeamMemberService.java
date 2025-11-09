package com.abc.exprezo.group.service;

import com.abc.exprezo.group.domain.TeamMember;
import com.abc.exprezo.group.repository.TeamMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamMemberService {
    private final TeamMemberRepository repo;
    public TeamMemberService(TeamMemberRepository repo){ this.repo = repo; }

    public List<TeamMember> list() { return repo.findAll(); }

    public TeamMember get(String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
    }

    public TeamMember create(TeamMember m) {
        if (repo.existsByCodeIgnoreCase(m.getCode())) throw new RuntimeException("Member code already exists");
        return repo.save(m);
    }

    public TeamMember update(String id, TeamMember patch) {
        TeamMember db = get(id);
        db.setFullName(patch.getFullName());
        db.setTitle(patch.getTitle());
        db.setCategory(patch.getCategory());
        db.setStatus(patch.getStatus());
        db.setArea(patch.getArea());
        //db.setColorHex(patch.getColorHex());
        db.setAvatarUrl(patch.getAvatarUrl());
        db.setDisplayOrder(patch.getDisplayOrder());
        return repo.save(db);
    }

    public void delete(String id) { repo.deleteById(id); }
}
