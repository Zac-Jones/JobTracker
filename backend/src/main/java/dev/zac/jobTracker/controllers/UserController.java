package dev.zac.jobTracker.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.zac.jobTracker.dto.user.UpdateUserProfileDto;
import dev.zac.jobTracker.dto.user.UserProfileDto;
import dev.zac.jobTracker.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for user profile operations.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Get current user's profile.
     *
     * @return user profile DTO
     */
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getCurrentUserProfile() {
        log.info("Getting current user profile");
        
        UserProfileDto profile = userService.getCurrentUserProfile();
        
        return ResponseEntity.ok(profile);
    }

    /**
     * Update current user's profile.
     *
     * @param updateRequest the update request
     * @return updated user profile DTO
     */
    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> updateCurrentUserProfile(
            @Valid @RequestBody UpdateUserProfileDto updateRequest) {
        log.info("Updating current user profile");
        
        UserProfileDto updatedProfile = userService.updateCurrentUserProfile(updateRequest);
        
        return ResponseEntity.ok(updatedProfile);
    }
}