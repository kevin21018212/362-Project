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
    private static List<Course> allCourses = new ArrayList<>();

    public Course(String id, String name, String instructorId, List<String> prerequisites) {
        this.id = id;
        this.name = name;
        this.instructorId = instructorId;
        this.prerequisites = prerequisites;
        allCourses.add(this);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getInstructorId() { return instructorId; }
    public List<String> getPrerequisites() { return prerequisites; }

    public void saveCourseInfo() {
        String data = id + "," + name + "," + instructorId + "," + prerequisites.toString();
        FileUtils.writeToFile("", "courses.txt", data);
    }

    public static List<Course> loadCourses() {
        List<Course> courses = new ArrayList<>();
        List<String> lines = FileUtils.readFromFile("", "courses.txt");

        for (String line : lines) {
            String[] parts = line.split(",", 4);
            if (parts.length < 4) continue;
            String id = parts[0].trim();
            String name = parts[1].trim();
            String instructorId = parts[2].trim();
            String prereqStr = parts[3].trim();
            List<String> prereqs = new ArrayList<>();
            for (String prereq : prereqStr.replace("[", "").replace("]", "").split(",")) {
                if (!prereq.trim().isEmpty()) prereqs.add(prereq.trim());
            }
            courses.add(new Course(id, name, instructorId, prereqs));
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

    public void displayAssignments() {
       Display.displayMessage("Assignments for Course: " + this.name);
        List<Assignment> assignments = Assignment.loadAssignments(this.id);
        for (Assignment assignment : assignments) {
           Display.displayMessage(assignment.toString());
        }
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
        return "Course ID: " + id + ", Name: " + name + ", Instructor ID: " + instructorId + ", Prerequisites: " + prerequisites;
    }
}
