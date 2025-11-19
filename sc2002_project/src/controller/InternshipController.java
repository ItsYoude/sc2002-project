package controller;

import java.util.*;
import java.util.stream.Collectors;

import models.*;
import utility.ApprovedFilter;
import utility.BeforeDateFilter;
import utility.FileService;



import utility.InternshipFilter; //NEW : Pipelined
import utility.LevelFilter;
import utility.MajorFilter;
import utility.NotFilledFilter;
import utility.StatusFilter;
import utility.FilterPipeline;
import utility.VisibilityFilter;



import utility.IEligibilityFilter;
import utility.IInternshipSorter; // NEW: Abstraction for sorting
import utility.TitleSorter; // NEW: Concrete default sorter



// import utility.IEligibilityFilter; // NEW: Abstraction for eligibility
// import utility.IInternshipSorter; // NEW: Abstraction for sorting
// import utility.StudentEligibilityFilter; // NEW: Concrete default filter


public class InternshipController {
    private List<Internship> internships = new ArrayList<>();

    // NEW DIP FIELDS: These are the abstractions the controller relies on.
    //private final IEligibilityFilter eligibilityFilter;
    private final IInternshipSorter defaultSorter;

    public InternshipController() {
        //this.eligibilityFilter = new StudentEligibilityFilter(); 
        this.defaultSorter = new TitleSorter();
        loadInternships();
    }

    public InternshipController(IInternshipSorter defaultSorter) {
        this.defaultSorter = defaultSorter;
        loadInternships();
    }

    public void loadInternships() {
        internships = FileService.loadInternships();
    }

    // Add internship
    public boolean addInternship(Internship internship) {
        internships.add(internship);
        if (!FileService.saveInternships(internships)) {
            internships.remove(internship); // rollback
            return false;
        }
        return true;
    }

    // public List<Internship> getEligibleInternships(Student student, IInternshipSorter sorter) {
    //     // 1. Filter: Use the injected IEligibilityFilter abstraction (DIP)
    //     List<Internship> eligibleList = eligibilityFilter.filterEligible(this.internships, student);
    //     // 2. Sort: Use the provided sorter abstraction (DIP)
    //     return sorter.sort(eligibleList);
    // }

    //using pipeline


    // Build a fresh pipeline for each method call
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


    public List<Internship> getEligibleInternships(Student student, IInternshipSorter sorter)
    {
        FilterPipeline pipeline = buildPipelineForStudent(student);
        List<Internship> filtered = pipeline.filter(internships);
        return defaultSorter.sort(filtered);
    }
    
    public List<Internship> getEligibleInternships(Student student)
    {
        FilterPipeline pipeline = buildPipelineForStudent(student);
        List<Internship> filtered = pipeline.filter(internships);
        return filtered;  
    }
    
    
    
    
    // For students: view visible internships by major/year level
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

    // For staff: Sorts all internships using the injected defaultSorter before printing.
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

    // Get internship by ID
    public Internship getInternshipById(String id) {
        for (Internship i : internships) {
            if (i.getId().equalsIgnoreCase(id)) {
                return i;
            }
        }
        return null;
    }

    public List<Internship> getAllInternships() {
        return internships;
    }

    public void saveAllInternships() {
        FileService.saveInternships(internships);
    }

    public void setInternshipList(List<Internship> internships) {
        this.internships = internships;
    }

    public List<Internship> getPendingInternships() {
        // Filter the main list where the status is "Pending"
        return internships.stream()
                .filter(i -> i.getStatus().equalsIgnoreCase("Pending"))
                .collect(Collectors.toList());
    }

}
