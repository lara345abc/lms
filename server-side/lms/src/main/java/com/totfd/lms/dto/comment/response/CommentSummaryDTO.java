package com.totfd.lms.dto.comment.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentSummaryDTO {
    private Double averageRating;
    private Long totalComments;
}
