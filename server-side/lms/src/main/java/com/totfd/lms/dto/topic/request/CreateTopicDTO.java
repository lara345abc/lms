package com.totfd.lms.dto.topic.request;

import com.totfd.lms.dto.subtopics.request.CreateSubTopicDTO;

import java.util.List;

public record CreateTopicDTO(
        String title,
        String description,
        List<CreateSubTopicDTO> subTopics
) {}

