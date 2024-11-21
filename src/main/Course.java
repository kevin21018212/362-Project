package main;

import helpers.Display;
import helpers.FileUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Course {
    private String id;
    private String name;
    private String instructorId;
    private List<String> prerequisites;
    private int studentsEnrolled;
    private int classSize;
    public static List<Course> allCourses = new ArrayList<>();

    public Course(String id, String name, String instructorId, List<String> prerequisites, int studentsEnrolled, int classSize) {
        this.id = id;
        this.name = name;
        this.instructorId = instructorId;
        this.prerequisites = prerequisites;
        this.studentsEnrolled = studentsEnrolled;
        this.classSize = classSize;
        allCourses.add(this);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getInstructorId() { return instructorId; }
    public List<String> getPrerequisites() { return prerequisites; }
    public int getStudentsEnrolled() { return studentsEnrolled; }
    public int getClassSize() { return classSize; }

    public boolean isFull() {
        return studentsEnrolled >= classSize;
    }

    public void incrementEnrollment() {
        this.studentsEnrolled++;
    }

    public void updateEnrollmentCount() {
        this.studentsEnrolled++;
        // Save updated course data to file
        List<Course> allCourses = loadCourses();
        List<String[]> courseData = new ArrayList<>();
        for (Course course : allCourses) {
            String[] data = {
                    course.getId(),
                    course.getName(),
                    course.getInstructorId(),
                    course.getPrerequisites().toString(),
                    "[" + course.getStudentsEnrolled() + "," + course.getClassSize() + "]"
            };
            courseData.add(data);
        }
        FileUtils.writeStructuredData("", "courses.txt", new String[]{"id", "name", "instructor", "prerequisites", "capacity"}, courseData);
    }

    public static List<Course> loadCourses() {
        List<Course> courses = new ArrayList<>();
        List<String[]> data = FileUtils.readStructuredData("", "courses.txt");

        for (String[] row : data) {
            if (row.length >= 5) {
                String id = row[0].trim();
                String name = row[1].trim();
                String instructorId = row[2].trim();
                List<String> prereqs = checkPrerequisites(row[3].trim());
                int[] capacity = checkCapacity(row[4].trim());

                courses.add(new Course(id, name, instructorId, prereqs, capacity[0], capacity[1]));
            }
        }
        return courses;
    }



    private static List<String> checkPrerequisites(String prereqStr) {
        List<String> prereqs = new ArrayList<>();
        for (String prereq : prereqStr.replace("[", "").replace("]", "").split(",")) {
            if (!prereq.trim().isEmpty()) prereqs.add(prereq.trim());
        }
        return prereqs;
    }

    public static List<String> parsePrerequisites(String prereqsInput) {
        List<String> prerequisites = new ArrayList<>();
        for (String prereq : prereqsInput.split(",")) {
            prerequisites.add(prereq.trim());
        }
        return prerequisites;
    }

    private static int[] checkCapacity(String capacityStr) {
        String[] parts = capacityStr.replace("[", "").replace("]", "").split(",");
        int enrolled = Integer.parseInt(parts[0].trim());
        int size = Integer.parseInt(parts[1].trim());
        return new int[]{enrolled, size};
    }

    public static Course findCourseById(String id) {
        for (Course course : allCourses) {

            if (course.getId().equals(id)) {
                return course;
            }
        }
        return null;
    }

    public static void displayAllCourses() {
        List<Course> courses = loadCourses();
        Display.displayMessage("\nAvailable Courses:");
        for (Course course : courses) {
            Display.displayMessage(course.toString());
        }
    }

    @Override
    public String toString() {
        return id + "::" + name + "::" + instructorId + "::" +
                prerequisites.toString() + "::" + studentsEnrolled + "::" + classSize;
    }

    //Create a dir under courses with courseID and make a list of assignments in assignments.txt and a empty submissions file
    public static boolean createCourseDirectoryAndFiles(String courseId, List<Assignment> assignments) {
        FileUtils.initializeCourseFiles(courseId, assignments);
        return true;
    }

    //Just adds a new course to the course file
    public static void appendCourseToFile(Course course) {
        String courseData = String.format("%s::%s::%s::%s::[%d,%d]##",
                course.getId(),
                course.getName(),
                course.getInstructorId(),
                course.getPrerequisites().toString(),
                course.getStudentsEnrolled(),
                course.getClassSize());

        try (FileWriter writer = new FileWriter("src/data/courses.txt", true)) {
            writer.write(courseData + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("Error appending course to file: " + e.getMessage());
        }
    }
}