package com.totfd.lms.service;

import com.totfd.lms.dto.learnigPackages.request.PackageRequestDTO;
import com.totfd.lms.dto.learnigPackages.response.PackageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface PackageService {

    PackageResponseDTO createPackage(PackageRequestDTO dto);

    PackageResponseDTO getPackageById(Long id);

    List<PackageResponseDTO> getAllPackages();

    Page<PackageResponseDTO> getAllPackagesWithPage(Pageable pageable);

    PackageResponseDTO updatePackage(Long id, PackageRequestDTO dto);

    Page<PackageResponseDTO> findAllWithSkills(Pageable pageable);

    List<PackageResponseDTO> packgeWithAllSkills();

    void deletePackage(Long id);

    Page<PackageResponseDTO> findPackagesWithFilters(String tittle, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}
