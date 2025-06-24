package com.totfd.lms.service.impl;

import com.totfd.lms.dto.video.request.VideoRequestDTO;
import com.totfd.lms.dto.video.response.VideoResponseDTO;
import com.totfd.lms.entity.*;
import com.totfd.lms.mapper.VideoMapper;
import com.totfd.lms.repository.SubTopicRepository;
import com.totfd.lms.repository.VideoRepository;
import com.totfd.lms.service.VideoService;
import com.totfd.lms.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;
    private final SubTopicRepository subTopicRepository;
    private final FileUploadUtil fileUploadUtil;

    @Override
    public VideoResponseDTO create(VideoRequestDTO dto) {
        Video video = videoMapper.toEntity(dto);
        return videoMapper.toResponseDTO(videoRepository.save(video));
    }

    @Override
    public VideoResponseDTO getById(Long id) {
        return videoMapper.toResponseDTO(
                videoRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("Video not found"))
        );
    }

    @Override
    public List<VideoResponseDTO> getAll() {
        return videoRepository.findAll()
                .stream()
                .map(videoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VideoResponseDTO update(Long id, VideoRequestDTO dto) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Video not found"));
        videoMapper.updateFromDto(dto, video);
        return videoMapper.toResponseDTO(videoRepository.save(video));
    }

    @Override
    public void delete(Long id) {
        if (!videoRepository.existsById(id)) {
            throw new NoSuchElementException("Video not found");
        }
        videoRepository.deleteById(id);
    }

    @Override
    public void uploadVideoAndThumbnail(Long subTopicId, MultipartFile video, MultipartFile thumbnail) {
        SubTopic subTopic = subTopicRepository.findById(subTopicId)
                .orElseThrow(() -> new RuntimeException("SubTopic not found"));

        Topic topic = subTopic.getTopic();
        Skill skill = topic.getSkill();
        LearningPackage learningPackage = skill.getLearningPackage();

        String folderPath = String.format("%s/%s/%s/%s",
                learningPackage.getTitle().replaceAll("\\s+", "-").toLowerCase(),
                skill.getTitle().replaceAll("\\s+", "-").toLowerCase(),
                topic.getTitle().replaceAll("\\s+", "-").toLowerCase(),
                subTopic.getTitle().replaceAll("\\s+", "-").toLowerCase()
        );

        try {
            String videoUrl = fileUploadUtil.uploadFile(folderPath + "/videos", video);
            String thumbnailUrl = fileUploadUtil.uploadFile(folderPath + "/thumbnails", thumbnail);

            Video newVideo = Video.builder()
                    .subTopic(subTopic)
                    .url(videoUrl)
                    .thumbnailUrl(thumbnailUrl)
                    .duration(null)
                    .version(1)
                    .isLatest(true)
                    .noOfViews(0L)
                    .build();

            videoRepository.save(newVideo);
        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

}
