package dev.zac.jobTracker.dto.jobApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import dev.zac.jobTracker.enums.JobApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for job application responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDto {
    
    private Long id;
    private String company;
    private String jobTitle;
    private LocalDate dateApplied;    
    private JobApplicationStatus status;
    private LocalDate lastResponseDate;
    private List<String> technologyStack;
    private Integer requiredExperience;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
