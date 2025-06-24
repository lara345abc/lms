package com.totfd.lms.service.impl;

import com.totfd.lms.dto.skill.request.SkillRequestDTO;
import com.totfd.lms.dto.skill.response.SkillResponseDTO;
import com.totfd.lms.entity.LearningPackage;
import com.totfd.lms.entity.Skill;
import com.totfd.lms.entity.SubTopic;
import com.totfd.lms.entity.Topic;
import com.totfd.lms.exceptions.ResourceNotFoundException;
import com.totfd.lms.mapper.SkillMapper;
import com.totfd.lms.repository.PackageRepository;
import com.totfd.lms.repository.SkillRepository;
import com.totfd.lms.service.SkillService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final PackageRepository packageRepository;
    private final SkillMapper skillMapper;

    @Override
    public SkillResponseDTO createSkill(SkillRequestDTO dto) {
        // Validate and fetch LearningPackage
        LearningPackage learningPackage = packageRepository.findById(dto.packageId())
                .orElseThrow(() -> new ResourceNotFoundException("Learning Package not found"));

        Skill skill = Skill.builder()
                .learningPackage(learningPackage)
                .title(dto.title())
                .description(dto.description())
                .price(dto.price())
                .build();

        Skill saved = skillRepository.save(skill);
        return skillMapper.toResponseDTO(saved);
    }


    @Override
    public SkillResponseDTO getSkillById(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
        return skillMapper.toResponseDTO(skill);
    }

    @Override
    public List<SkillResponseDTO> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(skillMapper::toResponseDTO).toList();
    }

    @Override
    public Page<SkillResponseDTO> getAllSkillsWithPage(Pageable pageable) {
        return skillRepository.findAll(pageable)
                .map(skillMapper::toResponseDTO);
    }

    @Override
    public SkillResponseDTO updateSkill(Long id, SkillRequestDTO dto) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        if (dto.packageId() != null) {
            LearningPackage pkg = packageRepository.findById(dto.packageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Package not found"));
            skill.setLearningPackage(pkg);
        }

        skillMapper.updateFromDto(dto, skill);
        return skillMapper.toResponseDTO(skillRepository.save(skill));
    }

    @Override
    public void deleteSkill(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
        skillRepository.delete(skill);
    }

    @Override
    public SkillResponseDTO getSkillDetailsById(Long id) {
        Skill skill = skillRepository.findByIdWithTopicsAndSubtopicsAndVideosAndMaterials(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with full details"));

        return skillMapper.toResponseDTO(skill);
    }

    @Transactional
    public SkillResponseDTO getSkillWithTopics(Long skillId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found: " + skillId));

        Hibernate.initialize(skill.getTopics());
        for (Topic topic : new ArrayList<>(skill.getTopics())) {
            Hibernate.initialize(topic.getSubTopics());
            for (SubTopic subTopic : new ArrayList<>(topic.getSubTopics())) {
                Hibernate.initialize(subTopic.getVideos());
                Hibernate.initialize(subTopic.getStudyMaterials());
            }
        }

        return skillMapper.toResponseDTO(skill); // includes topics/subtopics
    }




}

