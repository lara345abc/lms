package com.totfd.lms.service.impl;

import com.totfd.lms.dto.studymaterial.request.StudyMaterialRequestDTO;
import com.totfd.lms.dto.studymaterial.response.StudyMaterialResponseDTO;
import com.totfd.lms.entity.*;
import com.totfd.lms.mapper.StudyMaterialMapper;
import com.totfd.lms.repository.StudyMaterialRepository;
import com.totfd.lms.repository.SubTopicRepository;
import com.totfd.lms.service.StudyMaterialService;
import com.totfd.lms.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyMaterialServiceImpl implements StudyMaterialService {

    private final StudyMaterialRepository studyMaterialRepository;
    private final StudyMaterialMapper studyMaterialMapper;
    private final SubTopicRepository subTopicRepository;
    private final FileUploadUtil fileUploadUtil;

    @Override
    public StudyMaterialResponseDTO create(StudyMaterialRequestDTO dto) {
        StudyMaterial material = studyMaterialMapper.toEntity(dto);
        return studyMaterialMapper.toResponseDTO(studyMaterialRepository.save(material));
    }

    @Override
    public StudyMaterialResponseDTO getById(Long id) {
        return studyMaterialMapper.toResponseDTO(
                studyMaterialRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("Study Material not found"))
        );
    }

    @Override
    public List<StudyMaterialResponseDTO> getAll() {
        return studyMaterialRepository.findAll()
                .stream()
                .map(studyMaterialMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudyMaterialResponseDTO update(Long id, StudyMaterialRequestDTO dto) {
        StudyMaterial material = studyMaterialRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Study Material not found"));
        studyMaterialMapper.updateFromDto(dto, material);
        return studyMaterialMapper.toResponseDTO(studyMaterialRepository.save(material));
    }

    @Override
    public void delete(Long id) {
        if (!studyMaterialRepository.existsById(id)) {
            throw new NoSuchElementException("Study Material not found");
        }
        studyMaterialRepository.deleteById(id);
    }

    @Override
    public void uploadStudyMaterial(Long subTopicId, String name, MultipartFile pdfFile) {
        SubTopic subTopic = subTopicRepository.findById(subTopicId)
                .orElseThrow(() -> new RuntimeException("SubTopic not found"));

        Topic topic = subTopic.getTopic();
        Skill skill = topic.getSkill();
        LearningPackage learningPackage = skill.getLearningPackage();

        // Construct folder path: package/skill/topic/subtopic
        String folderPath = String.format("%s/%s/%s/%s",
                learningPackage.getTitle().replaceAll("\\s+", "-").toLowerCase(),
                skill.getTitle().replaceAll("\\s+", "-").toLowerCase(),
                topic.getTitle().replaceAll("\\s+", "-").toLowerCase(),
                subTopic.getTitle().replaceAll("\\s+", "-").toLowerCase()
        );

        try {
            String pdfUrl = fileUploadUtil.uploadFile(folderPath + "/pdfs", pdfFile);

            StudyMaterial material = StudyMaterial.builder()
                    .subTopic(subTopic)
                    .name(name)
                    .pdfUrl(pdfUrl)
                    .version(1)
                    .isLatest(true)
                    .build();

            studyMaterialRepository.save(material);
        } catch (IOException e) {
            throw new RuntimeException("PDF upload failed", e);
        }
    }

}

