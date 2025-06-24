package com.totfd.lms.mapper;

import com.totfd.lms.dto.mcq.McqDTO;
import com.totfd.lms.entity.Mcq;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-24T18:40:58+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class McqMapperImpl implements McqMapper {

    @Override
    public McqDTO toDto(Mcq mcq) {
        if ( mcq == null ) {
            return null;
        }

        McqDTO mcqDTO = new McqDTO();

        mcqDTO.setId( mcq.getId() );
        mcqDTO.setQuestion( mcq.getQuestion() );
        mcqDTO.setOptions( mcq.getOptions() );
        mcqDTO.setCorrectOption( mcq.getCorrectOption() );
        mcqDTO.setVersion( mcq.getVersion() );
        mcqDTO.setIsLatest( mcq.getIsLatest() );

        return mcqDTO;
    }

    @Override
    public Mcq fromDto(McqDTO mcqDTO) {
        if ( mcqDTO == null ) {
            return null;
        }

        Mcq.McqBuilder mcq = Mcq.builder();

        mcq.id( mcqDTO.getId() );
        mcq.question( mcqDTO.getQuestion() );
        mcq.options( mcqDTO.getOptions() );
        mcq.correctOption( mcqDTO.getCorrectOption() );
        mcq.version( mcqDTO.getVersion() );
        mcq.isLatest( mcqDTO.getIsLatest() );

        return mcq.build();
    }
}
