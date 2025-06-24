package com.totfd.lms.controller;

import com.totfd.lms.dto.video.request.VideoRequestDTO;
import com.totfd.lms.dto.video.response.VideoResponseDTO;
import com.totfd.lms.payload.ApiResponse;
import com.totfd.lms.service.impl.VideoServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoServiceImpl videoService;

    @PostMapping
    public ResponseEntity<ApiResponse<VideoResponseDTO>> createVideo(
            @RequestBody VideoRequestDTO dto,
            HttpServletRequest request) {
        VideoResponseDTO response = videoService.create(dto);
        return new ResponseEntity<>(
                ApiResponse.success(response, "Video created successfully", HttpStatus.CREATED.value(), request.getRequestURI()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VideoResponseDTO>> getVideoById(
            @PathVariable Long id,
            HttpServletRequest request) {
        VideoResponseDTO response = videoService.getById(id);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Video fetched successfully", HttpStatus.OK.value(), request.getRequestURI())
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VideoResponseDTO>>> getAllVideos(HttpServletRequest request) {
        List<VideoResponseDTO> response = videoService.getAll();
        return ResponseEntity.ok(
                ApiResponse.success(response, "All videos fetched successfully", HttpStatus.OK.value(), request.getRequestURI())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VideoResponseDTO>> updateVideo(
            @PathVariable Long id,
            @RequestBody VideoRequestDTO dto,
            HttpServletRequest request) {
        VideoResponseDTO response = videoService.update(id, dto);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Video updated successfully", HttpStatus.OK.value(), request.getRequestURI())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVideo(
            @PathVariable Long id,
            HttpServletRequest request) {
        videoService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Video deleted successfully", HttpStatus.OK.value(), request.getRequestURI())
        );
    }

    @PostMapping("/upload/{subTopicId}")
    public ResponseEntity<ApiResponse<String>> uploadVideoAndThumbnail(
            @PathVariable Long subTopicId,
            @RequestParam("video") MultipartFile video,
            @RequestParam("thumbnail") MultipartFile thumbnail,
            HttpServletRequest request) {
        videoService.uploadVideoAndThumbnail(subTopicId, video, thumbnail);
        return new ResponseEntity<>(
                ApiResponse.success(null, "Video and thumbnail uploaded successfully", HttpStatus.CREATED.value(), request.getRequestURI()),
                HttpStatus.CREATED
        );
    }
}
