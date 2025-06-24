package com.totfd.lms.mapper;

import com.totfd.lms.dto.studymaterial.request.StudyMaterialRequestDTO;
import com.totfd.lms.dto.studymaterial.response.StudyMaterialResponseDTO;
import com.totfd.lms.entity.StudyMaterial;
import com.totfd.lms.entity.SubTopic;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudyMaterialMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subTopic", source = "subTopicId", qualifiedByName = "mapSubTopicFromId")
    StudyMaterial toEntity(StudyMaterialRequestDTO dto);

    @Mapping(target = "subTopicId", source = "subTopic.id")
    @Mapping(target = "subTopicTitle", source = "subTopic.title")
    StudyMaterialResponseDTO toResponseDTO(StudyMaterial studyMaterial);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "subTopic", source = "subTopicId", qualifiedByName = "mapSubTopicFromId")
    void updateFromDto(StudyMaterialRequestDTO dto, @MappingTarget StudyMaterial studyMaterial);

    @Named("mapSubTopicFromId")
    static SubTopic mapSubTopicFromId(Long subTopicId) {
        if (subTopicId == null) return null;
        return SubTopic.builder().id(subTopicId).build();
    }
}
