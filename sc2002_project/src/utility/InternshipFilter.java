package utility;

import java.util.List;
import models.Internship;
/**
 * Represents a filter for a list of {@link Internship} objects.
 * 
 * <p>
 * Implementations of this interface define the filtering logic based on
 * criteria such as status, major, level, visibility, etc.
 * </p>
 * 
 * <p>
 * Example usage:
 * <pre>
 * InternshipFilter statusFilter = new StatusFilter("Approved");
 * List&lt;Internship&gt; approvedInternships = statusFilter.filter(allInternships);
 * </pre>
 * </p>
 */
public interface InternshipFilter {
/**
     * Filters a given list of internships according to specific criteria.
     *
     * @param internships the list of {@link Internship} objects to filter
     * @return a new list containing only the internships that match the filter
     */
     List<Internship> filter(List<Internship> internships);

}