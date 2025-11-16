package models;

public class CareerCenterStaff extends User {
    private String department;

    public CareerCenterStaff(String userId, String name, String department, String email) {
        super(userId, name, email); // default password
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
}
