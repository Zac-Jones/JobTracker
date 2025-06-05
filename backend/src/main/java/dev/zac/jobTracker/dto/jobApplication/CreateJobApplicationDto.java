package dev.zac.jobTracker.dto.jobApplication;

import java.time.LocalDate;

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
 * DTO for creating a new job application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobApplicationDto {

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

    // TODO: Use list of strings instead
    @Size(max = 1000, message = "Core stack must not exceed 1000 characters")
    private String coreStack;

    @PositiveOrZero(message = "Experience required must be positive or zero")
    private Integer experienceRequired;

    @Size(max = 2000, message = "Notes must not exceed 2000 characters")
    private String notes;
}
