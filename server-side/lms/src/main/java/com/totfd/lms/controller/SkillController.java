package com.totfd.lms.controller;

import com.totfd.lms.dto.skill.request.SkillRequestDTO;
import com.totfd.lms.dto.skill.response.SkillResponseDTO;
import com.totfd.lms.payload.ApiResponse;
import com.totfd.lms.service.impl.SkillServiceImpl;
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
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillServiceImpl skillService;

    @PostMapping("/addSkill")
    public ResponseEntity<ApiResponse<SkillResponseDTO>> createSkill(@RequestBody SkillRequestDTO dto, HttpServletRequest request) {
        SkillResponseDTO skill = skillService.createSkill(dto);
        return new ResponseEntity<>(
                ApiResponse.success(skill, "Skill created successfully", HttpStatus.CREATED.value(), request.getRequestURI()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SkillResponseDTO>> getSkillById(@PathVariable Long id, HttpServletRequest request) {
        SkillResponseDTO skill = skillService.getSkillById(id);
        return new ResponseEntity<>(
                ApiResponse.success(skill, "Skill found", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SkillResponseDTO>>> getAllSkills(HttpServletRequest request) {
        List<SkillResponseDTO> skills = skillService.getAllSkills();
        return new ResponseEntity<>(
                ApiResponse.success(skills, "All skills fetched", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<SkillResponseDTO>>> getAllSkillsWithPage(  @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "20") int size, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SkillResponseDTO> skillPage = skillService.getAllSkillsWithPage(pageable);
        return new ResponseEntity<>(
                ApiResponse.success(skillPage, "Paginated skills fetched", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SkillResponseDTO>> updateSkill(@PathVariable Long id, @RequestBody SkillRequestDTO dto, HttpServletRequest request) {
        SkillResponseDTO updated = skillService.updateSkill(id, dto);
        return new ResponseEntity<>(
                ApiResponse.success(updated, "Skill updated successfully", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSkill(@PathVariable Long id, HttpServletRequest request) {
        skillService.deleteSkill(id);
        return new ResponseEntity<>(
                ApiResponse.success(null, "Skill deleted successfully", HttpStatus.NO_CONTENT.value(), request.getRequestURI()),
                HttpStatus.NO_CONTENT
        );
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResponse<SkillResponseDTO>> getSkillDetails(@PathVariable Long id, HttpServletRequest request) {
        SkillResponseDTO skillDetails = skillService.getSkillDetailsById(id);
        return new ResponseEntity<>(
                ApiResponse.success(skillDetails, "Skill details with topics, subtopics, videos and materials", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/skills/{id}/topics")
    public ResponseEntity<ApiResponse<SkillResponseDTO>> getSkillWithTopics(@PathVariable Long id, HttpServletRequest request) {
        SkillResponseDTO dto = skillService.getSkillWithTopics(id);
        return ResponseEntity.ok(
                ApiResponse.success(dto, "Skill and its topics loaded", HttpStatus.OK.value(), request.getRequestURI())
        );
    }



}