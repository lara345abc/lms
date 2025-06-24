package com.totfd.lms.dto.video.request;

public record VideoRequestDTO(
        Long subTopicId,
        String url,
        Integer version,
        Boolean isLatest
) {}
