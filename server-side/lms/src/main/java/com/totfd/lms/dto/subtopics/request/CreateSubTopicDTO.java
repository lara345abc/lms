package com.totfd.lms.dto.subtopics.request;

import com.totfd.lms.dto.studymaterial.request.CreateStudyMaterialDTO;
import com.totfd.lms.dto.video.request.CreateVideoDTO;

import java.util.List;

public record CreateSubTopicDTO(
        String title,
        String description,
        List<CreateVideoDTO> videos,
        List<CreateStudyMaterialDTO> studyMaterials
) {}
