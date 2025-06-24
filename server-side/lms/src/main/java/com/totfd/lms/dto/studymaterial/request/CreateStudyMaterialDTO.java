package com.totfd.lms.dto.studymaterial.request;

public record CreateStudyMaterialDTO(
        String pdfUrl,
        Integer version,
        Boolean isLatest
) {}

