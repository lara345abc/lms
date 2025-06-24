package com.totfd.lms.service.impl;

import com.totfd.lms.dto.mcqattempt.McqAttemptDTO;
import com.totfd.lms.entity.Mcq;
import com.totfd.lms.entity.McqAttempt;
import com.totfd.lms.entity.SubTopic;
import com.totfd.lms.entity.Users;
import com.totfd.lms.exceptions.ResourceNotFoundException;
import com.totfd.lms.mapper.McqAttemptMapper;
import com.totfd.lms.repository.McqAttemptRepository;
import com.totfd.lms.repository.McqRepository;
import com.totfd.lms.repository.SubTopicRepository;
import com.totfd.lms.repository.UsersRepository;
import com.totfd.lms.service.McqAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class McqAttemptServiceImpl implements McqAttemptService {

    private final McqAttemptRepository mcqAttemptRepository;
    private final UsersRepository usersRepository;
    private final McqRepository mcqRepository;
    private final SubTopicRepository subTopicRepository;
    private final McqAttemptMapper mcqAttemptMapper;

    @Override
    public McqAttemptDTO createAttempt(String email, McqAttemptDTO dto) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SubTopic subTopic = subTopicRepository.findById(dto.getSubTopicId())
                .orElseThrow(() -> new ResourceNotFoundException("SubTopic not found"));

        // Find the last attempt number
        Integer lastAttemptNumber = mcqAttemptRepository
                .findTopByUsersIdAndSubTopicIdOrderByAttemptNumberDesc(user.getId(), subTopic.getId())
                .map(McqAttempt::getAttemptNumber)
                .orElse(0); // if no attempt yet, start from 0

        int nextAttemptNumber = lastAttemptNumber + 1;

        McqAttempt attempt = McqAttempt.builder()
                .users(user)
                .subTopic(subTopic)
                .attemptNumber(nextAttemptNumber)
                .score(dto.getScore())
                .totalMarks(dto.getTotalMarks())
                .attemptedAt(LocalDateTime.now())
                .build();

        return mcqAttemptMapper.toDto(mcqAttemptRepository.save(attempt));
    }

    @Override
    public List<McqAttemptDTO> getAttemptsByUserId(Long userId) {
        return mcqAttemptRepository.findByUsersId(userId)
                .stream()
                .map(mcqAttemptMapper::toDto)
                .collect(Collectors.toList());
    }

//    @Override
//    public List<McqAttemptDTO> getAttemptsByMcqId(Long mcqId) {
//        return mcqAttemptRepository.findByMcqId(mcqId)
//                .stream()
//                .map(mcqAttemptMapper::toDto)
//                .collect(Collectors.toList());
//    }

    @Override
    public List<McqAttemptDTO> getAttemptsBySubTopicId(Long subTopicId) {
        return mcqAttemptRepository.findBySubTopicId(subTopicId)
                .stream()
                .map(mcqAttemptMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public McqAttemptDTO getAttemptById(Long id) {
        McqAttempt attempt = mcqAttemptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("McqAttempt not found"));
        return mcqAttemptMapper.toDto(attempt);
    }

    @Override
    public List<McqAttemptDTO> getAttemptsByMultipleSubTopicIds(List<Long> subTopicIds) {
        return mcqAttemptRepository.findBySubTopicIdIn(subTopicIds)
                .stream()
                .map(mcqAttemptMapper::toDto)
                .collect(Collectors.toList());
    }


}

