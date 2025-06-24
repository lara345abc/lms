package com.totfd.lms.dto.subtopics.response;

import com.totfd.lms.dto.studymaterial.response.StudyMaterialResponseDTO;
import com.totfd.lms.dto.video.response.VideoResponseDTO;

import java.util.List;

public record SubTopicResponseDTO(
        Long id,
        String title,
        String description,
        Long topicId,
        String topicTitle,
        String topicDescription,
        List<VideoResponseDTO> videos,
        List<StudyMaterialResponseDTO> studyMaterials
) {

}
