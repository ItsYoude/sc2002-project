package utility;

import java.util.List;
import java.util.stream.Collectors;
import models.Internship;

public class MajorFilter implements InternshipFilter {
    private final String major;

    public MajorFilter(String major) {
        this.major = major;
    }

    @Override
    public List<Internship> filter(List<Internship> internships) {
        if (major.equalsIgnoreCase("All"))
            return internships;
        return internships.stream()
                .filter(i -> i.getMajor().equalsIgnoreCase(major))
                .collect(Collectors.toList());
    }
}