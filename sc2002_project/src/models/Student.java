package models;

import controller.InternshipController;
import controller.StudentController;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import utility.FileService;

public class Student extends User {
    private String major;
    //private int year;
    private int yearOfStudy; //1-4
    private List<AppliedRecord> appliedInternshipId; //list of internship ID
    private String acceptedInternshipId;

    public Student(String userId, String name, int yearOfStudy, String major, String email, String password) {
        super(userId, name, email, password);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.appliedInternshipId = new ArrayList<>();
        this.acceptedInternshipId = null;
    }

    //Getters
    public String getMajor() {
        return major;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public List<AppliedRecord> getAppliedInternshipId() {
        return appliedInternshipId;
    }

    public void updateStatusForRecord(String id,String status)
    {
        for (AppliedRecord record : appliedInternshipId) {
            if (record.getInternshipId().equalsIgnoreCase(id)) {
                record.setStatus(status);
                System.out.println("Hi");
                return; // done
            }
        }
    }

    public void readAppliedRecord()
    {
        for (AppliedRecord record : appliedInternshipId)
        {
            System.out.println(record.getInternshipId()+":"+record.getStatus());
        }
    }

    public String getAcceptedInternshipId() {
        return acceptedInternshipId;
    }

    //Setters
    public void setMajor(String major) {
        this.major = major;
    }

    public void setYear(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public void addAppliedInternshipId(AppliedRecord appliedRecord) {
        appliedInternshipId.add(appliedRecord);
    }

    public void setAcceptedInternshipId(String id)
    {
        acceptedInternshipId = id;
    }

    //application logic
    public boolean canApplyMore() {
        return appliedInternshipId.size() < 3;
    }

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
            System.out.println(
                    "Year " + yearOfStudy + " students cannot apply for " + internship.getYearType() + " internships.");
            return false;
        }

        if (appliedInternshipId.contains(internshipId)) {
            System.out.println("Already applied to this internship.");
            return false;
        }

        appliedInternshipId.add(new AppliedRecord(internship.getId(), "Pending"));
        System.out.println("Applied successfully to " + internship.getTitle());

        //some save logic 


        return true;
    }

    public void viewAppliedInternships(InternshipController internshipController) {
        if (appliedInternshipId.isEmpty()) {
            System.out.println("No applied internships.");
            return;
        }

        System.out.println("\n--- Applied Internships ---");
        for (AppliedRecord appliedRecord : appliedInternshipId) {
            Internship i = internshipController.getInternshipById(appliedRecord.getInternshipId());
            if (i != null) {
                System.out.printf("ID: %s | Title: %s | Level: %s | Status: %s | Company: %s |  My Acceptance: %s%n",
                        i.getId(), i.getTitle(), i.getYearType(), i.getStatus(), i.getCompany(),
                        appliedRecord.getStatus());
            }
        }
    }

    // public void acceptInternship(String internshipId, InternshipController internshipController) {
    //     // if (!appliedInternshipId.contains(internshipId)) {
    //     //     System.out.println("You have not applied for this internship.");
    //     //     return;
    //     // }

    //     for (AppliedRecord appliedRecord: appliedInternshipId)
    //     {
    //         if (appliedRecord.getInternshipId().equalsIgnoreCase(internshipId)) {
    //             Internship i = internshipController.getInternshipById(internshipId);
    //             if (i == null || !"Successful".equalsIgnoreCase(i.getStatus())) {
    //                 System.out.println("Cannot accept. Internship status must be 'Successful'.");
    //                 return;
    //             }

    //             acceptedInternshipId = internshipId;
    //             appliedInternshipId.removeIf(id -> !id.equals(internshipId));
    //             System.out.println("Accepted internship: " + i.getTitle() + ". All other applications withdrawn.");
    //             return;
    //         }
    //     }
    //     System.out.println("You have not applied for this internship.");

    // }

    public void acceptInternship(String internshipId, InternshipController internshipController) {
        AppliedRecord toAccept = null;

        for (AppliedRecord record : appliedInternshipId) {
            if (record.getInternshipId().equalsIgnoreCase(internshipId)) {
                Internship i = internshipController.getInternshipById(internshipId);
                if (i == null || !"Successful".equalsIgnoreCase(record.getStatus())) {
                    System.out.println("Cannot accept. Internship status must be 'Successful'.");
                    return;
                }
                toAccept = record;
                break;
            }
        }

        if (toAccept == null) {
            System.out.println("You have not applied for this internship.");
            return;
        }
        // Accept the chosen internship
        acceptedInternshipId = internshipId;
        // Remove all other applied internships
        appliedInternshipId.removeIf(record -> !record.getInternshipId().equalsIgnoreCase(internshipId));
        System.out.println("Accepted internship: " + internshipController.getInternshipById(internshipId).getTitle() +
                ". All other applications withdrawn.");


    }

    // public void withdrawApplication(String internshipId) {
    //     appliedInternshipId.remove(internshipId);
    //     if (internshipId.equals(acceptedInternshipId))
    //         acceptedInternshipId = null;
    //     System.out.println("Withdrawn application for internship: " + internshipId);
    // }

    public void withdrawApplication(String internshipId) {
    boolean removed = appliedInternshipId.removeIf(record -> record.getInternshipId().equalsIgnoreCase(internshipId));

    if (!removed) {
        System.out.println("You have not applied for this internship: " + internshipId);
        return;
    }

    if (internshipId.equalsIgnoreCase(acceptedInternshipId)) {
        acceptedInternshipId = null;
    }

    System.out.println("Withdrawn application for internship: " + internshipId);
    }
    
    
    @Override
    public String getUserType() {
        return "Student";
    }

    public boolean changePassword() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your current password: ");
        String current = sc.nextLine().trim();

        if (!passwordValidator(current)) {
            System.out.println("Incorrect password. Please try again.");
            return false; // indicate failure
        }

        System.out.print("Enter your new password: ");
        String newPassword = sc.nextLine().trim();

        if (newPassword.isEmpty()) {
            System.out.println("New password cannot be empty.");
            return false;
        }

        if (newPassword.equals(current)) {
            System.out.println("New password cannot be the same as your old password.");
            return false;
        }

        setPassword(newPassword); // update in memory

        boolean success = FileService.saveStudents(StudentController.getInstance().getAllStudents());
        if (success) {
            System.out.println("Password successfully changed!");
            System.out.println("You will now be logged out. Please login again with your new password.");
            return true; // indicate success
        } else {
            System.out.println("Error saving password. Try again later.");
            return false;
        }
    }

}
