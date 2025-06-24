package com.totfd.lms.mapper;

import com.totfd.lms.dto.mcqbestscore.McqBestScoreDTO;
import com.totfd.lms.entity.McqBestScore;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-24T18:40:58+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class McqBestScoreMapperImpl implements McqBestScoreMapper {

    @Override
    public McqBestScoreDTO toDto(McqBestScore entity) {
        if ( entity == null ) {
            return null;
        }

        McqBestScoreDTO mcqBestScoreDTO = new McqBestScoreDTO();

        mcqBestScoreDTO.setId( entity.getId() );
        mcqBestScoreDTO.setBestScore( entity.getBestScore() );
        mcqBestScoreDTO.setTotalMarks( entity.getTotalMarks() );
        mcqBestScoreDTO.setLastUpdated( entity.getLastUpdated() );

        return mcqBestScoreDTO;
    }

    @Override
    public McqBestScore fromDto(McqBestScoreDTO dto) {
        if ( dto == null ) {
            return null;
        }

        McqBestScore.McqBestScoreBuilder mcqBestScore = McqBestScore.builder();

        mcqBestScore.id( dto.getId() );
        mcqBestScore.bestScore( dto.getBestScore() );
        mcqBestScore.totalMarks( dto.getTotalMarks() );
        mcqBestScore.lastUpdated( dto.getLastUpdated() );

        return mcqBestScore.build();
    }
}
