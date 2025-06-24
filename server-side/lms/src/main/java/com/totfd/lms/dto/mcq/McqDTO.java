package com.totfd.lms.dto.mcq;

import lombok.Data;

@Data
public class McqDTO {
    private Long id;
    private Long subTopicId;
    private String question;
    private String options;
    private String correctOption;
    private Integer version;
    private Boolean isLatest;
}
