package com.totfd.lms.dto.skill;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SkillBasicDTO(
        Long id,
        String title,
        String description,
        BigDecimal price,
        LocalDateTime createdAt
) {}

