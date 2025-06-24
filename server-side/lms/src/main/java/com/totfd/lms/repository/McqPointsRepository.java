package com.totfd.lms.repository;

import com.totfd.lms.entity.McqPoints;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface McqPointsRepository extends JpaRepository<McqPoints, Long> {
    List<McqPoints> findByUsersId(Long userId);
    List<McqPoints> findByMcqId(Long mcqId);
}

