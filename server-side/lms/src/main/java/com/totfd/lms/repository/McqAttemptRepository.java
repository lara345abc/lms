package com.totfd.lms.repository;

import com.totfd.lms.entity.McqAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface McqAttemptRepository extends JpaRepository<McqAttempt, Long> {
    List<McqAttempt> findByUsersId(Long userId);
//    List<McqAttempt> findByMcqId(Long mcqId);
    List<McqAttempt> findBySubTopicId(Long subTopicId);
    Optional<McqAttempt> findTopByUsersIdAndSubTopicIdOrderByAttemptNumberDesc(Long userId, Long subTopicId);
    List<McqAttempt> findBySubTopicIdIn(List<Long> subTopicIds);
}

