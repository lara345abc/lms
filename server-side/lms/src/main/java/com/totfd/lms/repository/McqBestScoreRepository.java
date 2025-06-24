package com.totfd.lms.repository;

import com.totfd.lms.entity.McqBestScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface McqBestScoreRepository extends JpaRepository<McqBestScore, Long> {
    Optional<McqBestScore> findByUsersIdAndSubTopicId(Long userId, Long subTopicId);
    List<McqBestScore> findByUsersId(Long userId);
    List<McqBestScore> findBySubTopicId(Long subTopicId);
}

