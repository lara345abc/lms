package com.totfd.lms.dto.user;

import com.totfd.lms.dto.learnigPackages.PackageBasicDTO;

import java.util.List;

public record UserWithPackagesBasicDTO(
        Long id,
        String email,
        String name,
        String role,
        String givenName,
        String familyName,
        String pictureUrl,
        String locale,
        List<PackageBasicDTO> packages
) {}

