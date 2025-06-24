package com.totfd.lms.dto.video.response;

public record VideoResponseDTO(
        Long id,
        Long subTopicId,
        String subTopicTitle,
        String url,
        String thumbnailUrl,
        Integer duration,
        Integer version,
        Boolean isLatest,
        Long noOfViews
) {}
