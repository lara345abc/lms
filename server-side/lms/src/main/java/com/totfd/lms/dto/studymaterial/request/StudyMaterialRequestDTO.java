package com.totfd.lms.dto.studymaterial.request;

public record StudyMaterialRequestDTO(
        Long subTopicId,
        String pdfUrl,
        String name,
        Integer version,
        Boolean isLatest
) {}
