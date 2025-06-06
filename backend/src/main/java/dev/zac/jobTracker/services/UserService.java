package dev.zac.jobTracker.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.zac.jobTracker.dto.user.UpdateUserProfileDto;
import dev.zac.jobTracker.dto.user.UserProfileDto;
import dev.zac.jobTracker.entities.User;
import dev.zac.jobTracker.exceptions.EmailAlreadyExistsException;
import dev.zac.jobTracker.exceptions.ResourceNotFoundException;
import dev.zac.jobTracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for handling user-related operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    /**
     * Get current authenticated user's profile.
     *
     * @return user profile DTO
     */
    public UserProfileDto getCurrentUserProfile() {
        User user = getCurrentUser();
        return convertToUserProfileDto(user);
    }

    /**
     * Update current authenticated user's profile.
     *
     * @param updateRequest the update request
     * @return updated user profile DTO
     */
    @Transactional
    public UserProfileDto updateCurrentUserProfile(UpdateUserProfileDto updateRequest) {
        User user = getCurrentUser();
        
        log.info("Updating profile for user ID: {}", user.getId());

        // Check if email is being changed and if it already exists
        if (!user.getEmail().equals(updateRequest.getEmail()) 
                && userRepository.existsByEmail(updateRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + updateRequest.getEmail());
        }
        
        // Update user fields
        user.setName(updateRequest.getName());
        user.setEmail(updateRequest.getEmail());
        user.setExperienceYears(updateRequest.getExperienceYears());
        user.setTechnologyStack(updateRequest.getTechnologyStack());
        user.setJobTitle(updateRequest.getJobTitle());

        User updatedUser = userRepository.save(user);
        
        log.info("Profile updated successfully for user ID: {}", updatedUser.getId());
        
        return convertToUserProfileDto(updatedUser);
    }

    /**
     * Get the current authenticated user.
     *
     * @return the current user
     * @throws ResourceNotFoundException if user not found
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    /**
     * Convert User entity to UserProfileDto.
     *
     * @param user the user entity
     * @return user profile DTO
     */
    private UserProfileDto convertToUserProfileDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .experienceYears(user.getExperienceYears())
                .technologyStack(user.getTechnologyStack())
                .jobTitle(user.getJobTitle())
                .build();
    }
}
