package com.totfd.lms.repository;

import com.totfd.lms.entity.SubTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubTopicRepository extends JpaRepository<SubTopic, Long> , JpaSpecificationExecutor<SubTopic> {
}
