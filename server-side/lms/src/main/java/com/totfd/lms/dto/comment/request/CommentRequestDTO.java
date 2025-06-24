package com.totfd.lms.dto.comment.request;

public record CommentRequestDTO(
//        Long userId,
        Long videoId,
        String comment,
        Integer rating
) {}
