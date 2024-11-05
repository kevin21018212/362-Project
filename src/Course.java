import java.util.ArrayList;
import java.util.List;
import helpers.FileUtils;
import helpers.Utils;

public class Course {
    private String id;
    private String name;
    private List<Enrollment> enrollments;
    private List<Assignment> assignments;

    // list ofall courses
    private static List<Course> allCourses = new ArrayList<>();

    public Course(String id, String name) {
        this.id = id;
        this.name = name;
        this.enrollments = new ArrayList<>();
        this.assignments = new ArrayList<>();
        allCourses.add(this);
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public List<Enrollment> getEnrollments() { return enrollments; }
    public List<Assignment> getAssignments() { return assignments; }

    public boolean addEnrollment(Enrollment enrollment) {
        if (enrollments.size() >= 30) {
            Utils.displayMessage("Course is full.");
            return false;
        }
        enrollments.add(enrollment);
        saveEnrollment(enrollment);
        return true;
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
        Utils.displayMessage("Assignment " + assignment.getTitle() + " added to " + name);
        saveAssignment(assignment);
    }

    private void saveEnrollment(Enrollment enrollment) {
        String directory = "courses/" + id;
        String fileName = "enrollments.txt";
        FileUtils.writeToFile(directory, fileName, enrollment.toString());
    }

    private void saveAssignment(Assignment assignment) {
        String directory = "courses/" + id;
        String fileName = "assignments.txt";
        FileUtils.writeToFile(directory, fileName, assignment.toString());
    }

    public static List<Course> loadCourses() {
        List<Course> courses = new ArrayList<>();
        List<String> lines = FileUtils.readFromFile("", "courses.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            courses.add(new Course(data[0], data[1]));
        }
        return courses;
    }

    public static Course findCourseById(String id) {
        for (Course course : allCourses) {
            if (course.getId().equals(id)) {
                return course;
            }
        }
        return null;
    }

    public void loadEnrollments() {
        String directory = "courses/" + id;
        String fileName = "enrollments.txt";
        List<String> lines = FileUtils.readFromFile(directory, fileName);
        for (String line : lines) {
            String[] data = line.split(",");
            Student student = Student.findStudentById(data[1]);
            if (student != null) {
                Enrollment enrollment = new Enrollment(data[0], student, this);
                enrollments.add(enrollment);
                student.getEnrollments().add(enrollment);
            }
        }
    }

    public void loadAssignments() {
        String directory = "courses/" + id;
        String fileName = "assignments.txt";
        List<String> lines = FileUtils.readFromFile(directory, fileName);
        for (String line : lines) {
            String[] data = line.split(",");
            Assignment assignment = new Assignment(data[0], data[1], data[2], this);
            assignment.setGrade(data.length > 4 ? data[4] : "Ungraded");
            assignments.add(assignment);
        }
    }

    @Override
    public String toString() {
        return id + "," + name;
    }
}
