package com.totfd.lms.dto.skill.request;

import java.math.BigDecimal;

public record SkillRequestDTO(
        Long packageId,
        String title,
        String description,
        BigDecimal price
) {
}
