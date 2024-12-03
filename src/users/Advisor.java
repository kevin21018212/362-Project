package users;

import helpers.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Advisor extends User {
    private String department;
    private HashSet<String> students;
    private List<String>[][] schedule;
    public static enum Days {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY
    }
    public static final String[] ADVISOR_FIELDS = {"id::name::email::department::[students]::[schedule]"};

    public Advisor(String id, String name, String email, String department) {
        super(id, name, email);
        this.department = department;
        this.students = new HashSet<>();
        this.schedule = new ArrayList[5][8];
    }

    public String getDepartment() {
        return department;
    }

    public HashSet<String> getStudents() {
        return students;
    }

    public List<String>[][] getSchedule() {
        return schedule;
    }

    public boolean addStudent(String studentId) {
        if (students.contains(studentId)) {
            return false;
        }
        students.add(studentId);
        return true;
    }

    public boolean removeStudent(String studentId) {
        return students.remove(studentId);
    }

    public boolean addMeeting(int day, int time, String studentId) {
        if (day < Days.MONDAY.ordinal()|| day > Days.FRIDAY.ordinal() || time < 0 || time >= 8) {
            return false;
        }
        if (!schedule[day][time].isEmpty()) {
            return false;
        }
        schedule[day][time].add(studentId);
        return true;
    }

    public boolean cancelMeeting(int day, int time, String studentId) {
        if (day < Days.MONDAY.ordinal()|| day > Days.FRIDAY.ordinal() || time < 0 || time >= 8) {
            return false;
        }
        return schedule[day][time].remove(studentId);
    }

    public void printSchedule() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Days.values()[i]);
            for (int j = 0; j < 8; j++) {
                System.out.println("Time: " + j);
                for (String student : schedule[i][j]) {
                    System.out.println(student);
                }
            }
        }
    }
}