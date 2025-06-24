package com.totfd.lms.mapper;

import com.totfd.lms.dto.skill.response.SkillResponseDTO;
import com.totfd.lms.dto.learnigPackages.request.PackageRequestDTO;
import com.totfd.lms.dto.learnigPackages.response.PackageResponseDTO;
import com.totfd.lms.entity.LearningPackage;
import com.totfd.lms.entity.Skill;
import org.mapstruct.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface PackageMapper {

    LearningPackage toEntity(PackageRequestDTO dto);

    @Mapping(target = "skills", source = "skills")
    PackageResponseDTO toResponseDTO(LearningPackage entity);

    @Mapping(source = "learningPackage.id", target = "packageId")
    @Mapping(source = "learningPackage.title", target = "packageTitle")
    SkillResponseDTO toSkillResponseDTO(Skill skill);

    List<SkillResponseDTO> mapSkills(List<Skill> skills);

    default List<SkillResponseDTO> mapSkills(Collection<Skill> skills) {
        if (skills == null) return List.of();
        return skills.stream().map(this::toSkillResponseDTO).toList();
    }



    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePackageFromDto(PackageRequestDTO dto, @MappingTarget LearningPackage entity);
}
