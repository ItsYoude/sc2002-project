package models;

import java.util.ArrayList;
import java.util.List;

import controller.InternshipController;

public class Student extends User {
    private String major;
    //private int year;
    private int yearOfStudy;    //1-4
    private List<String> appliedInternshipId;   //list of internship ID
    private String acceptedInternshipId;

    public Student(String userId, String name, int yearOfStudy, String major, String email) {
        super(userId, name, email);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.appliedInternshipId = new ArrayList<>();
        this.acceptedInternshipId = null;
    }
    

    //Getters
    public String getMajor() { return major; }
    public int getYearOfStudy(){ return yearOfStudy; }
    public List<String> getAppliedInternshipId() { return appliedInternshipId; }
    public String getAcceptedInternshipId() { return acceptedInternshipId; }


    //Setters
    public void setMajor(String major) { this.major = major; }
    public void setYear(int yearOfStudy) { this.yearOfStudy = yearOfStudy;}

    //application logic
    public boolean canApplyMore(){ return appliedInternshipId.size() < 3; }
    public boolean isLevelAllowed(String level) {
        return (yearOfStudy <= 2 && level.equalsIgnoreCase("Basic")) || (yearOfStudy >= 3);
    }

    public boolean applyInternship(String internshipId, InternshipController internshipController) {
        if (!canApplyMore()) {
            System.out.println("Cannot apply for more than 3 internships.");
            return false;
        }

        Internship internship = internshipController.getInternshipById(internshipId);
        if (internship == null) {
            System.out.println("Internship not found.");
            return false;
        }

        if (!isLevelAllowed(internship.getYearType())) {
            System.out.println("Year " + yearOfStudy + " students cannot apply for " + internship.getYearType() + " internships.");
            return false;
        }

        if (appliedInternshipId.contains(internshipId)) {
            System.out.println("Already applied to this internship.");
            return false;
        }

        appliedInternshipId.add(internshipId);
        System.out.println("Applied successfully to " + internship.getTitle());
        return true;
    }

    public void viewAppliedInternships(InternshipController internshipController) {
        if (appliedInternshipId.isEmpty()) {
            System.out.println("No applied internships.");
            return;
        }
        System.out.println("\n--- Applied Internships ---");
        for (String id : appliedInternshipId) {
            Internship i = internshipController.getInternshipById(id);
            if (i != null) {
                System.out.printf("ID: %s | Title: %s | Level: %s | Status: %s | Company: %s%n",
                        i.getId(), i.getTitle(), i.getYearType(), i.getStatus(), i.getCompany());
            }
        }
    }

    public void acceptInternship(String internshipId, InternshipController internshipController) {
        if (!appliedInternshipId.contains(internshipId)) {
            System.out.println("You have not applied for this internship.");
            return;
        }

        Internship i = internshipController.getInternshipById(internshipId);
        if (i == null || !"Successful".equalsIgnoreCase(i.getStatus())) {
            System.out.println("Cannot accept. Internship status must be 'Successful'.");
            return;
        }

        acceptedInternshipId = internshipId;
        appliedInternshipId.removeIf(id -> !id.equals(internshipId));
        System.out.println("Accepted internship: " + i.getTitle() + ". All other applications withdrawn.");
    }

    public void withdrawApplication(String internshipId) {
        appliedInternshipId.remove(internshipId);
        if (internshipId.equals(acceptedInternshipId)) acceptedInternshipId = null;
        System.out.println("Withdrawn application for internship: " + internshipId);
    }

    @Override
    public String getUserType() {
        return "Student";
    }
}
