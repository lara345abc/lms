package com.totfd.lms.dto.skill.response;

import com.totfd.lms.dto.topic.response.TopicResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SkillResponseDTO(
        Long id,
        String title,
        String description,
        BigDecimal price,
        LocalDateTime createdAt,
        Long packageId,
        String packageTitle,
        List<TopicResponseDTO> topics
) {}
