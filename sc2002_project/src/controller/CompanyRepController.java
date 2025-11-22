package controller;

import java.util.*;
import models.CompanyRepresentative;
import models.Internship;
import utility.ClosingDateFilter;
import utility.FileService;
import utility.FilterManager;
import utility.FilterPipeline;
import utility.LevelFilter;
import utility.MajorFilter;
import utility.StatusFilter;
import utility.UserFilterSettings;


/**
 * Controller for managing Company Representative operations.
 * Handles registration, approval, rejection, loading/saving reps, 
 * and filtering internships for company representatives.
 */
public class CompanyRepController { //interaction between UI and CompanyRepresentative 
    /** List of pending company representatives. */
    private List<CompanyRepresentative> pendingReps = new ArrayList<>();

     /** List of approved company representatives. */

     private List<CompanyRepresentative> approvedReps = new ArrayList<>();
     /** List of rejected company representatives. */

     private List<CompanyRepresentative> rejectedReps = new ArrayList<>();

    /** Singleton instance of the controller. */

    private static CompanyRepController instance;

    /** Master list of all company representatives. */
    private List<CompanyRepresentative> companyReps = new ArrayList<>();

    /** Manager for user-specific internship filters. */
    private final FilterManager filterManager; 




        /**
     * Constructor to initialize the controller with a filter manager.
     * @param filterManager the filter manager to use
     */
        public CompanyRepController(FilterManager filterManager) {// Constructor
            this.filterManager = filterManager;
            loadReps();
            instance = this;
        }
    
   /** Returns the singleton instance of this controller. */

   public static CompanyRepController getInstance() {
       return instance;
   }
    
     /** Returns the list of all company representatives. */

    public List<CompanyRepresentative> getAllCompanyReps() {
        return companyReps;
    }

       /** Returns the list of pending reps. */
    public List<CompanyRepresentative> getPendingReps() {
        return pendingReps;
    }
    /** Returns the list of approved reps. */

    public List<CompanyRepresentative> getApprovedReps() {
        return approvedReps;
    }
    /** Returns the list of rejected reps. */

    public List<CompanyRepresentative> getRejectedReps() {
        return rejectedReps;
    }



    /** Loads company representatives from file and categorizes them by status. */

    public final void loadReps() { //load all company rep entries into the two list 
        //clear both list first, then append the latest records into the two list
        pendingReps.clear();
        approvedReps.clear();
        rejectedReps.clear();
        companyReps.clear();

        List<CompanyRepresentative> repsFromFile = FileService.loadCompanyReps();
        for (CompanyRepresentative rep : repsFromFile) {
            // System.out.println(rep.getStatus());
            //System.out.println(rep.getName() + rep.isApproved());
            companyReps.add(rep); // <--- track all reps
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
    /** Saves all company representatives to file. */

    public boolean saveAllReps() {
        // Save the master list that contains all reps
        if (!FileService.saveCompanyReps(companyReps)) {
            System.out.println("Error while saving.");
            return false;
        }
        return true;
    }



    /** Finds a representative by user ID in the company representative list. */
    private CompanyRepresentative findRepInList(String userId, List<CompanyRepresentative> list) {
        for (CompanyRepresentative rep : list) {
            if (rep.getUserId().equalsIgnoreCase(userId)) { //userID exist within system. 
                return rep;
            }
        }
        return null;
    }

     /** Registers a new company representative (adds to pending list). */
    public boolean registerRep(String company_id, String name, String companyName, String department, String position,
            String email) {

        // Check if userID exists in the system before registering
        if (findRepInList(company_id, pendingReps) != null ||
                findRepInList(company_id, approvedReps) != null ||
                findRepInList(company_id, rejectedReps) != null) {
            System.out.println("Registration failed: CompanyRepID already exists.");
            return false;
        }

        // Create new CompanyRepresentative with default password "password" and status "Pending"
        CompanyRepresentative rep = new CompanyRepresentative(
                company_id, name, companyName, department, position, email, "Pending", "password");

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

    /** Approves a pending representative. */
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

        /** Rejects a pending representative. */

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


    /** Returns the latest internship ID based on existing internships. */
    public String getLatestId()
    {
        List<Internship> internships = FileService.loadInternships();
        int count = internships.size();
        System.out.println();
        return ("I" + String.valueOf(count + 1));
    }
    
    
    
    /** Checks if the representative has reached the maximum allowed created internships. */
    public void findIfMaxInternshipCreated(CompanyRepresentative rep, InternshipController internshipController) {
        List<Internship> allInternships = internshipController.getAllInternships();
        long count = allInternships.stream()
                .filter(i -> i.getRepresentativeId().equals(rep.getUserId()))
                .count();

        if (count >= 5) {
            rep.setMaxCreated(true);
        } else {
            rep.setMaxCreated(false);
        }
        //System.out.println((count));
    }


    /** Returns internships filtered according to the representative's saved settings. */
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
    
    /** Saves filter settings for a company representative. */
    public void saveUserFilterSettings(CompanyRepresentative companyRepresentative, UserFilterSettings settings) {
        filterManager.setFilters(companyRepresentative.getUserId(), settings);
    }

    /** Returns previously saved filter settings for a representative. */
    public UserFilterSettings getPreviousFilter(String userId)
    {
        return filterManager.getFilters(userId);

    }

}
