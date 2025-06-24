package com.totfd.lms.mapper;

import com.totfd.lms.dto.learnigPackages.request.PackageRequestDTO;
import com.totfd.lms.dto.learnigPackages.response.PackageResponseDTO;
import com.totfd.lms.dto.skill.request.SkillRequestDTO;
import com.totfd.lms.dto.skill.response.SkillResponseDTO;
import com.totfd.lms.dto.studymaterial.response.StudyMaterialResponseDTO;
import com.totfd.lms.dto.subtopics.response.SubTopicResponseDTO;
import com.totfd.lms.dto.topic.response.TopicResponseDTO;
import com.totfd.lms.dto.video.response.VideoResponseDTO;
import com.totfd.lms.entity.LearningPackage;
import com.totfd.lms.entity.Skill;
import com.totfd.lms.entity.StudyMaterial;
import com.totfd.lms.entity.SubTopic;
import com.totfd.lms.entity.Topic;
import com.totfd.lms.entity.Video;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class PackageMapperImpl implements PackageMapper {

    @Override
    public LearningPackage toEntity(PackageRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        LearningPackage.LearningPackageBuilder learningPackage = LearningPackage.builder();

        learningPackage.title( dto.title() );
        learningPackage.description( dto.description() );
        learningPackage.price( dto.price() );
        learningPackage.skills( skillRequestDTOListToSkillList( dto.skills() ) );

        return learningPackage.build();
    }

    @Override
    public PackageResponseDTO toResponseDTO(LearningPackage entity) {
        if ( entity == null ) {
            return null;
        }

        List<SkillResponseDTO> skills = null;
        Long id = null;
        String title = null;
        String description = null;
        BigDecimal price = null;
        LocalDateTime createdAt = null;

        skills = mapSkills( entity.getSkills() );
        id = entity.getId();
        title = entity.getTitle();
        description = entity.getDescription();
        price = entity.getPrice();
        createdAt = entity.getCreatedAt();

        PackageResponseDTO packageResponseDTO = new PackageResponseDTO( id, title, description, price, createdAt, skills );

        return packageResponseDTO;
    }

    @Override
    public SkillResponseDTO toSkillResponseDTO(Skill skill) {
        if ( skill == null ) {
            return null;
        }

        Long packageId = null;
        String packageTitle = null;
        Long id = null;
        String title = null;
        String description = null;
        BigDecimal price = null;
        LocalDateTime createdAt = null;
        List<TopicResponseDTO> topics = null;

        packageId = skillLearningPackageId( skill );
        packageTitle = skillLearningPackageTitle( skill );
        id = skill.getId();
        title = skill.getTitle();
        description = skill.getDescription();
        price = skill.getPrice();
        createdAt = skill.getCreatedAt();
        topics = topicListToTopicResponseDTOList( skill.getTopics() );

        SkillResponseDTO skillResponseDTO = new SkillResponseDTO( id, title, description, price, createdAt, packageId, packageTitle, topics );

        return skillResponseDTO;
    }

    @Override
    public List<SkillResponseDTO> mapSkills(List<Skill> skills) {
        if ( skills == null ) {
            return null;
        }

        List<SkillResponseDTO> list = new ArrayList<SkillResponseDTO>( skills.size() );
        for ( Skill skill : skills ) {
            list.add( toSkillResponseDTO( skill ) );
        }

        return list;
    }

    @Override
    public void updatePackageFromDto(PackageRequestDTO dto, LearningPackage entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.title() != null ) {
            entity.setTitle( dto.title() );
        }
        if ( dto.description() != null ) {
            entity.setDescription( dto.description() );
        }
        if ( dto.price() != null ) {
            entity.setPrice( dto.price() );
        }
        if ( entity.getSkills() != null ) {
            List<Skill> list = skillRequestDTOListToSkillList( dto.skills() );
            if ( list != null ) {
                entity.getSkills().clear();
                entity.getSkills().addAll( list );
            }
        }
        else {
            List<Skill> list = skillRequestDTOListToSkillList( dto.skills() );
            if ( list != null ) {
                entity.setSkills( list );
            }
        }
    }

    protected Skill skillRequestDTOToSkill(SkillRequestDTO skillRequestDTO) {
        if ( skillRequestDTO == null ) {
            return null;
        }

        Skill.SkillBuilder skill = Skill.builder();

        skill.title( skillRequestDTO.title() );
        skill.description( skillRequestDTO.description() );
        skill.price( skillRequestDTO.price() );

        return skill.build();
    }

    protected List<Skill> skillRequestDTOListToSkillList(List<SkillRequestDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Skill> list1 = new ArrayList<Skill>( list.size() );
        for ( SkillRequestDTO skillRequestDTO : list ) {
            list1.add( skillRequestDTOToSkill( skillRequestDTO ) );
        }

        return list1;
    }

    private Long skillLearningPackageId(Skill skill) {
        if ( skill == null ) {
            return null;
        }
        LearningPackage learningPackage = skill.getLearningPackage();
        if ( learningPackage == null ) {
            return null;
        }
        Long id = learningPackage.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String skillLearningPackageTitle(Skill skill) {
        if ( skill == null ) {
            return null;
        }
        LearningPackage learningPackage = skill.getLearningPackage();
        if ( learningPackage == null ) {
            return null;
        }
        String title = learningPackage.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }

    protected VideoResponseDTO videoToVideoResponseDTO(Video video) {
        if ( video == null ) {
            return null;
        }

        Long id = null;
        String url = null;
        String thumbnailUrl = null;
        Integer duration = null;
        Integer version = null;
        Boolean isLatest = null;
        Long noOfViews = null;

        id = video.getId();
        url = video.getUrl();
        thumbnailUrl = video.getThumbnailUrl();
        duration = video.getDuration();
        version = video.getVersion();
        isLatest = video.getIsLatest();
        noOfViews = video.getNoOfViews();

        Long subTopicId = null;
        String subTopicTitle = null;

        VideoResponseDTO videoResponseDTO = new VideoResponseDTO( id, subTopicId, subTopicTitle, url, thumbnailUrl, duration, version, isLatest, noOfViews );

        return videoResponseDTO;
    }

    protected List<VideoResponseDTO> videoListToVideoResponseDTOList(List<Video> list) {
        if ( list == null ) {
            return null;
        }

        List<VideoResponseDTO> list1 = new ArrayList<VideoResponseDTO>( list.size() );
        for ( Video video : list ) {
            list1.add( videoToVideoResponseDTO( video ) );
        }

        return list1;
    }

    protected StudyMaterialResponseDTO studyMaterialToStudyMaterialResponseDTO(StudyMaterial studyMaterial) {
        if ( studyMaterial == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String pdfUrl = null;
        Integer version = null;
        Boolean isLatest = null;

        id = studyMaterial.getId();
        name = studyMaterial.getName();
        pdfUrl = studyMaterial.getPdfUrl();
        version = studyMaterial.getVersion();
        isLatest = studyMaterial.getIsLatest();

        Long subTopicId = null;
        String subTopicTitle = null;

        StudyMaterialResponseDTO studyMaterialResponseDTO = new StudyMaterialResponseDTO( id, subTopicId, name, subTopicTitle, pdfUrl, version, isLatest );

        return studyMaterialResponseDTO;
    }

    protected List<StudyMaterialResponseDTO> studyMaterialListToStudyMaterialResponseDTOList(List<StudyMaterial> list) {
        if ( list == null ) {
            return null;
        }

        List<StudyMaterialResponseDTO> list1 = new ArrayList<StudyMaterialResponseDTO>( list.size() );
        for ( StudyMaterial studyMaterial : list ) {
            list1.add( studyMaterialToStudyMaterialResponseDTO( studyMaterial ) );
        }

        return list1;
    }

    protected SubTopicResponseDTO subTopicToSubTopicResponseDTO(SubTopic subTopic) {
        if ( subTopic == null ) {
            return null;
        }

        Long id = null;
        String title = null;
        String description = null;
        List<VideoResponseDTO> videos = null;
        List<StudyMaterialResponseDTO> studyMaterials = null;

        id = subTopic.getId();
        title = subTopic.getTitle();
        description = subTopic.getDescription();
        videos = videoListToVideoResponseDTOList( subTopic.getVideos() );
        studyMaterials = studyMaterialListToStudyMaterialResponseDTOList( subTopic.getStudyMaterials() );

        Long topicId = null;
        String topicTitle = null;
        String topicDescription = null;

        SubTopicResponseDTO subTopicResponseDTO = new SubTopicResponseDTO( id, title, description, topicId, topicTitle, topicDescription, videos, studyMaterials );

        return subTopicResponseDTO;
    }

    protected List<SubTopicResponseDTO> subTopicListToSubTopicResponseDTOList(List<SubTopic> list) {
        if ( list == null ) {
            return null;
        }

        List<SubTopicResponseDTO> list1 = new ArrayList<SubTopicResponseDTO>( list.size() );
        for ( SubTopic subTopic : list ) {
            list1.add( subTopicToSubTopicResponseDTO( subTopic ) );
        }

        return list1;
    }

    protected TopicResponseDTO topicToTopicResponseDTO(Topic topic) {
        if ( topic == null ) {
            return null;
        }

        Long id = null;
        String title = null;
        String description = null;
        LocalDateTime createdAt = null;
        List<SubTopicResponseDTO> subTopics = null;

        id = topic.getId();
        title = topic.getTitle();
        description = topic.getDescription();
        createdAt = topic.getCreatedAt();
        subTopics = subTopicListToSubTopicResponseDTOList( topic.getSubTopics() );

        Long skillId = null;
        String skillTitle = null;

        TopicResponseDTO topicResponseDTO = new TopicResponseDTO( id, title, description, createdAt, skillId, skillTitle, subTopics );

        return topicResponseDTO;
    }

    protected List<TopicResponseDTO> topicListToTopicResponseDTOList(List<Topic> list) {
        if ( list == null ) {
            return null;
        }

        List<TopicResponseDTO> list1 = new ArrayList<TopicResponseDTO>( list.size() );
        for ( Topic topic : list ) {
            list1.add( topicToTopicResponseDTO( topic ) );
        }

        return list1;
    }
}
