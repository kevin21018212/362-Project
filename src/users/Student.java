package users;

import helpers.FileUtils;
import helpers.User;
import helpers.Utils;
import main.Assignment;
import main.Course;
import main.Enrollment;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {

    public Student(String id, String name, String email) {
        super(id, name, email);
    }

    public String getId() {
        return super.id;
    }



    public void enrollInCourse() {
        String courseId = Utils.getInput("Enter main.Course ID to enroll: ");
        Course course = Course.findCourseById(courseId);
        if (course != null) {
            Enrollment enrollment = new Enrollment(this.id, courseId);
            Enrollment.saveEnrollment(enrollment);
            Utils.displayMessage("Enrolled in course " + course.getName());
        } else {
            Utils.displayMessage("main.Course not found.");
        }
    }

    public void submitAssignment() {
        String courseId = Utils.getInput("Enter main.Course ID: ");
        Course course = Course.findCourseById(courseId);
        if (course != null) {
            String assignmentId = Utils.getInput("Enter main.Assignment ID: ");
            Assignment assignment = course.findAssignmentById(assignmentId);
            if (assignment != null) {
                String directory = "courses/" + courseId;
                String fileName = "submissions.txt";
                String data = this.id + "," + assignmentId + ",Not Graded";
                FileUtils.writeToFile(directory, fileName, data);
                Utils.displayMessage("main.Assignment submitted.");
            } else {
                Utils.displayMessage("main.Assignment not found.");
            }
        } else {
            Utils.displayMessage("main.Course not found.");
        }
    }

    // Load students from file
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

    @Override
    public String toString() {
        return id + "," + name + "," + email;
    }
}

