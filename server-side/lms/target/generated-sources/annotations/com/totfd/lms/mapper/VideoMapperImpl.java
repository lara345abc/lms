package com.totfd.lms.mapper;

import com.totfd.lms.dto.video.request.VideoRequestDTO;
import com.totfd.lms.dto.video.response.VideoResponseDTO;
import com.totfd.lms.entity.SubTopic;
import com.totfd.lms.entity.Video;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-24T18:40:58+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class VideoMapperImpl implements VideoMapper {

    @Override
    public Video toEntity(VideoRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Video.VideoBuilder video = Video.builder();

        video.subTopic( VideoMapper.mapSubTopicFromId( dto.subTopicId() ) );
        video.url( dto.url() );
        video.version( dto.version() );
        video.isLatest( dto.isLatest() );

        return video.build();
    }

    @Override
    public VideoResponseDTO toResponseDTO(Video video) {
        if ( video == null ) {
            return null;
        }

        Long subTopicId = null;
        String subTopicTitle = null;
        Long id = null;
        String url = null;
        String thumbnailUrl = null;
        Integer duration = null;
        Integer version = null;
        Boolean isLatest = null;
        Long noOfViews = null;

        subTopicId = videoSubTopicId( video );
        subTopicTitle = videoSubTopicTitle( video );
        id = video.getId();
        url = video.getUrl();
        thumbnailUrl = video.getThumbnailUrl();
        duration = video.getDuration();
        version = video.getVersion();
        isLatest = video.getIsLatest();
        noOfViews = video.getNoOfViews();

        VideoResponseDTO videoResponseDTO = new VideoResponseDTO( id, subTopicId, subTopicTitle, url, thumbnailUrl, duration, version, isLatest, noOfViews );

        return videoResponseDTO;
    }

    @Override
    public void updateFromDto(VideoRequestDTO dto, Video video) {
        if ( dto == null ) {
            return;
        }

        if ( dto.subTopicId() != null ) {
            video.setSubTopic( VideoMapper.mapSubTopicFromId( dto.subTopicId() ) );
        }
        if ( dto.url() != null ) {
            video.setUrl( dto.url() );
        }
        if ( dto.version() != null ) {
            video.setVersion( dto.version() );
        }
        if ( dto.isLatest() != null ) {
            video.setIsLatest( dto.isLatest() );
        }
    }

    private Long videoSubTopicId(Video video) {
        if ( video == null ) {
            return null;
        }
        SubTopic subTopic = video.getSubTopic();
        if ( subTopic == null ) {
            return null;
        }
        Long id = subTopic.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String videoSubTopicTitle(Video video) {
        if ( video == null ) {
            return null;
        }
        SubTopic subTopic = video.getSubTopic();
        if ( subTopic == null ) {
            return null;
        }
        String title = subTopic.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }
}
