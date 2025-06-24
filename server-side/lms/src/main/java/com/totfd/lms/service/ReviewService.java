package com.totfd.lms.service;

import com.totfd.lms.dto.review.request.ReviewRequestDTO;
import com.totfd.lms.dto.review.response.ReviewResponseDTO;
import com.totfd.lms.dto.review.response.TargetReviewSummaryDTO;

import java.util.List;

public interface ReviewService {
//    ReviewResponseDTO createReview(ReviewRequestDTO dto);
    List<ReviewResponseDTO> getReviewsByTarget(String targetType, Long targetId);
    ReviewResponseDTO createReview(String email, ReviewRequestDTO dto);
    ReviewResponseDTO updateReview(Long id, ReviewRequestDTO dto);
    void deleteReview(Long id);
    TargetReviewSummaryDTO getReviewSummary(String targetType, Long targetId);

}
