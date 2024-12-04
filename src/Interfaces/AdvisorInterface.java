package Interfaces;

import users.Advisor;

import java.util.ArrayList;

public interface AdvisorInterface {
    void printSchedule();
    Advisor getAdvisor();
    Advisor getAdvisorFromData(String id);
    void addStudents(Advisor advisor, ArrayList<String> students);
    void addSchedule(String id);
    boolean cancelMeeting(int day, int time, String studentId);
    boolean addMeeting(int day, int time, String studentId);
    void saveToData();
    void messageStudent(String ID, String subject, String message);
    void messageAdvisor(String subject, String message);
}
