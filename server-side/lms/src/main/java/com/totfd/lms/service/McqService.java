package com.totfd.lms.service;

import com.totfd.lms.dto.mcq.McqDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface McqService {
    McqDTO createMcq(McqDTO mcqDTO);
    McqDTO updateMcq(Long id, McqDTO mcqDTO);
    void deleteMcq(Long id);
    McqDTO getMcqById(Long id);
    List<McqDTO> getAllMcqs();
    List<McqDTO> getMcqsBySubTopicId(Long subTopicId);
    List<McqDTO> getMcqsBySubTopicIds(List<Long>  subTopicId);
    void uploadMcqsFromExcel(MultipartFile file, Long subTopicId) throws IOException;

}

