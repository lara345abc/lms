package com.totfd.lms.service.impl;

import com.totfd.lms.dto.mcqProgress.McqProgressDTO;
import com.totfd.lms.entity.Mcq;
import com.totfd.lms.entity.McqProgress;
import com.totfd.lms.entity.Users;
import com.totfd.lms.exceptions.ResourceNotFoundException;
import com.totfd.lms.mapper.McqProgressMapper;
import com.totfd.lms.repository.McqProgressRepository;
import com.totfd.lms.repository.McqRepository;
import com.totfd.lms.repository.UsersRepository;
import com.totfd.lms.service.McqProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class McqProgressServiceImpl implements McqProgressService {

    private final McqProgressRepository repository;
    private final UsersRepository usersRepository;
    private final McqRepository mcqRepository;
    private final McqProgressMapper mapper;

    @Override
    public McqProgressDTO saveOrUpdateProgress(McqProgressDTO dto) {
        Users user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Mcq mcq = mcqRepository.findById(dto.getMcqId())
                .orElseThrow(() -> new ResourceNotFoundException("Mcq not found"));

        McqProgress progress = repository.findByUsersIdAndMcqId(dto.getUserId(), dto.getMcqId())
                .orElse(mapper.fromDto(dto));

        progress.setUsers(user);
        progress.setMcq(mcq);
        progress.setStatus(dto.getStatus());
        progress.setLastAccessedAt(LocalDateTime.now());
        if (progress.getCreatedAt() == null) {
            progress.setCreatedAt(LocalDateTime.now());
        }

        return mapper.toDto(repository.save(progress));
    }

    @Override
    public McqProgressDTO getById(Long id) {
        McqProgress progress = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progress not found"));
        return mapper.toDto(progress);
    }

    @Override
    public List<McqProgressDTO> getByUserId(Long userId) {
        return repository.findByUsersId(userId)
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<McqProgressDTO> getByMcqId(Long mcqId) {
        return repository.findByMcqId(mcqId)
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public McqProgressDTO getByUserIdAndMcqId(Long userId, Long mcqId) {
        McqProgress progress = repository.findByUsersIdAndMcqId(userId, mcqId)
                .orElseThrow(() -> new ResourceNotFoundException("Progress not found"));
        return mapper.toDto(progress);
    }
}

