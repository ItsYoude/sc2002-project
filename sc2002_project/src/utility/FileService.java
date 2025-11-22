package utility;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import controller.InternshipController;
import models.*;

public class FileService {

    private static final String STUDENT_DATA_FILE = "src/data/sample_student_list.csv";
    private static final String CSS_STAFF_DATA_FILE = "src/data/sample_staff_list.csv";
    private static final String COMPANY_REP_DATA_FILE = "src/data/sample_company_representative_list.csv";
    private static final String INTERNSHIP_DATA_FILE = "src/data/internship_list.csv";
    private static final String WITHDRAW_DATA_FILE = "src/data/withdraw_list.csv";
   
    // Load students
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_DATA_FILE))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 8) continue;

                String id = parts[0].trim();
                String name = parts[1].trim();
                String major = parts[2].trim();
                int year = Integer.parseInt(parts[3].trim());
                String email = parts[4].trim();
                String applied  = parts[5].trim();   // semicolon separated
                String accepted = parts[6].trim();
                String password = parts[7].trim();

                Student student = new Student(id, name, year, major, email, password);
                
                    // restore applied internship IDs
                    if (!applied.isEmpty()) {
                        String[] appliedRecords = applied.split(";");
                        for (String record : appliedRecords) {
                            String[] recordParts = record.split(":", -1);
                            if (recordParts.length == 2) {
                                String internshipId = recordParts[0].trim();
                                String status = recordParts[1].trim();
                                student.getAppliedInternshipId().add(new AppliedRecord(internshipId, status));
                            }
                        }
                    }

                    // restore accepted internship
                    if (!accepted.isEmpty()) {
                        student.setAcceptedInternshipId(accepted.trim());
                    }

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
            bw.write("StudentID,Name,Major,Year,Email,AppliedInternships,AcceptedInternship,Password");
            bw.newLine();

            for (Student s : students) {
                // String applied = String.join(";", s.getAppliedInternshipId()); // separate multiple IDs by ;
                // String accepted = s.getAcceptedInternshipId() != null ? s.getAcceptedInternshipId() : "";
                
            List<String> appliedStrings = new ArrayList<>();
            for (AppliedRecord ar : s.getAppliedInternshipId()) {
                appliedStrings.add(ar.getInternshipId() + ":" + ar.getStatus());
            }

            String applied = String.join(";", appliedStrings);

            String accepted = s.getAcceptedInternshipId() != null ? s.getAcceptedInternshipId() : "";


                bw.write(String.join(",",
                        s.getUserId(),
                        s.getName(),
                        s.getMajor(),
                        String.valueOf(s.getYearOfStudy()),
                        s.getEmail(),
                        applied,
                        accepted,
                        s.getPassword()
                ));
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
                if (parts.length < 8)
                    continue; //invalid row
                String id = parts[0].trim();
                String name = parts[1].trim();
                String companyName = parts[2].trim();
                String department = parts[3].trim();
                String position = parts[4].trim();
                String email = parts[5].trim();
                String status = parts[6].trim();
                String password = parts[7].trim();

                CompanyRepresentative rep = new CompanyRepresentative(id, name, companyName, department, position,
                        email, status, password);

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
            bw.write("CompanyRepID,Name,CompanyName,Department,Position,Email,Status,Password");
            bw.newLine();

            for (CompanyRepresentative rep : reps) {
                bw.write(String.join(",",
                        rep.getUserId(),
                        rep.getName(),
                        rep.getCompanyName(),
                        rep.getDepartment(),
                        rep.getPosition(),
                        rep.getEmail(),
                        rep.getStatus(),
                        rep.getPassword()));
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
            if (parts.length < 6) continue; // now at least 6 columns

            String id = parts[0].trim();
            String name = parts[1].trim();
            String department = parts[3].trim(); // correct index
            String email = parts[4].trim();      // correct index
            String password = parts[5].trim();   // correct index

            CareerCenterStaff staff = new CareerCenterStaff(id, name, department, email, password);
            staffList.add(staff);
        }
    } catch (IOException e) {
        System.out.println("Error loading staff: " + e.getMessage());
    }
    return staffList;
}


    //save all career staff
    public static boolean saveStaff(List<CareerCenterStaff> staffList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSS_STAFF_DATA_FILE))) {
            bw.write("StaffID,Name,Role,Department,Email,Password");
            bw.newLine();
            for (CareerCenterStaff s : staffList) {
                bw.write(String.join(",",
                        s.getUserId(),
                        s.getName(),
                        "Career Center Staff",
                        s.getDepartment(),
                        s.getEmail(),
                        s.getPassword()));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving staff: " + e.getMessage());
            return false;
        }
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
            bw.write(
                    "id,title,company,representativeId,major,yearType,description,openDate,closeDate,slots,visible,status");
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
                        i.getStatus()));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving internships: " + e.getMessage());
            return false;
        }
    }

    public static List<Application> loadApplications(List<Student> students,
            InternshipController internshipController) {
        List<Application> applications = new ArrayList<>();
        for (Student student : students) {
            for (AppliedRecord record : student.getAppliedInternshipId()) {
                Internship internship = internshipController.getInternshipById(record.getInternshipId());
                if (internship != null) {
                    Application a = new Application(student, internship);
                    a.setStatus(record.getStatus());
                    applications.add(a);
                }
            }
        }

        return applications;
    }




    public static void saveWithdrawRequests(List<WithdrawRequest> requests) {
        File file = new File(WITHDRAW_DATA_FILE);

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {

            // Header row
            pw.println("uid,student_id,internship_id,reason,status");

            // Data rows
            for (WithdrawRequest req : requests) {
                pw.println(
                        req.getId() + "," +
                                req.getStudent().getUserId() + "," +
                                req.getInternship().getId() + "," +
                                req.getReason().replace(",", " ") + "," + // avoid breaking CSV
                                req.getStatus());
            }

            pw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
        
            public static List<WithdrawRequest> loadWithdrawRequests(List<Student> students, List<Internship> internships) {
            List<WithdrawRequest> requests = new ArrayList<>();
            File file = new File(WITHDRAW_DATA_FILE);

            if (!file.exists()) return requests; // return empty if file does not exist

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                br.readLine(); // skip header
                String line;

                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    if (parts.length < 5) continue;

                    String uid = parts[0].trim();
                    String studentId = parts[1].trim();
                    String internshipId = parts[2].trim();
                    String reason = parts[3].trim();
                    String status = parts[4].trim();

                    // find student object
                    Student student = students.stream()
                            .filter(s -> s.getUserId().equalsIgnoreCase(studentId))
                            .findFirst()
                            .orElse(null);

                    // find internship object
                    Internship internship = internships.stream()
                            .filter(i -> i.getId().equalsIgnoreCase(internshipId))
                            .findFirst()
                            .orElse(null);

                    if (student != null && internship != null) {
                        WithdrawRequest req = new WithdrawRequest(student, internship, reason);
                        req.setStatus(status); // restore status
                        // overwrite UID if you want to preserve it
                        try {
                            java.lang.reflect.Field idField = WithdrawRequest.class.getDeclaredField("id");
                            idField.setAccessible(true);
                            idField.set(req, uid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        requests.add(req);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return requests;
        }

    

}
