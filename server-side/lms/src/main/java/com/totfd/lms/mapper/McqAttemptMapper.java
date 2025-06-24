package com.totfd.lms.mapper;

import com.totfd.lms.dto.mcqattempt.McqAttemptDTO;
import com.totfd.lms.entity.McqAttempt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface McqAttemptMapper {

    @Mapping(source = "subTopic.id", target = "subTopicId")
    McqAttemptDTO toDto(McqAttempt entity);

    @Mapping(source = "subTopicId", target = "subTopic.id")
    McqAttempt fromDto(McqAttemptDTO dto);
}

