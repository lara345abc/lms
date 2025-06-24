package com.totfd.lms.controller;

import com.totfd.lms.dto.comment.request.CommentRequestDTO;
import com.totfd.lms.dto.comment.response.CommentRatingDistributionDTO;
import com.totfd.lms.dto.comment.response.CommentResponseDTO;
import com.totfd.lms.dto.comment.response.CommentSummaryDTO;
import com.totfd.lms.entity.Comment;
import com.totfd.lms.entity.Users;
import com.totfd.lms.mapper.CommentMapper;
import com.totfd.lms.payload.ApiResponse;
import com.totfd.lms.repository.CommentRepository;
import com.totfd.lms.repository.UsersRepository;
import com.totfd.lms.service.CommentService;
import com.totfd.lms.service.impl.CommentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UsersRepository usersRepository;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final CommentServiceImpl commentServiceImpl;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponseDTO>> create(
            @RequestBody CommentRequestDTO dto,
            HttpServletRequest request,
            Principal principal) {
        String email = principal.getName();
        CommentResponseDTO response = commentService.createComment(email, dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Comment created", 200, request.getRequestURI()));
    }

//    @PostMapping
//    public ResponseEntity<ApiResponse<CommentResponseDTO>> createComment(
//            @RequestBody CommentRequestDTO dto,
//            HttpServletRequest request,
//            Principal principal
//    ) {
//        String email = principal.getName();
//        CommentResponseDTO response = commentServiceImpl.createComment(email, dto);
//        return ResponseEntity.ok(ApiResponse.success(response, "Comment created", 200, request.getRequestURI()));
//    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> getById(@PathVariable Long id, HttpServletRequest request) {
        log.info("id====", id);
        CommentResponseDTO response = commentService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Fetched", 200, request.getRequestURI()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponseDTO>>> getAll(HttpServletRequest request) {
        return ResponseEntity.ok(ApiResponse.success(commentService.getAll(), "Fetched", 200, request.getRequestURI()));
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<ApiResponse<List<CommentResponseDTO>>> getByVideoId(@PathVariable Long videoId, HttpServletRequest request) {
        return ResponseEntity.ok(ApiResponse.success(commentService.getByVideoId(videoId), "Fetched", 200, request.getRequestURI()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> update(@PathVariable Long id, @RequestBody CommentRequestDTO dto, HttpServletRequest request) {
        CommentResponseDTO response = commentService.update(id, dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Updated", 200, request.getRequestURI()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id, HttpServletRequest request) {
        commentService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Comment deleted", "Success", 200, request.getRequestURI()));
    }

    @GetMapping("/summary/{videoId}")
    public ResponseEntity<ApiResponse<CommentSummaryDTO>> getSummaryByVideoId(
            @PathVariable Long videoId,
            HttpServletRequest request
    ) {
        CommentSummaryDTO summary = commentService.getSummaryByVideoId(videoId);
        return ResponseEntity.ok(ApiResponse.success(summary, "Summary fetched", 200, request.getRequestURI()));
    }

    @GetMapping("/ratings/{videoId}")
    public ResponseEntity<ApiResponse<CommentRatingDistributionDTO>> getRatingDistributionByVideoId(
            @PathVariable Long videoId,
            HttpServletRequest request
    ) {
        CommentRatingDistributionDTO dto = commentService.getRatingDistributionByVideoId(videoId);
        return ResponseEntity.ok(ApiResponse.success(dto, "Rating distribution fetched", 200, request.getRequestURI()));
    }


    @GetMapping("/video/{videoId}/paged")
    public ResponseEntity<ApiResponse<Page<CommentResponseDTO>>> getPagedCommentsByVideoId(
            @PathVariable Long videoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        Page<CommentResponseDTO> comments = commentService.getCommentsByVideoId(videoId, page, size);
        return ResponseEntity.ok(ApiResponse.success(comments, "Paged comments fetched", 200, request.getRequestURI()));
    }


}

