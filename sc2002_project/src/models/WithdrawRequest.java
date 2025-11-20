package models;

import java.util.UUID;

public class WithdrawRequest {
    private final String id;
    private final Student student;
    private final Internship internship;
    private final String reason;
    private String status; // Pending / Approved / Rejected

    public WithdrawRequest(Student student, Internship internship, String reason) {
        this.id = UUID.randomUUID().toString(); // unique ID for each request
        this.student = student;
        this.internship = internship;
        this.reason = reason;
        this.status = "Pending";




    //   System.out.println("Reference of internship inside Withdraw Request: " 
    //         + System.identityHashCode(internship));


    }

    //getters
    public String getId() { return id; }
    public Student getStudent() { return student; }
    public Internship getInternship() { return internship; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    //setters
    public void setStatus(String status) { this.status = status; }
    public void approve() { this.status = "Approved"; }
    public void reject() { this.status = "Rejected"; }

    @Override
    public String toString() {                  
            return ("Request ID: " + id +
                            " | Student: " + student.getName()+
                            " | Internship: " + internship.getTitle() +
                            " | Reason: " + reason + " | Status: "+status);
   }
    

}
