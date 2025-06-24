package com.totfd.lms.controller;

import com.totfd.lms.dto.mcqbestscore.McqBestScoreDTO;
import com.totfd.lms.payload.ApiResponse;
import com.totfd.lms.service.impl.McqBestScoreServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mcq-best-score")
@RequiredArgsConstructor
public class McqBestScoreController {

    private final McqBestScoreServiceImpl service;

    @PostMapping("/saveOrUpdate")
    public ResponseEntity<ApiResponse<McqBestScoreDTO>> saveOrUpdate(@RequestBody McqBestScoreDTO dto, HttpServletRequest request) {
        McqBestScoreDTO saved = service.saveOrUpdate(dto);
        return new ResponseEntity<>(
                ApiResponse.success(saved, "Best Score Saved/Updated", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/getById")
    public ResponseEntity<ApiResponse<McqBestScoreDTO>> getById(@RequestParam Long id, HttpServletRequest request) {
        McqBestScoreDTO dto = service.getById(id);
        return new ResponseEntity<>(
                ApiResponse.success(dto, "Best Score Found", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/byUser")
    public ResponseEntity<ApiResponse<List<McqBestScoreDTO>>> getByUser(@RequestParam Long userId, HttpServletRequest request) {
        List<McqBestScoreDTO> list = service.getByUserId(userId);
        return new ResponseEntity<>(
                ApiResponse.success(list, "Best Scores by User", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/bySubTopic")
    public ResponseEntity<ApiResponse<List<McqBestScoreDTO>>> getBySubTopic(@RequestParam Long subTopicId, HttpServletRequest request) {
        List<McqBestScoreDTO> list = service.getBySubTopicId(subTopicId);
        return new ResponseEntity<>(
                ApiResponse.success(list, "Best Scores by SubTopic", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/byUserAndSubTopic")
    public ResponseEntity<ApiResponse<McqBestScoreDTO>> getByUserAndSubTopic(@RequestParam Long userId, @RequestParam Long subTopicId, HttpServletRequest request) {
        McqBestScoreDTO dto = service.getByUserIdAndSubTopicId(userId, subTopicId);
        return new ResponseEntity<>(
                ApiResponse.success(dto, "Best Score by User and SubTopic", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }
}
