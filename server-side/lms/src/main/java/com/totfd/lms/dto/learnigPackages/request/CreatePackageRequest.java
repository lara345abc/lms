package com.totfd.lms.dto.learnigPackages.request;

import com.totfd.lms.dto.skill.request.CreateSkillDTO;

import java.math.BigDecimal;
import java.util.List;

public record CreatePackageRequest(
        String title,
        String description,
        BigDecimal price,
        List<CreateSkillDTO> skills
) {
}
