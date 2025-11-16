package models;

import java.time.LocalDate;

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

    //setter
    public void setStatus(String status) { 
        this.status = status; 
    }
    public void setSlots(int slots) { 
        this.slots = slots; 
    }
    public void toggleVisibility() {
        visible = !visible;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - %s | Level: %s | Major: %s | Slots: %d | Status: %s | Visible: %s",
                id, title, company, description, yearType, major, slots, status, visible ? "Yes" : "No");
    }
}
