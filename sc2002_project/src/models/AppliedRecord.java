package models;

public class AppliedRecord {
    private String internshipId;
    private String status;

    public AppliedRecord(String internshipId, String status) {
        this.internshipId = internshipId;
        this.status = status;
    }

    public String getInternshipId() {
        return internshipId;
    }

    public String getStatus() {
        return status;
    }
}