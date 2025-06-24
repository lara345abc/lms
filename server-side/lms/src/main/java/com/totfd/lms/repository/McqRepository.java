package com.totfd.lms.repository;

import com.totfd.lms.entity.Mcq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface McqRepository extends JpaRepository<Mcq, Long> {
    List<Mcq> findBySubTopicId(Long subTopicId);
    List<Mcq> findBySubTopicIdIn(List<Long> subTopicIds);
}
