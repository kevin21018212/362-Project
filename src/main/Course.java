package main;

import helpers.Display;
import helpers.FileUtils;
import java.util.ArrayList;
import java.util.List;

public class Course {
    private String id;
    private String name;
    private String instructorId;
    private List<String> prerequisites;
    private int studentsEnrolled;
    private int classSize;
    private static List<Course> allCourses = new ArrayList<>();

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

    public static List<Course> loadCourses() {
        List<Course> courses = new ArrayList<>();
        List<String> lines = FileUtils.readFromFile("", "courses.txt");

        for (String line : lines) {
            String[] parts = line.split(",", 5);
            if (parts.length < 5) continue;
            String id = parts[0].trim();
            String name = parts[1].trim();
            String instructorId = parts[2].trim();
            List<String> prereqs = checkPrerequisites(parts[3].trim());
            int[] capacity = checkCapacity(parts[4].trim());

            courses.add(new Course(id, name, instructorId, prereqs, capacity[0], capacity[1]));
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
        return "Course ID: " + id + ", Name: " + name + ", Instructor ID: " + instructorId +
                ", Prerequisites: " + prerequisites + ", Enrollment: " + studentsEnrolled + "/" + classSize;
    }
}

