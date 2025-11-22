package controller;

import java.util.*;
import java.util.stream.Collectors;
import models.*;
import utility.ApprovedFilter;
import utility.BeforeDateFilter;
import utility.FileService;
import utility.FilterPipeline; //NEW : Pipelined
import utility.IInternshipSorter;
import utility.LevelFilter;
import utility.MajorFilter;
import utility.NotFilledFilter;
import utility.TitleSorter;
import utility.VisibilityFilter;



// import utility.IEligibilityFilter; // NEW: Abstraction for eligibility
// import utility.IInternshipSorter; // NEW: Abstraction for sorting
// import utility.StudentEligibilityFilter; // NEW: Concrete default filter




/**
 * Controller for managing Internship operations.
 * Handles loading, saving, adding internships, 
 * filtering eligible internships for students, and viewing/sorting internships.
 */


public class InternshipController {
    /** List of all internships. */
    private List<Internship> internships = new ArrayList<>();

    // NEW DIP FIELDS: These are the abstractions the controller relies on.
    //private final IEligibilityFilter eligibilityFilter;

    /** Default sorter for internships. */
    private final IInternshipSorter defaultSorter;


    /**
     * Default constructor initializes controller with default sorter and loads internships.
     */
    public InternshipController() {
        //this.eligibilityFilter = new StudentEligibilityFilter(); 
        this.defaultSorter = new TitleSorter();
        loadInternships();
    }

    /**
     * Constructor with custom sorter.
     * @param defaultSorter the sorter to use for displaying internships
     */

    public InternshipController(IInternshipSorter defaultSorter) {
        this.defaultSorter = defaultSorter;
        loadInternships();
    }

    /** Loads all internships from file. */
    public void loadInternships() {
        internships = FileService.loadInternships();
    }

    /** Adds a new internship and saves it. */
    public boolean addInternship(Internship internship) {
        internships.add(internship);
        if (!FileService.saveInternships(internships)) {
            internships.remove(internship); // rollback
            return false;
        }
        return true;
    }


    /** Builds a filter pipeline for a student based on eligibility criteria. */
    private FilterPipeline buildPipelineForStudent(Student student) {
        FilterPipeline pipeline = new FilterPipeline();
        pipeline.add(new ApprovedFilter());      // only Approved internships
        pipeline.add(new NotFilledFilter());     // optionally filter NotFilled
        pipeline.add(new VisibilityFilter());
        pipeline.add(new MajorFilter(student.getMajor()));
        pipeline.add(new LevelFilter(student.getYearOfStudy()));
        pipeline.add(new BeforeDateFilter());
        return pipeline;
    }


    /** Returns eligible internships for a student, optionally sorted. */
    public List<Internship> getEligibleInternships(Student student, IInternshipSorter sorter)
    {
        FilterPipeline pipeline = buildPipelineForStudent(student);
        List<Internship> filtered = pipeline.filter(internships);
        return defaultSorter.sort(filtered);
    }
    
    /** Returns eligible internships for a student without sorting. */
    public List<Internship> getEligibleInternships(Student student)
    {
        FilterPipeline pipeline = buildPipelineForStudent(student);
        List<Internship> filtered = pipeline.filter(internships);
        return filtered;  
    }
    
    
    
    
    /** Displays all visible internships filtered by major and year of study for students. */
    public void viewAllInternships(String major, int yearOfStudy) {
        // 1. Filter the list based on the original logic
        List<Internship> filteredList = new ArrayList<>();
        for (Internship i : internships) {
            if (i.isVisible()) {
                if (i.getMajor().equalsIgnoreCase(major) || major.equalsIgnoreCase("All")) {
                    if (yearOfStudy <= 2 && i.getYearType().equalsIgnoreCase("Basic") || yearOfStudy >= 3) {
                        filteredList.add(i);
                    }
                }
            }
        }

        // 2. Sort the filtered list using the injected defaultSorter (DIP in action)
        List<Internship> sortedList = defaultSorter.sort(filteredList);

        // 3. Print the results (Presentation Logic maintained)
        System.out.println("\n--- Internship Opportunities ---");
        if (sortedList.isEmpty()) {
            System.out.println("No internships available at this time.");
        } else {
            for (Internship i : sortedList) {
                System.out.println(i);
            }
        }
    }

    /** Displays all internships, sorted using the default sorter, for staff. */
    public void viewAllInternships() {
        // 1. Sort the entire list using the injected defaultSorter (DIP in action)
        List<Internship> sortedList = defaultSorter.sort(this.internships);

        // 2. Print the results (Presentation Logic maintained)
        System.out.println("\n--- All Internships ---");
        if (sortedList.isEmpty()) {
            System.out.println("No internships available.");
            return;
        }
        for (Internship i : sortedList) {
            System.out.println(i);
        }
    }

    /** Returns an internship by its ID. */
    public Internship getInternshipById(String id) {
        for (Internship i : internships) {
            if (i.getId().equalsIgnoreCase(id)) {
                return i;
            }
        }
        return null;
    }

    /** Returns the list of all internships. */
    public List<Internship> getAllInternships() {
        return internships;
    }

    /** Saves all internships to file. */
    public void saveAllInternships() {
        FileService.saveInternships(internships);
    }

    /** Replaces the current internship list with a new list. */
    public void setInternshipList(List<Internship> internships) {
        this.internships = internships;
    }

    /** Returns internships with status "Pending". */
    public List<Internship> getPendingInternships() {
        // Filter the main list where the status is "Pending"
        return internships.stream()
                .filter(i -> i.getStatus().equalsIgnoreCase("Pending"))
                .collect(Collectors.toList());
    }

    /** Decrements slots of an internship after a student acceptance and updates status if filled. */
    public void decrementSlotsAfterAcceptance(String internship_id)
    {
        for (Internship it:internships)
        {
            if (it.getId().equalsIgnoreCase(internship_id)) {
                int updated_slot = it.getSlots() - 1;
                if (!(updated_slot > 0)) {
                    it.setStatus("Filled");
                }
                it.setSlots(updated_slot);

            }
        }
        
        //save the changes to the internship csv
        FileService.saveInternships(internships);
    }


}
