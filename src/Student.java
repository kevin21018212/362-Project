import java.util.ArrayList;
import java.util.List;

public class Student {
    private String studentID;
    private String name;
    private String email;
    private String address;
    private List<Enrollment> enrollments = new ArrayList<>();
    private List<Assignment> assignments = new ArrayList<>();

    // Constructor
    public Student(String studentID, String name, String email, String address) {
        this.studentID = studentID;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    // Getters and Setters
    public String getStudentID() { return studentID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<Enrollment> getEnrollments() { return enrollments; }
    public List<Assignment> getAssignments() { return assignments; }

    // Methods
    public void enrollInCourse(Course course) {
        Enrollment enrollment = new Enrollment("E" + (enrollments.size() + 1), this, course, "Enrolled");
        if (course.addEnrollment(enrollment)) {
            enrollments.add(enrollment);
            System.out.println(name + " successfully enrolled in " + course.getCourseName());
        }
    }

    public void submitAssignment(Assignment assignment) {
        assignments.add(assignment);
        System.out.println(name + " submitted assignment: " + assignment.getTitle());
    }

    public void listCourses() {
        enrollments.forEach(enrollment ->
                System.out.println("- " + enrollment.getCourse().getCourseName())
        );
    }

    public void listSubmittedAssignments() {
        assignments.forEach(assignment ->
                System.out.println("- " + assignment.getTitle() + " (Course: " + assignment.getCourse().getCourseName() + ")")
        );
    }
}
