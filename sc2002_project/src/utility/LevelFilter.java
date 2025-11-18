package utility;

import java.util.List;
import java.util.stream.Collectors;
import models.Internship;

public class LevelFilter implements InternshipFilter {
    private final int studentYear;

    public LevelFilter(int studentYear) {
        this.studentYear = studentYear;
    }

    @Override
    public List<Internship> filter(List<Internship> internships) {
        return internships.stream()
                .filter(i -> {
                    if (studentYear >= 3) return true; // upper year can apply any
                    return i.getYearType().equalsIgnoreCase("Basic"); // lower year only basic
                })
                .collect(Collectors.toList());
    }
}