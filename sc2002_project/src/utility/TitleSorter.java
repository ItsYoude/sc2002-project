package utility;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import models.Internship;

public class TitleSorter implements IInternshipSorter {
    
    @Override
    public List<Internship> sort(List<Internship> opportunities) {
        // Default sort is by alphabetical order (Title) as mentioned in miscellaneous
        return opportunities.stream()
                .sorted(Comparator.comparing(Internship::getTitle))
                .collect(Collectors.toList());
    }
}