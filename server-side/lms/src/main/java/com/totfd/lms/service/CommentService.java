package com.totfd.lms.service;

import com.totfd.lms.dto.comment.request.CommentRequestDTO;
import com.totfd.lms.dto.comment.response.CommentRatingDistributionDTO;
import com.totfd.lms.dto.comment.response.CommentResponseDTO;
import com.totfd.lms.dto.comment.response.CommentSummaryDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {
//    CommentResponseDTO create(CommentRequestDTO dto);
    CommentResponseDTO getById(Long id);
    List<CommentResponseDTO> getAll();
    List<CommentResponseDTO> getByVideoId(Long videoId);
    CommentResponseDTO update(Long id, CommentRequestDTO dto);
    void delete(Long id);
    CommentResponseDTO createComment(String email, CommentRequestDTO dto);
    CommentSummaryDTO getSummaryByVideoId(Long videoId);
    CommentRatingDistributionDTO getRatingDistributionByVideoId(Long videoId);
    Page<CommentResponseDTO> getCommentsByVideoId(Long videoId, int page, int size);

}

