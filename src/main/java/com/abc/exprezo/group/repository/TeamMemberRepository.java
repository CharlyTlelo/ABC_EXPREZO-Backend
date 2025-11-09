package com.abc.exprezo.group.repository;

import com.abc.exprezo.group.domain.TeamMember;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamMemberRepository extends MongoRepository<TeamMember, String> {
    boolean existsByCodeIgnoreCase(String code);
}
