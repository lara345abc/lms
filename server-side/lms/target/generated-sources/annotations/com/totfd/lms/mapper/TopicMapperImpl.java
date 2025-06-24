package com.totfd.lms.mapper;

import com.totfd.lms.dto.skill.request.SkillRequestDTO;
import com.totfd.lms.dto.subtopics.request.SubTopicRequestDTO;
import com.totfd.lms.dto.subtopics.response.SubTopicResponseDTO;
import com.totfd.lms.dto.topic.request.TopicRequestDTO;
import com.totfd.lms.dto.topic.response.TopicResponseDTO;
import com.totfd.lms.entity.Skill;
import com.totfd.lms.entity.SubTopic;
import com.totfd.lms.entity.Topic;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-24T18:40:58+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class TopicMapperImpl implements TopicMapper {

    @Autowired
    private SubTopicMapper subTopicMapper;

    @Override
    public Skill toEntity(SkillRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Skill.SkillBuilder skill = Skill.builder();

        skill.title( dto.title() );
        skill.description( dto.description() );
        skill.price( dto.price() );

        return skill.build();
    }

    @Override
    public TopicResponseDTO toResponseDTO(Topic topic) {
        if ( topic == null ) {
            return null;
        }

        Long skillId = null;
        String skillTitle = null;
        List<SubTopicResponseDTO> subTopics = null;
        Long id = null;
        String title = null;
        String description = null;
        LocalDateTime createdAt = null;

        skillId = topicSkillId( topic );
        skillTitle = topicSkillTitle( topic );
        subTopics = subTopicListToSubTopicResponseDTOList( topic.getSubTopics() );
        id = topic.getId();
        title = topic.getTitle();
        description = topic.getDescription();
        createdAt = topic.getCreatedAt();

        TopicResponseDTO topicResponseDTO = new TopicResponseDTO( id, title, description, createdAt, skillId, skillTitle, subTopics );

        return topicResponseDTO;
    }

    @Override
    public Topic toEntity(TopicRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Topic.TopicBuilder topic = Topic.builder();

        topic.skill( TopicMapper.mapSkillFromId( dto.skillId() ) );
        topic.title( dto.title() );
        topic.description( dto.description() );
        topic.subTopics( subTopicRequestDTOListToSubTopicList( dto.subTopics() ) );

        return topic.build();
    }

    @Override
    public void updateFromDto(TopicRequestDTO dto, Topic topic) {
        if ( dto == null ) {
            return;
        }

        if ( dto.skillId() != null ) {
            topic.setSkill( TopicMapper.mapSkillFromId( dto.skillId() ) );
        }
        if ( dto.title() != null ) {
            topic.setTitle( dto.title() );
        }
        if ( dto.description() != null ) {
            topic.setDescription( dto.description() );
        }
        if ( topic.getSubTopics() != null ) {
            List<SubTopic> list = subTopicRequestDTOListToSubTopicList( dto.subTopics() );
            if ( list != null ) {
                topic.getSubTopics().clear();
                topic.getSubTopics().addAll( list );
            }
        }
        else {
            List<SubTopic> list = subTopicRequestDTOListToSubTopicList( dto.subTopics() );
            if ( list != null ) {
                topic.setSubTopics( list );
            }
        }
    }

    private Long topicSkillId(Topic topic) {
        if ( topic == null ) {
            return null;
        }
        Skill skill = topic.getSkill();
        if ( skill == null ) {
            return null;
        }
        Long id = skill.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String topicSkillTitle(Topic topic) {
        if ( topic == null ) {
            return null;
        }
        Skill skill = topic.getSkill();
        if ( skill == null ) {
            return null;
        }
        String title = skill.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }

    protected List<SubTopicResponseDTO> subTopicListToSubTopicResponseDTOList(List<SubTopic> list) {
        if ( list == null ) {
            return null;
        }

        List<SubTopicResponseDTO> list1 = new ArrayList<SubTopicResponseDTO>( list.size() );
        for ( SubTopic subTopic : list ) {
            list1.add( subTopicMapper.toResponseDTO( subTopic ) );
        }

        return list1;
    }

    protected List<SubTopic> subTopicRequestDTOListToSubTopicList(List<SubTopicRequestDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<SubTopic> list1 = new ArrayList<SubTopic>( list.size() );
        for ( SubTopicRequestDTO subTopicRequestDTO : list ) {
            list1.add( subTopicMapper.toEntity( subTopicRequestDTO ) );
        }

        return list1;
    }
}
