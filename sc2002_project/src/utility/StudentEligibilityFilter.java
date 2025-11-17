package utility;

import models.Internship;
import models.Student;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Concrete implementation of IEligibilityFilter containing the complex 
 * student eligibility rules (major, level, visibility).
 */
public class StudentEligibilityFilter implements IEligibilityFilter {

    @Override
    public List<Internship> filterEligible(List<Internship> opportunities, Student student) {
        return opportunities.stream()
            // 1. Filter by Visibility (Requirement 4, 58)
            .filter(Internship::isVisible)
            // 2. Filter by Major (Requirement 4, 57)
            .filter(i -> i.getMajor().equalsIgnoreCase(student.getMajor()))
            // 3. Filter by Year/Level Eligibility (Requirement 4, 61)
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