package com.totfd.lms.dto.review.response;


import java.time.LocalDateTime;

public record ReviewResponseDTO(
        Long id,
        Long userId,
        String userName,
        String targetType,
        Long targetId,
        Integer rating,
        Integer presentationRating,
        String reviewText,
        LocalDateTime createdAt
) {}
