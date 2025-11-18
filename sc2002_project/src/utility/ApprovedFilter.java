package utility;

import models.Internship;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Keeps only internships with status "Approved".
 */
public class ApprovedFilter implements InternshipFilter {

    @Override
    public List<Internship> filter(List<Internship> internships) {
        return internships.stream()
                .filter(i -> i.getStatus().equalsIgnoreCase("Approved"))
                .collect(Collectors.toList());
    }
}
