package com.totfd.lms.mapper;

import com.totfd.lms.dto.video.request.VideoRequestDTO;
import com.totfd.lms.dto.video.response.VideoResponseDTO;
import com.totfd.lms.entity.SubTopic;
import com.totfd.lms.entity.Video;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VideoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subTopic", source = "subTopicId", qualifiedByName = "mapSubTopicFromId")
    Video toEntity(VideoRequestDTO dto);

    @Mapping(target = "subTopicId", source = "subTopic.id")
    @Mapping(target = "subTopicTitle", source = "subTopic.title")
    VideoResponseDTO toResponseDTO(Video video);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "subTopic", source = "subTopicId", qualifiedByName = "mapSubTopicFromId")
    void updateFromDto(VideoRequestDTO dto, @MappingTarget Video video);

    @Named("mapSubTopicFromId")
    static SubTopic mapSubTopicFromId(Long subTopicId) {
        if (subTopicId == null) return null;
        return SubTopic.builder().id(subTopicId).build();
    }
}
