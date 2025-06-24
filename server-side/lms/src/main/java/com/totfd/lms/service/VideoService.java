package com.totfd.lms.service;

import com.totfd.lms.dto.video.request.VideoRequestDTO;
import com.totfd.lms.dto.video.response.VideoResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    VideoResponseDTO create(VideoRequestDTO dto);
    VideoResponseDTO getById(Long id);
    List<VideoResponseDTO> getAll();
    VideoResponseDTO update(Long id, VideoRequestDTO dto);
    void delete(Long id);
    void uploadVideoAndThumbnail(Long subTopicId, MultipartFile video, MultipartFile thumbnail);
}
