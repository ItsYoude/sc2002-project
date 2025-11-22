package models;

/**
 * Represents a student's application for an internship.
 * Tracks the student, internship, and application status.
 * Provides methods to approve an application and update internship slots.
 */

public class Application {

    /** The student who applied. */
    private final Student student;

    /** The internship applied to. */
    private final Internship internship;

    /** Status of the application (e.g., Pending, Successful, Withdrawn, Accepted). */
    private String status;


    /** Constructor initializes student, internship, and default status as "Pending". */
    public Application(Student student, Internship internship) {
        this.student = student;
        this.internship = internship;
        this.status = "Pending";
    }

    /** Get the student. */
    public Student getStudent() {
        return student;
    }

    /** Get the internship. */
    public Internship getInternship() {
        return internship;
    }

    /** Get the current status of the application. */
    public String getStatus() {
        return status;
    }

    /** Set the status of the application. */
    public void setStatus(String status) {
        this.status = status;
    }
    
     /**
     * Approve the application if slots are available.
     * Updates internship slots and sets application status to "Successful".
     */
     public void approveApplication(Application app) {
         Internship internship = app.getInternship();

         if (internship.getSlots() <= 0) {
             System.out.println("No slots available for this internship.");
             return;
         }

         app.setStatus("Successful");
         internship.setSlots(internship.getSlots() - 1);

         System.out.println("Application approved. Remaining slots: " + internship.getSlots());
     }

    /** Returns a human-readable string representation of the application. */
    @Override
    public String toString() {
        return "Internship ID: " + internship.getId() + " Student: " + student.getName() + ", Internship: " + internship.getTitle() + " at " + internship.getCompany() + ", Status: " + this.getStatus();
    }
}
