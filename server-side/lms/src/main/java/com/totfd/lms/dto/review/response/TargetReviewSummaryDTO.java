package com.totfd.lms.dto.review.response;

import java.util.List;

public record TargetReviewSummaryDTO(
        String targetType,
        Long targetId,
        String targetTitle,
        String targetDescription,
        double averageRating,
        int totalReviews,
        List<ReviewResponseDTO> reviews
) {}

