package com.totfd.lms.dto.mcqattempt;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class McqAttemptDTO {
    private Long id;
//    private Long userId;
//    private Long mcqId;
    private Long subTopicId;
    private Integer attemptNumber;
    private Integer score;
    private Integer totalMarks;
    private LocalDateTime attemptedAt;
}

