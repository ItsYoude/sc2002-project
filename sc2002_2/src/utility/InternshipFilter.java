package utility;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import models.Internship;
import models.Student;

public class InternshipFilter {

    /**
     * Filters a list of internships by student eligibility:
     * - Major matches
     * - Year type matches student's yearOfStudy
     * - Internship is visible
     */

    public static List<Internship> filterByStatus(List<Internship> internships, String status) {
        return internships.stream()
                .filter(i -> i.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public static List<Internship> filterByMajor(List<Internship> internships, String major) {
        return internships.stream()
                .filter(i -> i.getMajor().equalsIgnoreCase(major))
                .collect(Collectors.toList());
    }

    public static List<Internship> filterByLevel(List<Internship> internships, String level) {
        return internships.stream()
                .filter(i -> i.getYearType().equalsIgnoreCase(level))
                .collect(Collectors.toList());
    }

    public static List<Internship> filterVisible(List<Internship> internships) {
        return internships.stream()
                .filter(Internship::isVisible)
                .collect(Collectors.toList());
    }

    public static List<Internship> sortByTitle(List<Internship> internships) {
        return internships.stream()
                .sorted(Comparator.comparing(Internship::getTitle))
                .collect(Collectors.toList());
    }
}