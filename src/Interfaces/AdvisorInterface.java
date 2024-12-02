package Interfaces;

import java.util.ArrayList;

public interface AdvisorInterface {
    void populateData(String id);
    public String getDepartment();
    public ArrayList<String> getStudents();
    public int[][] getSchedule();
    public boolean addStudent(String studentId);
    public boolean removeStudent(String studentId);
    public boolean addMeeting(int day, int time);
}
