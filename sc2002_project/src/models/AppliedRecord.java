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

    public void setInternshipId(String id) {
        this.internshipId = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}