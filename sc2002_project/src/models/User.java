package models;

import java.util.Scanner;
/**
 * Abstract base class representing a generic system user.
 * Provides common fields such as userId, name, email, and password.
 * Concrete subclasses must implement getUserType() to define their role.
 */

public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String password;
    private Scanner sc;

     /**
     * Constructs a new User
     * 
     * @param userId   Unique user ID
     * @param name     Full name
     * @param email    Email address
     * @param password Password
     */

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;     
        this.sc = new Scanner(System.in);
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Validates a given input against the stored password
     * 
     * @param input Password input to validate
     * @return true if input matches password, false otherwise
     */
    public boolean passwordValidator(String input) {
        return password.equals(input);
    }
    /**
     * Returns the type of the user.
     * Must be implemented by subclasses (e.g., Student, CompanyRepresentative, CareerCenterStaff)
     * 
     * @return String representing the user type
     */
    public abstract String getUserType();
}
