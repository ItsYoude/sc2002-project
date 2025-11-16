package UI;

import controller.*;
import java.util.Scanner;
import models.*;

public class StudentUI {
    private final Student student;
    
    private final InternshipController internshipController;
    private final ApplicationController applicationController;
    private final StudentController studentController;
    private final Scanner sc;

    public StudentUI(Student student, InternshipController internshipController, ApplicationController applicationController, StudentController studentController) {
        this.student = student;
        this.internshipController = internshipController;
        this.applicationController = applicationController;
        this.studentController = studentController;
        this.sc = new Scanner(System.in);
    }

    public void showMenu() {
        boolean continueMenu = true;

        while (continueMenu) {
            System.out.println("\n--- Student Dashboard ---");
            System.out.println("1. View Internship Opportunities");
            System.out.println("2. View My Applications");
            System.out.println("3. Apply for Internship");
            System.out.println("4. Withdraw Application");
            System.out.println("5. Change Password");
            System.out.println("6. Accept Application");
            System.out.println("0. Logout");
            System.out.print("Select option: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    viewInternships();
                    break;
                case "2":
                    viewMyApplications();
                    break;
                case "3":
                    applyInternship();
                    break;
                case "4":
                    withdrawApplication();
                    break;
                case "6":
                    acceptInternship();
                    break;
                case "5":
                    student.changePassword();
                    continueMenu = false;
                    break;
                case "0":
                    System.out.println("Logging out and returning to login screen...");
                    continueMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again!");
            }
        }
    }

    private void viewInternships() {
        System.out.println("Fetching internships for your profile...");
        internshipController.viewAllInternships(student.getMajor(), student.getYearOfStudy());
    }

    private void viewMyApplications() {
        applicationController.viewApplications(student);
    }

    private void applyInternship() {
        System.out.print("Enter Internship ID to apply: ");
        String id = sc.nextLine().trim();
        Internship internship = internshipController.getInternshipById(id);
        if (internship != null && internship.isVisible()) {
            applicationController.apply(student, internship);
        } else {
            System.out.println("Invalid ID or internship not visible.");
        }
    }

    private void withdrawApplication() {
        System.out.print("Enter Internship ID to withdraw: ");
        String id = sc.nextLine().trim();
        Internship internship = internshipController.getInternshipById(id);

        if (internship == null) {
            System.out.println("Invalid internship ID.");
            return;
        }

        System.out.print("Enter reason for withdrawal: ");
        String reason = sc.nextLine().trim();

        studentController.requestWithdrawal(student, internship, reason);
    }

    private void acceptInternship() {
        System.out.print("Enter Internship ID to accept: ");
        String id = sc.nextLine().trim();

        boolean success = studentController.acceptOffer(student, id);
        if (!success) {
            System.out.println("Could not accept the internship. Either it is not a successful application or you have already accepted another internship.");
        }
    }


    // private void changePassword() {
    //     System.out.print("Enter your current password: ");
    //     String current = sc.nextLine().trim();

    //     if (!student.passwordValidator(current)) {
    //         System.out.println("Incorrect password. Please try again.");
    //         return;
    //     }

    //     System.out.print("Enter your new password: ");
    //     String newPassword = sc.nextLine().trim();
    //     student.changePassword(newPassword);

    //     System.out.println("Password successfully changed!");
    //     System.out.println("You will now be logged out. Please login again with your new password.");
    // }
}
