package main;

import helpers.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Course {
    private String id;
    private String name;
    private String instructorId;
    private List<String> prerequisites;  // New field for prerequisites

    private static List<Course> allCourses = new ArrayList<>();

    public Course(String id, String name, String instructorId, List<String> prerequisites) {
        this.id = id;
        this.name = name;
        this.instructorId = instructorId;
        this.prerequisites = prerequisites;
        allCourses.add(this);
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getInstructorId() { return instructorId; }
    public List<String> getPrerequisites() { return prerequisites; }

    // Save course information to courses.txt
    public void saveCourseInfo() {
        String data = id + "," + name + "," + instructorId + "," + prerequisites.toString();
        FileUtils.writeToFile("", "courses.txt", data);
    }
    @Override
    public String toString() {
        return "Course ID: " + id + ", Name: " + name + ", Instructor ID: " + instructorId +
                ", Prerequisites: " + prerequisites;
    }


    // Load all courses from courses.txt
    public static List<Course> loadCourses() {
        List<Course> courses = new ArrayList<>();
        List<String> lines = FileUtils.readFromFile("", "courses.txt");

        for (String line : lines) {

            String[] parts = line.split(",", 4);


            if (parts.length < 4) {
                continue;  // Skip invalid lines
            }

            String id = parts[0].trim();
            String name = parts[1].trim();
            String instructorId = parts[2].trim();
            String prereqStr = parts[3].trim();

            // Parse the prerequisites
            String[] prereqArray = prereqStr.replace("[", "").replace("]", "").split(",");
            List<String> prereqs = new ArrayList<>();
            for (String prereq : prereqArray) {
                if (!prereq.trim().isEmpty()) {
                    prereqs.add(prereq.trim());
                }
            }

            courses.add(new Course(id, name, instructorId, prereqs));
        }
        return courses;
    }


    // Find a course by its ID
    public static Course findCourseById(String id) {
        for (Course course : allCourses) {
            if (course.getId().equals(id)) {
                return course;
            }
        }
        List<Course> courses = loadCourses();
        for (Course course : courses) {
            if (course.getId().equals(id)) {
                return course;
            }
        }
        return null;
    }

    public static void displayAllCourses() {
        List<Course> courses = loadCourses();
        System.out.println("\nAvailable Courses:");
        for (Course course : courses) {
            System.out.println(course);
        }
    }
}
