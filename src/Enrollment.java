public class Enrollment {
    private String enrollmentID;
    private Student student;
    private Course course;
    private String status; // e.g., "Enrolled", "Waitlisted", "Dropped"

    // Constructor
    public Enrollment(String enrollmentID, Student student, Course course, String status) {
        this.enrollmentID = enrollmentID;
        this.student = student;
        this.course = course;
        this.status = status;
    }

    // Getters and Setters
    public String getEnrollmentID() { return enrollmentID; }
    public void setEnrollmentID(String enrollmentID) { this.enrollmentID = enrollmentID; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
