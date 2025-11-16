package models;

public class CompanyRepresentative extends User {
    private String companyName;
    private String department;
    private String position;
    private String status;
    //private boolean approved = false;

    public CompanyRepresentative(String userId, String name, String companyName, String department, String position, String email,String status) {
        super(userId, name, email); // default password
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
}
