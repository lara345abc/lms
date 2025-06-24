package com.totfd.lms.controller;

//import com.totfd.lms.dto.user.request.UserRegisterDTO;
//import com.totfd.lms.dto.auth.request.LoginRequestDTO;
//import com.totfd.lms.dto.auth.response.LoginResponseDTO;
import com.totfd.lms.dto.user.UserWithPackagesBasicDTO;
import com.totfd.lms.dto.user.request.AssignPackagesByEmailRequestDTO;
import com.totfd.lms.dto.user.request.AssignPackagesRequestDTO;
import com.totfd.lms.dto.user.response.UserResponseDTO;
import com.totfd.lms.dto.user.response.UserWithPackagesResponseDTO;
import com.totfd.lms.payload.ApiResponse;
import com.totfd.lms.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/getUserById")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@RequestParam Long id, HttpServletRequest request) {
        UserResponseDTO responseDTO = userServiceImpl.getUserById(id);
        return new ResponseEntity<>(
                ApiResponse.success(responseDTO, "User Found", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/getUserWithPackages")
    public ResponseEntity<ApiResponse<UserWithPackagesResponseDTO>> getUserWithPackages(@RequestParam Long id, HttpServletRequest request) {
        UserWithPackagesResponseDTO dto = userServiceImpl.getUserWithPackages(id);
        return new ResponseEntity<>(
                ApiResponse.success(dto, "User and Assigned Packages Found", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @GetMapping("/user/basic-with-packages")
    public ResponseEntity<ApiResponse<UserWithPackagesBasicDTO>> getUserWithBasicPackages(@RequestParam Long id, HttpServletRequest request) {
        UserWithPackagesBasicDTO dto = userServiceImpl.getUserWithBasicPackages(id);
        return ResponseEntity.ok(
                ApiResponse.success(dto, "User with packages and skills fetched", HttpStatus.OK.value(), request.getRequestURI())
        );
    }



    @GetMapping("/getUserByEmail")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getCurrentUser(HttpServletRequest request, Principal principal) {
        String email = principal.getName(); // From token
        UserResponseDTO responseDTO = userServiceImpl.getByEmail(email);
        return new ResponseEntity<>(
                ApiResponse.success(responseDTO, "Current User", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }


    //    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getPaginatedUsers")
    public ResponseEntity<ApiResponse<Page<UserResponseDTO>>> getPaginatedUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request){

        Pageable pageable = PageRequest.of(page, size);

        Page<UserResponseDTO> paginatedUsers = userServiceImpl.getAllUsersWithPage(pageable);

        return  new ResponseEntity<>(
                ApiResponse.success(paginatedUsers, "Paginated List of Users", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );

    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verifyUser(@RequestParam String token,HttpServletRequest request) {
        String responseMessage = userServiceImpl.verifyUser(token);
        return new ResponseEntity<>(
                ApiResponse.success(null, responseMessage, HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<String>> resendVerificationEmail(@RequestParam String email, HttpServletRequest request) {
        String responseMessage = userServiceImpl.resendVerificationEmail(email);
        return new ResponseEntity<>(
                ApiResponse.success(null, responseMessage, HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

    @PutMapping("/{userId}/assign-packages")
    public ResponseEntity<ApiResponse<UserResponseDTO>> assignPackagesToUser(
            @PathVariable Long userId,
            @RequestBody AssignPackagesRequestDTO dto,
            HttpServletRequest request
    ) {
        UserResponseDTO updatedUser = userServiceImpl.assignPackages(userId, dto);
        return new ResponseEntity<>(
                ApiResponse.success(updatedUser, "Packages assigned successfully", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }

//    @PutMapping("/assign-packages-to-users")
//    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> assignPackagesToUsers(
//            @RequestBody AssignPackagesToUsersRequestDTO dto,
//            HttpServletRequest request
//    ) {
//        List<UserResponseDTO> updatedUsers = userServiceImpl.assignPackagesToUsers(dto);
//        return new ResponseEntity<>(
//                ApiResponse.success(updatedUsers, "Packages assigned to users", HttpStatus.OK.value(), request.getRequestURI()),
//                HttpStatus.OK
//        );
//    }

    @PutMapping("/assign-packages-by-email")
    public ResponseEntity<ApiResponse<UserResponseDTO>> assignPackagesByEmail(
            @RequestBody AssignPackagesByEmailRequestDTO dto,
            HttpServletRequest request
    ) {
        UserResponseDTO updatedUser = userServiceImpl.assignPackagesToUserByEmail(dto);
        return new ResponseEntity<>(
                ApiResponse.success(updatedUser, "Packages assigned to user", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }



    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers(HttpServletRequest request) {
        List<UserResponseDTO> users = userServiceImpl.getAllUsers();
        return new ResponseEntity<>(
                ApiResponse.success(users, "All users retrieved", HttpStatus.OK.value(), request.getRequestURI()),
                HttpStatus.OK
        );
    }



}
