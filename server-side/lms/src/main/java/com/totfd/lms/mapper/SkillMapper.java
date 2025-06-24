package com.totfd.lms.mapper;

import com.totfd.lms.dto.skill.request.SkillRequestDTO;
import com.totfd.lms.dto.skill.response.SkillResponseDTO;
import com.totfd.lms.entity.Skill;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "learningPackage.id", source = "packageId")
    Skill toEntity(SkillRequestDTO dto);

    @Mapping(target = "packageId", source = "learningPackage.id")
    @Mapping(target = "packageTitle", source = "learningPackage.title")
    SkillResponseDTO toResponseDTO(Skill skill);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "learningPackage.id", source = "packageId")
    void updateFromDto(SkillRequestDTO dto, @MappingTarget Skill skill);
}

