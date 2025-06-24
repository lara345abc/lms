package com.totfd.lms.mapper;

import com.totfd.lms.dto.learnigPackages.PackageBasicDTO;
import com.totfd.lms.dto.learnigPackages.response.PackageResponseDTO;
import com.totfd.lms.dto.user.UserWithPackagesBasicDTO;
import com.totfd.lms.dto.user.request.UserRequestDTO;
import com.totfd.lms.dto.user.response.UserResponseDTO;
import com.totfd.lms.dto.user.response.UserWithPackagesResponseDTO;
import com.totfd.lms.entity.Users;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-24T18:40:58+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponseDTO toResponseDTO(Users users) {
        if ( users == null ) {
            return null;
        }

        Long id = null;
        String email = null;
        String name = null;
        String role = null;
        String givenName = null;
        String familyName = null;
        String pictureUrl = null;
        String locale = null;

        id = users.getId();
        email = users.getEmail();
        name = users.getName();
        role = map( users.getRole() );
        givenName = users.getGivenName();
        familyName = users.getFamilyName();
        pictureUrl = users.getPictureUrl();
        locale = users.getLocale();

        List<PackageBasicDTO> packages = mapBasicPackages(users);

        UserResponseDTO userResponseDTO = new UserResponseDTO( id, email, name, role, givenName, familyName, pictureUrl, locale, packages );

        return userResponseDTO;
    }

    @Override
    public Users toEntity(UserRequestDTO requestDTO) {
        if ( requestDTO == null ) {
            return null;
        }

        Users.UsersBuilder users = Users.builder();

        users.id( requestDTO.id() );
        users.email( requestDTO.email() );
        users.name( requestDTO.name() );
        users.givenName( requestDTO.givenName() );
        users.familyName( requestDTO.familyName() );
        users.pictureUrl( requestDTO.pictureUrl() );
        users.locale( requestDTO.locale() );
        users.role( map( requestDTO.role() ) );

        return users.build();
    }

    @Override
    public UserWithPackagesResponseDTO toUserWithPackagesResponseDTO(Users user, PackageMapper packageMapper) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String email = null;
        String name = null;
        String role = null;
        String givenName = null;
        String familyName = null;
        String pictureUrl = null;
        String locale = null;

        id = user.getId();
        email = user.getEmail();
        name = user.getName();
        role = map( user.getRole() );
        givenName = user.getGivenName();
        familyName = user.getFamilyName();
        pictureUrl = user.getPictureUrl();
        locale = user.getLocale();

        List<PackageResponseDTO> packages = mapPackages(user, packageMapper);

        UserWithPackagesResponseDTO userWithPackagesResponseDTO = new UserWithPackagesResponseDTO( id, email, name, role, givenName, familyName, pictureUrl, locale, packages );

        return userWithPackagesResponseDTO;
    }

    @Override
    public UserWithPackagesBasicDTO toUserWithBasicPackagesDTO(Users user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String email = null;
        String name = null;
        String role = null;
        String givenName = null;
        String familyName = null;
        String pictureUrl = null;
        String locale = null;

        id = user.getId();
        email = user.getEmail();
        name = user.getName();
        role = map( user.getRole() );
        givenName = user.getGivenName();
        familyName = user.getFamilyName();
        pictureUrl = user.getPictureUrl();
        locale = user.getLocale();

        List<PackageBasicDTO> packages = mapBasicPackages(user);

        UserWithPackagesBasicDTO userWithPackagesBasicDTO = new UserWithPackagesBasicDTO( id, email, name, role, givenName, familyName, pictureUrl, locale, packages );

        return userWithPackagesBasicDTO;
    }
}
