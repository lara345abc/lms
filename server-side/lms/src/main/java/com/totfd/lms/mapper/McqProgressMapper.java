package com.totfd.lms.mapper;


import com.totfd.lms.dto.mcqProgress.McqProgressDTO;
import com.totfd.lms.entity.McqProgress;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface McqProgressMapper {
    McqProgressDTO toDto(McqProgress entity);
    McqProgress fromDto(McqProgressDTO dto);
}

