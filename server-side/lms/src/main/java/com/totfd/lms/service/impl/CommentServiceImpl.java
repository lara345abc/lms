package com.totfd.lms.service.impl;

import com.totfd.lms.dto.comment.request.CommentRequestDTO;
import com.totfd.lms.dto.comment.response.CommentRatingDistributionDTO;
import com.totfd.lms.dto.comment.response.CommentResponseDTO;
import com.totfd.lms.dto.comment.response.CommentSummaryDTO;
import com.totfd.lms.entity.Comment;
import com.totfd.lms.entity.Users;
import com.totfd.lms.exceptions.ResourceNotFoundException;
import com.totfd.lms.mapper.CommentMapper;
import com.totfd.lms.repository.CommentRepository;
import com.totfd.lms.repository.UsersRepository;
import com.totfd.lms.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UsersRepository usersRepository;

    @Override
    public CommentResponseDTO createComment(String email, CommentRequestDTO dto) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        Comment comment = commentMapper.toEntity(dto, user);
        comment.setCreatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);
        return commentMapper.toResponseDTO(saved);
    }


    @Override
    public CommentResponseDTO getById(Long id) {
        return commentRepository.findById(id)
                .map(commentMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
    }

    @Override
    public List<CommentResponseDTO> getAll() {
        return commentRepository.findAll().stream()
                .map(commentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponseDTO> getByVideoId(Long videoId) {
        return commentRepository.findByVideoId(videoId).stream()
                .map(commentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

//    @Override
//    public CommentResponseDTO createComment(String email, CommentRequestDTO dto) {
//        Users user = usersRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Comment comment = commentMapper.toEntity(dto);
//        comment.setCreatedAt(LocalDateTime.now());
//        return commentMapper.toResponseDTO(commentRepository.save(comment));
//    }




    @Override
    public CommentResponseDTO update(Long id, CommentRequestDTO dto) {
        Comment existing = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        existing.setComment(dto.comment());
        return commentMapper.toResponseDTO(commentRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public CommentSummaryDTO getSummaryByVideoId(Long videoId) {
        Double avgRating = commentRepository.getAverageRatingByVideoId(videoId);
        Long count = commentRepository.countByVideoId(videoId);
        return new CommentSummaryDTO(avgRating != null ? avgRating : 0.0, count);
    }

    @Override
    public CommentRatingDistributionDTO getRatingDistributionByVideoId(Long videoId) {
        List<Object[]> rawData = commentRepository.getRatingDistributionByVideoId(videoId);
        Map<Integer, Long> ratingMap = rawData.stream()
                .collect(Collectors.toMap(
                        r -> (Integer) r[0],
                        r -> (Long) r[1]
                ));
        return new CommentRatingDistributionDTO(ratingMap);
    }

    @Override
    public Page<CommentResponseDTO> getCommentsByVideoId(Long videoId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return commentRepository.findByVideoId(videoId, pageable)
                .map(commentMapper::toResponseDTO);
    }


}

