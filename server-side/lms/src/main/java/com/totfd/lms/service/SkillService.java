package com.totfd.lms.service;

import com.totfd.lms.dto.skill.request.SkillRequestDTO;
import com.totfd.lms.dto.skill.response.SkillResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SkillService {
    SkillResponseDTO createSkill(SkillRequestDTO dto);
    SkillResponseDTO getSkillById(Long id);
    List<SkillResponseDTO> getAllSkills();
    Page<SkillResponseDTO> getAllSkillsWithPage(Pageable pageable);
    SkillResponseDTO updateSkill(Long id, SkillRequestDTO dto);
    void deleteSkill(Long id);
    SkillResponseDTO getSkillDetailsById(Long id);

}

