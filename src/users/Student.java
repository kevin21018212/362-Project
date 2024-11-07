package users;



import helpers.Display;
import helpers.User;
import helpers.Utils;
import main.Assignment;
import main.Course;
import main.Enrollment;
import main.Submission;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private String major;

    public Student(String id, String name, String email, String major) {
        super(id, name, email);
        this.major = major;
    }

    public String getMajor() {
        return major;
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
     * Change the major of a student
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
                System.out.println("1. Type 1 to change your major.");
                System.out.println("2. Press 2 for No, go back.");
                String choice = Utils.getInput("\nAre you sure you want to change your major?");

                if (choice.equals("1")) {
                    System.out.println("To choose a new major, select a department to view their majors.");
                    displayDepartmentNames();
                } else if (choice.equals("2")) {
                    changeMajor();
                } else {
                    System.out.println("Error");
                }
                return;
            case "3":
                Display.displayMessage("Your current Major is: " + getStudentDepartment());
                return;
            case "4":
                Display.displayStudentMenu();
                return;

            default:
                System.out.println("Yikers");
        }
        //displayMajors(selectedDepartment);
    }

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

    // Method to display majors for a specified department
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
}