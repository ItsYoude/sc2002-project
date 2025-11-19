package UI;
import controller.*;
import java.util.Scanner;
import models.*;

/*LoginUI ---- CLI for User Login + route them to respective dashboard upon successful login */
public class LoginUI {
    private final SystemController systemController;
    private final UserController userController;
    private final InternshipController internshipController;
    private final ApplicationController applicationController;
    private final CSSController careerController;
    private final CompanyRepController companyRepController; 
    private final StudentController studentController;
    private final Scanner sc;

    //contructor
    public LoginUI(SystemController systemController, UserController userController,
            InternshipController internshipController, ApplicationController applicationController,
            CSSController careerController, CompanyRepController companyRepController, StudentController studentController) {
        this.systemController = systemController;
        this.userController = userController;
        this.internshipController = internshipController;
        this.applicationController = applicationController;
        this.careerController = careerController;
        this.companyRepController = companyRepController;
        this.studentController = studentController;
        this.sc = new Scanner(System.in);
    }

public void handleLogin() {
    systemController.initializeSystem();

    while (true) {
        System.out.println("\nInternship Placement Management System");
        System.out.print("Enter User ID / Email (or type exit to quit): ");
        String id = sc.nextLine().trim();

        if (id.equalsIgnoreCase("exit")) {
            System.out.println("See you again!");
            break;
        }

        System.out.print("Enter Password: ");
        String password = sc.nextLine().trim();

        User loggedInUser = systemController.authenticateUser(id, password);

        if (loggedInUser == null) {
            System.out.println("Invalid ID or password! Please try again!");
            continue;
        }

        // Handle company rep approval/rejection
        if (loggedInUser instanceof CompanyRepresentative) {
            final String loginId = loggedInUser.getUserId();  // final copy for lambda
            CompanyRepresentative repFromCSV = companyRepController.getAllCompanyReps().stream()
                    .filter(r -> r.getUserId().equalsIgnoreCase(loginId))
                    .findFirst()
                    .orElse(null);

            if (repFromCSV != null) {
                loggedInUser = repFromCSV; // assign the CSV version
                String status = repFromCSV.getStatus();

                if (status.equalsIgnoreCase("Pending")) {
                    System.out.println("Your account is still pending approval by Career Center Staff.");
                    continue;
                }

                if (status.equalsIgnoreCase("Rejected")) {
                    System.out.println("Your account has been rejected.");
                    continue;
                }
                // If status is Approved → continue to dashboard
            } else {
                System.out.println("Error: Company Representative not found in system.");
                continue;
            }
        }

        System.out.println("Welcome " + loggedInUser.getName() + " (" + loggedInUser.getUserType() + ")");
        redirectToDashboard(loggedInUser);
    }
}

    
    public void displayLoginScreen() {
        while (true) {
            System.out.println("Internship Placement Management System");
            System.out.println("1. Login");
            System.out.println("2. Register as Company Representative");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleCompanyRepRegistration();
                    break;
                case "3":
                    System.out.println("See you again!");
                    return;
                default:
                    System.out.println("Invalid option! Try again.");
            }
        }
    }


    public void handleCompanyRepRegistration() {
            System.out.println("Register as Company Representative");
            
            System.out.println("Enter Company ID: ");
            String company_id = sc.nextLine().trim();

            System.out.print("Enter Name: ");
            String name = sc.nextLine().trim();

            System.out.print("Enter Company Name: ");
            String company = sc.nextLine().trim();

            System.out.print("Enter Department: ");
            String dept = sc.nextLine().trim();

            System.out.print("Enter Position: ");
            String position = sc.nextLine().trim();

            System.out.print("Enter Email: ");
            String email = sc.nextLine().trim();

            boolean success = companyRepController.registerRep(company_id, name, company, dept, position, email);
            if (success == true)
            {
                System.out.println("Registration successful! Pending approval by Career Center Staff.");
                // List<CompanyRepresentative> test = repController.getPendingReps();
                // for (CompanyRepresentative a:test)
                // {
                //     System.out.println(a.getName());
                // }

            }
            else
            {
                System.out.println("Please try to register again.");
            }
            
    }


    private void redirectToDashboard(User user) {
        switch (user.getUserType()) {
            case "Student": {
                new StudentUI((Student) user, internshipController, applicationController, studentController).showMenu();
                break; // <--- important
            }
            case "Company Representative": {
                new CompanyRepUI((CompanyRepresentative) user, internshipController,applicationController,companyRepController).showMenu();
                break; // <--- important
            }
            case "Career Center Staff": {
                new CSStaffUI((CareerCenterStaff) user,
                        userController,
                        internshipController,
                        applicationController,
                        careerController,
                        companyRepController)
                        .showMenu();
                break; // <--- important
            }
            default:
                System.out.println("Unknown user type!");
                break;
        }
    }


}