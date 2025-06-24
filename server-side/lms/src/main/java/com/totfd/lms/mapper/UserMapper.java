package com.totfd.lms.mapper;

import com.totfd.lms.dto.learnigPackages.PackageBasicDTO;
import com.totfd.lms.dto.learnigPackages.response.PackageResponseDTO;
import com.totfd.lms.dto.skill.SkillBasicDTO;
import com.totfd.lms.dto.user.UserWithPackagesBasicDTO;
import com.totfd.lms.dto.user.request.UserRequestDTO;
import com.totfd.lms.dto.user.response.UserResponseDTO;
import com.totfd.lms.dto.user.response.UserWithPackagesResponseDTO;
import com.totfd.lms.entity.*;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = PackageMapper.class)
public interface UserMapper {

    // Use custom Role → String mapping
//    @Mapping(source = "role", target = "role")
    @Mapping(target = "packages", expression = "java(mapBasicPackages(users))")
    UserResponseDTO toResponseDTO(Users users);

//    @Mapping(source = "role", target = "role")
    Users toEntity(UserRequestDTO requestDTO);

    // Custom mapping from String → Role
    default Role map(String roleName) {
        if (roleName == null) return null;
        Role role = new Role();
        role.setName(roleName);
        return role;
    }

    // Custom mapping from Role → String
    default String map(Role role) {
        return role != null ? role.getName() : null;
    }

    // Helper to map deeply loaded packages with skills
    default List<PackageResponseDTO> mapPackages(Users user, @Context PackageMapper packageMapper) {
        if (user.getAssignedPackages() == null) return List.of();

        return user.getAssignedPackages().stream()
                .map(UserLearningPackage::getLearningPackage)
                .map(pkg -> new PackageResponseDTO(
                        pkg.getId(),
                        pkg.getTitle(),
                        pkg.getDescription(),
                        pkg.getPrice(),
                        pkg.getCreatedAt(),
                        packageMapper.mapSkills(pkg.getSkills())
                ))
                .toList();
    }

//    @Mapping(source = "role", target = "role")
    @Mapping(target = "packages", expression = "java(mapPackages(user, packageMapper))")
    UserWithPackagesResponseDTO toUserWithPackagesResponseDTO(Users user, @Context PackageMapper packageMapper);

//    @Mapping(source = "role", target = "role")
    @Mapping(target = "packages", expression = "java(mapBasicPackages(user))")
    UserWithPackagesBasicDTO toUserWithBasicPackagesDTO(Users user);

    default List<PackageBasicDTO> mapBasicPackages(Users user) {
        if (user.getTempPackages() == null) return List.of();

        return user.getTempPackages().stream()
                .map(pkg -> new PackageBasicDTO(
                        pkg.getId(),
                        pkg.getTitle(),
                        pkg.getDescription(),
                        pkg.getPrice(),
                        pkg.getCreatedAt(),
                        pkg.getSkills().stream()
                                .map(skill -> new SkillBasicDTO(
                                        skill.getId(),
                                        skill.getTitle(),
                                        skill.getDescription(),
                                        skill.getPrice(),
                                        skill.getCreatedAt()
                                ))
                                .collect(Collectors.toList())
                ))
                .toList();
    }

}
