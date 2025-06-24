package com.totfd.lms.controller;

import com.totfd.lms.dto.learnigPackages.response.PackageResponseDTO;
import com.totfd.lms.dto.topic.request.TopicRequestDTO;
import com.totfd.lms.dto.topic.response.TopicResponseDTO;
import com.totfd.lms.entity.Skill;
import com.totfd.lms.entity.SubTopic;
import com.totfd.lms.entity.Topic;
import com.totfd.lms.exceptions.ResourceNotFoundException;
import com.totfd.lms.mapper.TopicMapper;
import com.totfd.lms.payload.ApiResponse;
import com.totfd.lms.repository.SkillRepository;
import com.totfd.lms.repository.SubTopicRepository;
import com.totfd.lms.repository.TopicRepository;
import com.totfd.lms.service.impl.TopicServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicServiceImpl topicService;
    private final SkillRepository skillRepository;
    private final SubTopicRepository subTopicRepository;
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    @PostMapping("/addTopic")
    public ResponseEntity<ApiResponse<TopicResponseDTO>> createTopic(@RequestBody TopicRequestDTO dto, HttpServletRequest request) {
        TopicResponseDTO topic = topicService.createTopic(dto);
        return new ResponseEntity<>(
                ApiResponse.success(topic, "Topic created successfully", HttpStatus.CREATED.value(), request.getRequestURI()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/by-skill/{skillId}")
    public ResponseEntity<ApiResponse<List<TopicResponseDTO>>> getTopicsBySkill(@PathVariable Long skillId, HttpServletRequest request) {
        List<TopicResponseDTO> topics = topicService.getTopicsBySkillId(skillId);
        return new ResponseEntity<>(
                ApiResponse.success(topics, "Topics fetched successfully", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }



    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TopicResponseDTO>> getTopicById(@PathVariable Long id, HttpServletRequest request) {
        TopicResponseDTO topic = topicService.getTopicById(id);
        return new ResponseEntity<>(
                ApiResponse.success(topic, "Topic found", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TopicResponseDTO>>> getAllTopics(HttpServletRequest request) {
        List<TopicResponseDTO> topics = topicService.getAllTopics();
        return new ResponseEntity<>(
                ApiResponse.success(topics, "All topics fetched", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<TopicResponseDTO>>> getAllTopicsWithPage(  @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "20") int size, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TopicResponseDTO> topicPage = topicService.getAllTopicsWithPage(pageable);
        return new ResponseEntity<>(
                ApiResponse.success(topicPage, "Paginated topics fetched", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TopicResponseDTO>> updateTopic(@PathVariable Long id, @RequestBody TopicRequestDTO dto, HttpServletRequest request) {
        TopicResponseDTO updated = topicService.updateTopic(id, dto);
        return new ResponseEntity<>(
                ApiResponse.success(updated, "Topic updated successfully", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTopic(@PathVariable Long id, HttpServletRequest request) {
        topicService.deleteTopic(id);
        return new ResponseEntity<>(
                ApiResponse.success(null, "Topic deleted successfully", HttpStatus.NO_CONTENT.value(), request.getRequestURI()),
                HttpStatus.NO_CONTENT
        );
    }
    @GetMapping("/filtered")
    public ResponseEntity<ApiResponse<?>> getFilteredPackages(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TopicResponseDTO> result = topicService.findTopicsWithFilters(title,  pageable);

        return ResponseEntity.ok(ApiResponse.success(result, "Filtered paged packages fetched", 200, request.getRequestURI()));
    }

    @GetMapping("/getTopicWithDetails")
    public ResponseEntity<ApiResponse<TopicResponseDTO>> getTopicWithDetails(@RequestParam Long topicId, HttpServletRequest request) {
        TopicResponseDTO dto = topicService.getTopicWithDetails(topicId);
        return new ResponseEntity<>(
                ApiResponse.success(dto, "Topic with SubTopics, Videos, and StudyMaterials", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

}
