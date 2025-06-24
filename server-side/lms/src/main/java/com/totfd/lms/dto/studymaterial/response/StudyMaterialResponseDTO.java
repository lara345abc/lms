package com.totfd.lms.dto.studymaterial.response;

public record StudyMaterialResponseDTO(
        Long id,
        Long subTopicId,
        String name,
        String subTopicTitle,
        String pdfUrl,
        Integer version,
        Boolean isLatest
) {}
