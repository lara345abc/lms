package com.totfd.lms.service.impl;

import com.totfd.lms.dto.mcq.McqDTO;
import com.totfd.lms.dto.mcqupload.McqUploadResult;
import com.totfd.lms.entity.Mcq;
import com.totfd.lms.entity.SubTopic;
import com.totfd.lms.exceptions.ResourceNotFoundException;
import com.totfd.lms.mapper.McqMapper;
import com.totfd.lms.repository.McqRepository;
import com.totfd.lms.repository.SubTopicRepository;
import com.totfd.lms.service.McqService;
import com.totfd.lms.utils.ExcelMcqParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class McqServiceImpl implements McqService {

    private final McqRepository mcqRepository;
    private final SubTopicRepository subTopicRepository;
    private final McqMapper mcqMapper;
    private final ExcelMcqParser excelMcqParser;

    @Override
    public McqDTO createMcq(McqDTO mcqDTO) {
        SubTopic subTopic = subTopicRepository.findById(mcqDTO.getSubTopicId())
                .orElseThrow(() -> new ResourceNotFoundException("SubTopic not found"));

        Mcq mcq = mcqMapper.fromDto(mcqDTO);
        mcq.setSubTopic(subTopic);
        return mcqMapper.toDto(mcqRepository.save(mcq));
    }

    @Override
    public McqDTO updateMcq(Long id, McqDTO mcqDTO) {
        Mcq existingMcq = mcqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mcq not found"));

        existingMcq.setQuestion(mcqDTO.getQuestion());
        existingMcq.setOptions(mcqDTO.getOptions());
        existingMcq.setCorrectOption(mcqDTO.getCorrectOption());
        existingMcq.setVersion(mcqDTO.getVersion());
        existingMcq.setIsLatest(mcqDTO.getIsLatest());

        return mcqMapper.toDto(mcqRepository.save(existingMcq));
    }

    @Override
    public void deleteMcq(Long id) {
        if (!mcqRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mcq not found");
        }
        mcqRepository.deleteById(id);
    }

    @Override
    public McqDTO getMcqById(Long id) {
        Mcq mcq = mcqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mcq not found"));
        return mcqMapper.toDto(mcq);
    }

    @Override
    public List<McqDTO> getAllMcqs() {
        return mcqRepository.findAll()
                .stream()
                .map(mcqMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<McqDTO> getMcqsBySubTopicId(Long subTopicId) {
        List<Mcq> mcqs = mcqRepository.findBySubTopicId(subTopicId);
        return mcqs.stream().map(mcqMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<McqDTO> getMcqsBySubTopicIds(List<Long> subTopicIds) {
        List<Mcq> mcqs = mcqRepository.findBySubTopicIdIn(subTopicIds);
        return mcqs.stream().map(mcqMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void uploadMcqsFromExcel(MultipartFile file, Long subTopicId) throws IOException {
        McqUploadResult result = excelMcqParser.parseAndValidate(file.getInputStream(), subTopicId);
        List<McqDTO> validatedMcqs = result.getValidMcqs();

        for (McqDTO dto : validatedMcqs) {
            createMcq(dto);
        }

    }


}
