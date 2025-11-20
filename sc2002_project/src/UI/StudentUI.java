package UI;

import controller.*;
import java.util.List;
import java.util.Scanner;
import models.*;
import utility.IInternshipSorter;
import utility.TitleSorter;

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
                case "5":
                    boolean changed = student.changePassword();
                    if (changed) {
                        continueMenu = false; // log out only if password was changed
                    }
                    break;
                case "6":
                    acceptInternship();
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
        System.out.println("Fetching and sorting internships for your profile...");
        // --- DIP IN ACTION: UI chooses the concrete sorting implementation ---
        // For the default view, we use the TitleSorter (alphabetical)
        IInternshipSorter sorter = new TitleSorter(); 
        // 1. Controller retrieves the filtered and sorted list
        List<Internship> opportunities = internshipController.getEligibleInternships(
            this.student, 
            sorter // Passes the abstraction
        );
        // 2. UI handles presentation (Printing to console)
        System.out.println("\n--- Internship Opportunities for " + student.getMajor() + " ---");
        System.out.println("(Filtered by eligibility, visible status, and sorted by Title)");
        
        if (opportunities.isEmpty()) {
            System.out.println("No internships are currently available based on your profile and visibility settings.");
        } else {
            // Print the clean, filtered, and sorted list retrieved from the controller
            for (Internship opp : opportunities) {
                System.out.println(opp);
            }
        }
    }

    private void viewMyApplications() {
        applicationController.viewApplications(student);
    }

    private void applyInternship() {

        if (student.getAcceptedInternshipId() != null)
        {
            System.out.println("You have already accepted an internship and not allowed to apply for more.");
            return;
        }
        //show the students what they can apply first
        
        
        // 1. Controller retrieves the avalialbe internships for the student
        List<Internship> opportunities = internshipController.getEligibleInternships(this.student);



        if (!opportunities.isEmpty())
        {
            //2. display to the user first
            System.out.println("\n--- Internship Opportunities  " + student.getMajor() + " ---");
            for (Internship opp : opportunities) {
                System.out.println(opp);
            }

            System.out.print("Enter Internship ID to apply: ");
            String id = sc.nextLine().trim();

            boolean exists = opportunities.stream()
                    .anyMatch(i -> i.getId().equalsIgnoreCase(id));

            if (!exists) {
                System.out.println("Internship ID entered not inside list of opportunities!");
                return;
            }

            Internship internship = internshipController.getInternshipById(id);
            // if (internship != null &&
            //         internship.isVisible() &&
            //         internship.getStatus().equalsIgnoreCase("Approved")) {
            //     applicationController.apply(student, internship);
            // } else {
            //     System.out.println("Invalid ID or internship not visible.");
            // }
            applicationController.apply(student, internship);
        } 
        else
        {
            System.out.println("No avaliable internships for you to apply for.");
        }
       
        
    }

    private void withdrawApplication() {

        //draw from applied/accepted

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
        if (id.isEmpty())
        {
            System.out.println("Do not enter blankspace as ID.");
            return;
        }

        boolean success = studentController.acceptOffer(student, id);
        if (!success) {
            System.out.println("Could not accept the internship. Either it is not a successful application or you already have an accepted internship.");
        }
    }

}
