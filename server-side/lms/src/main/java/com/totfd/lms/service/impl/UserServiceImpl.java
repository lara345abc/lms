package com.totfd.lms.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.totfd.lms.dto.auth.request.LoginRequestDTO;
import com.totfd.lms.dto.auth.response.LoginResponseDTO;
import com.totfd.lms.dto.learnigPackages.response.PackageResponseDTO;
import com.totfd.lms.dto.user.UserWithPackagesBasicDTO;
import com.totfd.lms.dto.user.request.AssignPackagesByEmailRequestDTO;
import com.totfd.lms.dto.user.request.AssignPackagesRequestDTO;
import com.totfd.lms.dto.user.request.UserRegisterDTO;
import com.totfd.lms.dto.user.request.UserRequestDTO;
import com.totfd.lms.dto.user.response.UserResponseDTO;
import com.totfd.lms.dto.user.response.UserWithPackagesResponseDTO;
import com.totfd.lms.entity.*;
import com.totfd.lms.exceptions.DuplicateEmailException;
import com.totfd.lms.exceptions.ResourceNotFoundException;
import com.totfd.lms.exceptions.UserNotFoundException;
import com.totfd.lms.mapper.PackageMapper;
import com.totfd.lms.mapper.UserMapper;
import com.totfd.lms.repository.PackageRepository;
import com.totfd.lms.repository.RoleRepository;
import com.totfd.lms.repository.UsersRepository;
import com.totfd.lms.service.EmailService;
import com.totfd.lms.service.JwtService;
import com.totfd.lms.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private  static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final EmailServiceImpl emailServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PackageRepository packageRepository;
    private final PackageMapper packageMapper;


    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        Users user = userMapper.toEntity(userRequestDTO);
        Users savedUser = usersRepository.save(user);
        return userMapper.toResponseDTO(savedUser);
    }

    @Override
    public UserResponseDTO registerUser(UserRegisterDTO userRegisterDTO) {
        if (usersRepository.findByEmail(userRegisterDTO.email()).isPresent()) {
            throw new DuplicateEmailException("Email is already registered");
        }

        Role roleUser = roleRepository.findByName("USER").orElseGet(() ->
                roleRepository.save(Role.builder().name("USER").build())
        );

        Users user = Users.builder()
                .email(userRegisterDTO.email())
                .name(userRegisterDTO.name())
                .password(passwordEncoder.encode(userRegisterDTO.password()))
                .role(roleUser)
                .enabled(false)
                .build();

        // Generate JWT token for email verification
        String token = jwtService.generateEmailVerificationToken(userRegisterDTO.email());

        user.setVerificationToken(token);
        Users savedUser = usersRepository.save(user);

        // Send verification email with the token
        emailServiceImpl.sendVerificationEmail(savedUser.getEmail(), token);

        return userMapper.toResponseDTO(savedUser);
    }

    @Override
    public String verifyUser(String token) {
        String email = jwtService.validateAndGetEmail(token);

        if (jwtService.isTokenExpired(token)) {
            throw new RuntimeException("Verification token has expired");
        }

        Users user = usersRepository.findByEmail(email).orElseThrow(() ->
                new RuntimeException("Invalid email address"));

        user.setEnabled(true);
        user.setVerificationToken(null);
        usersRepository.save(user);

        return "Email verified successfully!";
    }

    @Override
    public String resendVerificationEmail(String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User not found"));

        if (user.getEnabled()) {
            throw new RuntimeException("User is already verified");
        }

        // Generate a new JWT token for email verification
        String token = jwtService.generateEmailVerificationToken(email);
        user.setVerificationToken(token);
        usersRepository.save(user);

        // Send the email again
        emailServiceImpl.sendVerificationEmail(user.getEmail(), token);

        return "Verification email resent successfully!";
    }

    @Override
    public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {

        String email = loginRequestDTO.email();
        String rawPassword = loginRequestDTO.password();

        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }

        UserResponseDTO userResponseDTO = userMapper.toResponseDTO(user);
        String token = jwtService.generateToken(user);

        return new LoginResponseDTO(userResponseDTO, "Login Success", token);

    }

//    @Override
//    @Transactional
//    public UserResponseDTO loginWithGoogle(GoogleIdToken.Payload payload) {
//        String email = payload.getEmail();
//        String name = (String) payload.get("name");
//        String oauthId = payload.getSubject();
//        String givenName = (String) payload.get("given_name");
//        String familyName = (String) payload.get("family_name");
//        String pictureUrl = (String) payload.get("picture");
//        String locale = (String) payload.get("locale");
//
//        Users user = usersRepository.findByEmail(email).orElse(null);
//
//        if (user != null) {
//            boolean updated = false;
//            if (!Objects.equals(user.getName(), name)) {
//                user.setName(name);
//                updated = true;
//            }
//            if (!Objects.equals(user.getOauthId(), oauthId)) {
//                user.setOauthId(oauthId);
//                updated = true;
//            }
//            if (!Objects.equals(user.getGivenName(), givenName)) {
//                user.setGivenName(givenName);
//                updated = true;
//            }
//            if (!Objects.equals(user.getFamilyName(), familyName)) {
//                user.setFamilyName(familyName);
//                updated = true;
//            }
//            if (!Objects.equals(user.getPictureUrl(), pictureUrl)) {
//                user.setPictureUrl(pictureUrl);
//                updated = true;
//            }
//            if (!Objects.equals(user.getLocale(), locale)) {
//                user.setLocale(locale);
//                updated = true;
//            }
//
//            if (updated) {
//                usersRepository.save(user);
//            }
//
//        } else {
//            Role roleUser = roleRepository.findByName("USER").orElseGet(() -> {
//                Role newRole = Role.builder().name("USER").build();
//                return roleRepository.save(newRole);
//            });
//
//            user = Users.builder()
//                    .email(email)
//                    .name(name)
//                    .oauthId(oauthId)
//                    .givenName(givenName)
//                    .familyName(familyName)
//                    .pictureUrl(pictureUrl)
//                    .locale(locale)
//                    .role(roleUser)
//                    .build();
//
//            usersRepository.save(user);
//        }
//
//        return userMapper.toResponseDTO(user);
//    }

    @Override
    @Transactional
    public LoginResponseDTO loginWithGoogle(GoogleIdToken.Payload payload) {
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String oauthId = payload.getSubject();
        String givenName = (String) payload.get("given_name");
        String familyName = (String) payload.get("family_name");
        String pictureUrl = (String) payload.get("picture");
        String locale = (String) payload.get("locale");

        Users user = usersRepository.findByEmail(email).orElse(null);

        if (user != null) {
            boolean updated = false;
            if (!Objects.equals(user.getName(), name)) {
                user.setName(name);
                updated = true;
            }
            if (!Objects.equals(user.getOauthId(), oauthId)) {
                user.setOauthId(oauthId);
                updated = true;
            }
            if (!Objects.equals(user.getGivenName(), givenName)) {
                user.setGivenName(givenName);
                updated = true;
            }
            if (!Objects.equals(user.getFamilyName(), familyName)) {
                user.setFamilyName(familyName);
                updated = true;
            }
            if (!Objects.equals(user.getPictureUrl(), pictureUrl)) {
                user.setPictureUrl(pictureUrl);
                updated = true;
            }
            if (!Objects.equals(user.getLocale(), locale)) {
                user.setLocale(locale);
                updated = true;
            }

            if (updated) {
                usersRepository.save(user);
            }

        } else {
            Role roleUser = roleRepository.findByName("USER").orElseGet(() -> {
                Role newRole = Role.builder().name("USER").build();
                return roleRepository.save(newRole);
            });

            user = Users.builder()
                    .email(email)
                    .name(name)
                    .oauthId(oauthId)
                    .givenName(givenName)
                    .familyName(familyName)
                    .pictureUrl(pictureUrl)
                    .locale(locale)
                    .role(roleUser)
                    .password("OAUTH_USER")
                    .build();

            usersRepository.save(user);
        }
        UserResponseDTO userResponseDTO = userMapper.toResponseDTO(user);
        String token = jwtService.generateToken(user);

        return new LoginResponseDTO(userResponseDTO, "Login Success", token);
    }


    @Override
    public UserResponseDTO getUserById(Long id) {

        Users user = usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not Found with this id :" + id));

        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public UserWithPackagesResponseDTO getUserWithPackages(Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not Found with this id: " + id));

        // Extract packages through the join table
        List<LearningPackage> packages = user.getAssignedPackages().stream()
                .map(UserLearningPackage::getLearningPackage)
                .peek(pkg -> Hibernate.initialize(pkg.getSkills()))
                .toList();

        // Optional: store temporarily if your mapper doesn't re-query
        user.setTempPackages(packages); // You may need to create this transient helper field

        return userMapper.toUserWithPackagesResponseDTO(user, packageMapper);
    }

    @Transactional
    public UserWithPackagesBasicDTO getUserWithBasicPackages(Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not Found with this id: " + id));

        List<LearningPackage> packages = user.getAssignedPackages().stream()
                .map(UserLearningPackage::getLearningPackage)
                .peek(pkg -> Hibernate.initialize(pkg.getSkills()))
                .toList();

        user.setTempPackages(packages); // same as above

        return userMapper.toUserWithBasicPackagesDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO getByEmail(String email) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not Found with this email: " + email));

        // Extract packages through the join table
        List<LearningPackage> packages = user.getAssignedPackages().stream()
                .map(UserLearningPackage::getLearningPackage)
                .peek(pkg -> Hibernate.initialize(pkg.getSkills())) // Load skills if needed
                .toList();

        // Store temporarily for mapping
        user.setTempPackages(packages); // Make sure `tempPackages` is marked as @Transient in Users entity

        return userMapper.toResponseDTO(user);
    }


    @Override
    @Transactional
    public List<UserResponseDTO> getAllUsers() {
        List<Users> usersList = usersRepository.findAll();

        List<UserResponseDTO> responseDTOList = new ArrayList<>();

        for (Users user : usersList) {
            // Initialize packages and skills
            List<LearningPackage> packages = user.getAssignedPackages().stream()
                    .map(UserLearningPackage::getLearningPackage)
                    .peek(pkg -> Hibernate.initialize(pkg.getSkills()))
                    .toList();

            // Store in transient field if needed by mapper
            user.setTempPackages(packages); // Add transient field in Users entity

            // Map to DTO
            UserResponseDTO dto = userMapper.toResponseDTO(user);
            responseDTOList.add(dto);
        }

        return responseDTOList;
    }

    @Override
    public Page<UserResponseDTO> getAllUsersWithPage(Pageable pageable) {
        Page<Users> usersPage = usersRepository.findAll(pageable);
        return usersPage.map(userMapper::toResponseDTO);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        Users user = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("User not Found with this id :" + id));

        user.setName(userRequestDTO.name());

        Users updatedUser = usersRepository.save(user);

        return userMapper.toResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        Users user = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("User not Found with this id :" + id));
        usersRepository.delete(user);
    }

    @Transactional
    public UserResponseDTO assignPackages(Long userId, AssignPackagesRequestDTO dto) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<Long> existingPackageIds = user.getAssignedPackages().stream()
                .map(link -> link.getLearningPackage().getId())
                .collect(Collectors.toSet());

        List<LearningPackage> packages = packageRepository.findAllById(dto.packageIds());

        for (LearningPackage pkg : packages) {
            if (!existingPackageIds.contains(pkg.getId())) {
                UserLearningPackage link = UserLearningPackage.builder()
                        .user(user)
                        .learningPackage(pkg)
                        .build();
                user.getAssignedPackages().add(link); // owning side
            }
        }

        return userMapper.toResponseDTO(usersRepository.save(user));
    }

//    @Transactional
//    public List<UserResponseDTO> assignPackagesToUsers(AssignPackagesToUsersRequestDTO dto) {
//        List<Users> users = usersRepository.findAllById(dto.userIds());
//        Set<LearningPackage> newPackages = new HashSet<>(packageRepository.findAllById(dto.packageIds()));
//
//        List<UserResponseDTO> result = new ArrayList<>();
//
//        for (Users user : users) {
//            Set<LearningPackage> currentPackages = user.getLearningPackages();
//
//            boolean updated = false;
//            for (LearningPackage pkg : newPackages) {
//                if (!currentPackages.contains(pkg)) {
//                    currentPackages.add(pkg);
//                    updated = true;
//                }
//            }
//
//            if (updated) {
//                usersRepository.save(user);
//            }
//
//            UserResponseDTO dtoResult = new UserResponseDTO(
//                    user.getId(),
//                    user.getEmail(),
//                    user.getName(),
//                    user.getRole().getName(),
//                    user.getGivenName(),
//                    user.getFamilyName(),
//                    user.getPictureUrl(),
//                    user.getLocale()
////                    user.getLearningPackages().stream()
////                            .map(pkg -> new PackageResponseDTO(
////                                    pkg.getId(),
////                                    pkg.getTitle(),
////                                    pkg.getDescription(),
////                                    pkg.getPrice(),
////                                    pkg.getCreatedAt(),
////                                    null
////                            ))
////                            .toList()
//            );
//
//            result.add(dtoResult);
//        }
//
//        return result;
//    }


@Transactional
public UserResponseDTO assignPackagesToUserByEmail(AssignPackagesByEmailRequestDTO dto) {
    Users user = usersRepository.findByEmail(dto.email())
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + dto.email()));

    Set<Long> existingPackageIds = user.getAssignedPackages().stream()
            .map(link -> link.getLearningPackage().getId())
            .collect(Collectors.toSet());

    List<LearningPackage> packages = packageRepository.findAllById(dto.packageIds());
    boolean updated = false;

    for (LearningPackage pkg : packages) {
        if (!existingPackageIds.contains(pkg.getId())) {
            UserLearningPackage link = UserLearningPackage.builder()
                    .user(user)
                    .learningPackage(pkg)
                    .build();
            user.getAssignedPackages().add(link);
            updated = true;
        }
    }

    if (updated) {
        usersRepository.save(user); // cascade persists links
    }

    return userMapper.toResponseDTO(user);
}


    @Override
    public Optional<Users> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }
}
