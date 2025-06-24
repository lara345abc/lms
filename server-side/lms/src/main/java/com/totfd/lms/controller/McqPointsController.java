package com.totfd.lms.controller;

import com.totfd.lms.dto.mcqpoint.McqPointsDTO;
import com.totfd.lms.payload.ApiResponse;
import com.totfd.lms.service.impl.McqPointsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mcq-points")
@RequiredArgsConstructor
public class McqPointsController {

    private final McqPointsServiceImpl service;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<McqPointsDTO>> create(@RequestBody McqPointsDTO dto, HttpServletRequest request) {
        McqPointsDTO saved = service.createMcqPoints(dto);
        return new ResponseEntity<>(
                ApiResponse.success(saved, "Points created", HttpStatus.CREATED.value(), request.getRequestURI()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/getById")
    public ResponseEntity<ApiResponse<McqPointsDTO>> getById(@RequestParam Long id, HttpServletRequest request) {
        McqPointsDTO found = service.getById(id);
        return new ResponseEntity<>(
                ApiResponse.success(found, "Points found", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/byUser")
    public ResponseEntity<ApiResponse<List<McqPointsDTO>>> getByUserId(@RequestParam Long userId, HttpServletRequest request) {
        List<McqPointsDTO> list = service.getByUserId(userId);
        return new ResponseEntity<>(
                ApiResponse.success(list, "Points by user", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/byMcq")
    public ResponseEntity<ApiResponse<List<McqPointsDTO>>> getByMcqId(@RequestParam Long mcqId, HttpServletRequest request) {
        List<McqPointsDTO> list = service.getByMcqId(mcqId);
        return new ResponseEntity<>(
                ApiResponse.success(list, "Points by mcq", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }
}

