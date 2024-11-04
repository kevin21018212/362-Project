public class Enrollment {
    private String enrollmentID;
    private Student student;
    private Course course;
    private String enrollmentDate;
    private String status;

    // Constructors and Getter/Setters
    public Enrollment(Student student, Course course) {
        this.enrollmentID = generateEnrollmentID();
        this.student = student;
        this.course = course;
        this.enrollmentDate = getCurrentDate();
        this.status = "Enrolled";
    }

    public String getEnrollmentID() { return enrollmentID; }

    public Student getStudent() { return student; }

    public Course getCourse() { return course; }

    public String getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(String enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Methods Case
    private String generateEnrollmentID() {
        // Generate a unique enrollment ID
        return "ENR" + System.currentTimeMillis();
    }

    private String getCurrentDate() {
        // To do
        return java.time.LocalDate.now().toString();
    }
}
