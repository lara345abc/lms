package com.totfd.lms.service.impl;

import com.totfd.lms.dto.mcqbestscore.McqBestScoreDTO;
import com.totfd.lms.entity.McqBestScore;
import com.totfd.lms.entity.SubTopic;
import com.totfd.lms.entity.Users;
import com.totfd.lms.exceptions.ResourceNotFoundException;
import com.totfd.lms.mapper.McqBestScoreMapper;
import com.totfd.lms.repository.McqBestScoreRepository;
import com.totfd.lms.repository.SubTopicRepository;
import com.totfd.lms.repository.UsersRepository;
import com.totfd.lms.service.McqBestScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class McqBestScoreServiceImpl implements McqBestScoreService {

    private final McqBestScoreRepository repository;
    private final UsersRepository usersRepository;
    private final SubTopicRepository subTopicRepository;
    private final McqBestScoreMapper mapper;

    @Override
    public McqBestScoreDTO saveOrUpdate(McqBestScoreDTO dto) {
        Users user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        SubTopic subTopic = subTopicRepository.findById(dto.getSubTopicId())
                .orElseThrow(() -> new ResourceNotFoundException("SubTopic not found"));

        McqBestScore score = repository.findByUsersIdAndSubTopicId(dto.getUserId(), dto.getSubTopicId())
                .orElse(mapper.fromDto(dto));

        score.setUsers(user);
        score.setSubTopic(subTopic);
        score.setBestScore(dto.getBestScore());
        score.setTotalMarks(dto.getTotalMarks());
        score.setLastUpdated(LocalDateTime.now());

        return mapper.toDto(repository.save(score));
    }

    @Override
    public McqBestScoreDTO getById(Long id) {
        McqBestScore score = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Best Score not found"));
        return mapper.toDto(score);
    }

    @Override
    public List<McqBestScoreDTO> getByUserId(Long userId) {
        return repository.findByUsersId(userId)
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<McqBestScoreDTO> getBySubTopicId(Long subTopicId) {
        return repository.findBySubTopicId(subTopicId)
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public McqBestScoreDTO getByUserIdAndSubTopicId(Long userId, Long subTopicId) {
        McqBestScore score = repository.findByUsersIdAndSubTopicId(userId, subTopicId)
                .orElseThrow(() -> new ResourceNotFoundException("Best score not found"));
        return mapper.toDto(score);
    }
}

