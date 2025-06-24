package com.totfd.lms.repository;

import com.totfd.lms.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByVideoId(Long videoId);
    @Query("SELECT c.rating FROM Comment c WHERE c.video.id = :videoId")
    List<Integer> findRatingsByVideoId(@Param("videoId") Long videoId);

    @Query("SELECT AVG(c.rating) FROM Comment c WHERE c.video.id = :videoId")
    Double getAverageRatingByVideoId(@Param("videoId") Long videoId);

    Long countByVideoId(Long videoId);

    @Query("SELECT c.rating, COUNT(c) FROM Comment c WHERE c.video.id = :videoId GROUP BY c.rating")
    List<Object[]> getRatingDistributionByVideoId(@Param("videoId") Long videoId);

    Page<Comment> findByVideoId(Long videoId, Pageable pageable);


}

