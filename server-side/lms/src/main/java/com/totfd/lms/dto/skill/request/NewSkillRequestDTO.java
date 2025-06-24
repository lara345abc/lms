package com.totfd.lms.dto.skill.request;

import java.math.BigDecimal;

public record NewSkillRequestDTO(
        String title,
        String description,
        BigDecimal price
) {
}
