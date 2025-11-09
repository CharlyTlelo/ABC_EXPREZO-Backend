package com.abc.exprezo.requirements.api.dto;

import com.abc.exprezo.requirements.domain.RequirementStatus;

public record RequirementUpdateDto(
        String title,
        String description,
        RequirementStatus status
) {}
