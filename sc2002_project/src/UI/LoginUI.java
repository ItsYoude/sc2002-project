package UI;
import controller.*;
import java.util.Scanner;
import models.*;

/**
 * Provides a console-based login and registration interface for the Internship
 * Placement Management System. Handles authentication, user type verification,
 * and routing to the respective dashboard (Student, Company Representative,
 * Career Center Staff) upon successful login. Also handles registration of
 * Company Representatives.
 */

public class LoginUI {
    private final SystemController systemController;
    private final UserController userController;
    private final InternshipController internshipController;
    private final ApplicationController applicationController;
    private final CSSController careerController;
    private final CompanyRepController companyRepController; 
    private final StudentController studentController;
    private final Scanner sc;

  
    /**
     * Constructs a LoginUI instance with required controllers for system operations.
     *
     * @param systemController        SystemController instance
     * @param userController          UserController instance
     * @param internshipController    InternshipController instance
     * @param applicationController   ApplicationController instance
     * @param careerController        CSSController instance
     * @param companyRepController    CompanyRepController instance
     * @param studentController       StudentController instance
     */
    public LoginUI(SystemController systemController, UserController userController,
            InternshipController internshipController, ApplicationController applicationController,
            CSSController careerController, CompanyRepController companyRepController,
            StudentController studentController) {
        this.systemController = systemController;
        this.userController = userController;
        this.internshipController = internshipController;
        this.applicationController = applicationController;
        this.careerController = careerController;
        this.companyRepController = companyRepController;
        this.studentController = studentController;
        this.sc = new Scanner(System.in);
    }

    
        /**
     * Handles the login process:
     * 1. Prompts for User ID and validates its existence.
     * 2. Prompts for password and validates credentials.
     * 3. Checks account status for Company Representatives.
     * 4. Redirects authenticated users to their respective dashboards.
     */
    private void handleLogin() {
        systemController.initializeSystem();

    while (true) {
        System.out.println("\nInternship Placement Management System");
        System.out.print("Enter User ID (or type exit to quit): ");
        String id = sc.nextLine().trim();

            if (id.equalsIgnoreCase("exit")) {
                System.out.println("See you again!");
                break;
            }

        // STEP 1 — Check whether ID exists FIRST
        Object idCheck = systemController.authenticateUser(id, ""); // blank password

        if (idCheck == null) {
            System.out.println("User ID does not exist. Please try again.");
            continue;
        }

        // STEP 2 — Now ask for password
        System.out.print("Enter Password: ");
        String password = sc.nextLine().trim();

        Object authResult = systemController.authenticateUser(id, password);

        // password wrong → returns Boolean.FALSE
        if (authResult instanceof Boolean && authResult.equals(Boolean.FALSE)) {
            System.out.println("Incorrect password. Please try again.");
            continue;
        }

        User loggedInUser = (User) authResult;


        if (loggedInUser instanceof CompanyRepresentative) {
    final String loginId = loggedInUser.getUserId();  

    CompanyRepresentative repFromCSV = companyRepController
            .getAllCompanyReps().stream()
            .filter(r -> r.getUserId().equalsIgnoreCase(loginId))
            .findFirst()
            .orElse(null);

    if (repFromCSV != null) {
        loggedInUser = repFromCSV;
        String status = repFromCSV.getStatus();

        if (status.equalsIgnoreCase("Pending")) {
            System.out.println("Your account is still pending approval by Career Center Staff.");
            continue;
        } else if (status.equalsIgnoreCase("Rejected")) {
            System.out.println("Your account has been rejected.");
            continue;
        }
        // else → status is Approved, continue
    } else {
        System.out.println("Error: Company Representative not found in system.");
        continue;
    }
}
        // Outside the CompanyRep check
    System.out.println("Welcome " + loggedInUser.getName() + " (" + loggedInUser.getUserType() + ")");
    boolean loggedOut = redirectToDashboard(loggedInUser);
    if (loggedOut) {
    // User logged out → return to START PAGE
        return;
    }

}
}


        /**
     * Displays the initial login screen allowing users to:
     * 1. Login
     * 2. Register as Company Representative
     * 3. Exit the system
     */
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

    /**
     * Handles registration of a new Company Representative.
     * Prompts for required details and submits to CompanyRepController.
     * Shows success message if registration is pending approval.
     */
    private void handleCompanyRepRegistration() {
            System.out.println("Register as Company Representative");
            
            System.out.println("Enter Company ID which is your email: ");
            String company_id = sc.nextLine().trim();

            System.out.print("Enter Name: ");
            String name = sc.nextLine().trim();

            System.out.print("Enter Company Name: ");
            String company = sc.nextLine().trim();

            System.out.print("Enter Department: ");
            String dept = sc.nextLine().trim();

            System.out.print("Enter Position: ");
            String position = sc.nextLine().trim();

            // System.out.print("Enter Email: ");
            // String email = sc.nextLine().trim();


            boolean success = companyRepController.registerRep(company_id, name, company, dept, position, company_id);
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

    /**
     * Redirects an authenticated user to the appropriate dashboard
     * based on their user type.
     *
     * @param user The authenticated User
     * @return true if user was redirected successfully, false if unknown user type
     */
    private boolean redirectToDashboard(User user) {
        switch (user.getUserType()) {
            case "Student": {
                new StudentUI((Student) user, internshipController, applicationController, studentController).showMenu();
                return true; // <--- important
            }
            case "Company Representative": {
                new CompanyRepUI((CompanyRepresentative) user, internshipController,applicationController,companyRepController).showMenu();
                return true; // <--- important
            }
            case "Career Center Staff": {
                new CSStaffUI((CareerCenterStaff) user,
                        userController,
                        internshipController,
                        applicationController,
                        careerController,
                        companyRepController)
                        .showMenu();
                return true; // <--- important
            }
            default:
                System.out.println("Unknown user type!");
                return false;
        }
    }


}