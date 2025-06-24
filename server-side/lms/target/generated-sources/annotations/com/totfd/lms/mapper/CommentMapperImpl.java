package com.totfd.lms.mapper;

import com.totfd.lms.dto.comment.request.CommentRequestDTO;
import com.totfd.lms.dto.comment.response.CommentResponseDTO;
import com.totfd.lms.entity.Comment;
import com.totfd.lms.entity.Users;
import com.totfd.lms.entity.Video;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-24T18:40:58+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment toEntity(CommentRequestDTO dto, Users user) {
        if ( dto == null && user == null ) {
            return null;
        }

        Comment.CommentBuilder comment = Comment.builder();

        if ( dto != null ) {
            comment.video( CommentMapper.mapVideoFromId( dto.videoId() ) );
            comment.comment( dto.comment() );
            comment.rating( dto.rating() );
        }
        if ( user != null ) {
            comment.users( user );
            comment.createdAt( user.getCreatedAt() );
        }

        return comment.build();
    }

    @Override
    public CommentResponseDTO toResponseDTO(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        String userName = null;
        Long videoId = null;
        Long id = null;
        String comment1 = null;
        Integer rating = null;
        LocalDateTime createdAt = null;

        userName = commentUsersName( comment );
        videoId = commentVideoId( comment );
        id = comment.getId();
        comment1 = comment.getComment();
        rating = comment.getRating();
        createdAt = comment.getCreatedAt();

        CommentResponseDTO commentResponseDTO = new CommentResponseDTO( id, userName, videoId, comment1, rating, createdAt );

        return commentResponseDTO;
    }

    private String commentUsersName(Comment comment) {
        if ( comment == null ) {
            return null;
        }
        Users users = comment.getUsers();
        if ( users == null ) {
            return null;
        }
        String name = users.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long commentVideoId(Comment comment) {
        if ( comment == null ) {
            return null;
        }
        Video video = comment.getVideo();
        if ( video == null ) {
            return null;
        }
        Long id = video.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
