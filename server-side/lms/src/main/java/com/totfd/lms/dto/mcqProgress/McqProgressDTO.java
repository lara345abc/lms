package com.totfd.lms.dto.mcqProgress;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class McqProgressDTO {
    private Long id;
    private Long userId;
    private Long mcqId;
    private String status; // "started" or "completed"
    private LocalDateTime lastAccessedAt;
    private LocalDateTime createdAt;
}

