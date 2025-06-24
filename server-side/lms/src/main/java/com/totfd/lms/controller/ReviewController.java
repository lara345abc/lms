package com.totfd.lms.controller;

import com.totfd.lms.dto.review.request.ReviewRequestDTO;
import com.totfd.lms.dto.review.response.ReviewResponseDTO;
import com.totfd.lms.dto.review.response.TargetReviewSummaryDTO;
import com.totfd.lms.payload.ApiResponse;
import com.totfd.lms.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> create(
            @RequestBody ReviewRequestDTO dto,
            HttpServletRequest request,
            Principal principal
    ) {
        String email = principal.getName();
        ReviewResponseDTO response = reviewService.createReview(email, dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Review created", 200, request.getRequestURI()));
    }


    @GetMapping("/target")
    public ResponseEntity<ApiResponse<List<ReviewResponseDTO>>> getByTarget(
            @RequestParam String targetType,
            @RequestParam Long targetId,
            HttpServletRequest request) {

        List<ReviewResponseDTO> reviews = reviewService.getReviewsByTarget(targetType, targetId);
        return ResponseEntity.ok(ApiResponse.success(reviews, "Reviews fetched", 200, request.getRequestURI()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> update(@PathVariable Long id, @RequestBody ReviewRequestDTO dto, HttpServletRequest request) {
        ReviewResponseDTO updated = reviewService.updateReview(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Review updated", 200, request.getRequestURI()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id, HttpServletRequest request) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.success("Review deleted", "OK", 200, request.getRequestURI()));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<TargetReviewSummaryDTO>> getReviewSummary(
            @RequestParam String targetType,
            @RequestParam Long targetId,
            HttpServletRequest request) {

        TargetReviewSummaryDTO summary = reviewService.getReviewSummary(targetType, targetId);
        return ResponseEntity.ok(ApiResponse.success(summary, "Review summary fetched", 200, request.getRequestURI()));
    }

}

