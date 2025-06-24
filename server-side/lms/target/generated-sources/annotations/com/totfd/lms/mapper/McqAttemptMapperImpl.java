package com.totfd.lms.mapper;

import com.totfd.lms.dto.mcqattempt.McqAttemptDTO;
import com.totfd.lms.entity.McqAttempt;
import com.totfd.lms.entity.SubTopic;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-24T18:40:58+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class McqAttemptMapperImpl implements McqAttemptMapper {

    @Override
    public McqAttemptDTO toDto(McqAttempt entity) {
        if ( entity == null ) {
            return null;
        }

        McqAttemptDTO mcqAttemptDTO = new McqAttemptDTO();

        mcqAttemptDTO.setSubTopicId( entitySubTopicId( entity ) );
        mcqAttemptDTO.setId( entity.getId() );
        mcqAttemptDTO.setAttemptNumber( entity.getAttemptNumber() );
        mcqAttemptDTO.setScore( entity.getScore() );
        mcqAttemptDTO.setTotalMarks( entity.getTotalMarks() );
        mcqAttemptDTO.setAttemptedAt( entity.getAttemptedAt() );

        return mcqAttemptDTO;
    }

    @Override
    public McqAttempt fromDto(McqAttemptDTO dto) {
        if ( dto == null ) {
            return null;
        }

        McqAttempt.McqAttemptBuilder mcqAttempt = McqAttempt.builder();

        mcqAttempt.subTopic( mcqAttemptDTOToSubTopic( dto ) );
        mcqAttempt.id( dto.getId() );
        mcqAttempt.attemptNumber( dto.getAttemptNumber() );
        mcqAttempt.score( dto.getScore() );
        mcqAttempt.totalMarks( dto.getTotalMarks() );
        mcqAttempt.attemptedAt( dto.getAttemptedAt() );

        return mcqAttempt.build();
    }

    private Long entitySubTopicId(McqAttempt mcqAttempt) {
        if ( mcqAttempt == null ) {
            return null;
        }
        SubTopic subTopic = mcqAttempt.getSubTopic();
        if ( subTopic == null ) {
            return null;
        }
        Long id = subTopic.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected SubTopic mcqAttemptDTOToSubTopic(McqAttemptDTO mcqAttemptDTO) {
        if ( mcqAttemptDTO == null ) {
            return null;
        }

        SubTopic.SubTopicBuilder subTopic = SubTopic.builder();

        subTopic.id( mcqAttemptDTO.getSubTopicId() );

        return subTopic.build();
    }
}
