package main;

import helpers.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class Major {

    private String majorName;

    public Major(String majorName) {
        this.majorName = majorName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }


     // Gets a list of majors offered by a specific department.
    public static List<String> getMajors(String department) {
        List<String[]> data = FileUtils.readStructuredData("", "majors.txt");
        List<String> majors = new ArrayList<>();

        for (String[] row : data) {
            if (row.length > 0 && row[0].trim().equalsIgnoreCase(department)) {
                if (row.length > 1) {
                    String majorsStr = row[1].trim();
                    majorsStr = majorsStr.replaceAll("[\\[\\]]", ""); // Remove [ and ]
                    String[] majorArray = majorsStr.split(",");

                    for (String major : majorArray) {
                        majors.add(major.trim());
                    }
                }
                break;
            }
        }
        return majors;
    }

     //Gets the department name associated with a specific major.
    public static String getDepartmentForMajor(String majorName) {
        List<String[]> data = FileUtils.readStructuredData("", "majors.txt");

        for (String[] row : data) {
            if (row.length > 1) {
                String departmentName = row[0].trim();
                String majorsList = row[1].trim();

                majorsList = majorsList.replace("[", "").replace("]", "");
                String[] majors = majorsList.split(",");

                for (String major : majors) {
                    if (major.trim().equalsIgnoreCase(majorName)) {
                        return departmentName;
                    }
                }
            }
        }
        return "Department not found";
    }


     // Gets all department names available in the system.
    public static List<String> getAllDepartments() {
        List<String[]> data = FileUtils.readStructuredData("", "departments.txt");
        List<String> departmentNames = new ArrayList<>();

        for (String[] row : data) {
            if (row.length > 0) {
                departmentNames.add(row[0].trim());
            }
        }
        return departmentNames;
    }
}
