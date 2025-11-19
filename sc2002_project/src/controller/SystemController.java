package controller;

import UI.LoginUI;
import java.util.List;
import models.*;
import utility.FileService;

/**
 * Central coordinator — manages login, user session, and data loading/saving.
 */
public class SystemController {
    private List<User> users;   // all users (Student, CareerCenterStaff, CompanyRepresentative)
    private UserController userController;
    private InternshipController internshipController;
    private ApplicationController applicationController;
    private final CSSController careerController;
    private final CompanyRepController repController;
    private StudentController studentController;

    // Constructor
    public SystemController() {
        users = new java.util.ArrayList<>();
        this.userController = new UserController();
        this.internshipController = new InternshipController();
        this.applicationController = new ApplicationController();
        this.repController = new CompanyRepController();
        this.careerController = new CSSController(applicationController,repController);
        this.studentController = new StudentController(careerController, internshipController, applicationController, null);
    }

    /**
     * Authenticate user by ID and password
     */
    public User authenticateUser(String id, String password) {
    // reload company reps every time
    List<CompanyRepresentative> reps = FileService.loadCompanyReps();
    
    for (User user : users) { 
        if (user instanceof CompanyRepresentative) {
            for (CompanyRepresentative rep : reps) {
                if (rep.getUserId().equalsIgnoreCase(id) && rep.getPassword().equals(password)) {
                    return rep;
                }
            }
        } else {
            if (user.getUserId().equalsIgnoreCase(id) && user.getPassword().equals(password)) {
                return user;
            }
        }
    }
    return null;
}


    public void updateUserPassword(String userId, String newPassword) {
    for (User u : users) {
        if (u.getUserId().equalsIgnoreCase(userId)) {
            u.setPassword(newPassword);
            break;
        }
    }
}

    /**
     * Initialize system by loading data from CSV files
     */
    public void initializeSystem() {
        List<Student> students = FileService.loadStudents();
        List<CareerCenterStaff> staff = FileService.loadCSStaff();
        List<CompanyRepresentative> reps = FileService.loadCompanyReps();
        
        users.clear();
        users.addAll(students);
        users.addAll(staff);
        users.addAll(reps);

        //test if all accounts are retrived from excel
        System.out.println("============Validation for Account Retrieval==========");
        for (User u : users) {
            System.out.println(u.getUserType() + " " + u.getUserId() + " " + u.getName()+ " "+u.getPassword());
        }
        System.out.println("Accounts in total = "+users.size());
        System.out.println("============End==========");

        //load internship
        List<Internship> internships = FileService.loadInternships();
        internshipController.setInternshipList(internships);
        this.studentController = new StudentController(careerController, internshipController, applicationController, students);

        //Debug print for internships
        System.out.println("============ Validation for Internship Retrieval ============");
        for (Internship i : internships) {
            System.out.println(i.toString());
        }
        System.out.println("Internships in total = " + internships.size());
        System.out.println("============ End ===========");


        // Pass company reps to userController
        // userController.setCompanyReps(reps);
    }

    /**
     * Start the system and display login screen
     */
    public void startSystem() {
        LoginUI loginUI = new LoginUI(this, userController, internshipController, applicationController,careerController,repController, studentController);
        loginUI.displayLoginScreen();
    }

    //Getters
    public List<User> getUsers() {
        return users;
    }

    public UserController getUserController() {
        return userController;
    }

    public InternshipController getInternshipController() {
        return internshipController;
    }

    public ApplicationController getApplicationController() {
        return applicationController;
    }
}
