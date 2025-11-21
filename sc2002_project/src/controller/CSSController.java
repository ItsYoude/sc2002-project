package controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import models.*;
import utility.ClosingDateFilter;
import utility.FileService;
import utility.FilterManager;
import utility.FilterPipeline;
import utility.LevelFilter;
import utility.MajorFilter;
import utility.StatusFilter;
import utility.UserFilterSettings;

public class CSSController {
    private static CSSController instance;
    private final CompanyRepController repController;
    private final ApplicationController applicationController;
    private final InternshipController internshipController;
    private List<WithdrawRequest> withdrawRequests;
    private final List<CareerCenterStaff> staffList = new ArrayList<>();
    private final Scanner sc;
    private final FilterManager filterManager; 


   public CSSController(ApplicationController applicationController, CompanyRepController repController,InternshipController internshipController,FilterManager filterManager) {
        this.applicationController = applicationController;
        this.repController = repController;
        this.withdrawRequests = new ArrayList<>();
        this.internshipController = internshipController;
        this.sc = new Scanner(System.in);
        this.staffList.addAll(FileService.loadCSStaff()); // <-- load from CSV
        this.filterManager = filterManager;
        instance = this;
    }
    
    public List<CareerCenterStaff> getAllStaff() {
    return staffList;
}

    public static CSSController getInstance() {
        return instance;
    }

    public List<WithdrawRequest> getWithdrawList()
    {
        return withdrawRequests;
    }

    public void setWithdrawlist(List<WithdrawRequest> withdraw_list)
    {
        this.withdrawRequests = withdraw_list;
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
                //     System.out.println("Reference of internship inside Withdraw Request: " 
                //  + System.identityHashCode(req.getInternship()));
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
        //this is a list that is not persistent yet includes id,student,internship,reason,status
        //add to csv
        FileService.saveWithdrawRequests(withdrawRequests);

    }

    //check if there exist a pending withdrawal request. 
    public boolean checkIfExist(WithdrawRequest req) {
        return withdrawRequests.stream()
                .anyMatch(r -> r.getStudent().getName().equalsIgnoreCase(req.getStudent().getName()) &&
                        r.getInternship().getId().equalsIgnoreCase(req.getInternship().getId()) &&
                        r.getStatus().equalsIgnoreCase("Pending"));
    }
        
    public void viewAllWithdrawlRequests()
    {
        if (!withdrawRequests.isEmpty()) {
            System.out.println("\n--- All Withdrawal Requests ---");
            for (WithdrawRequest wr : withdrawRequests) {
                System.out.println(wr);
            }
        }
    }
    
    public List<WithdrawRequest> getWithdrawalRequestsForStudent(Student student) {
        return withdrawRequests.stream()
                .filter(req -> req.getStudent().getUserId().equalsIgnoreCase(student.getUserId()))
                .collect(Collectors.toList());
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

                //change the application status to Withdrawn
                app.setStatus("Withdrawn");

                //change the appliedInternship status of this internship to Withdrawn for Student
                List<AppliedRecord> ar = student.getAppliedInternshipId();
                for (AppliedRecord a : ar) {
                    if (a.getInternshipId().equalsIgnoreCase(request.getInternship().getId())) {
                        a.setStatus("Withdrawn");
                    }
                }

                if (student.getAcceptedInternshipId() != null)
                {
                    //check if the acceptedInternship is this internship, if it is, remove. 
                    if (student.getAcceptedInternshipId().equalsIgnoreCase(request.getInternship().getId())) {
                        //System.out.println("This is accepeted ! "+student.getAcceptedInternshipId());
                        student.setAcceptedInternshipId(null);
                        //increase internship slots because accepted has withdrawn got more slots now.
                        internship.setSlots(internship.getSlots() + 1);

                    }
                }

                //remove this withdrawing request
                //withdrawRequests.removeIf(r -> r.getId().equals(request.getId()));

                List<Student> list = StudentController.getAllStudents();

                for (int i = 0; i < list.size(); i++) {
                    Student s = list.get(i);
                    if (s.getUserId().equalsIgnoreCase(student.getUserId())) {
                        list.set(i, student);
                        break;
                    }
                }

                List<Internship> internships = internshipController.getAllInternships();
                for (int i = 0; i < internships.size(); i++) {
                    if (internships.get(i).getId().equalsIgnoreCase(internship.getId())) {
                        internships.set(i, internship);
                        break;
                    }
                }

                
                
                //update student, application, withdraw, internship records into csv
                FileService.saveStudents(StudentController.getAllStudents());
                FileService.saveWithdrawRequests(withdrawRequests);
                FileService.saveInternships(internshipController.getAllInternships());
        

                System.out.println("Withdrawal request approved.");
            } else {
                System.out.println("Application not found for student " 
                                + student.getName() + " and internship " 
                        + internship.getTitle());
     
            }
        } else {
            //set request to reject and save to the csv
            request.setStatus("Rejected");
            FileService.saveWithdrawRequests(withdrawRequests);
            System.out.println("Withdrawal request rejected.");

        }





    }

    public void handleInternshipApproval(Internship internship, boolean approve,
            InternshipController internshipController) {
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
    


    public List<Internship> getFilteredInternships(String userId, List<Internship> all) {
        UserFilterSettings settings = filterManager.getFilters(userId);

        FilterPipeline pipeline = new FilterPipeline();

        if (settings != null) {
            pipeline.add(new StatusFilter(settings.getStatus()));
            pipeline.add(new LevelFilter(settings.getLevel()));
            pipeline.add(new MajorFilter(settings.getMajor()));
            pipeline.add(new ClosingDateFilter(settings.getClosingBefore()));
            // add other filters as needed
        }

        // Always add default sorting (alphabetical)
        pipeline.add(internships -> {
            internships.sort(Comparator.comparing(Internship::getTitle, String.CASE_INSENSITIVE_ORDER));
            return internships;
        });

        return pipeline.filter(all);

    }
    
    public void saveUserFilterSettings(CareerCenterStaff staff, UserFilterSettings settings) {
        filterManager.setFilters(staff.getUserId(), settings);
    }

    public UserFilterSettings getPreviousFilter(String userId)
    {
        return filterManager.getFilters(userId);

    }







}
