package com.totfd.lms.service;

import com.totfd.lms.dto.mcqbestscore.McqBestScoreDTO;

import java.util.List;

public interface McqBestScoreService {
    McqBestScoreDTO saveOrUpdate(McqBestScoreDTO dto);
    McqBestScoreDTO getById(Long id);
    List<McqBestScoreDTO> getByUserId(Long userId);
    List<McqBestScoreDTO> getBySubTopicId(Long subTopicId);
    McqBestScoreDTO getByUserIdAndSubTopicId(Long userId, Long subTopicId);
}

