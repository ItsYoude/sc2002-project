package utility;

import models.Internship;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

public class ClosingDateFilter implements InternshipFilter {
    private final LocalDate date;

    public ClosingDateFilter(LocalDate date) {
        this.date = date;
    }

    @Override
    public List<Internship> filter(List<Internship> internships) {
        if (date == null) return internships;
        return internships.stream()
                .filter(i -> !i.getCloseDate().isAfter(date))
                .collect(Collectors.toList());
    }
}