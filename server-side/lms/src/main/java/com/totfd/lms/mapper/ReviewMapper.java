package com.totfd.lms.mapper;

import com.totfd.lms.dto.review.request.ReviewRequestDTO;
import com.totfd.lms.dto.review.response.ReviewResponseDTO;
import com.totfd.lms.entity.Review;
import com.totfd.lms.entity.Users;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true) // We set this manually in service
    @Mapping(target = "createdAt", ignore = true)
    Review toEntity(ReviewRequestDTO dto);

    @Mapping(target = "userId", source = "users.id")
    @Mapping(target = "userName", source = "users.name")
    ReviewResponseDTO toResponseDTO(Review review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "users", ignore = true)
    void updateFromDto(ReviewRequestDTO dto, @MappingTarget Review review);
}
