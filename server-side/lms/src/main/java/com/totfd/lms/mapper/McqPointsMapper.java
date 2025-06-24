package com.totfd.lms.mapper;

import com.totfd.lms.dto.mcqpoint.McqPointsDTO;
import com.totfd.lms.entity.McqPoints;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface McqPointsMapper {
    McqPointsDTO toDto(McqPoints entity);
    McqPoints fromDto(McqPointsDTO dto);
}

