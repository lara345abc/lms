package com.totfd.lms.mapper;

import com.totfd.lms.dto.mcqbestscore.McqBestScoreDTO;
import com.totfd.lms.entity.McqBestScore;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface McqBestScoreMapper {
    McqBestScoreDTO toDto(McqBestScore entity);
    McqBestScore fromDto(McqBestScoreDTO dto);
}

