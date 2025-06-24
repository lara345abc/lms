package com.totfd.lms.mapper;

import com.totfd.lms.dto.skill.request.SkillRequestDTO;
import com.totfd.lms.dto.topic.request.TopicRequestDTO;
import com.totfd.lms.dto.topic.response.TopicResponseDTO;
import com.totfd.lms.entity.Skill;
import com.totfd.lms.entity.Topic;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = SubTopicMapper.class)
public interface TopicMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "learningPackage", ignore = true)
    Skill toEntity(SkillRequestDTO dto);

    // Convert Topic -> TopicResponseDTO (with nested subTopics)
    @Mapping(target = "skillId", source = "skill.id")
    @Mapping(target = "skillTitle", source = "skill.title")
    @Mapping(target = "subTopics", source = "subTopics")  // needs proper OneToMany setup
    TopicResponseDTO toResponseDTO(Topic topic);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "skill", source = "skillId", qualifiedByName = "mapSkillFromId")
    @Mapping(target = "createdAt", ignore = true)
    Topic toEntity(TopicRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "skill", source = "skillId", qualifiedByName = "mapSkillFromId")
    void updateFromDto(TopicRequestDTO dto, @MappingTarget Topic topic);

    @Named("mapSkillFromId")
    static Skill mapSkillFromId(Long skillId) {
        if (skillId == null) return null;
        return Skill.builder().id(skillId).build();
    }
}
