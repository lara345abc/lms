package com.totfd.lms.dto.learnigPackages.response;

import com.totfd.lms.dto.skill.response.SkillResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PackageResponseDTO(
        Long id,
        String title,
        String description,
        BigDecimal price,
        LocalDateTime createdAt,
        List<SkillResponseDTO> skills
) {}

