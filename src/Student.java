import helpers.FileUtils;
import helpers.User;
import helpers.Utils;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private List<Enrollment> enrollments;
    private List<Assignment> submittedAssignments; // To track submitted assignments

    public Student(String studentID, String name, String email) {
        super(studentID, name, email);
        this.enrollments = new ArrayList<>();
        this.submittedAssignments = new ArrayList<>();
    }

    public String getStudentID() {
        return super.userID;
    }

    // Getters
    public List<Enrollment> getEnrollments() { return enrollments; }
    public List<Assignment> getSubmittedAssignments() { return submittedAssignments; }

    public void enrollInCourse(Course course) {
        String enrollmentID = Utils.generateID("E", enrollments.size() + 1);
        Enrollment enrollment = new Enrollment(enrollmentID, this, course);
        if (course.addEnrollment(enrollment)) {
            enrollments.add(enrollment);
            Utils.displayMessage(name + " successfully enrolled in " + course.getCourseName());
            saveEnrollment(enrollment);
        }
    }

    public void submitAssignment(Assignment assignment) {
        submittedAssignments.add(assignment);
        Utils.displayMessage(name + " submitted assignment: " + assignment.getTitle());
    }

    private void saveEnrollment(Enrollment enrollment) {
        FileUtils.writeToFile("enrollments.txt", enrollment.toString());
    }

    @Override
    public void displayInfo() {
        System.out.println("Student ID: " + userID + ", Name: " + name + ", Email: " + email);
    }

    @Override
    public String toString() {
        return userID + "," + name + "," + email;
    }

    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        List<String> lines = FileUtils.readFromFile("students.txt");
        for (String line : lines) {
            String[] data = line.split(","); // Assuming CSV format
            students.add(new Student(data[0], data[1], data[2]));
        }
        return students;
    }


}
