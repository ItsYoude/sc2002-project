package controller;

import java.util.*;
import models.*;
import utility.FileService;

public class InternshipController {
    private List<Internship> internships = new ArrayList<>();

    public InternshipController() {
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

    // For students: view visible internships by major/year level
    public void viewAllInternships(String major, int yearOfStudy) {
        System.out.println("\n--- Internship Opportunities ---");
        boolean found = false;
        for (Internship i : internships) {
            if (i.isVisible()) {
                if (i.getMajor().equalsIgnoreCase(major) || major.equalsIgnoreCase("All")) {
                    if (yearOfStudy <= 2 && i.getYearType().equalsIgnoreCase("Basic") || yearOfStudy >= 3) {
                        System.out.println(i);
                        found = true;
                    }
                }
            }
        }
        if (!found) System.out.println("No internships available at this time.");
    }

    // For staff: view all internships
    public void viewAllInternships() {
        System.out.println("\n--- All Internships ---");
        if (internships.isEmpty()) {
            System.out.println("No internships available.");
            return;
        }
        for (Internship i : internships) {
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
