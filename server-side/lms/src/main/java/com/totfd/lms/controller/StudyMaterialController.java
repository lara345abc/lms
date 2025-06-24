package com.totfd.lms.controller;

import com.totfd.lms.dto.studymaterial.request.StudyMaterialRequestDTO;
import com.totfd.lms.dto.studymaterial.response.StudyMaterialResponseDTO;
import com.totfd.lms.entity.StudyMaterial;
import com.totfd.lms.payload.ApiResponse;
import com.totfd.lms.repository.StudyMaterialRepository;
import com.totfd.lms.service.impl.StudyMaterialServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/study-materials")
@RequiredArgsConstructor
public class StudyMaterialController {

    private final StudyMaterialServiceImpl studyMaterialService;
    private final StudyMaterialRepository studyMaterialRepository;

    @PostMapping
    public ResponseEntity<ApiResponse<StudyMaterialResponseDTO>> createStudyMaterial(
            @RequestBody StudyMaterialRequestDTO dto,
            HttpServletRequest request) {
        StudyMaterialResponseDTO response = studyMaterialService.create(dto);
        return new ResponseEntity<>(
                ApiResponse.success(response, "Study Material created successfully", HttpStatus.CREATED.value(), request.getRequestURI()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudyMaterialResponseDTO>> getById(
            @PathVariable Long id,
            HttpServletRequest request) {
        StudyMaterialResponseDTO response = studyMaterialService.getById(id);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Study Material fetched successfully", HttpStatus.OK.value(), request.getRequestURI())
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudyMaterialResponseDTO>>> getAll(
            HttpServletRequest request) {
        List<StudyMaterialResponseDTO> response = studyMaterialService.getAll();
        return ResponseEntity.ok(
                ApiResponse.success(response, "All Study Materials fetched successfully", HttpStatus.OK.value(), request.getRequestURI())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudyMaterialResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody StudyMaterialRequestDTO dto,
            HttpServletRequest request) {
        StudyMaterialResponseDTO response = studyMaterialService.update(id, dto);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Study Material updated successfully", HttpStatus.OK.value(), request.getRequestURI())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            HttpServletRequest request) {
        studyMaterialService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Study Material deleted successfully", HttpStatus.OK.value(), request.getRequestURI())
        );
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Void>> uploadPdf(
            @RequestParam Long subTopicId,
            @RequestParam String name,
            @RequestParam MultipartFile pdf,
            HttpServletRequest request) {

        studyMaterialService.uploadStudyMaterial(subTopicId, name, pdf);
        return new ResponseEntity<>(
                ApiResponse.success(null, "PDF uploaded and saved successfully", HttpStatus.CREATED.value(), request.getRequestURI()),
                HttpStatus.CREATED
        );
    }


    @GetMapping("/download-all/{subTopicId}")
    public void downloadAllPdfs(@PathVariable Long subTopicId, HttpServletResponse response) throws IOException {
        List<StudyMaterial> materials = studyMaterialRepository.findBySubTopicId(subTopicId);

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=resources.zip");

        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
            for (StudyMaterial material : materials) {
                InputStream is = new URL(material.getPdfUrl()).openStream();
                zos.putNextEntry(new ZipEntry("resource-" + material.getId() + ".pdf"));
                StreamUtils.copy(is, zos);
                zos.closeEntry();
                is.close();
            }
        }
    }

}
