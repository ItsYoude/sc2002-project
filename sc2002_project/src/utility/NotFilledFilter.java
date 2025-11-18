package utility;

import models.Internship;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Keeps only internships that are NOT filled.
 */
public class NotFilledFilter implements InternshipFilter {

    @Override
    public List<Internship> filter(List<Internship> internships) {
        return internships.stream()
                .filter(i -> !i.getStatus().equalsIgnoreCase("Filled"))
                .collect(Collectors.toList());
    }
}
