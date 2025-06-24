package com.totfd.lms.mapper;

import com.totfd.lms.dto.learnigPackages.request.CreatePackageRequest;
import com.totfd.lms.dto.skill.request.CreateSkillDTO;
import com.totfd.lms.dto.studymaterial.request.CreateStudyMaterialDTO;
import com.totfd.lms.dto.subtopics.request.CreateSubTopicDTO;
import com.totfd.lms.dto.topic.request.CreateTopicDTO;
import com.totfd.lms.dto.video.request.CreateVideoDTO;
import com.totfd.lms.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreatePackageMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    LearningPackage toEntity(CreatePackageRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Skill toSkill(CreateSkillDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Topic toTopic(CreateTopicDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    SubTopic toSubTopic(CreateSubTopicDTO dto);

    @Mapping(target = "id", ignore = true)
    Video toVideo(CreateVideoDTO dto);

    @Mapping(target = "id", ignore = true)
    StudyMaterial toStudyMaterial(CreateStudyMaterialDTO dto);
}

