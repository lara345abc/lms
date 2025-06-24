package com.totfd.lms.mapper;

import com.totfd.lms.dto.review.request.ReviewRequestDTO;
import com.totfd.lms.dto.review.response.ReviewResponseDTO;
import com.totfd.lms.entity.Review;
import com.totfd.lms.entity.Users;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-24T18:40:57+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public Review toEntity(ReviewRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Review.ReviewBuilder review = Review.builder();

        review.targetType( dto.targetType() );
        review.targetId( dto.targetId() );
        review.rating( dto.rating() );
        review.reviewText( dto.reviewText() );
        review.presentationRating( dto.presentationRating() );

        return review.build();
    }

    @Override
    public ReviewResponseDTO toResponseDTO(Review review) {
        if ( review == null ) {
            return null;
        }

        Long userId = null;
        String userName = null;
        Long id = null;
        String targetType = null;
        Long targetId = null;
        Integer rating = null;
        Integer presentationRating = null;
        String reviewText = null;
        LocalDateTime createdAt = null;

        userId = reviewUsersId( review );
        userName = reviewUsersName( review );
        id = review.getId();
        targetType = review.getTargetType();
        targetId = review.getTargetId();
        rating = review.getRating();
        presentationRating = review.getPresentationRating();
        reviewText = review.getReviewText();
        createdAt = review.getCreatedAt();

        ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO( id, userId, userName, targetType, targetId, rating, presentationRating, reviewText, createdAt );

        return reviewResponseDTO;
    }

    @Override
    public void updateFromDto(ReviewRequestDTO dto, Review review) {
        if ( dto == null ) {
            return;
        }

        if ( dto.targetType() != null ) {
            review.setTargetType( dto.targetType() );
        }
        if ( dto.targetId() != null ) {
            review.setTargetId( dto.targetId() );
        }
        if ( dto.rating() != null ) {
            review.setRating( dto.rating() );
        }
        if ( dto.reviewText() != null ) {
            review.setReviewText( dto.reviewText() );
        }
        if ( dto.presentationRating() != null ) {
            review.setPresentationRating( dto.presentationRating() );
        }
    }

    private Long reviewUsersId(Review review) {
        if ( review == null ) {
            return null;
        }
        Users users = review.getUsers();
        if ( users == null ) {
            return null;
        }
        Long id = users.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String reviewUsersName(Review review) {
        if ( review == null ) {
            return null;
        }
        Users users = review.getUsers();
        if ( users == null ) {
            return null;
        }
        String name = users.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
