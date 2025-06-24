package com.totfd.lms.controller;

import com.totfd.lms.dto.mcqProgress.McqProgressDTO;
import com.totfd.lms.payload.ApiResponse;
import com.totfd.lms.service.impl.McqProgressServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mcq-progress")
@RequiredArgsConstructor
public class McqProgressController {

    private final McqProgressServiceImpl service;

    @PostMapping("/saveOrUpdate")
    public ResponseEntity<ApiResponse<McqProgressDTO>> saveOrUpdate(@RequestBody McqProgressDTO dto, HttpServletRequest request) {
        McqProgressDTO saved = service.saveOrUpdateProgress(dto);
        return new ResponseEntity<>(
                ApiResponse.success(saved, "Progress Saved/Updated", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/getById")
    public ResponseEntity<ApiResponse<McqProgressDTO>> getById(@RequestParam Long id, HttpServletRequest request) {
        McqProgressDTO dto = service.getById(id);
        return new ResponseEntity<>(
                ApiResponse.success(dto, "Progress Found", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/byUser")
    public ResponseEntity<ApiResponse<List<McqProgressDTO>>> getByUser(@RequestParam Long userId, HttpServletRequest request) {
        List<McqProgressDTO> list = service.getByUserId(userId);
        return new ResponseEntity<>(
                ApiResponse.success(list, "Progress List by User", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/byMcq")
    public ResponseEntity<ApiResponse<List<McqProgressDTO>>> getByMcq(@RequestParam Long mcqId, HttpServletRequest request) {
        List<McqProgressDTO> list = service.getByMcqId(mcqId);
        return new ResponseEntity<>(
                ApiResponse.success(list, "Progress List by Mcq", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/byUserAndMcq")
    public ResponseEntity<ApiResponse<McqProgressDTO>> getByUserAndMcq(@RequestParam Long userId, @RequestParam Long mcqId, HttpServletRequest request) {
        McqProgressDTO dto = service.getByUserIdAndMcqId(userId, mcqId);
        return new ResponseEntity<>(
                ApiResponse.success(dto, "Progress by User and Mcq", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }
}
