package utility;

import java.util.ArrayList;
import java.util.List;
import models.Internship;

/**
 * Represents a pipeline of multiple {@link InternshipFilter} instances.
 * 
 * <p>
 * Allows chaining multiple filters together. When filtering a list of internships,
 * each filter in the pipeline is applied sequentially, and the output of one
 * filter becomes the input of the next.
 * </p>
 * 
 * <p>
 * Example usage:
 * <pre>
 * FilterPipeline pipeline = new FilterPipeline();
 * pipeline.add(new StatusFilter("Approved"));
 * pipeline.add(new MajorFilter("CS"));
 * List&lt;Internship&gt; filtered = pipeline.filter(allInternships);
 * </pre>
 * </p>
 */

public class FilterPipeline implements InternshipFilter {
    private final List<InternshipFilter> filters = new ArrayList<>();


        /**
     * Adds a new filter to the pipeline.
     *
     * @param filter the {@link InternshipFilter} to add
     */
    public void add(InternshipFilter filter) {
        filters.add(filter);
    }
    /**
     * Clears all filters from the pipeline.
     */
    public void clear() {
        filters.clear(); // optional, if you want to rebuild for different cases
    }


        /**
        * Applies all filters in the pipeline to a given list of internships.
        * The filters are applied sequentially.
        *
        * @param internships the list of {@link Internship} to filter
        * @return the filtered list of internships
        */
    
    @Override
    public List<Internship> filter(List<Internship> internships) {
        List<Internship> result = internships;
        for (InternshipFilter filters : filters) {
            result = filters.filter(result);
        }
        return result;
    }
}