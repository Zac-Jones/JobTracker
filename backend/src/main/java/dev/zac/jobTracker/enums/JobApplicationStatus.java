package dev.zac.jobTracker.enums;

/**
 * Enum representing the various statuses of a job application.
 */
public enum JobApplicationStatus {
    APPLIED("Applied"),
    PHONE_INTERVIEW("Phone Interview"),
    TECHNICAL_INTERVIEW("Technical Interview"),
    BEHAVIOURAL_INTERVIEW("Behavioural Interview"),
    ON_SITE_INTERVIEW("On-site Interview"),
    FINAL_INTERVIEW("Final Interview"),
    OFFER_RECEIVED("Offer Received"),
    OFFER_ACCEPTED("Offer Accepted"),
    OFFER_REJECTED("Offer Rejected"),
    REJECTED("Rejected"),
    WITHDRAWN("Withdrawn"),
    NO_RESPONSE("No Response");

    private final String displayName;

    JobApplicationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
