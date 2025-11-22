package models;


/**
 * Represents a record of a student's application to an internship.
 * Tracks the internship ID and the current status of the application.
 */

public class AppliedRecord {
    /** ID of the internship applied to. */
    private String internshipId;
    /** Status of the application (e.g., Pending, Accepted, Withdrawn). */
    private String status;

    /** Constructor to initialize internship ID and status. */
    public AppliedRecord(String internshipId, String status) {
        this.internshipId = internshipId;
        this.status = status;
    }

    /** Get the internship ID. */
    public String getInternshipId() {
        return internshipId;
    }

    /** Get the application status. */
    public String getStatus() {
        return status;
    }

    /** Set a new internship ID. */
    public void setInternshipId(String id) {
        this.internshipId = id;
    }
    
    /** Set a new application status. */
    public void setStatus(String status) {
        this.status = status;
    }

}