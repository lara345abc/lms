package com.totfd.lms.dto.mcqpoint;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class McqPointsDTO {
    private Long id;
    private Long userId;
    private Long mcqId;
    private Integer points;
    private LocalDateTime createdAt;
}

