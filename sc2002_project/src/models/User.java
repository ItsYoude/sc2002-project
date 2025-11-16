package models;

import java.util.Scanner;

public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String password;
    private Scanner sc;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = "password".toLowerCase().trim();     //default password
        this.sc = new Scanner(System.in);
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public boolean passwordValidator(String input) {
        return password.equals(input);
    }

    public void changePassword() {
        //this.password = newPassword;
        System.out.print("Enter your current password: ");
        String current = sc.nextLine().trim();

        if (!passwordValidator(current)) {
            System.out.println("Incorrect password. Please try again.");
            return;
        }

        System.out.print("Enter your new password: ");
        String new_password = sc.nextLine().trim();
        this.password = new_password;
        System.out.println("Password successfully changed!"+ this.password);
        System.out.println("You will now be logged out. Please login again with your new password.");
    }

    public abstract String getUserType();
}
