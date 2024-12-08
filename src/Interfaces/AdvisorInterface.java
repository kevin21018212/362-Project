package Interfaces;

import users.Advisor;
import users.Student;

import java.util.ArrayList;

public interface AdvisorInterface {
    void printSchedule();
    Advisor getAdvisor();
    Advisor getAdvisorFromData(String id);
    void addStudents(Advisor advisor, ArrayList<String> students);
    boolean cancelMeeting(int day, int time, String studentId);
    boolean addMeeting(int day, int time, String studentId);
    void saveToData();
    void messageStudent(String ID, String subject, String message);
    void messageAdvisor(String subject, String message);
    boolean removeStudent(String studentId);
    boolean hasValidAppointment(String studentId);
    boolean releaseRegistrationHold(Student student);
    String getAllStudents(boolean hold);
}
