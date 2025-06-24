package com.totfd.lms.repository;

import com.totfd.lms.entity.McqProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface McqProgressRepository extends JpaRepository<McqProgress, Long> {
    Optional<McqProgress> findByUsersIdAndMcqId(Long userId, Long mcqId);
    List<McqProgress> findByUsersId(Long userId);
    List<McqProgress> findByMcqId(Long mcqId);
}
