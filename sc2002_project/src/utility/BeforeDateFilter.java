package utility;

import models.Internship;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BeforeDateFilter implements InternshipFilter {

    private final LocalDate today;

    public BeforeDateFilter() {
        this.today = LocalDate.now();
    }

    @Override
    public List<Internship> filter(List<Internship> internships) {
        return internships.stream()
            .filter(i -> {
                LocalDate closeDate = i.getCloseDate();
                return closeDate.isAfter(today) || closeDate.isEqual(today);
            })
            .collect(Collectors.toList());
    }
}
