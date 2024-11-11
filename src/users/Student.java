package users;

import helpers.Display;
import helpers.User;
import helpers.Utils;
import main.Assignment;
import main.Course;
import main.Enrollment;
import main.Submission;
import helpers.FileUtils;
import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private static final String[] STUDENT_HEADERS = {"ID", "Name", "Email", "Major"};
    private String major;

    public Student(String id, String name, String email, String major) {
        super(id, name, email);
        this.major = major;
    }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    @Override
    public String toString() {
        return id + "::" + name + "::" + email + "::" + major;
    }

    public void updateMajorInFile() {
        List<String[]> data = FileUtils.readStructuredData("", "students.txt");
        List<String[]> updatedData = new ArrayList<>();

        for (String[] row : data) {
            if (row[0].equals(this.id)) {
                updatedData.add(new String[]{this.id, this.name, this.email, this.major});
            } else {
                updatedData.add(row);
            }
        }

        FileUtils.writeStructuredData("", "students.txt", STUDENT_HEADERS, updatedData);
    }

    public List<String> getMajors(String department) {
        List<String[]> data = FileUtils.readStructuredData("", "majors.txt");
        List<String> majors = new ArrayList<>();

        for (String[] row : data) {
            if (row.length > 0 && row[0].trim().equalsIgnoreCase(department)) {
                if (row.length > 1) {
                    String majorsStr = row[1].trim();
                    // Remove brackets and split by comma
                    majorsStr = majorsStr.replaceAll("[\\[\\]]", ""); // Remove [ and ]
                    String[] majorArray = majorsStr.split(",");

                    // Add each major to the list, trimming whitespace
                    for (String major : majorArray) {
                        majors.add(major.trim());
                    }
                }
                break;
            }
        }
        return majors;
    }

    public List<String> getAllDepartments() {
        List<String[]> data = FileUtils.readStructuredData("", "departments.txt");
        List<String> departmentNames = new ArrayList<>();

        for (String[] row : data) {
            if (row.length > 0) {
                departmentNames.add(row[0].trim());
            }
        }
        return departmentNames;
    }

    public String getStudentDepartment() {
        List<String[]> data = FileUtils.readStructuredData("", "majors.txt");

        for (String[] row : data) {
            if (row.length > 1) {
                String departmentName = row[0].trim();
                for (int i = 1; i < row.length; i++) {
                    if (row[i].trim().equalsIgnoreCase(major)) {
                        return departmentName;
                    }
                }
            }
        }
        return "Department not found";
    }

    // Rest of the methods remain the same, just update file operations
    public void enrollInCourse() {
        String courseId = Utils.getInput("Enter course ID: ");
        Course course = Course.findCourseById(courseId);

        if (course == null) {
            Display.displayMessage("Course not found");
            return;
        }

        // Validate enrollment
        if (course.isFull()) {
            Display.displayMessage("Course is full");
            return;
        }

        if (!Enrollment.checkPrerequisites(this.getId(), course)) {
            Display.displayMessage("Prerequisites not met");
            return;
        }

        // Create and save enrollment
        Enrollment enrollment = new Enrollment(this.getId(), courseId);
        Enrollment.saveEnrollment(enrollment);

        // Update course capacity
        course.incrementEnrollment();
        course.updateEnrollmentCount();

        Display.displayMessage("Successfully enrolled in " + course.getName());
    }

    public void changeMajor() {
        System.out.println("1. View Major");
        System.out.println("2. Change Major");
        System.out.println("3. View Department");
        System.out.println("4. Go Back\n");

        String num = Utils.getInput("\nChoose an Option to Proceed:");
        switch(num) {
            case "1":
                Display.displayMessage("Your current Major is: " + getMajor());
                break;
            case "2":
                handleMajorChange();
                break;
            case "3":
                Display.displayMessage("Your current Department is: " + getStudentDepartment());
                break;
            case "4":
                Display.displayStudentMenu();
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    private void handleMajorChange() {
        Display.displayMessage("Your current Major is: " + getMajor());
        System.out.println("1. Type 1 to change your major.");
        System.out.println("2. Press 2 for No, go back.");

        String choice = Utils.getInput("\nAre you sure you want to change your major?");
        if (choice.equals("1")) {
            boolean selectingMajor = true;
            while (selectingMajor) {
                System.out.println("Changing your major...");
                displayDepartmentNames();
                String departmentName = Utils.getInput("\nSelect a department or type 'back' to cancel:");

                if (departmentName.equalsIgnoreCase("back")) {
                    break;
                }

                displayMajors(departmentName);
                String selectedMajor = Utils.getInput("\nSelect a major or type 'back' for departments:");

                if (selectedMajor.equals("1")) {
                    selectingMajor = false;
                } else if (!selectedMajor.equalsIgnoreCase("back")) {
                    setMajor(selectedMajor);
                    updateMajorInFile();
                    Display.displayMessage("Your new Major is: " + getMajor());
                    selectingMajor = false;
                }
            }
        }
    }

    public void displayMajors(String department) {
        List<String> majors = getMajors(department);
        System.out.println("Majors in " + department + " Department:");
        majors.forEach(System.out::println);
    }

    public void displayDepartmentNames() {
        System.out.println("\nDepartments Available:");
        getAllDepartments().forEach(Display::displayMessage);
    }

    public void submitAssignment() {
        // Display all enrolled courses for the student
        Enrollment.displayAllEnrolledCourses(this.id);

        // Ask for course ID
        String courseId = Utils.getInput("\nEnter Course ID from the list above: ");
        Course course = Course.findCourseById(courseId);
        if (course == null) {
            Display.displayMessage("Invalid Course ID.");
            return;
        }

        // Load assignments and display those not yet submitted
        List<Assignment> assignments = Assignment.loadAssignments(courseId);
        boolean hasUnsubmittedAssignments = false;
        System.out.println("Assignments not yet submitted:");

        for (Assignment assignment : assignments) {
            if (!Submission.isSubmitted(courseId, this.id, assignment.getId())) {
                System.out.println(assignment);
                hasUnsubmittedAssignments = true;
            }
        }

        if (!hasUnsubmittedAssignments) {
            Display.displayMessage("All assignments have been submitted for this course.");
            return;
        }

        // Ask for assignment ID
        String assignmentId = Utils.getInput("\nEnter Assignment ID: ");

        if (Submission.isSubmitted(courseId, this.id, assignmentId)) {
            Display.displayMessage("This assignment has already been submitted.");
        } else {
            // Create submission data
            String submissionId = "sub_" + System.currentTimeMillis();
            String submittedDate = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ISO_DATE);

            // Create submission data array
            String[] submissionData = {
                    submissionId,
                    assignmentId,
                    this.id,
                    "Not Graded",
                    submittedDate
            };

            // Read existing submissions
            String fileName = "courses/" + courseId + "/submissions.txt";
            List<String[]> existingSubmissions = FileUtils.readStructuredData("", fileName);

            // Add new submission
            existingSubmissions.add(submissionData);

            // Write back to file with headers
            FileUtils.writeStructuredData("", fileName,
                    new String[]{"SubmissionId", "AssignmentId", "StudentId", "Grade", "SubmittedDate"},
                    existingSubmissions);

            Display.displayMessage("Assignment submitted successfully.");
        }
    }



    public static Student findStudentById(String id) {
        return DataAccess.findStudentById(id);
    }

    public void viewGrades() {
        // Load the student's enrollments
        List<Enrollment> enrollments = Enrollment.loadEnrollments();
        boolean hasGrades = false;


        System.out.println("Grades for " + this.getName() + ":");
        for (Enrollment enrollment : enrollments) {
            // Check if the enrollment belongs to the current student
            if (enrollment.getStudentId().equals(this.id)) {
                Course course = Course.findCourseById(enrollment.getCourseId());
                if (course != null) {
                    // Retrieve and display the grade for each course
                    double grade = enrollment.getGrade();
                    System.out.println("Course: " + course.getName() + " - Grade: " + (grade >= 0 ? grade : "Not graded yet"));
                    hasGrades = true;
                }
            }
        }


        if (!hasGrades) {
            System.out.println("No grades available.");
        }
    }

    public void applyForGraduation() {
        // Sample requirements for graduation
        final int MINIMUM_CREDITS = 120; // Minimum credits required for graduation
        final double MINIMUM_GPA = 2.0; // Minimum GPA required for graduation

        // Assume methods `getTotalCredits()` and `getGPA()` are implemented to fetch these details
        int totalCredits = getTotalCredits();
        double gpa = getGPA();

        if (totalCredits >= MINIMUM_CREDITS && gpa >= MINIMUM_GPA) {
            Display.displayMessage("Congratulations! You are eligible to apply for graduation.");
            // Code to mark the application status, such as writing to a file or database
            markGraduationApplication();
            Display.displayMessage("Your graduation application has been submitted successfully.");
        } else {
            Display.displayMessage("You are not eligible to apply for graduation.");
            if (totalCredits < MINIMUM_CREDITS) {
                Display.displayMessage("You need at least " + (MINIMUM_CREDITS - totalCredits) + " more credits to be eligible.");
            }
            if (gpa < MINIMUM_GPA) {
                Display.displayMessage("Your GPA must be at least " + MINIMUM_GPA + " to be eligible.");
            }
        }
    }

    public int getTotalCredits() {
        // Placeholder - Implement logic to calculate total credits based on courses completed
        return 120; // Example return value for eligibility
    }

    public double getGPA() {
        // Placeholder - Implement logic to calculate GPA based on course grades
        return 3.0; // Example return value for eligibility
    }

    public void markGraduationApplication() {
        // Implement logic to mark graduation application status
        Display.displayMessage("Graduation application status has been updated in the system.");
    }
}