package com.totfd.lms.service;

import com.totfd.lms.dto.mcqpoint.McqPointsDTO;

import java.util.List;

public interface McqPointsService {
    McqPointsDTO createMcqPoints(McqPointsDTO dto);
    McqPointsDTO getById(Long id);
    List<McqPointsDTO> getByUserId(Long userId);
    List<McqPointsDTO> getByMcqId(Long mcqId);
}

