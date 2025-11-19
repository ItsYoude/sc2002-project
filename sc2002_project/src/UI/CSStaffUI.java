package UI;

import controller.ApplicationController;
import controller.CSSController;
import controller.CompanyRepController;
import controller.InternshipController;
import controller.UserController;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import models.CareerCenterStaff;
import models.Internship;
import models.WithdrawRequest;


import utility.FilterPipeline;
import utility.LevelFilter;
import utility.MajorFilter;
import utility.StatusFilter;
import utility.VisibilityFilter;
import utility.LevelType;


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
            System.out.println("4. Approve/Reject Internship Opportunities");
            System.out.println("5. Generate Reports");
            System.out.println("6. Change Password");
            System.out.println("7. View and Handle Withdrawal Requests");
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
                    manageInternshipApprovals();
                    break;
                case "5": 
                    generateReports(); 
                    break;
                case "6":
                    boolean changedStaff = staff.changePassword();
                    if (changedStaff) continueMenu = false;
                    break;
                case "7":
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



    private void generateReports() {
        List<Internship> list = internshipController.getAllInternships();

        System.out.println("Filter by: 1) Status  2) Major  3) Level  4) Visible Only");
        String option = sc.nextLine().trim();


        //Create pipeline and filtered list
        List<Internship> filtered = new ArrayList<>(list);
        FilterPipeline pipeline = new FilterPipeline();


        switch (option) {
            case "1":
                System.out.print("Enter status (Approved/Pending/Rejected/Filled): ");
                String status = sc.nextLine().trim();
                if (status.isEmpty()) {
                    System.out.println("Please do not enter an empty string.");
                    return;
                }
                if (!status.equalsIgnoreCase("Approved") &&
                        !status.equalsIgnoreCase("Pending") &&
                        !status.equalsIgnoreCase("Rejected") &&
                        !status.equalsIgnoreCase("Filled")) {
                    System.out.println(
                            "Invalid status entered. Please enter Approved, Pending, Rejected, or Filled.");
                    return;
                }

                //filtered = utility.InternshipFilter.filterByStatus(list, status);
                //Pipeline filtering for this.
                pipeline.add(new StatusFilter(status));
                break;
            case "2":
                System.out.print("Enter major: ");
                String major = sc.nextLine();
                if (major.isEmpty()) {
                    System.out.println("Invalid major entered.");
                    return;
                }
                //filtered = utility.InternshipFilter.filterByMajor(list, major);

                pipeline.add(new MajorFilter(major));
                break;
            case "3":
                System.out.print("Enter level: ");
                String level = sc.nextLine().trim();
                if (level.isEmpty())
                {
                    System.out.println("Level cannot be empty.");
                    return;
                }

                //check if its inside the enum
                LevelType levelEnum;
                try
                {
                    levelEnum = LevelType.valueOf(level.toUpperCase());
                }
                catch (IllegalArgumentException e) {
                    System.out.println("Invalid level. Please enter Basic, Intermediate, or Advanced.");
                    return;
                }

                //filtered = utility.InternshipFilter.filterByLevel(list, level);
                pipeline.add(new LevelFilter(level));
                break;
            case "4":
                //filtered = utility.InternshipFilter.filterVisible(list);
                pipeline.add(new VisibilityFilter());
                break;
            default:
                System.out.println("Invalid filter option.");
                return;
        }
    
        System.out.println("\n--- Filtered Internships ---");
        filtered = pipeline.filter(list);
        for (Internship i : filtered) System.out.println(i);
    }

    //manage internship opportunity approval/reject
    private void manageInternshipApprovals() {

        List<Internship> pendingOpportunities = internshipController.getPendingInternships();
        
        if (pendingOpportunities.isEmpty()) {
            System.out.println("No internship opportunities are currently pending approval.");
            return;
        }

        System.out.println("\n--- Pending Internship Opportunities ---");
        for (int i = 0; i < pendingOpportunities.size(); i++) {
            Internship opp = pendingOpportunities.get(i);
            System.out.println("ID: [" + opp.getId() + "] " + opp.getTitle() + 
                            " | Company: " + opp.getCompany() + 
                            " | Level: " + opp.getLevel() +
                            " | Status: " + opp.getStatus());
        }

        System.out.print("\nEnter the Internship ID to handle (or type 0 to cancel): ");
        String id = sc.nextLine().trim();

        if (id.equals("0")) {
            System.out.println("Approval cancelled.");
            return;
        }
        
        Internship selectedInternship = internshipController.getInternshipById(id);

        if (selectedInternship == null || !selectedInternship.getStatus().equalsIgnoreCase("Pending")) {
            System.out.println("Invalid ID or internship is already approved/rejected.");
            return;
        }

        System.out.print("Approve or Reject this opportunity? (A/R): ");
        String decision = sc.nextLine().trim().toLowerCase();

        if (decision.equals("a")) {
            careerController.handleInternshipApproval(selectedInternship, true, internshipController);
        } else if (decision.equals("r")) {
            careerController.handleInternshipApproval(selectedInternship, false, internshipController);
        } else {
            System.out.println("Invalid input. Please enter 'A' for Approve or 'R' for Reject.");
        }
    }
}
