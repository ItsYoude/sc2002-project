package utility;

import java.time.LocalDate;

//generic filtering settings
public class UserFilterSettings {
    private String status;      // e.g., "Approved"
    private String major;       // for students / applicable users
    private String level;       // Year type
    private LocalDate closingBefore;

    // Getters and setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public LocalDate getClosingBefore() { return closingBefore; }
    public void setClosingBefore(LocalDate closingBefore) { this.closingBefore = closingBefore; }
}
