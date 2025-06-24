package com.totfd.lms.dto.comment.response;

import java.time.LocalDateTime;

public record CommentResponseDTO(
        Long id,
        String userName,
        Long videoId,
        String comment,
        Integer rating,
        LocalDateTime createdAt
) {}
