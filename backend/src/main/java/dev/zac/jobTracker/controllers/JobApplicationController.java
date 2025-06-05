package dev.zac.jobTracker.controllers;

import dev.zac.jobTracker.dto.jobApplication.CreateJobApplicationDto;
import dev.zac.jobTracker.dto.jobApplication.JobApplicationDto;
import dev.zac.jobTracker.dto.jobApplication.UpdateJobApplicationDto;
import dev.zac.jobTracker.enums.JobApplicationStatus;
import dev.zac.jobTracker.services.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * REST controller for job application operations.
 */
@RestController
@RequestMapping("/api/job-applications")
@RequiredArgsConstructor
@Slf4j
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    /**
     * Create a new job application.
     *
     * @param createRequest the creation request
     * @return created job application DTO
     */
    @PostMapping
    public ResponseEntity<JobApplicationDto> createJobApplication(
            @Valid @RequestBody CreateJobApplicationDto createRequest) {
        log.info("Creating job application for company: {}", createRequest.getCompany());
        
        JobApplicationDto createdApplication = jobApplicationService.createJobApplication(createRequest);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdApplication);
    }

    /**
     * Get all job applications for the current user.
     *
     * @param pageable pagination parameters
     * @return page of job application DTOs
     */
    @GetMapping
    public ResponseEntity<Page<JobApplicationDto>> getAllJobApplications(
            @PageableDefault(size = 20, sort = "dateApplied") Pageable pageable) {
        log.info("Getting all job applications for current user");
        
        Page<JobApplicationDto> applications = jobApplicationService.getCurrentUserJobApplications(pageable);
        
        return ResponseEntity.ok(applications);
    }

    /**
     * Get a specific job application by ID.
     *
     * @param applicationId the application ID
     * @return job application DTO
     */
    @GetMapping("/{applicationId}")
    public ResponseEntity<JobApplicationDto> getJobApplicationById(@PathVariable Long applicationId) {
        log.info("Getting job application with ID: {}", applicationId);
        
        JobApplicationDto application = jobApplicationService.getJobApplicationById(applicationId);
        
        return ResponseEntity.ok(application);
    }

    /**
     * Update a job application.
     *
     * @param applicationId the application ID
     * @param updateRequest the update request
     * @return updated job application DTO
     */
    @PutMapping("/{applicationId}")
    public ResponseEntity<JobApplicationDto> updateJobApplication(
            @PathVariable Long applicationId,
            @Valid @RequestBody UpdateJobApplicationDto updateRequest) {
        log.info("Updating job application with ID: {}", applicationId);
        
        JobApplicationDto updatedApplication = jobApplicationService.updateJobApplication(applicationId, updateRequest);
        
        return ResponseEntity.ok(updatedApplication);
    }

    /**
     * Delete a job application.
     *
     * @param applicationId the application ID
     * @return success response
     */
    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Void> deleteJobApplication(@PathVariable Long applicationId) {
        log.info("Deleting job application with ID: {}", applicationId);
        
        jobApplicationService.deleteJobApplication(applicationId);
        
        return ResponseEntity.noContent().build();
    }

    /**
     * Get job applications by status.
     *
     * @param status the application status
     * @param pageable pagination parameters
     * @return page of job application DTOs
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<JobApplicationDto>> getJobApplicationsByStatus(
            @PathVariable JobApplicationStatus status,
            @PageableDefault(size = 20, sort = "dateApplied") Pageable pageable) {
        log.info("Getting job applications with status: {}", status);
        
        Page<JobApplicationDto> applications = jobApplicationService.getJobApplicationsByStatus(status, pageable);
        
        return ResponseEntity.ok(applications);
    }

    /**
     * Search job applications by company name.
     *
     * @param company the company name search term
     * @param pageable pagination parameters
     * @return page of job application DTOs
     */
    @GetMapping("/search")
    public ResponseEntity<Page<JobApplicationDto>> searchJobApplicationsByCompany(
            @RequestParam String company,
            @PageableDefault(size = 20, sort = "dateApplied") Pageable pageable) {
        log.info("Searching job applications for company: {}", company);
        
        Page<JobApplicationDto> applications = jobApplicationService.searchJobApplicationsByCompany(company, pageable);
        
        return ResponseEntity.ok(applications);
    }

    /**
     * Get job applications within a date range.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination parameters
     * @return page of job application DTOs
     */
    @GetMapping("/date-range")
    public ResponseEntity<Page<JobApplicationDto>> getJobApplicationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 20, sort = "dateApplied") Pageable pageable) {
        log.info("Getting job applications between {} and {}", startDate, endDate);
        
        Page<JobApplicationDto> applications = jobApplicationService.getJobApplicationsByDateRange(startDate, endDate, pageable);
        
        return ResponseEntity.ok(applications);
    }

    /**
     * Get application statistics for the current user.
     *
     * @return map of status to count
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<JobApplicationStatus, Long>> getApplicationStatistics() {
        log.info("Getting application statistics for current user");
        
        Map<JobApplicationStatus, Long> statistics = jobApplicationService.getApplicationStatistics();
        
        return ResponseEntity.ok(statistics);
    }
}
