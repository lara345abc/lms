package com.totfd.lms.dto.subtopics.request;

public record SubTopicRequestDTO(
        Long topicId,
        String title,
        String description
) {}