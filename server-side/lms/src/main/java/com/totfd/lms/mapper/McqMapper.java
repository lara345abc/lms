package com.totfd.lms.mapper;

import com.totfd.lms.dto.mcq.McqDTO;
import com.totfd.lms.entity.Mcq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface McqMapper {
    McqDTO toDto(Mcq mcq);
    Mcq fromDto(McqDTO mcqDTO);
}