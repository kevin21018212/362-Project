import helpers.FileUtils;
import helpers.Utils;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseID;
    private String courseName;
    private List<Enrollment> enrollments;
    private List<Assignment> assignments;

    public Course(String courseID, String courseName) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.enrollments = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    // Vital Getters
    public String getCourseID() { return courseID; }
    public String getCourseName() { return courseName; }
    public List<Enrollment> getEnrollments() { return enrollments; }
    public List<Assignment> getAssignments() { return assignments; }

    public boolean addEnrollment(Enrollment enrollment) {
        if (enrollments.size() >= 30) { // Assume capacity is 30 for simplicity
            Utils.displayMessage("Course is full.");
            return false;
        }
        enrollments.add(enrollment);
        return true;
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
        Utils.displayMessage("Assignment " + assignment.getTitle() + " added to " + courseName);
    }

    public static List<Course> loadCourses() {
        List<Course> courses = new ArrayList<>();
        List<String> lines = FileUtils.readFromFile("courses.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            courses.add(new Course(data[0], data[1]));
        }
        return courses;
    }

    @Override
    public String toString() {
        return courseID + "," + courseName;
    }
}
