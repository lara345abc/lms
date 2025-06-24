package com.totfd.lms.dto.comment.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRatingDistributionDTO {
    private Map<Integer, Long> ratingCounts; // e.g., {5: 10, 4: 2, 3: 1}
}
