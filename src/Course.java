import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseID;
    private String courseName;
    private int credits;
    private int capacity;
    private List<Enrollment> enrollments;
    private List<Assignment> assignments;


    public Course(String courseID, String courseName, int credits, int capacity) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.credits = credits;
        this.capacity = capacity;
        this.enrollments = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    public String getCourseID() { return courseID; }
    public void setCourseID(String courseID) { this.courseID = courseID; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public List<Enrollment> getEnrollments() { return enrollments; }

    public List<Assignment> getAssignments() { return assignments; }

    public boolean addEnrollment(Enrollment enrollment) {
        if (enrollments.size() >= capacity) {
            System.out.println("Course is full.");
            return false;
        }
        if (enrollments.contains(enrollment)) {
            System.out.println("Student is already enrolled.");
            return false;
        }
        enrollments.add(enrollment);
        return true;
    }

    public boolean removeEnrollment(Enrollment enrollment) {
        if (enrollments.remove(enrollment)) {
            return true;
        } else {
            System.out.println("Enrollment not found.");
            return false;
        }
    }

    public void listEnrolledStudents() {
        System.out.println("Students enrolled in " + courseName + ":");
        for (Enrollment enrollment : enrollments) {
            System.out.println("- " + enrollment.getStudent().getName());
        }
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public void listAssignments() {
        System.out.println("Assignments for course " + courseName + ":");
        for (Assignment assignment : assignments) {
            System.out.println("- " + assignment.getTitle() + " (Due: " + assignment.getDueDate() + ")");
        }
    }
}
