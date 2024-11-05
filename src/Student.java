import java.util.ArrayList;
import java.util.List;
import helpers.FileUtils;
import helpers.User;
import helpers.Utils;

public class Student extends User {
    private List<Enrollment> enrollments;
    private List<Assignment> submittedAssignments;

    public Student(String id, String name, String email) {
        super(id, name, email);
        this.enrollments = new ArrayList<>();
        this.submittedAssignments = new ArrayList<>();
    }

    public String getId() {
        return super.userID;
    }

    // Getters
    public List<Enrollment> getEnrollments() { return enrollments; }
    public List<Assignment> getSubmittedAssignments() { return submittedAssignments; }

    public void enrollInCourse(Course course) {
        String enrollmentId = Utils.generateID("E", FileUtils.getNextId("courses/" + course.getId(), "enrollments.txt"));
        Enrollment enrollment = new Enrollment(enrollmentId, this, course);
        if (course.addEnrollment(enrollment)) {
            enrollments.add(enrollment);
            Utils.displayMessage(name + " successfully enrolled in " + course.getName());
        }
    }

    public void submitAssignment(Assignment assignment) {
        submittedAssignments.add(assignment);
        Utils.displayMessage(name + " submitted assignment: " + assignment.getTitle());
        saveAssignmentSubmission(assignment);
    }

    private void saveAssignmentSubmission(Assignment assignment) {
        String directory = "courses/" + assignment.getCourse().getId();
        String fileName = "submissions.txt";
        String data = getId() + "," + assignment.getId();
        FileUtils.writeToFile(directory, fileName, data);
    }

    @Override
    public void displayInfo() {
        System.out.println("Student ID: " + super.userID + ", Name: " + name + ", Email: " + email);
    }

    @Override
    public String toString() {
        return super.userID + "," + name + "," + email;
    }

    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        List<String> lines = FileUtils.readFromFile("", "students.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            students.add(new Student(data[0], data[1], data[2]));
        }
        return students;
    }

    public static Student findStudentById(String id) {
        List<Student> students = loadStudents();
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }

    public void loadEnrollments() {
        List<Course> courses = Course.loadCourses();
        for (Course course : courses) {
            course.loadEnrollments();
            for (Enrollment enrollment : course.getEnrollments()) {
                if (enrollment.getStudent().getId().equals(this.getId())) {
                    enrollments.add(enrollment);
                }
            }
        }
    }
}
