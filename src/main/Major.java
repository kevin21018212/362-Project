package main;

import helpers.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class Major {
    private static final String DEPARTMENT_FILE = "departments.txt";
    private static final String MAJOR_COURSE_FILE = "majorCourses.txt";

    /**
     * Retrieves a list of all departments from the departments file.
     *
     * @return A list of department names.
     */
    public List<String> getAllDepartments() {
        List<String[]> data = FileUtils.readStructuredData("", DEPARTMENT_FILE);
        List<String> departmentNames = new ArrayList<>();

        for (String[] row : data) {
            if (row.length > 0) {
                departmentNames.add(row[0].trim());
            }
        }
        return departmentNames;
    }

    /**
     * Retrieves a list of all majors for a given department.
     *
     * @param department The department name.
     * @return A list of majors under the specified department.
     */
    public List<String> getMajors(String department) {
        List<String[]> data = FileUtils.readStructuredData("", DEPARTMENT_FILE);
        List<String> majors = new ArrayList<>();

        for (String[] row : data) {
            if (row.length > 0 && row[0].trim().equalsIgnoreCase(department)) {
                if (row.length > 1) {
                    String majorsStr = row[1].trim();
                    // Remove brackets and split by commas
                    majorsStr = majorsStr.replaceAll("[\\[\\]]", "");
                    String[] majorArray = majorsStr.split(",");

                    // Add each major to the list, trimming whitespace
                    for (String major : majorArray) {
                        majors.add(major.trim());
                    }
                }
                break;
            }
        }
        return majors;
    }

    /**
     * Displays the list of majors for a given department.
     *
     * @param department The department name.
     */
    public void displayMajors(String department) {
        List<String> majors = getMajors(department);
        System.out.println("Majors in " + department + " Department:");
        majors.forEach(System.out::println);
    }

    /**
     * Retrieves the department for a given major.
     *
     * @param major The name of the major.
     * @return The name of the department the major belongs to.
     */
    public String getDepartmentByMajor(String major) {
        List<String[]> data = FileUtils.readStructuredData("", DEPARTMENT_FILE);

        for (String[] row : data) {
            if (row.length > 1) {
                String departmentName = row[0].trim();
                String majorsList = row[1].trim();

                // Remove the square brackets and split by commas
                majorsList = majorsList.replaceAll("[\\[\\]]", "");
                String[] majors = majorsList.split(",");

                // Check if the specified major is in the majors array
                for (String m : majors) {
                    if (m.trim().equalsIgnoreCase(major)) {
                        return departmentName;
                    }
                }
            }
        }
        return "Department not found";
    }

    /**
     * Retrieves the list of courses required for a given major.
     *
     * @param major The name of the major.
     * @return A list of required courses for the specified major.
     */
    public List<String> getCoursesByMajor(String major) {
        List<String[]> data = FileUtils.readStructuredData("", MAJOR_COURSE_FILE);
        List<String> courses = new ArrayList<>();

        for (String[] row : data) {
            if (row.length > 0 && row[0].trim().equalsIgnoreCase(major)) {
                if (row.length > 1) {
                    String coursesStr = row[1].trim();
                    // Remove brackets and split by commas
                    coursesStr = coursesStr.replaceAll("[\\[\\]]", "");
                    String[] courseArray = coursesStr.split(",");

                    // Add each course to the list, trimming whitespace
                    for (String course : courseArray) {
                        courses.add(course.trim());
                    }
                }
                break;
            }
        }
        return courses;
    }
}
