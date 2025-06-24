package com.totfd.lms.dto.learnigPackages.request;

import com.totfd.lms.dto.skill.request.SkillRequestDTO;

import java.math.BigDecimal;
import java.util.List;

public record PackageRequestDTO(
        String title,
        String description,
        BigDecimal price,
        List<SkillRequestDTO> skills
) {
}
