package com.totfd.lms.controller;

import com.totfd.lms.dto.mcq.McqDTO;
import com.totfd.lms.dto.mcqupload.McqUploadResult;
import com.totfd.lms.payload.ApiResponse;
import com.totfd.lms.service.impl.McqServiceImpl;
import com.totfd.lms.utils.ExcelMcqParser;
import com.totfd.lms.utils.ExcelMcqWithSubTopicParser;
import com.totfd.lms.utils.McqExcelTemplateGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mcqs")
@RequiredArgsConstructor
public class McqController {

    private final McqServiceImpl mcqService;
    private final ExcelMcqParser excelMcqParser;
    private final McqExcelTemplateGenerator mcqExcelTemplateGenerator;
    private final ExcelMcqWithSubTopicParser excelMcqWithSubTopicParser;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<McqDTO>> createMcq(@RequestBody McqDTO mcqDTO, HttpServletRequest request) {
        McqDTO created = mcqService.createMcq(mcqDTO);
        return new ResponseEntity<>(
                ApiResponse.success(created, "MCQ Created", HttpStatus.CREATED.value(), request.getRequestURI()),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<McqDTO>> updateMcq(@RequestParam Long id, @RequestBody McqDTO mcqDTO, HttpServletRequest request) {
        McqDTO updated = mcqService.updateMcq(id, mcqDTO);
        return new ResponseEntity<>(
                ApiResponse.success(updated, "MCQ Updated", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteMcq(@RequestParam Long id, HttpServletRequest request) {
        mcqService.deleteMcq(id);
        return new ResponseEntity<>(
                ApiResponse.success("Deleted successfully", "MCQ Deleted", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/getById")
    public ResponseEntity<ApiResponse<McqDTO>> getMcqById(@RequestParam Long id, HttpServletRequest request) {
        McqDTO dto = mcqService.getMcqById(id);
        return new ResponseEntity<>(
                ApiResponse.success(dto, "MCQ Found", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<McqDTO>>> getAllMcqs(HttpServletRequest request) {
        List<McqDTO> list = mcqService.getAllMcqs();
        return new ResponseEntity<>(
                ApiResponse.success(list, "All MCQs", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/getBySubTopic")
    public ResponseEntity<ApiResponse<List<McqDTO>>> getBySubTopic(@RequestParam Long subTopicId, HttpServletRequest request) {
        List<McqDTO> list = mcqService.getMcqsBySubTopicId(subTopicId);
        return new ResponseEntity<>(
                ApiResponse.success(list, "MCQs by SubTopic", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/getBySubTopics")
    public ResponseEntity<ApiResponse<List<McqDTO>>> getBySubTopics(@RequestParam List<Long> subTopicIds, HttpServletRequest request) {
        List<McqDTO> list = mcqService.getMcqsBySubTopicIds(subTopicIds);
        return new ResponseEntity<>(
                ApiResponse.success(list, "MCQs by SubTopics", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }


    @PostMapping("/uploadExcel")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadExcel(@RequestParam("file") MultipartFile file,
                                                                        @RequestParam Long subTopicId,
                                                                        HttpServletRequest request) {
        try {
            McqUploadResult result = excelMcqParser.parseAndValidate(file.getInputStream(), subTopicId);
            result.getValidMcqs().forEach(mcqService::createMcq);

            Map<String, Object> response = new HashMap<>();
            response.put("uploaded", result.getValidMcqs().size());
            response.put("skipped", result.getSkippedRows().size());
            response.put("skippedDetails", result.getSkippedRows());

            return new ResponseEntity<>(
                    ApiResponse.success(response, "Excel Upload Summary", HttpStatus.OK.value(), request.getRequestURI()),
                    HttpStatus.OK
            );
        } catch (IOException e) {
            throw new RuntimeException("Excel Processing Failed: " + e.getMessage());
        }
    }


    @GetMapping("/download-template")
    public ResponseEntity<Resource> downloadTemplate() throws IOException {
        ByteArrayInputStream templateStream = mcqExcelTemplateGenerator.generateTemplate();
        InputStreamResource resource = new InputStreamResource(templateStream); // cast to interface

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mcq-template.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body((Resource) resource);
    }

    @GetMapping("/download-template-with-subtopic")
    public ResponseEntity<Resource> downloadTemplateWithSubTopic() throws IOException {
        ByteArrayInputStream templateStream = mcqExcelTemplateGenerator.generateTemplateWithSubTopic();
        InputStreamResource resource = new InputStreamResource(templateStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mcq-template-with-subtopic.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }

    @PostMapping("/uploadExcelWithSubTopic")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadExcelWithSubTopic(@RequestParam("file") MultipartFile file,
                                                                                    HttpServletRequest request) {
        try {
            McqUploadResult result = excelMcqWithSubTopicParser.parseAndValidate(file.getInputStream());
            result.getValidMcqs().forEach(mcqService::createMcq);

            Map<String, Object> response = new HashMap<>();
            response.put("uploaded", result.getValidMcqs().size());
            response.put("skipped", result.getSkippedRows().size());
            response.put("skippedDetails", result.getSkippedRows());

            return new ResponseEntity<>(
                    ApiResponse.success(response, "Bulk MCQ Upload Summary", HttpStatus.OK.value(), request.getRequestURI()),
                    HttpStatus.OK
            );
        } catch (IOException e) {
            throw new RuntimeException("Excel Processing Failed: " + e.getMessage());
        }
    }

}

