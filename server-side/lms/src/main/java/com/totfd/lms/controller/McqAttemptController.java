package com.totfd.lms.controller;

import com.totfd.lms.dto.mcqattempt.McqAttemptDTO;
import com.totfd.lms.payload.ApiResponse;
import com.totfd.lms.service.impl.McqAttemptServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/mcq-attempts")
@RequiredArgsConstructor
public class McqAttemptController {

    private final McqAttemptServiceImpl mcqAttemptService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<McqAttemptDTO>> createAttempt(
            @RequestBody McqAttemptDTO dto,
            HttpServletRequest request,
            Principal principal) {

        String email = principal.getName(); // Authenticated user's email
        McqAttemptDTO saved = mcqAttemptService.createAttempt(email, dto); // Pass email instead of userId

        return new ResponseEntity<>(
                ApiResponse.success(saved, "MCQ Attempt Saved", HttpStatus.CREATED.value(), request.getRequestURI()),
                HttpStatus.CREATED
        );
    }


    @GetMapping("/getById")
    public ResponseEntity<ApiResponse<McqAttemptDTO>> getById(@RequestParam Long id, HttpServletRequest request) {
        McqAttemptDTO attempt = mcqAttemptService.getAttemptById(id);
        return new ResponseEntity<>(
                ApiResponse.success(attempt, "MCQ Attempt Found", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/byUser")
    public ResponseEntity<ApiResponse<List<McqAttemptDTO>>> getByUserId(@RequestParam Long userId, HttpServletRequest request) {
        List<McqAttemptDTO> attempts = mcqAttemptService.getAttemptsByUserId(userId);
        return new ResponseEntity<>(
                ApiResponse.success(attempts, "Attempts by User", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

//    @GetMapping("/byMcq")
//    public ResponseEntity<ApiResponse<List<McqAttemptDTO>>> getByMcqId(@RequestParam Long mcqId, HttpServletRequest request) {
//        List<McqAttemptDTO> attempts = mcqAttemptService.getAttemptsByMcqId(mcqId);
//        return new ResponseEntity<>(
//                ApiResponse.success(attempts, "Attempts by MCQ", HttpStatus.OK.value(), request.getRequestURI()),
//                HttpStatus.OK
//        );
//    }

    @GetMapping("/bySubTopic")
    public ResponseEntity<ApiResponse<List<McqAttemptDTO>>> getBySubTopicId(@RequestParam Long subTopicId, HttpServletRequest request) {
        List<McqAttemptDTO> attempts = mcqAttemptService.getAttemptsBySubTopicId(subTopicId);
        return new ResponseEntity<>(
                ApiResponse.success(attempts, "Attempts by SubTopic", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/byMultipleSubTopicIds")
    public ResponseEntity<ApiResponse<List<McqAttemptDTO>>> getByMultipleSubTopicIds(
            @RequestParam List<Long> ids,
            HttpServletRequest request) {

        List<McqAttemptDTO> attempts = mcqAttemptService.getAttemptsByMultipleSubTopicIds(ids);
        return new ResponseEntity<>(
                ApiResponse.success(attempts, "Attempts by multiple SubTopic IDs", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

}
