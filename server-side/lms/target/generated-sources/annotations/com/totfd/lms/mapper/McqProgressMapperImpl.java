package com.totfd.lms.mapper;

import com.totfd.lms.dto.mcqProgress.McqProgressDTO;
import com.totfd.lms.entity.McqProgress;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-24T18:40:58+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class McqProgressMapperImpl implements McqProgressMapper {

    @Override
    public McqProgressDTO toDto(McqProgress entity) {
        if ( entity == null ) {
            return null;
        }

        McqProgressDTO mcqProgressDTO = new McqProgressDTO();

        mcqProgressDTO.setId( entity.getId() );
        mcqProgressDTO.setStatus( entity.getStatus() );
        mcqProgressDTO.setLastAccessedAt( entity.getLastAccessedAt() );
        mcqProgressDTO.setCreatedAt( entity.getCreatedAt() );

        return mcqProgressDTO;
    }

    @Override
    public McqProgress fromDto(McqProgressDTO dto) {
        if ( dto == null ) {
            return null;
        }

        McqProgress.McqProgressBuilder mcqProgress = McqProgress.builder();

        mcqProgress.id( dto.getId() );
        mcqProgress.status( dto.getStatus() );
        mcqProgress.lastAccessedAt( dto.getLastAccessedAt() );
        mcqProgress.createdAt( dto.getCreatedAt() );

        return mcqProgress.build();
    }
}
