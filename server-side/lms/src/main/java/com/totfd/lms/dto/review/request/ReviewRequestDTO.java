package com.totfd.lms.dto.review.request;

public record ReviewRequestDTO(
//        Long userId,
        String targetType,
        Long targetId,
        Integer rating,
        Integer presentationRating,
        String reviewText
) {}
