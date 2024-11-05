import helpers.FileUtils;
import helpers.Utils;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseID;
    private String courseName;
    private List<Enrollment> enrollments;
    private List<Assignment> assignments;

   //List of all courses
    private static List<Course> allCourses = new ArrayList<>();

    public Course(String courseID, String courseName) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.enrollments = new ArrayList<>();
        this.assignments = new ArrayList<>();
        allCourses.add(this);
    }

    // Vital Getters
    public String getCourseID() { return courseID; }
    public String getCourseName() { return courseName; }
    public List<Enrollment> getEnrollments() { return enrollments; }
    public List<Assignment> getAssignments() { return assignments; }

    public boolean addEnrollment(Enrollment enrollment) {
        if (enrollments.size() >= 30) {
            Utils.displayMessage("Course is full.");
            return false;
        }
        enrollments.add(enrollment);
        return true;
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
        Utils.displayMessage("Assignment " + assignment.getTitle() + " added to " + courseName);
        saveAssignment(assignment);
    }

    private void saveAssignment(Assignment assignment) {
        FileUtils.writeToFile("assignments.txt", assignment.toString());
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

    // Find course by ID
    public static Course findCourseByID(String courseID) {
        for (Course course : allCourses) {
            if (course.getCourseID().equals(courseID)) {
                return course;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return courseID + "," + courseName;
    }
}
