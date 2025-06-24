package com.totfd.lms.service;

import com.totfd.lms.dto.mcqattempt.McqAttemptDTO;

import java.util.List;

public interface McqAttemptService {
    McqAttemptDTO createAttempt(String email, McqAttemptDTO attemptDTO);
    List<McqAttemptDTO> getAttemptsByUserId(Long userId);
//    List<McqAttemptDTO> getAttemptsByMcqId(Long mcqId);
    List<McqAttemptDTO> getAttemptsBySubTopicId(Long subTopicId);
    McqAttemptDTO getAttemptById(Long id);
    List<McqAttemptDTO> getAttemptsByMultipleSubTopicIds(List<Long> subTopicIds);

}

