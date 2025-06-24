package com.totfd.lms.mapper;

import com.totfd.lms.dto.studymaterial.response.StudyMaterialResponseDTO;
import com.totfd.lms.dto.subtopics.request.SubTopicRequestDTO;
import com.totfd.lms.dto.subtopics.response.SubTopicResponseDTO;
import com.totfd.lms.dto.video.response.VideoResponseDTO;
import com.totfd.lms.entity.StudyMaterial;
import com.totfd.lms.entity.SubTopic;
import com.totfd.lms.entity.Topic;
import com.totfd.lms.entity.Video;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-24T18:40:57+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class SubTopicMapperImpl implements SubTopicMapper {

    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private StudyMaterialMapper studyMaterialMapper;

    @Override
    public SubTopicResponseDTO toResponseDTO(SubTopic subTopic) {
        if ( subTopic == null ) {
            return null;
        }

        Long topicId = null;
        String topicTitle = null;
        String topicDescription = null;
        List<VideoResponseDTO> videos = null;
        List<StudyMaterialResponseDTO> studyMaterials = null;
        Long id = null;
        String title = null;
        String description = null;

        topicId = subTopicTopicId( subTopic );
        topicTitle = subTopicTopicTitle( subTopic );
        topicDescription = subTopicTopicDescription( subTopic );
        videos = videoListToVideoResponseDTOList( subTopic.getVideos() );
        studyMaterials = studyMaterialListToStudyMaterialResponseDTOList( subTopic.getStudyMaterials() );
        id = subTopic.getId();
        title = subTopic.getTitle();
        description = subTopic.getDescription();

        SubTopicResponseDTO subTopicResponseDTO = new SubTopicResponseDTO( id, title, description, topicId, topicTitle, topicDescription, videos, studyMaterials );

        return subTopicResponseDTO;
    }

    @Override
    public SubTopic toEntity(SubTopicRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SubTopic.SubTopicBuilder subTopic = SubTopic.builder();

        subTopic.topic( SubTopicMapper.mapTopicFromId( dto.topicId() ) );
        subTopic.title( dto.title() );
        subTopic.description( dto.description() );

        return subTopic.build();
    }

    @Override
    public void updateFromDto(SubTopicRequestDTO dto, SubTopic subTopic) {
        if ( dto == null ) {
            return;
        }

        if ( dto.title() != null ) {
            subTopic.setTitle( dto.title() );
        }
        if ( dto.description() != null ) {
            subTopic.setDescription( dto.description() );
        }
    }

    private Long subTopicTopicId(SubTopic subTopic) {
        if ( subTopic == null ) {
            return null;
        }
        Topic topic = subTopic.getTopic();
        if ( topic == null ) {
            return null;
        }
        Long id = topic.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String subTopicTopicTitle(SubTopic subTopic) {
        if ( subTopic == null ) {
            return null;
        }
        Topic topic = subTopic.getTopic();
        if ( topic == null ) {
            return null;
        }
        String title = topic.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }

    private String subTopicTopicDescription(SubTopic subTopic) {
        if ( subTopic == null ) {
            return null;
        }
        Topic topic = subTopic.getTopic();
        if ( topic == null ) {
            return null;
        }
        String description = topic.getDescription();
        if ( description == null ) {
            return null;
        }
        return description;
    }

    protected List<VideoResponseDTO> videoListToVideoResponseDTOList(List<Video> list) {
        if ( list == null ) {
            return null;
        }

        List<VideoResponseDTO> list1 = new ArrayList<VideoResponseDTO>( list.size() );
        for ( Video video : list ) {
            list1.add( videoMapper.toResponseDTO( video ) );
        }

        return list1;
    }

    protected List<StudyMaterialResponseDTO> studyMaterialListToStudyMaterialResponseDTOList(List<StudyMaterial> list) {
        if ( list == null ) {
            return null;
        }

        List<StudyMaterialResponseDTO> list1 = new ArrayList<StudyMaterialResponseDTO>( list.size() );
        for ( StudyMaterial studyMaterial : list ) {
            list1.add( studyMaterialMapper.toResponseDTO( studyMaterial ) );
        }

        return list1;
    }
}
