package utility;

import models.Internship;
import models.Student;
import java.util.List;

public interface IEligibilityFilter {
    /**
     * Filters a list of opportunities based on student-specific eligibility 
     * criteria (major, year level, visibility).
     * @param opportunities The complete list of internships.
     * @param student The student whose profile dictates eligibility.
     * @return A list containing only eligible and visible internships.
     */
    List<Internship> filterEligible(List<Internship> opportunities, Student student);
}