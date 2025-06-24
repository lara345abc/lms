package com.totfd.lms.repository;

import com.totfd.lms.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTargetTypeAndTargetId(String targetType, Long targetId);
}
