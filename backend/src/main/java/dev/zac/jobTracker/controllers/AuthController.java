package dev.zac.jobTracker.controllers;

import dev.zac.jobTracker.dto.auth.AuthResponseDto;
import dev.zac.jobTracker.dto.auth.ChangePasswordDto;
import dev.zac.jobTracker.dto.auth.LoginRequestDto;
import dev.zac.jobTracker.dto.auth.RegisterRequestDto;
import dev.zac.jobTracker.services.AuthService;
import dev.zac.jobTracker.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    /**
     * Register a new user.
     *
     * @param registerRequest the registration request
     * @return authentication response with JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        log.info("Registration request received for email: {}", registerRequest.getEmail());
        
        AuthResponseDto response = authService.register(registerRequest);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticate user login.
     *
     * @param loginRequest the login request
     * @return authentication response with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        log.info("Login request received for email: {}", loginRequest.getEmail());
        
        AuthResponseDto response = authService.login(loginRequest);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Logout endpoint (mainly for frontend to clear tokens).
     * Since we're using stateless JWT, no server-side action needed.
     *
     * @return success response
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * Change user password.
     *
     * @param changePasswordRequest the password change request
     * @return success response
     */
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordRequest) {
        log.info("Password change request received");
        
        userService.changePassword(changePasswordRequest);
        
        return ResponseEntity.ok("Password changed successfully");
    }
}
