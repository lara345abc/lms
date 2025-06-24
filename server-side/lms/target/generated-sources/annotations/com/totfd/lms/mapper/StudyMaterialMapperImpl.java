package com.totfd.lms.mapper;

import com.totfd.lms.dto.studymaterial.request.StudyMaterialRequestDTO;
import com.totfd.lms.dto.studymaterial.response.StudyMaterialResponseDTO;
import com.totfd.lms.entity.StudyMaterial;
import com.totfd.lms.entity.SubTopic;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-24T18:40:57+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class StudyMaterialMapperImpl implements StudyMaterialMapper {

    @Override
    public StudyMaterial toEntity(StudyMaterialRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        StudyMaterial.StudyMaterialBuilder studyMaterial = StudyMaterial.builder();

        studyMaterial.subTopic( StudyMaterialMapper.mapSubTopicFromId( dto.subTopicId() ) );
        studyMaterial.pdfUrl( dto.pdfUrl() );
        studyMaterial.version( dto.version() );
        studyMaterial.name( dto.name() );
        studyMaterial.isLatest( dto.isLatest() );

        return studyMaterial.build();
    }

    @Override
    public StudyMaterialResponseDTO toResponseDTO(StudyMaterial studyMaterial) {
        if ( studyMaterial == null ) {
            return null;
        }

        Long subTopicId = null;
        String subTopicTitle = null;
        Long id = null;
        String name = null;
        String pdfUrl = null;
        Integer version = null;
        Boolean isLatest = null;

        subTopicId = studyMaterialSubTopicId( studyMaterial );
        subTopicTitle = studyMaterialSubTopicTitle( studyMaterial );
        id = studyMaterial.getId();
        name = studyMaterial.getName();
        pdfUrl = studyMaterial.getPdfUrl();
        version = studyMaterial.getVersion();
        isLatest = studyMaterial.getIsLatest();

        StudyMaterialResponseDTO studyMaterialResponseDTO = new StudyMaterialResponseDTO( id, subTopicId, name, subTopicTitle, pdfUrl, version, isLatest );

        return studyMaterialResponseDTO;
    }

    @Override
    public void updateFromDto(StudyMaterialRequestDTO dto, StudyMaterial studyMaterial) {
        if ( dto == null ) {
            return;
        }

        if ( dto.subTopicId() != null ) {
            studyMaterial.setSubTopic( StudyMaterialMapper.mapSubTopicFromId( dto.subTopicId() ) );
        }
        if ( dto.pdfUrl() != null ) {
            studyMaterial.setPdfUrl( dto.pdfUrl() );
        }
        if ( dto.version() != null ) {
            studyMaterial.setVersion( dto.version() );
        }
        if ( dto.name() != null ) {
            studyMaterial.setName( dto.name() );
        }
        if ( dto.isLatest() != null ) {
            studyMaterial.setIsLatest( dto.isLatest() );
        }
    }

    private Long studyMaterialSubTopicId(StudyMaterial studyMaterial) {
        if ( studyMaterial == null ) {
            return null;
        }
        SubTopic subTopic = studyMaterial.getSubTopic();
        if ( subTopic == null ) {
            return null;
        }
        Long id = subTopic.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String studyMaterialSubTopicTitle(StudyMaterial studyMaterial) {
        if ( studyMaterial == null ) {
            return null;
        }
        SubTopic subTopic = studyMaterial.getSubTopic();
        if ( subTopic == null ) {
            return null;
        }
        String title = subTopic.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }
}
