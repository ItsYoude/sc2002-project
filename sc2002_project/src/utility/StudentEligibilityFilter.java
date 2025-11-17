package utility;

import models.Internship;
import models.Student;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Concrete implementation of IEligibilityFilter containing the complex 
 * student eligibility rules (major, level, visibility).
 */
public class StudentEligibilityFilter implements IEligibilityFilter {

    @Override
    public List<Internship> filterEligible(List<Internship> opportunities, Student student) {
        LocalDate today = LocalDate.now();
        return opportunities.stream()
            // 1. MUST NOT be "Filled" (New Check based on rule)
            .filter(i -> !i.getStatus().equalsIgnoreCase("Filled"))
            // 2. MUST be "Approved" status (Hides Pending/Rejected)
            .filter(i -> i.getStatus().equalsIgnoreCase("Approved"))
            // 3. MUST NOT be past the closing date (New Check based on rule)
            // Assuming Internship has getClosingDate() returning a LocalDate or similar object.
            .filter(i -> i.getCloseDate().isAfter(today) || i.getCloseDate().isEqual(today))
            // 4. MUST have Visibility toggled "on" [
            .filter(Internship::isVisible)
            // 5. MUST match the student's Major
            .filter(i -> i.getMajor().equalsIgnoreCase(student.getMajor()))
            // 6. MUST match the student's Year/Level Eligibility [cite: 61]
            .filter(i -> isLevelEligible(i, student))
            
            .collect(Collectors.toList());
    }
    
    // Helper method to enforce the Basic/Intermediate/Advanced rule
    private boolean isLevelEligible(Internship i, Student s) {
        // Year 3 and above students can apply for any level
        if (s.getYearOfStudy() >= 3) {
            return true;
        } else {
            // Year 1 and 2 students can ONLY apply for Basic-level internships
            return i.getLevel().equalsIgnoreCase("Basic");
        }
    }
}