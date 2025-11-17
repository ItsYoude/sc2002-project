package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import controller.*;
import models.*;
import utility.FileService;

public class CSSController {
    private final CompanyRepController repController;
    private final ApplicationController applicationController;
    private final List<WithdrawRequest> withdrawRequests;
    private final Scanner sc;


   public CSSController(ApplicationController applicationController, CompanyRepController repController) {
        this.applicationController = applicationController;
        this.repController = repController;
        this.withdrawRequests = new ArrayList<>();
        this.sc = new Scanner(System.in);
    }
    

    public void manageCompanyRepresentatives() {
        System.out.println("Managing company representative accounts...");
        // TODO: integrate with CSSController

        //retrieve list of representatives with pending approvals.
        List<CompanyRepresentative> pending = repController.getPendingReps();
        if (pending.isEmpty()) {
            System.out.println("No pending reps for approval..");
            return;
        }

        System.out.println("Pending Representatives:");
        for (int i = 0; i < pending.size(); i++) {
            CompanyRepresentative rep = pending.get(i);
            System.out.println("id = " + i + " " + " " + rep.getName() + " from " + rep.getCompanyName());
        }

        //ask career staff to select which rep to approve
        System.out.print("Enter the id of the representative to approve/reject: ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        if (choice < 0 || choice > pending.size()) //invalid choices/index
        {
            System.out.println("Invalid selection. Please try again.");
            return;
        }

        CompanyRepresentative selectedRep = pending.get(choice); //get the rep by index
        System.out.println("Selected Rep = " + selectedRep.getName() + " from " + selectedRep.getCompanyName());
        System.out.println("Enter 1 to Approve\nEnter 2 to Reject\nEnter Choice : ");
        int action;
        try {
            action = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter 1 or 2.");
            return;
        }

        boolean success;
        if (action == 1) {
            success = repController.approveRep(selectedRep.getUserId());
            if (success)
                System.out.println("Rep approved successfully!");
            else
                System.out.println("Error while saving approval.");
        } else if (action == 2) {
            success = repController.rejectRep(selectedRep.getUserId());
            if (success)
                System.out.println("Rep rejected successfully!");
            else
                System.out.println("Error while saving rejection.");
        } else {
            System.out.println("Invalid action. Please enter 1 or 2.");

        }
        
    }
    
    public WithdrawRequest getRequestById(String id) {
        for (WithdrawRequest req : withdrawRequests) {
            if (req.getId().equals(id)) {
                return req;
            }
        }
        return null;
    }
    public List<WithdrawRequest> getPendingWithdrawalRequests() {
        return withdrawRequests.stream()
            .filter(r -> r.getStatus().equals("Pending"))
            .collect(Collectors.toList());
    }
    public void addWithdrawRequest(WithdrawRequest req) {
        withdrawRequests.add(req);
    }
    public void viewWithdrawalRequests() {
        List<WithdrawRequest> pending = withdrawRequests.stream()
            .filter(r -> r.getStatus().equals("Pending"))
            .collect(Collectors.toList());
        for (WithdrawRequest r : pending) {
            System.out.println("Request ID: " + r.getId() + " | " + r);
        }
    }
    public void handleWithdrawalRequest(WithdrawRequest request, boolean approve) {
        Student student = request.getStudent();
        Internship internship = request.getInternship();
        
        if (approve) {
            request.setStatus("Approved");
            Application app = applicationController.getApplication(student, internship);
            if (app != null) {
                app.setStatus("Withdrawn");
                System.out.println("Withdrawal request approved.");
                // Decrease slots if needed (optional)
                internship.setSlots(internship.getSlots() + 1);

                // Save the updated internships and students
                //FileService.saveInternships(system.getAllInternships());  // or internshipController.getAllInternships()
            } else {
                System.out.println("Application not found for student " 
                                + student.getName() + " and internship " 
                                + internship.getTitle());
            }
        } else {
            request.setStatus("Rejected");
            System.out.println("Withdrawal request rejected.");
        }
    }

    public void handleInternshipApproval(Internship internship, boolean approve, InternshipController internshipController) {
        if (internship == null || !internship.getStatus().equalsIgnoreCase("Pending")) {
            System.out.println("Error: Internship is not found or is not pending approval.");
            return;
        }
        if (approve) {
            internship.setStatus("Approved");
            System.out.println("Internship '" + internship.getTitle() + "' approved. Students may now apply.");
        } else {
            internship.setStatus("Rejected");
            System.out.println("Internship '" + internship.getTitle() + "' rejected.");
        }
        // Save the changes to the persistence layer
        internshipController.saveAllInternships();
    }
}
