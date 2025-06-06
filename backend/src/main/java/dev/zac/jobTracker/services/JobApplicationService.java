package dev.zac.jobTracker.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.zac.jobTracker.dto.jobApplication.CreateJobApplicationDto;
import dev.zac.jobTracker.dto.jobApplication.JobApplicationDto;
import dev.zac.jobTracker.dto.jobApplication.UpdateJobApplicationDto;
import dev.zac.jobTracker.entities.JobApplication;
import dev.zac.jobTracker.entities.User;
import dev.zac.jobTracker.enums.JobApplicationStatus;
import dev.zac.jobTracker.exceptions.ResourceNotFoundException;
import dev.zac.jobTracker.repositories.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for handling job application operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final UserService userService;

    /**
     * Create a new job application.
     *
     * @param createRequest the creation request
     * @return created job application DTO
     */
    @Transactional
    public JobApplicationDto createJobApplication(CreateJobApplicationDto createRequest) {
        User currentUser = userService.getCurrentUser();
        
        log.info("Creating job application for user ID: {} at company: {}", 
                currentUser.getId(), createRequest.getCompany());
                
                JobApplication jobApplication = JobApplication.builder()
                .company(createRequest.getCompany())
                .jobTitle(createRequest.getJobTitle())
                .dateApplied(createRequest.getDateApplied())
                .status(createRequest.getStatus())
                .lastResponseDate(createRequest.getLastResponseDate())
                .technologyStack(createRequest.getTechnologyStack())
                .requiredExperience(createRequest.getRequiredExperience())
                .notes(createRequest.getNotes())
                .user(currentUser)
                .build();

        JobApplication savedApplication = jobApplicationRepository.save(jobApplication);
        
        log.info("Job application created successfully with ID: {}", savedApplication.getId());
        
        return convertToJobApplicationDto(savedApplication);
    }

    /**
     * Get all job applications for the current user.
     *
     * @param pageable pagination information
     * @return page of job application DTOs
     */
    public Page<JobApplicationDto> getCurrentUserJobApplications(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        
        Page<JobApplication> applications = jobApplicationRepository.findByUser(currentUser, pageable);
        
        return applications.map(this::convertToJobApplicationDto);
    }

    /**
     * Get a specific job application by ID for the current user.
     *
     * @param applicationId the application ID
     * @return job application DTO
     * @throws ResourceNotFoundException if application not found
     */
    public JobApplicationDto getJobApplicationById(Long applicationId) {
        User currentUser = userService.getCurrentUser();
        
        JobApplication application = jobApplicationRepository.findByIdAndUser(applicationId, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found with ID: " + applicationId));
        
        return convertToJobApplicationDto(application);
    }

    /**
     * Update a job application.
     *
     * @param applicationId the application ID
     * @param updateRequest the update request
     * @return updated job application DTO
     * @throws ResourceNotFoundException if application not found
     */
    @Transactional
    public JobApplicationDto updateJobApplication(Long applicationId, UpdateJobApplicationDto updateRequest) {
        User currentUser = userService.getCurrentUser();
        
        log.info("Updating job application ID: {} for user ID: {}", applicationId, currentUser.getId());
        
        JobApplication application = jobApplicationRepository.findByIdAndUser(applicationId, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found with ID: " + applicationId));
                
        // Update fields
        application.setCompany(updateRequest.getCompany());
        application.setJobTitle(updateRequest.getJobTitle());
        application.setDateApplied(updateRequest.getDateApplied());
        application.setStatus(updateRequest.getStatus());
        application.setLastResponseDate(updateRequest.getLastResponseDate());
        application.setTechnologyStack(updateRequest.getTechnologyStack());
        application.setRequiredExperience(updateRequest.getRequiredExperience());
        application.setNotes(updateRequest.getNotes());

        JobApplication updatedApplication = jobApplicationRepository.save(application);
        
        log.info("Job application updated successfully with ID: {}", updatedApplication.getId());
        
        return convertToJobApplicationDto(updatedApplication);
    }

    /**
     * Delete a job application.
     *
     * @param applicationId the application ID
     * @throws ResourceNotFoundException if application not found
     */
    @Transactional
    public void deleteJobApplication(Long applicationId) {
        User currentUser = userService.getCurrentUser();
        
        log.info("Deleting job application ID: {} for user ID: {}", applicationId, currentUser.getId());
        
        JobApplication application = jobApplicationRepository.findByIdAndUser(applicationId, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found with ID: " + applicationId));

        jobApplicationRepository.delete(application);
        
        log.info("Job application deleted successfully with ID: {}", applicationId);
    }

    /**
     * Get job applications by status for the current user.
     *
     * @param status the application status
     * @param pageable pagination information
     * @return page of job application DTOs
     */
    public Page<JobApplicationDto> getJobApplicationsByStatus(JobApplicationStatus status, Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        
        Page<JobApplication> applications = jobApplicationRepository.findByUserAndStatus(currentUser, status, pageable);
        
        return applications.map(this::convertToJobApplicationDto);
    }

    /**
     * Search job applications by company name for the current user.
     *
     * @param company the company name search term
     * @param pageable pagination information
     * @return page of job application DTOs
     */
    public Page<JobApplicationDto> searchJobApplicationsByCompany(String company, Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        
        Page<JobApplication> applications = jobApplicationRepository
                .findByUserAndCompanyContainingIgnoreCase(currentUser, company, pageable);
        
        return applications.map(this::convertToJobApplicationDto);
    }

    /**
     * Get job applications within a date range for the current user.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination information
     * @return page of job application DTOs
     */
    public Page<JobApplicationDto> getJobApplicationsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        
        Page<JobApplication> applications = jobApplicationRepository
                .findByUserAndDateAppliedBetween(currentUser, startDate, endDate, pageable);
        
        return applications.map(this::convertToJobApplicationDto);
    }

    /**
     * Get application statistics for the current user.
     *
     * @return map of status to count
     */
    public Map<JobApplicationStatus, Long> getApplicationStatistics() {
        User currentUser = userService.getCurrentUser();
        
        List<Object[]> stats = jobApplicationRepository.getApplicationStatsByUser(currentUser.getId());
        
        Map<JobApplicationStatus, Long> statusCounts = new HashMap<>();
        
        for (Object[] stat : stats) {
            JobApplicationStatus status = (JobApplicationStatus) stat[0];
            Long count = (Long) stat[1];
            statusCounts.put(status, count);
        }
        
        return statusCounts;
    }

    /**
     * Convert JobApplication entity to JobApplicationDto.
     *
     * @param application the job application entity
     * @return job application DTO
     */    
    private JobApplicationDto convertToJobApplicationDto(JobApplication application) {
        return JobApplicationDto.builder()
                .id(application.getId())
                .company(application.getCompany())
                .jobTitle(application.getJobTitle())
                .dateApplied(application.getDateApplied())
                .status(application.getStatus())
                .lastResponseDate(application.getLastResponseDate())
                .technologyStack(application.getTechnologyStack())
                .requiredExperience(application.getRequiredExperience())
                .notes(application.getNotes())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}
