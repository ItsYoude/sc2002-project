package models;

public class Application {
    private final Student student;
    private final Internship internship;
    private String status;

    public Application(Student student, Internship internship) {
        this.student = student;
        this.internship = internship;
        this.status = "Pending";
    }

    public Student getStudent() {
        return student;
    }

    public Internship getInternship() {
        return internship;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void approveApplication(Application app) {
        Internship internship = app.getInternship();

        if (internship.getSlots() <= 0) {
            System.out.println("No slots available for this internship.");
            return;
        }

        app.setStatus("Approved");
        internship.setSlots(internship.getSlots() - 1);

        System.out.println("Application approved. Remaining slots: " + internship.getSlots());
    }

    @Override
    public String toString() {
        return "Student: " + student.getName() + ", Internship: " + internship.getTitle() + ", Status: " + status;
    }
}
