package com.totfd.lms.service.impl;

import com.totfd.lms.dto.learnigPackages.request.CreatePackageRequest;
import com.totfd.lms.dto.learnigPackages.request.PackageRequestDTO;
import com.totfd.lms.dto.learnigPackages.response.PackageResponseDTO;
import com.totfd.lms.dto.skill.request.CreateSkillDTO;
import com.totfd.lms.dto.subtopics.request.CreateSubTopicDTO;
import com.totfd.lms.dto.topic.request.CreateTopicDTO;
import com.totfd.lms.entity.*;
import com.totfd.lms.exceptions.ResourceNotFoundException;
import com.totfd.lms.mapper.CreatePackageMapper;
import com.totfd.lms.mapper.PackageMapper;
import com.totfd.lms.repository.PackageRepository;
import com.totfd.lms.repository.SkillRepository;
import com.totfd.lms.service.PackageService;
import com.totfd.lms.specification.LearningPackageSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;
    private final PackageMapper packageMapper;
    private  final SkillRepository skillRepository;
    private  final CreatePackageMapper createPackageMapper;

//    @Override
//    public PackageResponseDTO createPackage(PackageRequestDTO dto) {
//        LearningPackage pkg = packageMapper.toEntity(dto);
//        LearningPackage savedPkg = packageRepository.save(pkg);
//        return packageMapper.toResponseDTO(savedPkg);
//    }

    @Override
    @Transactional
    public PackageResponseDTO createPackage(PackageRequestDTO dto) {
        LearningPackage packageEntity = new LearningPackage();
        packageEntity.setTitle(dto.title());
        packageEntity.setDescription(dto.description());
        packageEntity.setPrice(dto.price());

        // Save package first to get its ID
        LearningPackage savedPackage = packageRepository.save(packageEntity);

        // Handle associated skills
        if (dto.skills() != null && !dto.skills().isEmpty()) {
            List<Skill> skills = dto.skills().stream().map(skillDto -> {
                Skill skill = new Skill();
                skill.setTitle(skillDto.title());
                skill.setDescription(skillDto.description());
                skill.setPrice(skillDto.price());
                skill.setLearningPackage(savedPackage); // set relation
                return skill;
            }).toList();
            skillRepository.saveAll(skills);
//            savedPackage.setSkills(skills); // optional, if bi-directional
        }

        return packageMapper.toResponseDTO(savedPackage);
    }


    @Override
    public PackageResponseDTO getPackageById(Long id) {
        LearningPackage  pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));
        return packageMapper.toResponseDTO(pkg);
    }

    @Override
    public List<PackageResponseDTO> getAllPackages() {
        return packageRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(packageMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Page<PackageResponseDTO> getAllPackagesWithPage(Pageable pageable) {
        return packageRepository.findAll(pageable).map(packageMapper::toResponseDTO);
    }

    @Override
    public Page<PackageResponseDTO> findAllWithSkills(Pageable pageable) {
        Page<Long> idsPage = packageRepository.findAllPackageIds(pageable);
        List<LearningPackage> packages = packageRepository.findAllWithSkillsByIds(idsPage.getContent());

        List<PackageResponseDTO> dtoList = packages.stream()
                .map(packageMapper::toResponseDTO)
                .toList();

        return new PageImpl<>(dtoList, pageable, idsPage.getTotalElements());
    }

    @Override
    public List<PackageResponseDTO> packgeWithAllSkills() {
        List<LearningPackage> packages = packageRepository.packgeWithAllSkills();

        return packages.stream()
                .map(packageMapper::toResponseDTO)
                .toList();
    }



    @Override
    @Transactional
    public PackageResponseDTO updatePackage(Long id, PackageRequestDTO dto) {
        LearningPackage pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));

        // Update package fields
        pkg.setTitle(dto.title());
        pkg.setDescription(dto.description());
        pkg.setPrice(dto.price());

        // Handle skills update
        if (dto.skills() != null) {
            // 1. Remove existing skills for this package
            skillRepository.deleteByLearningPackageId(pkg.getId());

            // 2. Add updated/new skills
            List<Skill> updatedSkills = dto.skills().stream().map(skillDto -> {
                Skill skill = new Skill();
                skill.setTitle(skillDto.title());
                skill.setDescription(skillDto.description());
                skill.setPrice(skillDto.price());
                skill.setLearningPackage(pkg); // maintain relation
                return skill;
            }).toList();

            skillRepository.saveAll(updatedSkills);
            // pkg.setSkills(updatedSkills); // optional if bi-directional
        }

        return packageMapper.toResponseDTO(packageRepository.save(pkg));
    }

    @Override
    public void deletePackage(Long id) {
        LearningPackage  pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));
        packageRepository.delete(pkg);
    }

    @Override
    public Page<PackageResponseDTO> findPackagesWithFilters(String title, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Specification<LearningPackage> spec = Specification
                .where(LearningPackageSpecification.hasTitle(title))
                .and(LearningPackageSpecification.hasMinPrice(minPrice))
                .and(LearningPackageSpecification.hasMaxPrice(maxPrice));

        return packageRepository.findAll(spec, pageable).map(packageMapper::toResponseDTO);
    }


    @Transactional
    public PackageResponseDTO createFullPackage(CreatePackageRequest dto) {
        LearningPackage learningPackage = createPackageMapper.toEntity(dto);

        for (CreateSkillDTO skillDTO : dto.skills()) {
            Skill skill = createPackageMapper.toSkill(skillDTO);
            skill.setLearningPackage(learningPackage);

            for (CreateTopicDTO topicDTO : skillDTO.topics()) {
                Topic topic = createPackageMapper.toTopic(topicDTO);
                topic.setSkill(skill);

                for (CreateSubTopicDTO subTopicDTO : topicDTO.subTopics()) {
                    SubTopic subTopic = createPackageMapper.toSubTopic(subTopicDTO);
                    subTopic.setTopic(topic);

                    subTopic.setVideos(
                            subTopicDTO.videos().stream().map(v -> {
                                Video video = createPackageMapper.toVideo(v);
                                video.setSubTopic(subTopic);
                                return video;
                            }).collect(Collectors.toList())
                    );

                    subTopic.setStudyMaterials(
                            subTopicDTO.studyMaterials().stream().map(sm -> {
                                StudyMaterial material = createPackageMapper.toStudyMaterial(sm);
                                material.setSubTopic(subTopic);
                                return material;
                            }).collect(Collectors.toList())
                    );

                    topic.getSubTopics().add(subTopic);
                }

                skill.getTopics().add(topic);
            }

            learningPackage.getSkills().add(skill);
        }

        packageRepository.save(learningPackage);
        return packageMapper.toResponseDTO(learningPackage);
    }

}

