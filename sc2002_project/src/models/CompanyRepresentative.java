package models;

import controller.CompanyRepController;
import controller.SystemController;
import java.util.Scanner;


/**
 * Represents a Company Representative user.
 * Can manage internships and be approved/rejected by Career Center Staff.
 */

public class CompanyRepresentative extends User {
    private String companyName;
    private String department;
    private String position;
    private String status;

    private SystemController systemController;
    //private boolean approved = false;
    private boolean max_created;

    /**
     * Constructor to create a Company Representative.
     *
     * @param userId      Unique user ID
     * @param name        Representative's name
     * @param companyName Name of the company
     * @param department  Department within the company
     * @param position    Position held
     * @param email       Representative's email
     * @param status      Current approval status (Pending/Approved/Rejected)
     * @param password    Login password
     */
    public CompanyRepresentative(String userId, String name, String companyName, String department, String position,
            String email, String status, String password) {
        super(userId, name, email, password);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.status = status;
    }

        /** Get the company name */
        public String getCompanyName() {
            return companyName;
        }
        /** Get the department name */

        public String getDepartment() {
            return department;
        }
    
            /** Get the position */

            public String getPosition() {
                return position;
            }
                /** Get current approval status */

                public String getStatus() {
                    return status;
                }
    
               /** Approve this representative */
    
               public void approve() {
                   this.status = "Approved";
               }
        /** Reject this representative */


        public void reject() {
            this.status = "Rejected";
        }
    
    /** Check if representative is approved */
    public boolean isApproved() 
    {
        if (status.equalsIgnoreCase("Approved")) {
            return true;
        } else {
            return false;
        }
    }
    
    /** Get max_created flag */

    public  boolean getMaxCreated()
    {
        return max_created;
    }

        /** Set max_created flag */

    public void setMaxCreated(boolean status)
    {
        max_created = status;
    }

        /** Returns user type as string */
    @Override
    public String getUserType() { return "Company Representative"; }

    public boolean changePassword() {
    Scanner sc = new Scanner(System.in);

    System.out.print("Enter current password: ");
    String current = sc.nextLine().trim();

    if (!getPassword().equals(current)) {
        System.out.println("Incorrect password.");
        return false;
    }

    System.out.print("Enter new password: ");
    String newPassword = sc.nextLine().trim();

    if (newPassword.isEmpty()) {
        System.out.println("New password cannot be empty.");
        return false;
    }

    if (newPassword.equals(current)) {
        System.out.println("New password cannot be the same as the old password.");
        return false;
    }

    setPassword(newPassword);

    // Save all reps to CSV
    CompanyRepController controller = CompanyRepController.getInstance();
    if (controller != null && controller.saveAllReps()) {
        System.out.println("Password successfully changed!");
        return true;
    } else {
        System.out.println("Error saving password to CSV.");
        return false;
    }
}

}
