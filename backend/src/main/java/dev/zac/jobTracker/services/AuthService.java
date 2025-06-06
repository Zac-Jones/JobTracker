package dev.zac.jobTracker.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.zac.jobTracker.dto.auth.AuthResponseDto;
import dev.zac.jobTracker.dto.auth.LoginRequestDto;
import dev.zac.jobTracker.dto.auth.RegisterRequestDto;
import dev.zac.jobTracker.entities.User;
import dev.zac.jobTracker.exceptions.EmailAlreadyExistsException;
import dev.zac.jobTracker.repositories.UserRepository;
import dev.zac.jobTracker.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for handling authentication operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Register a new user.
     *
     * @param request the registration request
     * @return authentication response with JWT token
     * @throws EmailAlreadyExistsException if email is already registered
     */
    @Transactional
    public AuthResponseDto register(RegisterRequestDto request) {
        log.info("Registering new user with email: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        }        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .experienceYears(request.getExperienceYears())
                .technologyStack(request.getTechnologyStack())
                .jobTitle(request.getJobTitle())
                .role(User.Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        
        String jwtToken = jwtService.generateToken(savedUser);
        
        log.info("User registered successfully with ID: {}", savedUser.getId());
        
        return new AuthResponseDto(jwtToken, savedUser.getId(), savedUser.getEmail(), savedUser.getName());
    }

    /**
     * Authenticate a user login.
     *
     * @param request the login request
     * @return authentication response with JWT token
     */
    public AuthResponseDto login(LoginRequestDto request) {
        log.info("User attempting login with email: {}", request.getEmail());
        
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String jwtToken = jwtService.generateToken(user);
        
        log.info("User logged in successfully with ID: {}", user.getId());
        
        return new AuthResponseDto(jwtToken, user.getId(), user.getEmail(), user.getName());
    }
}
