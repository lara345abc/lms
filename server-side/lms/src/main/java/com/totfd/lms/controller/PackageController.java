package com.totfd.lms.controller;

import com.totfd.lms.dto.learnigPackages.request.CreatePackageRequest;
import com.totfd.lms.dto.learnigPackages.request.PackageRequestDTO;
import com.totfd.lms.dto.learnigPackages.response.PackageResponseDTO;
import com.totfd.lms.payload.ApiResponse;
import com.totfd.lms.service.PackageService;
import com.totfd.lms.service.impl.PackageServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class PackageController {

    Logger logger = LoggerFactory.getLogger(PackageController.class);
    private final PackageService packageService;
    private final PackageServiceImpl packageServiceImpl;

    @PostMapping("/createPackage")
    public ResponseEntity<ApiResponse<PackageResponseDTO>> create(@RequestBody PackageRequestDTO dto, HttpServletRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(packageServiceImpl.createPackage(dto), "Package created", 200, request.getRequestURI())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PackageResponseDTO>> getById(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(packageServiceImpl.getPackageById(id), "Package found", 200, request.getRequestURI())
        );
    }

    @GetMapping("/getAllPackage")
    public ResponseEntity<ApiResponse<List<PackageResponseDTO>>> getAll(HttpServletRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(packageServiceImpl.getAllPackages(), "Packages fetched", 200, request.getRequestURI())
        );
    }
    @GetMapping("/getAllPackageWithSkills")
    public ResponseEntity<ApiResponse<List<PackageResponseDTO>>> getAllWithSkills(HttpServletRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        packageServiceImpl.packgeWithAllSkills(),
                        "Packages with skills fetched",
                        200,
                        request.getRequestURI()
                )
        );
    }
    @GetMapping("/findAllWithSkills")
    public ResponseEntity<ApiResponse<?>> findAllWithSkills(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "20") int size,
                                                            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PackageResponseDTO> result = packageServiceImpl.findAllWithSkills(pageable);

        return ResponseEntity.ok(
                ApiResponse.success(result, "Paged packages fetched with skills", 200, request.getRequestURI())
        );
    }




    @GetMapping("/paged")
    public ResponseEntity<ApiResponse<?>> getAllPaged(Pageable pageable, HttpServletRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(packageServiceImpl.getAllPackagesWithPage(pageable), "Paged packages fetched", 200, request.getRequestURI())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PackageResponseDTO>> update(@PathVariable Long id, @RequestBody PackageRequestDTO dto, HttpServletRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(packageService.updatePackage(id, dto), "Package updated", 200, request.getRequestURI())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id, HttpServletRequest request) {
        packageService.deletePackage(id);
        return ResponseEntity.ok(
                ApiResponse.success("Package deleted", "Success", 200, request.getRequestURI())
        );
    }


    @GetMapping("/filtered")
    public ResponseEntity<ApiResponse<?>> getFilteredPackages(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PackageResponseDTO> result = packageServiceImpl.findPackagesWithFilters(title, minPrice, maxPrice, pageable);

        return ResponseEntity.ok(ApiResponse.success(result, "Filtered paged packages fetched", 200, request.getRequestURI()));
    }

    @PostMapping("/createFullPackage")
    public ResponseEntity<ApiResponse<PackageResponseDTO>> createFullPackage(
            @RequestBody CreatePackageRequest dto, HttpServletRequest request) {
        return new ResponseEntity<>(
                ApiResponse.success(packageServiceImpl.createFullPackage(dto), "Package created with nested content", 201, request.getRequestURI()),
                HttpStatus.CREATED
        );
    }


}
