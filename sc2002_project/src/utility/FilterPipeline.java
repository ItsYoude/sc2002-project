package utility;

import java.util.ArrayList;
import java.util.List;
import models.Internship;

public class FilterPipeline implements InternshipFilter {
    private final List<InternshipFilter> filters = new ArrayList<>();

    public void add(InternshipFilter filter) {
        filters.add(filter);
    }

    public void clear() {
        filters.clear(); // optional, if you want to rebuild for different cases
    }


    @Override
    public List<Internship> filter(List<Internship> internships) {
        List<Internship> result = internships;
        for (InternshipFilter filters : filters) {
            result = filters.filter(result);
        }
        return result;
    }
}