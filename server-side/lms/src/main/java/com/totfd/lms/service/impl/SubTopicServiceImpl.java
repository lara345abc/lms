package com.totfd.lms.service.impl;

import com.totfd.lms.dto.subtopics.request.SubTopicRequestDTO;
import com.totfd.lms.dto.subtopics.response.SubTopicResponseDTO;
import com.totfd.lms.entity.SubTopic;
import com.totfd.lms.entity.Topic;
import com.totfd.lms.exceptions.ResourceNotFoundException;
import com.totfd.lms.mapper.SubTopicMapper;
import com.totfd.lms.repository.SubTopicRepository;
import com.totfd.lms.repository.TopicRepository;
import com.totfd.lms.service.SubTopicService;
import com.totfd.lms.specification.SubTopicSpeicification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubTopicServiceImpl implements SubTopicService {
    private final SubTopicRepository subTopicRepository;
    private final TopicRepository topicRepository;
    private final SubTopicMapper subTopicMapper;

    @Override
    public SubTopicResponseDTO createSubTopic(SubTopicRequestDTO dto) {
        Topic topic = topicRepository.findById(dto.topicId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        SubTopic subTopic = SubTopic.builder()
                .topic(topic)
                .title(dto.title())
                .description(dto.description())
                .build();

        SubTopic saved = subTopicRepository.save(subTopic);
        return subTopicMapper.toResponseDTO(saved);
    }


//    @Override
//    public SubTopicResponseDTO getSubTopicById(Long id) {
//        SubTopic subTopic = subTopicRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
//        return subTopicMapper.toResponseDTO(subTopic);
//    }

    @Override
    public List<SubTopicResponseDTO> getAllSubTopics() {
        return subTopicRepository.findAll().stream()
                .map(subTopicMapper::toResponseDTO).toList();
    }

    @Override
    public SubTopicResponseDTO getSubTopicById(Long subTopicId) {
        SubTopic subTopic = subTopicRepository.findById(subTopicId)
                .orElseThrow(() -> new ResourceNotFoundException("SubTopic not found"));

        return subTopicMapper.toResponseDTO(subTopic);
    }

    @Override
    public Page<SubTopicResponseDTO> getAllSubTopicsWithPage(Pageable pageable) {
        return subTopicRepository.findAll(pageable)
                .map(subTopicMapper::toResponseDTO);
    }

    @Override
    public SubTopicResponseDTO updateSubTopic(Long id, SubTopicRequestDTO dto) {
        SubTopic subTopic = subTopicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sub topic not found"));

        if (dto.topicId() != null) {
            Topic topic = topicRepository.findById(dto.topicId())
                    .orElseThrow(() -> new ResourceNotFoundException("topic not found"));
            subTopic.setTopic(topic);
        }

        subTopicMapper.updateFromDto(dto, subTopic);
        return subTopicMapper.toResponseDTO(subTopicRepository.save(subTopic));
    }

    @Override
    public void deleteSubTopic(Long id) {
        SubTopic subTopic = subTopicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sub-topic not found"));
        subTopicRepository.delete(subTopic);
    }

//    @Override
//    public Page<SubTopicResponseDTO> findSubTopicsWithFilters(String tittle, Pageable pageable) {
//        return null;
//    }

    @Override
    public Page<SubTopicResponseDTO> findSubTopicsWithFilters(String title,  Pageable pageable) {
        Specification<SubTopic> spec = Specification
                .where(SubTopicSpeicification.hasTitle(title));


        return subTopicRepository.findAll(spec, pageable).map(subTopicMapper::toResponseDTO);
    }

    public List<SubTopicResponseDTO> getSubTopicsByIds(List<Long> subTopicIds) {
        List<SubTopic> subTopics = subTopicRepository.findAllById(subTopicIds);
        return subTopics.stream()
                .map(subTopicMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

}
