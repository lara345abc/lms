package com.totfd.lms.mapper;

import com.totfd.lms.dto.comment.request.CommentRequestDTO;
import com.totfd.lms.dto.comment.response.CommentResponseDTO;
import com.totfd.lms.entity.Comment;
import com.totfd.lms.entity.Users;
import com.totfd.lms.entity.Video;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", source = "user")
    @Mapping(target = "video", source = "dto.videoId", qualifiedByName = "mapVideoFromId")
    Comment toEntity(CommentRequestDTO dto, Users user);

    @Mapping(target = "userName", source = "users.name")
    @Mapping(target = "videoId", source = "video.id")
    CommentResponseDTO toResponseDTO(Comment comment);

    @Named("mapVideoFromId")
    static Video mapVideoFromId(Long id) {
        return id == null ? null : Video.builder().id(id).build();
    }
}
