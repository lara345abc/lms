package com.totfd.lms.service.impl;

import com.totfd.lms.dto.review.request.ReviewRequestDTO;
import com.totfd.lms.dto.review.response.ReviewResponseDTO;
import com.totfd.lms.dto.review.response.TargetReviewSummaryDTO;
import com.totfd.lms.entity.*;
import com.totfd.lms.exceptions.ResourceNotFoundException;
import com.totfd.lms.mapper.ReviewMapper;
import com.totfd.lms.repository.*;
import com.totfd.lms.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UsersRepository usersRepository;
    private final PackageRepository packageRepository;
    private final SkillRepository skillRepository;
    private final VideoRepository videoRepository;

    @Override
    public ReviewResponseDTO createReview(String email, ReviewRequestDTO dto) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Review review = reviewMapper.toEntity(dto);
        review.setCreatedAt(LocalDateTime.now());
        review.setUsers(user); // Set user from email

        return reviewMapper.toResponseDTO(reviewRepository.save(review));
    }

    @Override
    public List<ReviewResponseDTO> getReviewsByTarget(String targetType, Long targetId) {
        return reviewRepository.findByTargetTypeAndTargetId(targetType, targetId)
                .stream().map(reviewMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public ReviewResponseDTO updateReview(Long id, ReviewRequestDTO dto) {
        Review existing = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        reviewMapper.updateFromDto(dto, existing);
        return reviewMapper.toResponseDTO(reviewRepository.save(existing));
    }

    @Override
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review not found");
        }
        reviewRepository.deleteById(id);
    }

    @Override
    public TargetReviewSummaryDTO getReviewSummary(String targetType, Long targetId) {
        List<Review> reviews = reviewRepository.findByTargetTypeAndTargetId(targetType, targetId);
        double average = reviews.stream()
                .mapToInt(r -> r.getPresentationRating() != null ? r.getPresentationRating() : 0)
                .average()
                .orElse(0.0);

        String title = "", description = "";

        switch (targetType.toUpperCase()) {
            case "PACKAGE" -> {
                LearningPackage pkg = packageRepository.findById(targetId)
                        .orElseThrow(() -> new ResourceNotFoundException("Package not found"));
                title = pkg.getTitle();
                description = pkg.getDescription();
            }
            case "SKILL" -> {
                Skill skill = skillRepository.findById(targetId)
                        .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
                title = skill.getTitle();
                description = skill.getDescription();
            }
//            case "VIDEO" -> {
//                Video video = videoRepository.findById(targetId)
//                        .orElseThrow(() -> new ResourceNotFoundException("Video not found"));
//                title = video.getTitle();
//                description = video.getDescription();
//            }
            // Add more cases as needed
            default -> throw new IllegalArgumentException("Invalid target type");
        }

        return new TargetReviewSummaryDTO(
                targetType.toUpperCase(),
                targetId,
                title,
                description,
                average,
                reviews.size(),
                reviews.stream().map(reviewMapper::toResponseDTO).toList()
        );
    }

}

