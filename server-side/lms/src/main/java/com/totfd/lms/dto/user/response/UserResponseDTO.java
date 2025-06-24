package com.totfd.lms.dto.user.response;

import com.totfd.lms.dto.learnigPackages.PackageBasicDTO;
import com.totfd.lms.dto.learnigPackages.response.PackageResponseDTO;

import java.util.List;

public record UserResponseDTO(
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
