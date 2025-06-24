package com.totfd.lms.dto.skill.request;

import com.totfd.lms.dto.topic.request.CreateTopicDTO;

import java.math.BigDecimal;
import java.util.List;

public record CreateSkillDTO(
        String title,
        String description,
        BigDecimal price,
        List<CreateTopicDTO> topics
) {}

