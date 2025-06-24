package com.totfd.lms.dto.video.request;

public record CreateVideoDTO(
        String url,
        String thumbnailUrl,
        Integer duration,
        Integer version,
        Boolean isLatest
) {}

