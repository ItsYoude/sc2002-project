package controller;

import java.util.*;
import models.CompanyRepresentative;
import models.Internship;
import utility.FileService;

public class CompanyRepController { //interaction between UI and CompanyRepresentative 
    private List<CompanyRepresentative> pendingReps = new ArrayList<>();
    private List<CompanyRepresentative> approvedReps = new ArrayList<>();
    private List<CompanyRepresentative> rejectedReps = new ArrayList<>();
    private static CompanyRepController instance;


    private List<CompanyRepresentative> companyReps = new ArrayList<>();

    public CompanyRepController() {// Constructor
        loadReps();
        instance = this;
    }
    
    public static CompanyRepController getInstance() {
        return instance;
    }
    
    public List<CompanyRepresentative> getAllCompanyReps() {
        return companyReps;
    }


    // Getters for UI/CSS/RepController
    public List<CompanyRepresentative> getPendingReps() {
        return pendingReps; 
    }

    public List<CompanyRepresentative> getApprovedReps() {
        return approvedReps;
    }

    public List<CompanyRepresentative> getRejectedReps() {
        return rejectedReps;
    }


    public final void loadReps() { //load all company rep entries into the two list 
        //clear both list first, then append the latest records into the two list
        pendingReps.clear();
        approvedReps.clear();
        rejectedReps.clear();
        companyReps.clear();

        List<CompanyRepresentative> repsFromFile = FileService.loadCompanyReps();
        for (CompanyRepresentative rep : repsFromFile) {
            // System.out.println(rep.getStatus());
            System.out.println(rep.getName() + rep.isApproved());
            companyReps.add(rep);   // <--- track all reps
            String status = rep.getStatus() == null ? "" : rep.getStatus().trim(); //null-safe default
                switch (status) {
                case "Approved":
                    approvedReps.add(rep);
                    break;
                case "Rejected":
                    rejectedReps.add(rep);
                    break;
                default:
                    pendingReps.add(rep);
                    break;
            }

        }

    }
    

    public boolean saveAllReps() {
    // Save the master list that contains all reps
    if (!FileService.saveCompanyReps(companyReps)) {
        System.out.println("Error while saving.");
        return false;
    }
    return true;
}




    //Caven5@gmail.com version
    
    // public boolean saveAllReps() {

    // // Save the master list that contains all reps

    // if (!FileService.saveCompanyReps(companyReps)) {
    //     System.out.println("Error while saving.");
    //     return false;
    // }
    // return true;
    // }




    //serach by user id in selected list, (pending or approved)
    private CompanyRepresentative findRepInList(String userId, List<CompanyRepresentative> list) {
        for (CompanyRepresentative rep : list) {
            if (rep.getUserId().equalsIgnoreCase(userId)) { //userID exist within system. 
                return rep;
            }
        }
        return null;
    }

    // Register a new rep (adds to pending list)
    public boolean registerRep(String company_id, String name, String companyName, String department, String position,String email) 
    {

    // Check if userID exists in the system before registering
    if (findRepInList(company_id, pendingReps) != null ||
        findRepInList(company_id, approvedReps) != null ||
        findRepInList(company_id, rejectedReps) != null) {
        System.out.println("Registration failed: CompanyRepID already exists.");
        return false;
    }

    // Create new CompanyRepresentative with default password "password" and status "Pending"
    CompanyRepresentative rep = new CompanyRepresentative(
            company_id, name, companyName, department, position,email, "Pending", "password"
    );

    // Add to master list for CSV saving
    companyReps.add(rep);

    // Add to pending list for UI/logic
    pendingReps.add(rep);

    // Save all reps to CSV
    if (!saveAllReps()) {
        // If saving fails, rollback both lists
        pendingReps.remove(rep);
        companyReps.remove(rep);
        System.out.println("Error saving new representative. Please try again.");
        return false;
    }

    //System.out.println("Registration successful! Pending approval by Career Center Staff.");
    return true;
}

    //Approve a rep
    //each rep account is tied to unique company ID

    public boolean approveRep(String company_id) {
        CompanyRepresentative rep = findRepInList(company_id, pendingReps);
        if (rep != null) // found and delete
        {
            rep.approve(); //change isApproved to True
            approvedReps.add(rep); //append to apporvedlist
            pendingReps.remove(rep); //remove from pending list

            //save to file
            if (saveAllReps() == true) {
                System.out.println("Approval Successful!");
                return true;
            } else {
                System.out.println("Approval Failed!");
                return false;
            }

        }
        return false;

    }

    // Reject a rep
    public boolean rejectRep(String company_id) {
        CompanyRepresentative rep = findRepInList(company_id, pendingReps);
        if (rep != null) //found and delete
        {
            rep.reject(); //change status to rejected
            rejectedReps.add(rep); //append to rejectedList
            pendingReps.remove(rep); //remove from pending list

            //save to file
            if (saveAllReps() == true) {
                System.out.println("Rejection Successful!");
                return true;
            } else {
                System.out.println("Rejection Failed!");
                return false;
            }
        }
        return false;
    }

    public boolean findAvaliableID(String id)
    {
        List<Internship> internships = FileService.loadInternships();
        for (Internship i : internships) {
            if (i.getId().equalsIgnoreCase(id)) {
                System.out.println("ID is taken, choose another.");
            }
        }
        return true;
    }


    public void findIfMaxInternshipCreated(CompanyRepresentative rep,InternshipController internshipController)
    {
        List<Internship> allInternships = internshipController.getAllInternships();
        long count = allInternships.stream()
            .filter(i -> i.getRepresentativeId().equals(rep.getUserId())) 
                .count();
            
        if (count >= 5)
        {
            rep.setMaxCreated(true);
        }
        else
        {
            rep.setMaxCreated(false);
        }
        System.out.println((count));
    }
        


}
