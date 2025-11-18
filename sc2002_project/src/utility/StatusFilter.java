package utility;

import java.util.List;
import java.util.stream.Collectors;
import models.Internship;

public class StatusFilter implements InternshipFilter {
    
    private final String status;

    public StatusFilter(String status) {
        this.status = status;
    }

    @Override
    public List<Internship> filter(List<Internship> internships) {
        return internships.stream()
                .filter(i -> i.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
    

}