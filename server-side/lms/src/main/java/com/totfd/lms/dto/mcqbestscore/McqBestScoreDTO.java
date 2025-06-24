package com.totfd.lms.dto.mcqbestscore;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class McqBestScoreDTO {
    private Long id;
    private Long userId;
    private Long subTopicId;
    private Integer bestScore;
    private Integer totalMarks;
    private LocalDateTime lastUpdated;
}

