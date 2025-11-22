package models;

import java.util.List;
import java.util.Scanner;
import utility.FileService;

/**
 * Represents a Career Center Staff user.
 * Can manage internships and student withdrawal requests.
 */

public class CareerCenterStaff extends User {
     /** Department of the staff */
     private String department;

    
    /**
     * Constructor to create a CareerCenterStaff.
     * 
     * @param userId   Unique user ID
     * @param name     Staff name
     * @param department Department name
     * @param email    Staff email
     * @param password Staff password
     */
    public CareerCenterStaff(String userId, String name, String department, String email, String password) {
        super(userId, name, email, password);
        this.department = department;
    }

     /** Get the department of this staff */
    public String getDepartment() { return department; }

    /** Approve a given internship */
    public void approveInternship(Internship internship) {
        internship.setStatus("Approved");
        System.out.println("Internship " + internship.getId() + " approved.");
    }

    /** Reject a given internship */
    public void rejectInternship(Internship internship) {
        internship.setStatus("Rejected");
        System.out.println("Internship " + internship.getId() + " rejected.");
    }

    /** Approve a student's withdrawal request */
    public void approveWithdrawRequest(WithdrawRequest request) {
        request.approve();
        System.out.println("Withdrawal request approved for " + request.getStudent().getName());
    }

    /** Reject a student's withdrawal request */
    public void rejectWithdrawRequest(WithdrawRequest request) {
        request.reject();
        System.out.println("Withdrawal request rejected for " + request.getStudent().getName());
    }


    /** Get user type for display */
    @Override
    public String getUserType() {
        return "Career Center Staff";
    }

    

    /**
     * Change the password for this staff.
     * Validates current password, non-empty new password, and updates CSV.
     * 
     * @return true if password changed successfully, false otherwise
     */
    public boolean changePassword() {
    Scanner sc = new Scanner(System.in);

    System.out.print("Enter your current password: ");
    String current = sc.nextLine().trim();

    if (!getPassword().equals(current)) {
        System.out.println("Incorrect password. Please try again.");
        return false;
    }

    System.out.print("Enter your new password: ");
    String newPassword = sc.nextLine().trim();

    // Empty password check
    if (newPassword.isEmpty()) {
        System.out.println("New password cannot be empty.");
        return false;
    }

    // Same password check
    if (newPassword.equals(current)) {
        System.out.println("New password cannot be the same as the old password.");
        return false;
    }

    // Set new password in memory
    setPassword(newPassword);

    List<CareerCenterStaff> allStaff = FileService.loadCSStaff();
    for (CareerCenterStaff staff : allStaff) {
        if (staff.getUserId().equals(this.getUserId())) {
            staff.setPassword(newPassword); // update password
            break;
        }
    }

    if (FileService.saveStaff(allStaff)) {
        System.out.println("Password successfully changed!");
        System.out.println("Please login again with your new password.");
        return true;
    } else {
        System.out.println("Error saving password. Try again later.");
        return false;
    }
}

}
