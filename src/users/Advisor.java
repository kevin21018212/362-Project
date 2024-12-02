package users;

import helpers.User;

import java.util.ArrayList;
import java.util.Arrays;

public class Advisor extends User {
    private String department;
    private ArrayList<String> students;
    private ArrayList<Integer>[] schedule;
    public Advisor(String id, String name, String email, String department) {
        super(id, name, email);
        this.department = department;
        this.students = new ArrayList<>();
        this.schedule = new ArrayList[5];
    }

    private void getData() {
        
    }

    public String getDepartment() {
        return department;
    }

    public ArrayList<String> getStudents() {
        return students;
    }

    public ArrayList<Integer>[] getSchedule() {
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

    public boolean addMeeting(int day, int time) {
        if (day < 0 || day >= 5 || time < 0 || time >= 8) {
            return false;
        }
        if (schedule[day] == null) {
            schedule[day] = new ArrayList<>();
        }
        if (schedule[day].contains(time)) {
            return false;
        }
        schedule[day].add(time);
        return true;
    }

}
