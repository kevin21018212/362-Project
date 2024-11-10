package users;

import helpers.FileUtils;
import java.util.ArrayList;
import java.util.List;

public class DataAccess {
    private static final String[] STUDENT_HEADERS = {"ID", "Name", "Email", "Major"};
    private static final String[] INSTRUCTOR_HEADERS = {"ID", "Name", "Email"};
    private static final String[] REGISTRAR_HEADERS = {"ID", "Name", "Email"};

    // Loaders
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        List<String[]> data = FileUtils.readStructuredData("", "students.txt");

        for (String[] row : data) {
            if (row.length >= 3) {
                students.add(new Student(row[0], row[1], row[2], row[3]));
            }
        }
        return students;
    }

    public static List<Instructor> loadInstructors() {
        List<Instructor> instructors = new ArrayList<>();
        List<String[]> data = FileUtils.readStructuredData("", "instructors.txt");

        for (String[] row : data) {
            if (row.length >= 3) {
                instructors.add(new Instructor(row[0], row[1], row[2]));
            }
        }
        return instructors;
    }

    // Save methods
    public static void saveStudent(Student student) {
        List<String[]> existingData = FileUtils.readStructuredData("", "students.txt");
        List<String[]> newData = new ArrayList<>(existingData);

        // Create new student data array
        String[] studentData = {
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getMajor()
        };

        // Check if student already exists and update
        boolean found = false;
        for (int i = 0; i < newData.size(); i++) {
            if (newData.get(i)[0].equals(student.getId())) {
                newData.set(i, studentData);
                found = true;
                break;
            }
        }

        // If student doesn't exist, add them
        if (!found) {
            newData.add(studentData);
        }

        FileUtils.writeStructuredData("", "students.txt", STUDENT_HEADERS, newData);
    }

    public static void saveInstructor(Instructor instructor) {
        List<String[]> existingData = FileUtils.readStructuredData("", "instructors.txt");
        List<String[]> newData = new ArrayList<>(existingData);

        String[] instructorData = {
                instructor.getId(),
                instructor.getName(),
                instructor.getEmail()
        };

        boolean found = false;
        for (int i = 0; i < newData.size(); i++) {
            if (newData.get(i)[0].equals(instructor.getId())) {
                newData.set(i, instructorData);
                found = true;
                break;
            }
        }

        if (!found) {
            newData.add(instructorData);
        }

        FileUtils.writeStructuredData("", "instructors.txt", INSTRUCTOR_HEADERS, newData);
    }

    // Find methods
    public static Student findStudentById(String id) {
        List<String[]> data = FileUtils.readStructuredData("", "students.txt");

        for (String[] row : data) {
            if (row.length >= 3 && row[0].equals(id)) {
                return new Student(row[0], row[1], row[2], row[3]);
            }
        }
        return null;
    }

    public static Instructor findInstructorById(String id) {
        List<String[]> data = FileUtils.readStructuredData("", "instructors.txt");

        for (String[] row : data) {
            if (row.length >= 3 && row[0].equals(id)) {
                return new Instructor(row[0], row[1], row[2]);
            }
        }
        return null;
    }

    public static Registrar findRegistrarById(String id) {
        List<String[]> data = FileUtils.readStructuredData("", "registrar.txt");

        for (String[] row : data) {
            if (row.length >= 3 && row[0].equals(id)) {
                return new Registrar(row[0], row[1], row[2]);
            }
        }
        return null;
    }

    // Delete methods
    public static void deleteStudent(String id) {
        List<String[]> existingData = FileUtils.readStructuredData("", "students.txt");
        List<String[]> newData = new ArrayList<>();

        for (String[] row : existingData) {
            if (!row[0].equals(id)) {
                newData.add(row);
            }
        }

        FileUtils.writeStructuredData("", "students.txt", STUDENT_HEADERS, newData);
    }

    public static void deleteInstructor(String id) {
        List<String[]> existingData = FileUtils.readStructuredData("", "instructors.txt");
        List<String[]> newData = new ArrayList<>();

        for (String[] row : existingData) {
            if (!row[0].equals(id)) {
                newData.add(row);
            }
        }

        FileUtils.writeStructuredData("", "instructors.txt", INSTRUCTOR_HEADERS, newData);
    }
}