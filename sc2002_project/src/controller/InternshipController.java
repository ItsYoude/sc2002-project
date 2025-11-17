package controller;

import java.util.*;
import models.*;
import utility.FileService;
import utility.IEligibilityFilter; // NEW: Abstraction for eligibility
import utility.IInternshipSorter; // NEW: Abstraction for sorting
import utility.StudentEligibilityFilter; // NEW: Concrete default filter
import utility.TitleSorter; // NEW: Concrete default sorter

public class InternshipController {
    private List<Internship> internships = new ArrayList<>();

    // NEW DIP FIELDS: These are the abstractions the controller relies on.
    private final IEligibilityFilter eligibilityFilter;
    private final IInternshipSorter defaultSorter;

    public InternshipController() {
        this.eligibilityFilter = new StudentEligibilityFilter(); 
        this.defaultSorter = new TitleSorter();
        loadInternships();
    }
    public InternshipController(IEligibilityFilter eligibilityFilter, IInternshipSorter defaultSorter) {
        this.eligibilityFilter = eligibilityFilter;
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

    public List<Internship> getEligibleInternships(Student student, IInternshipSorter sorter) {
        // 1. Filter: Use the injected IEligibilityFilter abstraction (DIP)
        List<Internship> eligibleList = eligibilityFilter.filterEligible(this.internships, student);
        // 2. Sort: Use the provided sorter abstraction (DIP)
        return sorter.sort(eligibleList);
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

}
