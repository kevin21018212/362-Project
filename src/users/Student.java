package users;



import helpers.Display;
import helpers.User;
import helpers.Utils;
import main.Assignment;
import main.Course;
import main.Enrollment;
import main.Submission;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private static final String FILE_PATH = "src/data/students.txt";
    private String major;

    public Student(String id, String name, String email, String major) {
        super(id, name, email);
        this.major = major;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void enrollInCourse() {
        String courseId = Utils.getInput("\nEnter Course ID to enroll: ");
        Course course = Course.findCourseById(courseId);

        if (course != null) {
            if (!Enrollment.checkPrerequisites(this.id, course)) {
                Display.displayMessage("Prerequisites not met for this course.");
                return;
            }

            if (course.isFull()) {
                Display.displayMessage("This course is full.");
                return;
            }

            for (Enrollment enrollment : Enrollment.loadEnrollments()) {
                if (enrollment.getStudentId().equals(this.id) && enrollment.getCourseId().equals(courseId)) {
                    Display.displayMessage("You are already enrolled in this course.");
                    return;
                }
            }

            Enrollment enrollment = new Enrollment(this.id, courseId);
            Enrollment.saveEnrollment(enrollment);
            course.incrementEnrollment();
            Display.displayMessage("Enrolled in course " + course.getName());
        } else {
            Display.displayMessage("Course not found.");
        }
    }

    /**
     * Full console prompts and file updating to change the major of the student logged-in
     */
    public void changeMajor(){
        System.out.println("1. View Major");
        System.out.println("2. Change Major");
        System.out.println("3. View Department");
        System.out.println("4. Go Back\n");
        String num = Utils.getInput("\nChoose an Option to Proceed:");

        switch(num) {
            case "1":
                Display.displayMessage("Your current Major is: " + getMajor());
                return;
            case "2":
                Display.displayMessage("Your current Major is: " + getMajor());
                System.out.println("1. Type 1 to change your major. ");
                System.out.println("2. Press 2 for No, go back. ");
                String choice = Utils.getInput("\nAre you sure you want to change your major? ");

                if (choice.equals("1")) {
                    boolean selectingMajor = true;

                    while (selectingMajor) {
                        System.out.println("Changing your major...");
                        displayDepartmentNames();
                        String departmentName = Utils.getInput("\nTo choose a new major, select a department to view their majors, or type 'back' to cancel:");

                        if (departmentName.equalsIgnoreCase("back")) {
                            break;
                        }

                        displayMajors(departmentName);

                        String selectedMajor = Utils.getInput("\nSelect a major from this list, type 'back' to view departments again, or press '1' to cancel:");

                        if (selectedMajor.equals("1")) {
                            selectingMajor = false;
                        } else if (selectedMajor.equalsIgnoreCase("back")) {
                            // Loop back to the department selection without exiting
                            continue;
                        } else {
                            setMajor(selectedMajor);
                            updateMajorInFile();
                            Display.displayMessage("Your new Major is: " + getMajor());
                            selectingMajor = false;
                        }
                    }
                }
                else if (choice.equals("2")) {
                    changeMajor(); //done
                } else {
                    System.out.println("Error"); //done
                }
                return;
            case "3":
                Display.displayMessage("Your current Major is: " + getStudentDepartment()); //done
                return;
            case "4":
                Display.displayStudentMenu(); //done
                return;

            default:
                System.out.println("Yikers");
        }
    }

    /**
     * Updates the major in the students.txt file of the logged-in student using studentID
     * Ex: 4992,John Smith,john.smith@university.edu,Stats
     * Would change "Stats"
     */
    public void updateMajorInFile() {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Check if the line matches the student's ID
                if (parts[0].equals(this.id)) {
                    // Update the major field in the line
                    line = parts[0] + "," + parts[1] + "," + parts[2] + "," + this.major;
                }

                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write the updated lines back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the majors.txt file to gather all majors from the list for the specified department
     * @param department - the selected department
     * @return A List of the available majors for the specified departments.
     */
    public List<String> getMajors(String department) {
        List<String> majors = new ArrayList<>();
        String filePath = "src/data/majors.txt"; // Update the path as needed

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].trim().equalsIgnoreCase(department)) {
                    // Add all majors from parts[1] onward to the majors list
                    for (int i = 1; i < parts.length; i++) {
                        majors.add(parts[i].trim());
                    }
                    break; // Exit the loop after finding the department
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return majors;
    }

    /**
     * Displays the majors available for the specified department
     * @param department - the selected department for which you want to view majors
     */
    public void displayMajors(String department) {
        List<String> majors = getMajors(department);
        System.out.println("Majors in " + department + " Department:");
        for (String major : majors) {
            System.out.println(major);
        }
    }

    /**
     * Helper method to display department names to the console
     */
    public void displayDepartmentNames(){
        System.out.println("\nDepartments Available: ");
        List<String> departmentNames = getAllDepartments();

        // Print each department name on its own line
        for (String department : departmentNames) {
            Display.displayMessage(department);
        }
    }

    /**
     * @return List of departments in the departments.txt file.
     */
    public List<String> getAllDepartments() {
        List<String> departmentNames = new ArrayList<>();

        String filePath = "src/data/departments.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String departmentName = parts[0].trim();
                    departmentNames.add(departmentName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return departmentNames;
    }

    /**
     * Uses the majors.txt file to get the department of the currently logged in student
     * @return - the major of the logged-in student as a String (Ex: "Stats")
     */
    public String getStudentDepartment() {
        String filePath = "src/data/majors.txt"; // Update with the correct path
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1) {
                    String departmentName = parts[0].trim();
                    for (int i = 1; i < parts.length; i++) {
                        if (parts[i].trim().equalsIgnoreCase(major)) {
                            return departmentName;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Department not found";
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
            Submission.submit(courseId, this.id, assignmentId);
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