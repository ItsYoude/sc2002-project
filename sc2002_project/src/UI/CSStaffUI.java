package UI;

import controller.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import models.*;

public class CSStaffUI {
    private final CareerCenterStaff staff;
    private final UserController userController;
    private final InternshipController internshipController;
    private final ApplicationController applicationController;
    private final CSSController careerController;
    private final CompanyRepController repController;
    private final Scanner sc;

    // Constructor
    public CSStaffUI(CareerCenterStaff staff, UserController userController, InternshipController internshipController, ApplicationController applicationController,
            CSSController careerController,
            CompanyRepController repController) {
        this.staff = staff;
        this.userController = userController;
        this.internshipController = internshipController;
        this.applicationController = applicationController;
        this.careerController = careerController;
        this.repController = repController;
        this.sc = new Scanner(System.in);
    }

    public void showMenu() {
        boolean continueMenu = true;

        while (continueMenu) {
            System.out.println("\n--- Career Center Staff Dashboard ---");
            System.out.println("1. View All Internships");
            System.out.println("2. View All Applications");
            System.out.println("3. Approve/Reject Company Representatives");
            System.out.println("4. Generate Reports");
            System.out.println("5. Change Password");
            System.out.println("6. View and Handle Withdrawal Requests");
            System.out.println("0. Logout");
            System.out.print("Select option: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    internshipController.viewAllInternships();
                    break;
                case "2":
                    applicationController.viewAllApplications();
                    break;
                case "3":
                    careerController.manageCompanyRepresentatives();
                    break;
                case "4": 
                    generateReports(); 
                    break;
                case "5":
                    staff.changePassword();
                    continueMenu = false;
                    break;
                case "6":
                    List<WithdrawRequest> pendingRequests = careerController.getPendingWithdrawalRequests();

                    if (pendingRequests.isEmpty()) {
                        System.out.println("No pending withdrawal requests.");
                        break;
                    }

                    System.out.println("\n--- Pending Withdrawal Requests ---");
                    for (WithdrawRequest r : pendingRequests) {
                        System.out.println("Request ID: " + r.getId() +
                                        " | Student: " + r.getStudent().getName() +
                                        " | Internship: " + r.getInternship().getTitle() +
                                        " | Reason: " + r.getReason());
                    }

                    System.out.print("\nEnter the Request ID to handle (or type 0 to cancel): ");
                    String requestId = sc.nextLine().trim();

                    if (requestId.equals("0")) {
                        System.out.println("Cancelled.");
                        break;
                    }

                    WithdrawRequest selectedRequest = careerController.getRequestById(requestId);

                    if (selectedRequest == null || !selectedRequest.getStatus().equals("Pending")) {
                        System.out.println("Invalid or already handled request ID.");
                        break;
                    }

                    System.out.print("Approve this request? (y/n): ");
                    String decision = sc.nextLine().trim().toLowerCase();

                    if (decision.equals("y")) {
                        careerController.handleWithdrawalRequest(selectedRequest, true);
                    } else if (decision.equals("n")) {
                        careerController.handleWithdrawalRequest(selectedRequest, false);
                    } else {
                        System.out.println("Invalid input. Please enter 'y' or 'n'.");
                    }
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

    // private void changePassword() {
    //     System.out.print("Enter your current password: ");
    //     String current = sc.nextLine().trim();

    //     if (!staff.passwordValidator(current)) {
    //         System.out.println("Incorrect password. Please try again.");
    //         return;
    //     }

    //     System.out.print("Enter new password: ");
    //     String newPassword = sc.nextLine().trim();
    //     staff.changePassword(newPassword);

    //     System.out.println("Password changed successfully. Please log in again.");
    // }
    private void generateReports() {
    List<Internship> list = internshipController.getAllInternships();

    System.out.println("Filter by: 1) Status  2) Major  3) Level  4) Visible Only");
    String option = sc.nextLine().trim();

    List<Internship> filtered = new ArrayList<>(list);

    switch (option) {
        case "1":
            System.out.print("Enter status (Approved/Pending/Rejected/Filled): ");
            String status = sc.nextLine();
            filtered = utility.InternshipFilter.filterByStatus(list, status);
            break;
        case "2":
            System.out.print("Enter major: ");
            String major = sc.nextLine();
            filtered = utility.InternshipFilter.filterByMajor(list, major);
            break;
        case "3":
            System.out.print("Enter level: ");
            String level = sc.nextLine();
            filtered = utility.InternshipFilter.filterByLevel(list, level);
            break;
        case "4":
            filtered = utility.InternshipFilter.filterVisible(list);
            break;
    }

    System.out.println("\n--- Filtered Internships ---");
    for (Internship i : filtered) System.out.println(i);
}
}
