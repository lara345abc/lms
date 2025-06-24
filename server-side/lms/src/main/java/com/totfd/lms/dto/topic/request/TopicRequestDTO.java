package com.totfd.lms.dto.topic.request;

import com.totfd.lms.dto.subtopics.request.SubTopicRequestDTO;

import java.util.List;

public record TopicRequestDTO(
        Long skillId,
        String title,
        String description,
        List<SubTopicRequestDTO> subTopics
) {
}

