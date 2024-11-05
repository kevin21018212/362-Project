import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseID;
    private String courseName;
    private int credits;
    private int capacity;
    private List<Enrollment> enrollments;


    public Course(String courseID, String courseName, int credits, int capacity) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.credits = credits;
        this.capacity = capacity;
        this.enrollments = new ArrayList<>();
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

    // Methods
    public void addEnrollment(Enrollment enrollment) {
        if (enrollments.size() < capacity) {
            enrollments.add(enrollment);
        } else {
            System.out.println("Course is full.");
            //Todo
        }
    }
}
