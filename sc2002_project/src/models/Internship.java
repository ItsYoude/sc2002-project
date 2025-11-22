package models;

import java.time.LocalDate;

/**
 * Represents an Internship opportunity posted by a company.
 * Contains all relevant information such as title, company, eligibility, dates, slots, and status.
 */

public class Internship {
    private final String id;
    private final String title;
    private final String company;
    private final String representativeId;  //companyrep user ID
    private final String major;
    private final String yearType;          // Basic / Intermediate / Advanced
    private final String description;   
    private final LocalDate openDate;
    private final LocalDate closeDate;
    private String status;                 // Pending / Approved / Rejected / Filled
    private int slots;                     // Max 10
    private boolean visible;


    /**
     * Constructor for Internship.
     *
     * @param id                Unique internship ID
     * @param title             Internship title
     * @param company           Name of the company
     * @param representativeId  User ID of the company representative
     * @param major             Target major for the internship
     * @param yearType          Eligibility level (Basic/Intermediate/Advanced)
     * @param description       Description of the internship
     * @param openDate          Date applications open
     * @param closeDate         Date applications close
     * @param slots             Number of available slots
     * @param visible           Whether the internship is visible to students
     * @param status            Current status (Pending/Approved/Rejected/Filled)
     */

    public Internship(String id, String title, String company, String representativeId, String major, String yearType, String description, LocalDate openDate, LocalDate closeDate,
                      int slots, boolean visible, String status) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.representativeId = representativeId;
        this.major = major;
        this.yearType = yearType;
        this.description = description;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.slots = slots;
        this.visible = visible;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getRepresentativeId() { 
        return representativeId; 
    }

    public String getMajor() {
        return major;
    }

    public String getYearType() {
        return yearType;
    }
    public String getDescription() { 
        return description; 
    }
    public LocalDate getOpenDate() { 
        return openDate; 
    }
    public LocalDate getCloseDate() { 
        return closeDate; 
    }
    public String getStatus() { 
        return status; 
    }
    public int getSlots() { 
        return slots; 
    }
    public boolean isVisible() {
        return visible;
    }

      /** Setters */
    public void setStatus(String status) { 
        this.status = status; 
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }
    
    /** Toggle visibility on/off */
    public void toggleVisibility() {
        visible = !visible;
    }


    /** Returns a full string representation of internship for staff/admin view */
    @Override
    public String toString() {
        return String.format(
                "[%s] %s (%s) - %s | Level: %s | Major: %s | Open Date : %s | Close Date : %s | Slots: %d | Status: %s | Visible: %s",
                id, title, company, description, yearType, major, openDate, closeDate, slots, status,
                visible ? "Yes" : "No");
    }
    
    /** Alias for yearType (for filtering convenience) */
    public String getLevel() {
        return yearType;
    }

    /** Returns a simplified string for student view (excludes status/visibility) */
   public String view_student() {
        return String.format("[%s] %s (%s) - %s | Level: %s | Major: %s | Open Date : %s | Close Date : %s | Slots: %d |",
                id, title, company, description, yearType, major,openDate,closeDate, slots);
    }


}
/** Returns a full string representation of internship for staff/admin view */