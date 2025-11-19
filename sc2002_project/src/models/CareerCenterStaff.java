package models;

import java.util.List;
import java.util.Scanner;
import utility.FileService;

public class CareerCenterStaff extends User {
    private String department;

    public CareerCenterStaff(String userId, String name, String department, String email, String password) {
        super(userId, name, email, password); 
        this.department = department;
    }

    public String getDepartment() { return department; }

    // --- New Functionalities which will be called by CSScontroller ---
    public void approveInternship(Internship internship) {
        internship.setStatus("Approved");
        System.out.println("Internship " + internship.getId() + " approved.");
    }

    public void rejectInternship(Internship internship) {
        internship.setStatus("Rejected");
        System.out.println("Internship " + internship.getId() + " rejected.");
    }

    public void approveWithdrawRequest(WithdrawRequest request) {
        request.approve();
        System.out.println("Withdrawal request approved for " + request.getStudent().getName());
    }

    public void rejectWithdrawRequest(WithdrawRequest request) {
        request.reject();
        System.out.println("Withdrawal request rejected for " + request.getStudent().getName());
    }

    @Override
    public String getUserType() { return "Career Center Staff"; }

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

    setPassword(newPassword);

    // --- Fix: load all staff, update this staff, save ---
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
