package utility;

import java.util.List;
import java.util.stream.Collectors;
import models.Internship;


public class LevelFilter implements InternshipFilter {
    private final Integer studentYear;  // Integer so null is possible
    private final String level;

    // Student constructor
    public LevelFilter(int studentYear) {
        this.studentYear = studentYear;
        this.level = null;
    }

    // Staff constructor
    public LevelFilter(String level) {
        this.level = level;
        this.studentYear = null;
    }

    @Override
    public List<Internship> filter(List<Internship> internships) {
        if (level != null) {
            if (level.equalsIgnoreCase("All"))
                return internships;
            return internships.stream()
                    .filter(i -> i.getYearType().equalsIgnoreCase(level))
                    .collect(Collectors.toList());
        }
        // return internships.stream()
        //         .filter(i -> {
        //             if (studentYear >= 3)
        //                 return !i.getYearType().equalsIgnoreCase("Basic"); // can see Intermediate + Advanced
        //             return i.getYearType().equalsIgnoreCase("Basic");
        //         })
        //         .collect(Collectors.toList());
        

        
        //return all 
        if (studentYear == 3 || studentYear == 4)
        {
            return internships;
        }
        //for year < 3 return basic only 
        return internships.stream()
        .filter(i -> {
            if (studentYear == 1 || studentYear == 2) {
                // Only BASIC allowed
                return i.getYearType().equalsIgnoreCase("Basic");
                    } else if (studentYear == 3 || studentYear == 4) {
                // // Intermediate + Advanced allowed
                // return i.getYearType().equalsIgnoreCase("Intermediate")
                //     || i.getYearType().equalsIgnoreCase("Advanced");
            }
            return false; // in case of invalid year
        })
        .collect(Collectors.toList());
    }
}













// public class LevelFilter implements InternshipFilter {
//     private final int studentYear;
//     private final String level;

//     public LevelFilter(int studentYear) {
//         this.studentYear = studentYear;
//     }

//     public LevelFilter(String level)
//     {
//         this.level = level;
//     }

//     @Override
//     public List<Internship> filter(List<Internship> internships) {

//         if (level == null)
//         {

//             return internships.stream()
//                     .filter(i -> {
//                         if (studentYear >= 3)
//                             return true; // upper year can apply any
//                         return i.getYearType().equalsIgnoreCase("Basic"); // lower year only basic
//                     })
//                     .collect(Collectors.toList());
//         }
//         else
//         {
//             return internships.stream()
//                     .filter(i -> {
//                         return i.getYearType().equalsIgnoreCase(level); // lower year only basic
//                     })
//                     .collect(Collectors.toList());
//         }
//     }
// }


