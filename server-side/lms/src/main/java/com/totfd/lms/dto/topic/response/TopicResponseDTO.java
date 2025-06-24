package com.totfd.lms.dto.topic.response;

import com.totfd.lms.dto.subtopics.response.SubTopicResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public record TopicResponseDTO(
        Long id,
        String title,
        String description,
        LocalDateTime createdAt,
        Long skillId,
        String skillTitle,
        List<SubTopicResponseDTO> subTopics
) {}

