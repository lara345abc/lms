package com.totfd.lms.controller;

import com.totfd.lms.dto.subtopics.request.SubTopicRequestDTO;
import com.totfd.lms.dto.subtopics.response.SubTopicResponseDTO;
import com.totfd.lms.payload.ApiResponse;
import com.totfd.lms.service.impl.SubTopicServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subTopic")
@RequiredArgsConstructor
public class SubTopicController {
    private final SubTopicServiceImpl subTopicService;

    @PostMapping("/addTopic")
    public ResponseEntity<ApiResponse<SubTopicResponseDTO>> createSubTopic(@RequestBody SubTopicRequestDTO dto, HttpServletRequest request) {
        SubTopicResponseDTO subtopic = subTopicService.createSubTopic(dto);
        return new ResponseEntity<>(
                ApiResponse.success(subtopic, "Topic created successfully", HttpStatus.CREATED.value(), request.getRequestURI()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubTopicResponseDTO>> getSubTopicById(
            @PathVariable Long id,
            HttpServletRequest request) {

        SubTopicResponseDTO dto = subTopicService.getSubTopicById(id);

        return new ResponseEntity<>(
                ApiResponse.success(dto, "SubTopic fetched successfully", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<SubTopicResponseDTO>>> getAllTopics(HttpServletRequest request) {
        List<SubTopicResponseDTO> subtopics = subTopicService.getAllSubTopics();
        return new ResponseEntity<>(
                ApiResponse.success(subtopics, "All subtopics fetched", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<SubTopicResponseDTO>>> getAllTopicsWithPage(@RequestParam(defaultValue = "0") int page,
                                                                                    @RequestParam(defaultValue = "20") int size, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SubTopicResponseDTO> subTopicPage = subTopicService.getAllSubTopicsWithPage(pageable);
        return new ResponseEntity<>(
                ApiResponse.success(subTopicPage, "Paginated topics fetched", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubTopicResponseDTO>> updateTopic(@PathVariable Long id, @RequestBody SubTopicRequestDTO dto, HttpServletRequest request) {
        SubTopicResponseDTO updated = subTopicService.updateSubTopic(id, dto);
        return new ResponseEntity<>(
                ApiResponse.success(updated, "Sub Topic updated successfully", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTopic(@PathVariable Long id, HttpServletRequest request) {
        subTopicService.deleteSubTopic(id);
        return new ResponseEntity<>(
                ApiResponse.success(null, "Sub Topic deleted successfully", HttpStatus.NO_CONTENT.value(), request.getRequestURI()),
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
        Page<SubTopicResponseDTO> result = subTopicService.findSubTopicsWithFilters(title,  pageable);

        return ResponseEntity.ok(ApiResponse.success(result, "Filtered paged packages fetched", 200, request.getRequestURI()));
    }

    @GetMapping("/getByIds")
    public ResponseEntity<ApiResponse<List<SubTopicResponseDTO>>> getSubTopicsByIds(
            @RequestParam List<Long> subTopicIds,
            HttpServletRequest request) {

        List<SubTopicResponseDTO> subTopics = subTopicService.getSubTopicsByIds(subTopicIds);

        return new ResponseEntity<>(
                ApiResponse.success(subTopics, "SubTopics fetched successfully", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

}
