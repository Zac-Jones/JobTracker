package dev.zac.jobTracker.dto.jobApplication;

import java.time.LocalDate;
import java.util.List;

import dev.zac.jobTracker.enums.JobApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing job application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateJobApplicationDto {

    @NotBlank(message = "Company name is required")
    @Size(max = 100, message = "Company name must not exceed 100 characters")
    private String company;

    @NotBlank(message = "Job title is required")
    @Size(max = 100, message = "Job title must not exceed 100 characters")
    private String jobTitle;

    @NotNull(message = "Date applied is required")
    private LocalDate dateApplied;

    @NotNull(message = "Status is required")
    private JobApplicationStatus status;
    
    private LocalDate dateLastHeardBack;

    @Size(max = 50, message = "Each technology stack item must not exceed 50 characters")
    private List<String> coreStack;

    @PositiveOrZero(message = "Experience required must be positive or zero")
    private Integer experienceRequired;

    @Size(max = 2000, message = "Notes must not exceed 2000 characters")
    private String notes;
}
