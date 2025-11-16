package utility;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import models.*;

public class FileService {


    //caven version
    // private static final String STUDENT_DATA_FILE = "src/data/sample_student_list.csv";
    // private static final String CSS_STAFF_DATA_FILE = "src/data/sample_staff_list.csv";
    // private static final String COMPANY_REP_DATA_FILE = "src/data/sample_company_representative_list.csv";
    // private static final String INTERNSHIP_DATA_FILE = "src/data/internship_list.csv";

  
    //youde version
    private static final String STUDENT_DATA_FILE = "data/sample_student_list.csv";
    private static final String COMPANY_REP_DATA_FILE = "data/sample_company_representative_list.csv";
    private static final String CSS_STAFF_DATA_FILE = "data/sample_staff_list.csv";
    private static final String INTERNSHIP_DATA_FILE = "data/internship_list.csv";
  
    // Load students
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_DATA_FILE))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;

                String id = parts[0].trim();
                String name = parts[1].trim();
                String major = parts[2].trim();
                int year = Integer.parseInt(parts[3].trim());
                String email = parts[4].trim();

                Student student = new Student(id, name, year, major, email);
                students.add(student);
            }
        } catch (IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
        return students;
    }
    //save students
    public static boolean saveStudents(List<Student> students) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STUDENT_DATA_FILE))) {
            // write header
            bw.write("StudentID,Name,Major,Year,Email,AppliedInternships,AcceptedInternship");
            bw.newLine();

            for (Student s : students) {
                String applied = String.join(";", s.getAppliedInternshipId()); // separate multiple IDs by ;
                String accepted = s.getAcceptedInternshipId() != null ? s.getAcceptedInternshipId() : "";
                
                bw.write(String.join(",",
                        s.getUserId(),
                        s.getName(),
                        s.getMajor(),
                        String.valueOf(s.getYearOfStudy()),
                        s.getEmail(),
                        applied,
                        accepted));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
            return false;
        }
    }

    //load company representative
    public static List<CompanyRepresentative> loadCompanyReps() {
        List<CompanyRepresentative> repList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(COMPANY_REP_DATA_FILE))) {
            String line = br.readLine(); //skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 7)
                    continue; //invalid row
                String id = parts[0].trim();
                String name = parts[1].trim();
                String companyName = parts[2].trim();
                String department = parts[3].trim();
                String position = parts[4].trim();
                String email = parts[5].trim();
                String status = parts[6].trim();

                CompanyRepresentative rep = new CompanyRepresentative(id, name, companyName, department, position,
                        email, status);

                repList.add(rep);
            }
        } catch (IOException e) {
            System.out.println("Error in loading company representatives: " + e.getMessage());
        }
        return repList;
    }
    
    // Save all company reps (pending + approved + rejected), if success will return true, else false.
    //false can be because excel is opened when program is trying to save new data into it.
      
    public static boolean saveCompanyReps(List<CompanyRepresentative> reps) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(COMPANY_REP_DATA_FILE))) {
            // write header
            bw.write("CompanyRepID,Name,CompanyName,Department,Position,Email,Status");
            bw.newLine();

            for (CompanyRepresentative rep : reps) {
                bw.write(String.join(",",
                        rep.getUserId(),
                        rep.getName(),
                        rep.getCompanyName(),
                        rep.getDepartment(),
                        rep.getPosition(),
                        rep.getEmail(),
                        rep.getStatus()));
                bw.newLine();
            }
              return true;
        } catch (IOException e) {
            System.out.println("Error saving company representatives: " + e.getMessage());
            return false;
        }
    }

    // Load career center staff
    public static List<CareerCenterStaff> loadCSStaff() {
        List<CareerCenterStaff> staffList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSS_STAFF_DATA_FILE))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 4) continue;

                String id = parts[0].trim();
                String name = parts[1].trim();
                String department = parts[2].trim();
                String email = parts[3].trim();

                CareerCenterStaff staff = new CareerCenterStaff(id, name, department, email);
                staffList.add(staff);
            }
        } catch (IOException e) {
            System.out.println("Error loading staff: " + e.getMessage());
        }
        return staffList;
    }

    //load internship
    public static List<Internship> loadInternships() {
        List<Internship> internships = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INTERNSHIP_DATA_FILE))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 12) continue;

                String id = parts[0].trim();
                String title = parts[1].trim();
                String company = parts[2].trim();
                String representativeId = parts[3].trim();
                String major = parts[4].trim();
                String yearType = parts[5].trim();
                String description = parts[6].trim();
                LocalDate openDate = LocalDate.parse(parts[7].trim());
                LocalDate closeDate = LocalDate.parse(parts[8].trim());
                int slots = Integer.parseInt(parts[9].trim());
                boolean visible = Boolean.parseBoolean(parts[10].trim());
                String status = parts[11].trim();

                Internship internship = new Internship(
                    id, title, company, representativeId, major, yearType,
                    description, openDate, closeDate, slots, visible, status
                );
                internships.add(internship);
            }
        } catch (IOException e) {
            System.out.println("Error loading internships: " + e.getMessage());
        }
        return internships;
    }

    //save internship
    public static boolean saveInternships(List<Internship> internships) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(INTERNSHIP_DATA_FILE))) {
            // Write header
            bw.write("id,title,company,representativeId,major,yearType,description,openDate,closeDate,slots,visible,status");
            bw.newLine();

            for (Internship i : internships) {
                bw.write(String.join(",",
                        i.getId(),
                        i.getTitle(),
                        i.getCompany(),
                        i.getRepresentativeId(),
                        i.getMajor(),
                        i.getYearType(),
                        i.getDescription().replace(",", " "), // prevent CSV issues
                        i.getOpenDate().toString(),
                        i.getCloseDate().toString(),
                        String.valueOf(i.getSlots()),
                        String.valueOf(i.isVisible()),
                        i.getStatus()
                ));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving internships: " + e.getMessage());
            return false;
        }
    }



}
