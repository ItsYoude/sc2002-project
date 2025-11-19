package models;

import controller.CompanyRepController;
import controller.SystemController;
import java.util.Scanner;

public class CompanyRepresentative extends User {
    private String companyName;
    private String department;
    private String position;
    private String status;

    private SystemController systemController;
    //private boolean approved = false;

    public CompanyRepresentative(String userId, String name, String companyName, String department, String position, String email,String status, String password) {
        super(userId, name, email, password); 
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.status = status;
    }

    public String getCompanyName(){
        return companyName;
    }
    public String getDepartment(){
        return department;
    }
    public String getPosition(){
        return position;
    }
    public String getStatus(){
        return status;
    }
    public void approve(){
        this.status = "Approved";
    }

    public void reject() {
        this.status = "Rejected";
    }

    public boolean isApproved() 
    {
        if (status.equalsIgnoreCase("Approved"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

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
