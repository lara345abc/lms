package com.totfd.lms.security;

import com.totfd.lms.entity.Users;
import com.totfd.lms.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Grant the user's role to Spring Security and add 'ROLE_' prefix if missing
        String roleName = "ROLE_" + user.getRole().getName();  // e.g., "ROLE_ADMIN"
        var authorities = List.of(new SimpleGrantedAuthority(roleName));  // e.g., "ROLE_ADMIN"

        logger.info("User authorities: {}", authorities); // Log the authorities for debugging

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
