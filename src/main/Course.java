package main;

import helpers.FileUtils;
import java.util.ArrayList;
import java.util.List;

public class Course {
    private String id;
    private String name;

    private String instructorId;

    private static List<Course> allCourses = new ArrayList<>();

    public Course(String id, String name, String instructorId) {
        this.id = id;
        this.name = name;

        this.instructorId = instructorId;
        allCourses.add(this);

    }



    // Getters
    public String getId() { return id; }
    public String getName() { return name; }

    public String getInstructorId() { return instructorId; }

    // Save course information to courses.txt
    public void saveCourseInfo() {
        String data = id + "," + name +  "," + instructorId;
        FileUtils.writeToFile("data", "courses.txt", data);
    }

    // Load all courses from courses.txt
    public static List<Course> loadCourses() {
        List<Course> courses = new ArrayList<>();
        List<String> lines = FileUtils.readFromFile("data", "courses.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            courses.add(new Course(data[0], data[1], data[2]));
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
            System.out.println(course.id + ": " + course.name + " Instructor: " + course.instructorId );
        }
    }
}
