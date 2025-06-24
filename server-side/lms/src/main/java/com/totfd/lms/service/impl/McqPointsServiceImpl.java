package com.totfd.lms.service.impl;

import com.totfd.lms.dto.mcqpoint.McqPointsDTO;
import com.totfd.lms.entity.Mcq;
import com.totfd.lms.entity.McqPoints;
import com.totfd.lms.entity.Users;
import com.totfd.lms.exceptions.ResourceNotFoundException;
import com.totfd.lms.mapper.McqPointsMapper;
import com.totfd.lms.repository.McqPointsRepository;
import com.totfd.lms.repository.McqRepository;
import com.totfd.lms.repository.UsersRepository;
import com.totfd.lms.service.McqPointsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class McqPointsServiceImpl implements McqPointsService {

    private final McqPointsRepository repository;
    private final UsersRepository usersRepository;
    private final McqRepository mcqRepository;
    private final McqPointsMapper mapper;

    @Override
    public McqPointsDTO createMcqPoints(McqPointsDTO dto) {
        Users user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Mcq mcq = mcqRepository.findById(dto.getMcqId())
                .orElseThrow(() -> new ResourceNotFoundException("Mcq not found"));

        McqPoints points = mapper.fromDto(dto);
        points.setUsers(user);
        points.setMcq(mcq);
        points.setCreatedAt(LocalDateTime.now());

        return mapper.toDto(repository.save(points));
    }

    @Override
    public McqPointsDTO getById(Long id) {
        McqPoints points = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Points not found"));
        return mapper.toDto(points);
    }

    @Override
    public List<McqPointsDTO> getByUserId(Long userId) {
        return repository.findByUsersId(userId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<McqPointsDTO> getByMcqId(Long mcqId) {
        return repository.findByMcqId(mcqId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}

