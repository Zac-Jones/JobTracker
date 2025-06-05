package dev.zac.jobTracker.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating user profile information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProfileDto {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @PositiveOrZero(message = "Years of experience must be positive or zero")
    private Integer yearsOfExperience;

    // TODO: Use list of strings instead
    @Size(max = 1000, message = "Proficient stack must not exceed 1000 characters")
    private String proficientStack;

    @Size(max = 100, message = "Role title must not exceed 100 characters")
    private String roleTitle;
}
