package UI;

import controller.ApplicationController;
import controller.CompanyRepController;
import controller.InternshipController;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import models.CompanyRepresentative;
import models.Internship;
import models.Student;

public class CompanyRepUI {
    private final CompanyRepresentative representative;
    private final InternshipController internshipController;
    private final ApplicationController applicationController;
    private final CompanyRepController companyRepController;
    private final Scanner sc;

    public CompanyRepUI(CompanyRepresentative representative, InternshipController internshipController, ApplicationController applicationController,CompanyRepController companyRepController) {
        this.representative = representative;
        this.internshipController = internshipController;
        this.applicationController = applicationController;
        this.companyRepController = companyRepController;
        this.sc = new Scanner(System.in);
    }

 public void showMenu() {
    boolean continueMenu = true;

    while (continueMenu) {
        System.out.println("\n--- Company Representative Dashboard ---");
        System.out.println("1. Create Internship Opportunity");
        System.out.println("2. View My Internships");
        System.out.println("3. Approve/Reject Applications");
        System.out.println("4. Toggle Internship Visibility");
        System.out.println("5. Change Password");
        System.out.println("0. Logout");
        System.out.print("Select option: ");

        String choice = sc.nextLine().trim();
        switch (choice) {
            case "1":
                createInternship();
                break;
            case "2":
                viewPostedInternships();
                break;
            case "3":
                manageApplications();
                break;
            case "4":
                toggleVisibility();
                break;
            case "5":
                boolean changedRep = representative.changePassword();
                if (changedRep)
                    continueMenu = false;
                break;
            case "0":
                System.out.println("Logging out...");
                continueMenu = false;
                break;
            default:
                System.out.println("Invalid option. Please try again!");
        }
    }
}

        private void createInternship() {
            companyRepController.findIfMaxInternshipCreated(representative, internshipController);
        
        if (!(representative.getMaxCreated()))
        {
            String id, title, major, yearType, description;
            int slots;
            LocalDate openDate, closeDate;

            System.out.println("\n--- Create Internship Posting ---");

            do {
                System.out.print("Enter internship ID: ");
                id = sc.nextLine().trim();
            } while (id.isEmpty() || !companyRepController.findAvaliableID(id)); //implement check the csv for exisitng ids. 

            do {
                System.out.print("Enter internship title: ");
                title = sc.nextLine().trim();
            } while (title.isEmpty());

            do {
                System.out.print("Enter prefered major: ");
                major = sc.nextLine().trim();
            } while (title.isEmpty());

            while (true) {
                System.out.print("Enter target year type (Basic/Intermediate/Advanced): ");
                yearType = sc.nextLine().trim();

                if (yearType.equalsIgnoreCase("Basic")
                        || yearType.equalsIgnoreCase("Intermediate")
                                | yearType.equalsIgnoreCase("Advanced")) {
                    break;
                } else {
                    System.out
                            .println("Invalid input. Enter either Basic for Y1/Y2 or Intermediate/Advanced for Y1-Y4.");
                }
            }

            while (true) {
                System.out.print("Enter number of slots (1 to 10): ");
                String input = sc.nextLine().trim();

                try {
                    slots = Integer.parseInt(input);
                    if (slots < 1 || slots > 10) {
                        System.out.println("Slots must be between 1-10.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter only integers.");
                }
            }

            do {
                System.out.print("Enter internship description: ");
                description = sc.nextLine().trim();
            } while (description.isEmpty());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while (true) {
                System.out.print("Enter opening date (yyyy-MM-dd): ");
                String input = sc.nextLine().trim();

                try {
                    openDate = LocalDate.parse(input, formatter);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date! Please use yyyy-MM-dd format.");
                }
            }

            // Closing Date (must be after opening date)
            while (true) {
                System.out.print("Enter closing date (yyyy-MM-dd): ");
                String input = sc.nextLine().trim();

                try {
                    closeDate = LocalDate.parse(input, formatter);

                    if (closeDate.isBefore(openDate)) {
                        System.out.println("Closing date cannot be earlier than opening date!");
                    } else {
                        break;
                    }

                } catch (Exception e) {
                    System.out.println("Invalid date! Please use yyyy-MM-dd format.");
                }
            }

            Internship newInternship = new Internship(id, title, representative.getCompanyName(),
                    representative.getUserId(), major, yearType, description, openDate, closeDate, slots, true,
                    "Pending");
            internshipController.addInternship(newInternship);
            System.out.println("Internship created successfully and is visible by default.");
        }
        else
        {
            System.out.println("Max internships created for "+representative.getName());
        }
    }

    private void viewPostedInternships() {
        System.out.println("\nYour Posted Internships:");
        List<Internship> list = internshipController.getAllInternships();
        for (Internship i : list) {
            if (i.getCompany().equalsIgnoreCase(representative.getCompanyName())) {
                System.out.println(i);
            }
        }
    }

    private void manageApplications() {
        System.out.print("Enter internship ID to manage applications: ");
        String id = sc.nextLine().trim();
        if (id.isEmpty())
        {
            System.out.println("Invalid ID entered.");
            return;
        }
        Internship internship = internshipController.getInternshipById(id);
        if (internship == null) {
            System.out.println("Internship ID does not exist.");
            return;
        }
        if (!(internship.getRepresentativeId().equals(representative.getUserId())))
        {
            System.out.println("This Internship is not assigned to you.");
            return;
        }

        if (applicationController.viewApplicationsForInternship(internship))
        {
            System.out.print("Enter student name to approve/reject (or press Enter to cancel): ");
            String studentName = sc.nextLine().trim();
            if (studentName.isEmpty())
                return;
            // VALIDATION LOOP: Keep asking until A or R is entered


            //if student is status is not pending, should not be allowed to edit their status. 
            if (applicationController.isPending(studentName, internship))
            {

                String decision;
                do {
                    System.out.print("Enter 'A' to approve or 'R' to reject: ");
                    decision = sc.nextLine().trim().toUpperCase();
                } while (!decision.equals("A") && !decision.equals("R") && (decision.trim().isEmpty()));
                // Convert to application status
                if (decision.equals("A")) {
                    decision = "Successful";
                } else {
                    decision = "Unsuccessful";
                }
                System.out.println("Decision given is: " + decision);
                //this part was changed to fufill the success/rejected to display to the student.
                applicationController.updateApplicationStatus(internship, studentName, decision);
            }
            else
            {
                System.out.println("Please only Approve Or Reject pending applicants.");
            }
        }
        else
        {
            System.out.println("No applications found for this internship.");
        }

    }

    private void toggleVisibility() {
        System.out.print("Enter internship ID to toggle visibility: ");
        String id = sc.nextLine().trim();
        Internship internship = internshipController.getInternshipById(id);
        if (internship != null && internship.getCompany().equalsIgnoreCase(representative.getCompanyName())) {
            internship.toggleVisibility();
            internshipController.saveAllInternships();
            System.out.println("Internship visibility updated: " + internship.isVisible());
        } else {
            System.out.println("Invalid internship ID or not your internship.");
        }
    }

    // private void changePassword() {
    //     System.out.print("Enter your current password: ");
    //     String current = sc.nextLine().trim();

    //     if (!representative.passwordValidator(current)) {
    //         System.out.println("Incorrect password. Please try again.");
    //         return;
    //     }

    //     System.out.print("Enter new password: ");
    //     String newPassword = sc.nextLine().trim();
    //     representative.changePassword(newPassword);

    //     System.out.println("Password changed successfully. Please log in again.");
    // }
}
