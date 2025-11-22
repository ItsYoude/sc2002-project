package controller;

import UI.LoginUI;
import java.util.List;
import models.*;
import utility.FileService;
import utility.FilterManager;

/**
 * Central system controller.
 * Manages user authentication, session, and initializes all controllers and data.
 * Coordinates loading and saving of:
 *  - Students
 *  - CareerCenterStaff
 *  - CompanyRepresentatives
 *  - Internships
 *  - Applications
 *  - WithdrawRequests
 */


public class SystemController {
    /** List of all users (Student, CSS, CompanyRep). */
    private List<User> users; // all users (Student, CareerCenterStaff, CompanyRepresentative)

    /** Controllers used by the system. */
    private UserController userController;
    private InternshipController internshipController;
    private ApplicationController applicationController;
    private final CSSController careerController;
    private final CompanyRepController repController;
    private StudentController studentController;


    /** Filter manager for all filters in system. */
    private final FilterManager filterManager = new FilterManager();

    /** Constructor initializes all controllers and sets up singleton-like references. */
    public SystemController() {
        users = new java.util.ArrayList<>();
        this.userController = new UserController();
        this.internshipController = new InternshipController();
        this.applicationController = new ApplicationController(null);
        this.repController = new CompanyRepController(filterManager);
        this.careerController = new CSSController(applicationController,repController,internshipController,filterManager);
        this.studentController = new StudentController(careerController, internshipController, applicationController,
                null,filterManager);
    }

  

    
    
    
    
    
    
    /** Authenticate user by ID and password. Returns User object, FALSE for wrong password, null if not found. */
    public Object authenticateUser(String id, String password) {
        for (User user : users) {

            // ID matches
            if (user.getUserId().equals(id)) {

                // correct password
                if (user.getPassword().equals(password)) {
                    return user;
                }

                // ID found but password wrong
                return Boolean.FALSE;
            }
        }

        // ID not found at all
        return null;
    }


    /** Update a user’s password by userId. */
    public void updateUserPassword(String userId, String newPassword) {
        for (User u : users) {
            if (u.getUserId().equalsIgnoreCase(userId)) {
                u.setPassword(newPassword);
                break;
            }
        }
    }

    /** Initialize system by loading all CSV data into controllers and lists. */
    public void initializeSystem() {
        List<Student> students = FileService.loadStudents();
        List<CareerCenterStaff> staff = FileService.loadCSStaff();
        List<CompanyRepresentative> reps = FileService.loadCompanyReps();
        users.clear();
        users.addAll(students);
        users.addAll(staff);
        users.addAll(reps);

        //test if all accounts are retrived from excel
        // System.out.println("============Validation for Account Retrieval==========");
        // for (User u : users) {
        //     System.out.println(u.getUserType() + " " + u.getUserId() + " " + u.getName() + " " + u.getPassword());
        // }
        // System.out.println("Accounts in total = " + users.size());
        // System.out.println("============End==========");

        //load internship
        List<Internship> internships = FileService.loadInternships();
        internshipController.setInternshipList(internships);
        this.studentController = new StudentController(careerController, internshipController, applicationController,
                students,filterManager);

        //Debug print for internships
        // System.out.println("============ Validation for Internship Retrieval ============");
        // for (Internship i : internships) {
        //     System.out.println(i.toString());
        // }
        // System.out.println("Internships in total = " + internships.size());
        // System.out.println("============ End ===========");

        // Pass company reps to userController
        // userController.setCompanyReps(reps);
        List<Application> appList = FileService.loadApplications(students, internshipController);
        this.applicationController.setApplicationList(appList);


        // populate WithdrawRequest list
        List<WithdrawRequest> withdraw_list = FileService.loadWithdrawRequests(students, internships);
        this.careerController.setWithdrawlist(withdraw_list);



    }

    /** Start the system by launching the login UI. */

    public void startSystem() {
        LoginUI loginUI = new LoginUI(this, userController, internshipController, applicationController,
                careerController, repController, studentController);
        loginUI.displayLoginScreen();
    }

    /** Get list of all users. */
    public List<User> getUsers() {
        return users;
    }

    /** Get UserController instance. */
    public UserController getUserController() {
        return userController;
    }


    /** Get InternshipController instance. */
    public InternshipController getInternshipController() {
        return internshipController;
    }


    /** Get ApplicationController instance. */
    public ApplicationController getApplicationController() {
        return applicationController;
    }
    
    /** Get list of all users (alias for getUsers). */
    public List<User> getAllUsers() {
        return users;
    }
}
