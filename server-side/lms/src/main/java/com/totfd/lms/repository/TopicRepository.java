package com.totfd.lms.repository;

import com.totfd.lms.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic,Long> , JpaSpecificationExecutor<Topic> {
    List<Topic> findBySkillId(Long skillId);
}
