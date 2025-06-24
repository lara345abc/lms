package com.totfd.lms.service;

import com.totfd.lms.dto.studymaterial.request.StudyMaterialRequestDTO;
import com.totfd.lms.dto.studymaterial.response.StudyMaterialResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudyMaterialService {
    StudyMaterialResponseDTO create(StudyMaterialRequestDTO dto);
    StudyMaterialResponseDTO getById(Long id);
    List<StudyMaterialResponseDTO> getAll();
    StudyMaterialResponseDTO update(Long id, StudyMaterialRequestDTO dto);
    void delete(Long id);
    void uploadStudyMaterial(Long subTopicId, String name, MultipartFile pdfFile);
}
