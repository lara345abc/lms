package com.totfd.lms.service;

import com.totfd.lms.dto.mcqProgress.McqProgressDTO;

import java.util.List;

public interface McqProgressService {
    McqProgressDTO saveOrUpdateProgress(McqProgressDTO dto);
    McqProgressDTO getById(Long id);
    List<McqProgressDTO> getByUserId(Long userId);
    List<McqProgressDTO> getByMcqId(Long mcqId);
    McqProgressDTO getByUserIdAndMcqId(Long userId, Long mcqId);
}
