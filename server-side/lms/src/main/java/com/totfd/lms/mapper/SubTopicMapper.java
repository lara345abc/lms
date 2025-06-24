package com.totfd.lms.mapper;

import com.totfd.lms.dto.subtopics.request.SubTopicRequestDTO;
import com.totfd.lms.dto.subtopics.response.SubTopicResponseDTO;
import com.totfd.lms.entity.SubTopic;
import com.totfd.lms.entity.Topic;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = { VideoMapper.class, StudyMaterialMapper.class } // ensure these exist
)
public interface SubTopicMapper {

    @Mapping(target = "topicId", source = "topic.id")
    @Mapping(target = "topicTitle", source = "topic.title")
    @Mapping(target = "topicDescription", source = "topic.description")
    @Mapping(target = "videos", source = "videos")
    @Mapping(target = "studyMaterials", source = "studyMaterials")
    SubTopicResponseDTO toResponseDTO(SubTopic subTopic);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "topic", source = "topicId", qualifiedByName = "mapTopicFromId")
    @Mapping(target = "createdAt", ignore = true)
    SubTopic toEntity(SubTopicRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(SubTopicRequestDTO dto, @MappingTarget SubTopic subTopic);

    @Named("mapTopicFromId")
    static Topic mapTopicFromId(Long topicId) {
        if (topicId == null) return null;
        return Topic.builder().id(topicId).build();
    }
}
