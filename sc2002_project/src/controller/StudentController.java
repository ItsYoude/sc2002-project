package controller;

import java.util.*;
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
import utility.VisibilityFilter;
/**
manage actions Student can perform
    - View available internships
    - Apply for internships
    - View their applications
    - Accept offers
    - Request withdrawals
    - Filter internships
Coordinate between:
    - Student model
    - InternshipController (for available internships)
    - ApplicationController (for applying, withdrawing)
    - FileService (for saving changes)
*/
public class StudentController {
    /** Singleton instance. */
    private static StudentController instance;
    /** Controllers used by StudentController. */
    private final CSSController cssController;
    private final InternshipController internshipController;
    private final ApplicationController applicationController;

    /** List of all students. */
    private static List<Student> students; // all students loaded from CSV

    /** Filter manager for student internship filters. */
    private final FilterManager filterManager;

    
    /**
     * Constructor initializes StudentController and sets singleton instance.
     */
    public StudentController(CSSController cssController, InternshipController internshipController,
                             ApplicationController applicationController,
                             List<Student> students,FilterManager filterManager) {
        this.cssController = cssController;
        this.internshipController = internshipController;
        this.applicationController = applicationController;
        this.students = students;
        instance = this;
        this.filterManager = filterManager;
    }

    /**
     * View available internships for a student (filtered by major/year)
     */
    public List<Internship> viewAvailableInternships(Student student) {
        List<Internship> all = internshipController.getAllInternships();

        return all.stream()
                .filter(Internship::isVisible)
                .filter(i -> i.getMajor().equalsIgnoreCase(student.getMajor()))
                .filter(i -> student.isLevelAllowed(i.getYearType()))
                .collect(Collectors.toList());
    }
    

    /** Returns the singleton instance. */
    public static StudentController getInstance() {
        return instance;
    }

    /** Apply for an internship for a student. */
    public boolean applyForInternship(Student student, String internshipId) {
        Internship internship = internshipController.getInternshipById(internshipId);

        if (internship == null) {
            System.out.println("Internship not found.");
            return false;
        }

        if (!student.canApplyMore()) {
            System.out.println("Cannot apply for more than 3 internships.");
            return false;
        }

        if (!student.isLevelAllowed(internship.getYearType())) {
            System.out.println("You are not eligible based on your year.");
            return false;
        }

        //Apply through ApplicationController
        applicationController.apply(student, internship);
        //Update student's applied list
        student.applyInternship(internshipId, internshipController);
        //Persist changes
        FileService.saveStudents(students);

        System.out.println("Applied successfully to " + internship.getTitle());
        return true;
    }


    /** Request withdrawal from an internship. */
    public void requestWithdrawal(Student student, Internship internship, String reason) {
        WithdrawRequest request = new WithdrawRequest(student, internship, reason);
        if (!(cssController.checkIfExist(request)))
        {
            cssController.addWithdrawRequest(request); // <-- Add centrally
            System.out.println("Withdrawal request submitted for internship " + internship.getTitle());
        }
        else
        {
            System.out.println("You already submitted an Withdrawal request for "+ internship.getTitle());
        }


    }

    /**
     * Accept an internship offer
     */ 
    public boolean acceptOffer(Student student, String internshipId) {
        // Check if the student has already accepted an internship
        if (student.getAcceptedInternshipId() != null) {
            System.out.println("You have already accepted an internship.");
            return false;
        }
        // Find the internship
        Internship internship = internshipController.getInternshipById(internshipId);
        if (internship == null) {
            System.out.println("Internship not found.");
            return false;
        }
        // Get the application for this internship
        Application app = applicationController.getApplication(student, internship);
        if (app == null) {
            System.out.println("You have not applied for this internship.");
            return false;
        }

        // Only successful applications can be accepted
        if (!app.getStatus().equalsIgnoreCase("Successful")) {    //need change to successful
            System.out.println("Only successful applications can be accepted.");
            return false;
        }

        // Accept this placement

        app.setStatus("Accepted");
        student.acceptInternship(internshipId);

        // Withdraw all other active applications
        List<Application> studentApps = applicationController.getApplicationsByStudent(student);
        for (Application otherApp : studentApps) {
            if (!otherApp.getInternship().getId().equalsIgnoreCase(internshipId) &&
                    !otherApp.getStatus().equalsIgnoreCase("Withdrawn") &&
                    !otherApp.getStatus().equalsIgnoreCase("Accepted") &&
                    !otherApp.getStatus().equalsIgnoreCase("Unsuccessful")) {
                otherApp.setStatus("Withdrawn");
          

            }
        }

        
        student.withdrawAllExcept(internshipId);
        List<Student> student_list = StudentController.getAllStudents();
        FileService.saveStudents(student_list);
        internshipController.decrementSlotsAfterAcceptance(internshipId);


        System.out.println(
                "Offer accepted for " + internship.getTitle() + ". All other applications have been withdrawn.");
        
        return true;
    }

    /** View all applications made by the student. */

    public void viewMyApplications(Student student) {
        applicationController.viewApplications(student);
    }

    /** Find a student by their ID. */

    public Student getStudentById(String studentId) {
        return students.stream()
                .filter(s -> s.getUserId().equalsIgnoreCase(studentId))
                .findFirst()
                .orElse(null);
    }

    /** View all withdrawal requests submitted by the student. */

    public void getAllWithdrawApplications(Student student)
    {
        List<WithdrawRequest> b = cssController.getWithdrawalRequestsForStudent(student);
        if (!b.isEmpty())
        {
            System.out.println("--- List of Withdraw Applications ----");
            for (WithdrawRequest a : b) {
                System.out.println(a);
            }
            return;
        }
        System.out.println("You have not submitted any Withdraw Applications.");
    }

    /** Returns the list of all students. */
    public static List<Student> getAllStudents() {
        return students;
    }


    /** Returns filtered internships based on saved user filter settings. */
    public List<Internship> getFilteredInternships(String userId, List<Internship> all) {
        UserFilterSettings settings = filterManager.getFilters(userId);

        FilterPipeline pipeline = new FilterPipeline();

        if (settings != null) {
            pipeline.add(new StatusFilter(settings.getStatus()));
            pipeline.add(new LevelFilter(settings.getLevel()));
            pipeline.add(new MajorFilter(settings.getMajor()));
            pipeline.add(new ClosingDateFilter(settings.getClosingBefore()));
            // add other filters as needed
            //students should not be able to see non visible
            pipeline.add(new VisibilityFilter());
        }

        // Always add default sorting (alphabetical)
        pipeline.add(internships -> {
            internships.sort(Comparator.comparing(Internship::getTitle, String.CASE_INSENSITIVE_ORDER));
            return internships;
        });

        return pipeline.filter(all);

    }
    

    /** Save filter settings for a student. */
    public void saveUserFilterSettings(Student student, UserFilterSettings settings) {
        filterManager.setFilters(student.getUserId(), settings);
    }

    
    /** Retrieve previous filter settings for a student. */
    public UserFilterSettings getPreviousFilter(String userId)
    {
        return filterManager.getFilters(userId);

    }







}