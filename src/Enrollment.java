public class Enrollment {
    private String enrollmentID;
    private Student student;
    private Course course;

    public Enrollment(String enrollmentID, Student student, Course course) {
        this.enrollmentID = enrollmentID;
        this.student = student;
        this.course = course;

    }
    // Getters
    public String getEnrollmentID() { return enrollmentID; }
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }

    @Override
    public String toString() {
        return enrollmentID + "," + student.getId() + "," + course.getId();
    }
}
