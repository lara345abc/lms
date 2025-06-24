package com.totfd.lms.dto.comment.response;

public record RatingSummaryDTO(
        Long videoId,
        Double averageRating,
        Long totalRatings
) {}

