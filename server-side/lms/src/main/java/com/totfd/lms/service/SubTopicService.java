package com.totfd.lms.service;

import com.totfd.lms.dto.subtopics.request.SubTopicRequestDTO;
import com.totfd.lms.dto.subtopics.response.SubTopicResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubTopicService {
    SubTopicResponseDTO createSubTopic(SubTopicRequestDTO dto);
    SubTopicResponseDTO getSubTopicById(Long id);
    List<SubTopicResponseDTO> getAllSubTopics();
    Page<SubTopicResponseDTO> getAllSubTopicsWithPage(Pageable pageable);
    SubTopicResponseDTO updateSubTopic(Long id, SubTopicRequestDTO dto);
    void deleteSubTopic(Long id);
    Page<SubTopicResponseDTO> findSubTopicsWithFilters(String tittle,  Pageable pageable);
}
