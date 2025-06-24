package com.totfd.lms.mapper;

import com.totfd.lms.dto.mcqpoint.McqPointsDTO;
import com.totfd.lms.entity.McqPoints;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-24T18:40:58+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class McqPointsMapperImpl implements McqPointsMapper {

    @Override
    public McqPointsDTO toDto(McqPoints entity) {
        if ( entity == null ) {
            return null;
        }

        McqPointsDTO mcqPointsDTO = new McqPointsDTO();

        mcqPointsDTO.setId( entity.getId() );
        mcqPointsDTO.setPoints( entity.getPoints() );
        mcqPointsDTO.setCreatedAt( entity.getCreatedAt() );

        return mcqPointsDTO;
    }

    @Override
    public McqPoints fromDto(McqPointsDTO dto) {
        if ( dto == null ) {
            return null;
        }

        McqPoints.McqPointsBuilder mcqPoints = McqPoints.builder();

        mcqPoints.id( dto.getId() );
        mcqPoints.points( dto.getPoints() );
        mcqPoints.createdAt( dto.getCreatedAt() );

        return mcqPoints.build();
    }
}
