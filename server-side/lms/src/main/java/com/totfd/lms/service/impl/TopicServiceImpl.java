package com.totfd.lms.service.impl;

import com.totfd.lms.dto.learnigPackages.response.PackageResponseDTO;
import com.totfd.lms.dto.topic.request.TopicRequestDTO;
import com.totfd.lms.dto.topic.response.TopicResponseDTO;
import com.totfd.lms.entity.LearningPackage;
import com.totfd.lms.entity.Skill;
import com.totfd.lms.entity.SubTopic;
import com.totfd.lms.entity.Topic;
import com.totfd.lms.exceptions.ResourceNotFoundException;
import com.totfd.lms.mapper.TopicMapper;
import com.totfd.lms.repository.SkillRepository;
import com.totfd.lms.repository.SkillRepository;
import com.totfd.lms.repository.SubTopicRepository;
import com.totfd.lms.repository.TopicRepository;
import com.totfd.lms.service.TopicService;
import com.totfd.lms.specification.LearningPackageSpecification;
import com.totfd.lms.specification.TopicSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final SkillRepository skillRepository;
    private final TopicMapper topicMapper;
    private final SubTopicRepository subTopicRepository;

    @Override
    public TopicResponseDTO createTopic(TopicRequestDTO dto) {
        Skill skill = skillRepository.findById(dto.skillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        Topic topic = Topic.builder()
                .skill(skill)
                .title(dto.title())
                .description(dto.description())
                .build();

        // Step 1: Save topic FIRST to get its ID
        Topic savedTopic = topicRepository.save(topic);

        // Step 2: Create subtopics now that topic is saved
        List<SubTopic> subTopics = dto.subTopics().stream()
                .map(subTopicDto -> SubTopic.builder()
                        .topic(savedTopic) // set the saved topic with ID
                        .title(subTopicDto.title())
                        .description(subTopicDto.description())
                        .build())
                .collect(Collectors.toList());

        // Step 3: Save all subtopics
        subTopicRepository.saveAll(subTopics);

        // Step 4: Attach to topic and return
        savedTopic.setSubTopics(subTopics);
        return topicMapper.toResponseDTO(savedTopic);
    }


    @Override
    public List<TopicResponseDTO> getTopicsBySkillId(Long skillId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        List<Topic> topics = topicRepository.findBySkillId(skillId);

        return topics.stream()
                .map(topicMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    @Override
    public TopicResponseDTO getTopicById(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
        return topicMapper.toResponseDTO(topic);
    }

    @Override
    public List<TopicResponseDTO> getAllTopics() {
        return topicRepository.findAll().stream()
                .map(topicMapper::toResponseDTO).toList();
    }

    @Override
    public Page<TopicResponseDTO> getAllTopicsWithPage(Pageable pageable) {
        return topicRepository.findAll(pageable)
                .map(topicMapper::toResponseDTO);
    }

    @Override
    public TopicResponseDTO updateTopic(Long id, TopicRequestDTO dto) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        if (dto.skillId() != null) {
            Skill skill = skillRepository.findById(dto.skillId())
                    .orElseThrow(() -> new ResourceNotFoundException("Package not found"));
            topic.setSkill(skill);
        }

        topicMapper.updateFromDto(dto, topic);
        return topicMapper.toResponseDTO(topicRepository.save(topic));
    }

    @Override
    public void deleteTopic(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
        topicRepository.delete(topic);
    }

    @Override
    public Page<TopicResponseDTO> findTopicsWithFilters(String title,  Pageable pageable) {
        Specification<Topic> spec = Specification
                .where(TopicSpecification.hasTitle(title));


        return topicRepository.findAll(spec, pageable).map(topicMapper::toResponseDTO);
    }

    @Transactional
    public TopicResponseDTO getTopicWithDetails(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        topic.getSubTopics().forEach(sub -> {
            sub.getVideos().size(); // force loading if LAZY
            sub.getStudyMaterials().size();
        });

        return topicMapper.toResponseDTO(topic);
    }

}

