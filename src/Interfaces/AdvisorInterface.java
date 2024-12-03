package Interfaces;

import users.Advisor;

import java.util.ArrayList;

public interface AdvisorInterface {
    void printSchedule();
    Advisor getAdvisor(String id);
    void addStudents(Advisor advisor, ArrayList<String> students);
    void addSchedule(String id);
    String[][] getSchedule(String id);
    boolean cancelMeeting(int day, int time, String studentId);
    boolean addMeeting(int day, int time, String studentId);
}
