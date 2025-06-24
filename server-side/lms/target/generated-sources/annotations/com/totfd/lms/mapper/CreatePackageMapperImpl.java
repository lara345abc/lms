package com.totfd.lms.mapper;

import com.totfd.lms.dto.learnigPackages.request.CreatePackageRequest;
import com.totfd.lms.dto.skill.request.CreateSkillDTO;
import com.totfd.lms.dto.studymaterial.request.CreateStudyMaterialDTO;
import com.totfd.lms.dto.subtopics.request.CreateSubTopicDTO;
import com.totfd.lms.dto.topic.request.CreateTopicDTO;
import com.totfd.lms.dto.video.request.CreateVideoDTO;
import com.totfd.lms.entity.LearningPackage;
import com.totfd.lms.entity.Skill;
import com.totfd.lms.entity.StudyMaterial;
import com.totfd.lms.entity.SubTopic;
import com.totfd.lms.entity.Topic;
import com.totfd.lms.entity.Video;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-24T18:40:58+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class CreatePackageMapperImpl implements CreatePackageMapper {

    @Override
    public LearningPackage toEntity(CreatePackageRequest dto) {
        if ( dto == null ) {
            return null;
        }

        LearningPackage.LearningPackageBuilder learningPackage = LearningPackage.builder();

        learningPackage.title( dto.title() );
        learningPackage.description( dto.description() );
        learningPackage.price( dto.price() );
        learningPackage.skills( createSkillDTOListToSkillList( dto.skills() ) );

        learningPackage.createdAt( java.time.LocalDateTime.now() );

        return learningPackage.build();
    }

    @Override
    public Skill toSkill(CreateSkillDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Skill.SkillBuilder skill = Skill.builder();

        skill.title( dto.title() );
        skill.description( dto.description() );
        skill.price( dto.price() );
        skill.topics( createTopicDTOListToTopicList( dto.topics() ) );

        skill.createdAt( java.time.LocalDateTime.now() );

        return skill.build();
    }

    @Override
    public Topic toTopic(CreateTopicDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Topic.TopicBuilder topic = Topic.builder();

        topic.title( dto.title() );
        topic.description( dto.description() );
        topic.subTopics( createSubTopicDTOListToSubTopicList( dto.subTopics() ) );

        topic.createdAt( java.time.LocalDateTime.now() );

        return topic.build();
    }

    @Override
    public SubTopic toSubTopic(CreateSubTopicDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SubTopic.SubTopicBuilder subTopic = SubTopic.builder();

        subTopic.title( dto.title() );
        subTopic.description( dto.description() );
        subTopic.videos( createVideoDTOListToVideoList( dto.videos() ) );
        subTopic.studyMaterials( createStudyMaterialDTOListToStudyMaterialList( dto.studyMaterials() ) );

        subTopic.createdAt( java.time.LocalDateTime.now() );

        return subTopic.build();
    }

    @Override
    public Video toVideo(CreateVideoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Video.VideoBuilder video = Video.builder();

        video.url( dto.url() );
        video.thumbnailUrl( dto.thumbnailUrl() );
        video.duration( dto.duration() );
        video.version( dto.version() );
        video.isLatest( dto.isLatest() );

        return video.build();
    }

    @Override
    public StudyMaterial toStudyMaterial(CreateStudyMaterialDTO dto) {
        if ( dto == null ) {
            return null;
        }

        StudyMaterial.StudyMaterialBuilder studyMaterial = StudyMaterial.builder();

        studyMaterial.pdfUrl( dto.pdfUrl() );
        studyMaterial.version( dto.version() );
        studyMaterial.isLatest( dto.isLatest() );

        return studyMaterial.build();
    }

    protected List<Skill> createSkillDTOListToSkillList(List<CreateSkillDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Skill> list1 = new ArrayList<Skill>( list.size() );
        for ( CreateSkillDTO createSkillDTO : list ) {
            list1.add( toSkill( createSkillDTO ) );
        }

        return list1;
    }

    protected List<Topic> createTopicDTOListToTopicList(List<CreateTopicDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Topic> list1 = new ArrayList<Topic>( list.size() );
        for ( CreateTopicDTO createTopicDTO : list ) {
            list1.add( toTopic( createTopicDTO ) );
        }

        return list1;
    }

    protected List<SubTopic> createSubTopicDTOListToSubTopicList(List<CreateSubTopicDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<SubTopic> list1 = new ArrayList<SubTopic>( list.size() );
        for ( CreateSubTopicDTO createSubTopicDTO : list ) {
            list1.add( toSubTopic( createSubTopicDTO ) );
        }

        return list1;
    }

    protected List<Video> createVideoDTOListToVideoList(List<CreateVideoDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Video> list1 = new ArrayList<Video>( list.size() );
        for ( CreateVideoDTO createVideoDTO : list ) {
            list1.add( toVideo( createVideoDTO ) );
        }

        return list1;
    }

    protected List<StudyMaterial> createStudyMaterialDTOListToStudyMaterialList(List<CreateStudyMaterialDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<StudyMaterial> list1 = new ArrayList<StudyMaterial>( list.size() );
        for ( CreateStudyMaterialDTO createStudyMaterialDTO : list ) {
            list1.add( toStudyMaterial( createStudyMaterialDTO ) );
        }

        return list1;
    }
}
