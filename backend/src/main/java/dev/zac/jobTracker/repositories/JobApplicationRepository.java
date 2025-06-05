package dev.zac.jobTracker.repositories;

import dev.zac.jobTracker.entities.JobApplication;
import dev.zac.jobTracker.entities.User;
import dev.zac.jobTracker.enums.JobApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for JobApplication entity operations.
 */
@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    /**
     * Find all job applications for a specific user.
     *
     * @param user the user
     * @param pageable pagination information
     * @return Page of job applications
     */
    Page<JobApplication> findByUser(User user, Pageable pageable);

    /**
     * Find all job applications for a specific user.
     *
     * @param user the user
     * @return List of job applications
     */
    List<JobApplication> findByUser(User user);

    /**
     * Find a job application by ID and user.
     *
     * @param id the job application ID
     * @param user the user
     * @return Optional containing the job application if found
     */
    Optional<JobApplication> findByIdAndUser(Long id, User user);

    /**
     * Find job applications by user and status.
     *
     * @param user the user
     * @param status the application status
     * @param pageable pagination information
     * @return Page of job applications
     */
    Page<JobApplication> findByUserAndStatus(User user, JobApplicationStatus status, Pageable pageable);

    /**
     * Find job applications by user and company containing a search term.
     *
     * @param user the user
     * @param company the company name search term
     * @param pageable pagination information
     * @return Page of job applications
     */
    Page<JobApplication> findByUserAndCompanyContainingIgnoreCase(User user, String company, Pageable pageable);

    /**
     * Find job applications by user within a date range.
     *
     * @param user the user
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination information
     * @return Page of job applications
     */
    Page<JobApplication> findByUserAndDateAppliedBetween(User user, LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Count job applications by user and status.
     *
     * @param user the user
     * @param status the application status
     * @return count of applications
     */
    long countByUserAndStatus(User user, JobApplicationStatus status);

    /**
     * Get job application statistics for a user.
     *
     * @param userId the user ID
     * @return List of status counts
     */
    @Query("SELECT ja.status, COUNT(ja) FROM JobApplication ja WHERE ja.user.id = :userId GROUP BY ja.status")
    List<Object[]> getApplicationStatsByUser(@Param("userId") Long userId);
}
