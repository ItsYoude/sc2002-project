package controller;

import java.util.*;
import models.*;

public class ApplicationController {

    private final List<Application> applications = new ArrayList<>();

    // Check if student can apply (max 3 active applications)
    private boolean canApply(Student student) {
        long activeCount = applications.stream()
            .filter(app -> app.getStudent().getUserId().equalsIgnoreCase(student.getUserId()))
            .filter(app -> !app.getStatus().equalsIgnoreCase("Withdrawn") &&
                           !app.getStatus().equalsIgnoreCase("Unsuccessful"))
            .count();
        return activeCount < 3;
    }
    // Check if student is eligible based on year and internship level
    private boolean isEligible(Student student, Internship internship) {
        int year = student.getYearOfStudy();
        String level = internship.getYearType(); // Basic, Intermediate, Advanced

        if (year <= 2 && !level.equalsIgnoreCase("Basic")) {
            return false;
        }
        return true;
    }

    // Add new application
    public void apply(Student student, Internship internship) {
                if (!canApply(student)) {
            System.out.println("You have reached the maximum of 3 active applications.");
            return;
        }

        if (!isEligible(student, internship)) {
            System.out.println("You are not eligible for this internship based on your year and level.");
            return;
        }

        // Check if already applied to same internship
        for (Application app : applications) {
            if (app.getStudent().getUserId().equalsIgnoreCase(student.getUserId())
                && app.getInternship().getId().equalsIgnoreCase(internship.getId())) {
                System.out.println("You have already applied for this internship.");
                return;
            }
        }
        Application app = new Application(student, internship);
        applications.add(app);
        System.out.println("Application submitted successfully!");
    }
    /*
    // Accept a successful internship placement
    public void acceptPlacement(Student student, String internshipId) {
        // Check if already accepted another placement
        for (Application app : applications) {
            if (app.getStudent().getUserId().equalsIgnoreCase(student.getUserId()) &&
                app.getStatus().equalsIgnoreCase("Accepted")) {
                System.out.println("You have already accepted another internship placement.");
                return;
            }
        }

        // Find the target application
        Application toAccept = null;
        for (Application app : applications) {
            if (app.getStudent().getUserId().equalsIgnoreCase(student.getUserId()) &&
                app.getInternship().getId().equalsIgnoreCase(internshipId)) {
                toAccept = app;
                break;
            }
        }

        if (toAccept == null) {
            System.out.println("No matching application found.");
            return;
        }

        if (!toAccept.getStatus().equalsIgnoreCase("Successful")) {
            System.out.println("Only successful applications can be accepted.");
            return;
        }

        // Accept this placement
        toAccept.setStatus("Accepted");
        System.out.println("You have accepted the internship placement: " + toAccept.getInternship().getTitle());

        // Withdraw all other applications
        for (Application app : applications) {
            if (app.getStudent().getUserId().equalsIgnoreCase(student.getUserId()) &&
                !app.getInternship().getId().equalsIgnoreCase(internshipId) &&
                !app.getStatus().equalsIgnoreCase("Withdrawn")) {
                app.setStatus("Withdrawn");
            }
        }
    } */

    // Withdraw application
    public void withdraw(Student student, String internshipId) {
        Iterator<Application> it = applications.iterator();
        while (it.hasNext()) {
            Application app = it.next();
            if (app.getStudent().getUserId().equalsIgnoreCase(student.getUserId())
                && app.getInternship().getId().equalsIgnoreCase(internshipId)) {
                it.remove();
                System.out.println("Application withdrawn successfully!");
                return;
            }
        }
        System.out.println("No matching application found to withdraw.");
    }

    // View all applications for a student
    public void viewApplications(Student student) {
        System.out.println("\n--- My Applications ---");
        boolean found = false;
        for (Application app : applications) {
            if (app.getStudent().getUserId().equalsIgnoreCase(student.getUserId())) {
                System.out.println(app);
                found = true;
            }
        }
        if (!found) {
            System.out.println("You have not applied for any internships.");
        }
    }

    // View applications for an internship (for company rep)
    public void viewApplicationsForInternship(Internship internship) {
        System.out.println("\n--- Applications for " + internship.getTitle() + " ---");
        boolean found = false;
        for (Application app : applications) {
            if (app.getInternship().getId().equalsIgnoreCase(internship.getId())) {
                System.out.println(app);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No applications found for this internship.");
        }
    }

    // Update application status (approve/reject)
    public void updateApplicationStatus(Internship internship, String studentName, String newStatus) {
        for (Application app : applications) {
            if (app.getInternship().getId().equalsIgnoreCase(internship.getId())
                && app.getStudent().getName().equalsIgnoreCase(studentName)) {
                app.setStatus(newStatus);
                System.out.println("Application status updated for " + studentName + ": " + app.getStatus());
                return;
            }
        }
        System.out.println("No matching application found for that student and internship.");
    }

    // Get applications by student (optional for data handling)
    public List<Application> getApplicationsByStudent(Student student) {
        List<Application> result = new ArrayList<>();
        for (Application app : applications) {
            if (app.getStudent().getUserId().equalsIgnoreCase(student.getUserId())) {
                result.add(app);
            }
        }
        return result;
    }

    // NEW: For Career Center Staff: view all applications
    public void viewAllApplications() {
        System.out.println("\n--- All Applications ---");
        if (applications.isEmpty()) {
            System.out.println("No applications available.");
            return;
        }
        for (Application app : applications) {
            System.out.println(app);
        }
    }

    public Application getApplication(Student student, Internship internship) {
        // Assuming you have a list of all applications
        return applications.stream()
                .filter(app -> app.getStudent().getUserId().equals(student.getUserId()) &&
                            app.getInternship().getId().equals(internship.getId()))
                .findFirst()
                .orElse(null); // or throw a custom exception if not found
    }
}
