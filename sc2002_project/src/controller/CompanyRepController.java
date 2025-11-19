package controller;

import java.util.*;
import models.CompanyRepresentative;
import models.Internship;
import utility.FileService;

public class CompanyRepController { //interaction between UI and CompanyRepresentative 
    private static CompanyRepController instance;
    private List<CompanyRepresentative> pendingReps = new ArrayList<>();
    private List<CompanyRepresentative> approvedReps = new ArrayList<>();
    private List<CompanyRepresentative> rejectedReps = new ArrayList<>();
    private List<CompanyRepresentative> companyReps = new ArrayList<>();

    public CompanyRepController() {// Constructor
        loadReps();
        instance = this;
    }

    public static CompanyRepController getInstance() {
        return instance;
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

    public List<CompanyRepresentative> getAllCompanyReps() {
    return companyReps; 
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
            System.out.println(rep.getName()+rep.isApproved());
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
    public boolean registerRep(String company_id, String name, String companyName, String department, String position,
            String email) {

        //check if userID exist in system before registering
        if (findRepInList(company_id, pendingReps) != null ||
            findRepInList(company_id, approvedReps) != null ||
            findRepInList(company_id, rejectedReps) != null) {
            System.out.println("Registration failed: CompanyRepID already exists.");
            return false;
        }

        CompanyRepresentative rep = new CompanyRepresentative(company_id, name, companyName, department, position,
                email,"Pending", "password");
        pendingReps.add(rep);
        //update into the csv file, if fail then remove rep from pending
        if (saveAllReps() == false) {
            pendingReps.remove(rep);
            return false;
        } else {
            return true;
        }
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
        


}
