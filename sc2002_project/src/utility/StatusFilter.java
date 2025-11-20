package utility;

import java.util.List;
import java.util.stream.Collectors;
import models.Internship;

public class StatusFilter implements InternshipFilter {
    
    private final String status;

    public StatusFilter(String statuss) {
            this.status = statuss;
        }
    

    @Override
    public List<Internship> filter(List<Internship> internships) {
        if (status.equalsIgnoreCase("All"))
            return internships;
        return internships.stream()
                .filter(i -> i.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
    

}