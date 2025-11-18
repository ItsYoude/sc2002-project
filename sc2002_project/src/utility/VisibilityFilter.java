package utility;

import java.util.List;
import java.util.stream.Collectors;
import models.Internship;

public class VisibilityFilter implements InternshipFilter {
    @Override
    public List<Internship> filter(List<Internship> internships) {
        return internships.stream()
                .filter(Internship::isVisible)
                .collect(Collectors.toList());
    }
}
