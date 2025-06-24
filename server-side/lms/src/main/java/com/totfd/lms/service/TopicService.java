package com.totfd.lms.service;

import com.totfd.lms.dto.learnigPackages.response.PackageResponseDTO;
import com.totfd.lms.dto.topic.request.TopicRequestDTO;
import com.totfd.lms.dto.topic.response.TopicResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface TopicService {
    TopicResponseDTO createTopic(TopicRequestDTO dto);
    TopicResponseDTO getTopicById(Long id);
    List<TopicResponseDTO> getAllTopics();
    Page<TopicResponseDTO> getAllTopicsWithPage(Pageable pageable);
    TopicResponseDTO updateTopic(Long id, TopicRequestDTO dto);
    void deleteTopic(Long id);
    Page<TopicResponseDTO> findTopicsWithFilters(String tittle,  Pageable pageable);
    List<TopicResponseDTO> getTopicsBySkillId(Long skillId);


}
