package users;


import helpers.FileUtils;
import java.util.ArrayList;
import java.util.List;

public class DataAccess {

    // Loaders
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        List<String> lines = FileUtils.readFromFile("data", "students.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            students.add(new Student(data[0], data[1], data[2]));
        }
        return students;
    }
    public static List<Instructor> loadInstructors() {
        List<Instructor> instructors = new ArrayList<>();
        List<String> lines = FileUtils.readFromFile("data", "instructors.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            instructors.add(new Instructor(data[0], data[1], data[2]));
        }
        return instructors;
    }


    // Find student by ID
    public static Student findStudentById(String id) {
        List<Student> students = loadStudents();
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }


    public static Instructor findInstructorById(String id) {
        List<Instructor> instructors = loadInstructors();
        for (Instructor instructor : instructors) {
            if (instructor.getId().equals(id)) {
                return instructor;
            }
        }
        return null;
    }



}
