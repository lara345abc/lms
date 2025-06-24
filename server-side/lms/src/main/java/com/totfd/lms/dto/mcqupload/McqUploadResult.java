package com.totfd.lms.dto.mcqupload;

import com.totfd.lms.dto.mcq.McqDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class McqUploadResult {
    private List<McqDTO> validMcqs;
    private List<String> skippedRows; // detailed error lines
}

