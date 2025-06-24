package com.totfd.lms.dto.learnigPackages;

import com.totfd.lms.dto.skill.SkillBasicDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PackageBasicDTO(
        Long id,
        String title,
        String description,
        BigDecimal price,
        LocalDateTime createdAt,
        List<SkillBasicDTO> skills
) {}

