package main;

import Interfaces.AdvisorInterface;
import users.Advisor;
import helpers.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class AdvisorController implements AdvisorInterface {
    private Advisor advisor;

    public AdvisorController(String id) {
        advisor = getAdvisor(id);

    }

    @Override
    public Advisor getAdvisor(String id) {
        List<String[]> advisorData = FileUtils.readStructuredData("advisors", "advisors.txt");
        for (String[] advisors : advisorData) {
            if (advisors[0].equals(id)) {
                Advisor advisor = new Advisor(advisors[0], advisors[1], advisors[2], advisors[3]);
                addStudents(advisor, new ArrayList<>(List.of(advisors[4].split(","))));
                return advisor;
            }
        }
        return null;
    }

    /**
     * @param students
     */
    @Override
    public void addStudents(Advisor advisor, ArrayList<String> students) {
        for (String student : students) {
            advisor.addStudent(student);
        }
    }

    /**
     * @param id
     */
    @Override
    public void addSchedule(String id) {

    }

    /**
     * @param id
     * @return
     */
    @Override
    public String[][] getSchedule(String id) {
        return new String[0][]; //TODO
    }

    /**
     * @param day
     * @param time
     * @param studentId
     * @return
     */
    @Override
    public boolean cancelMeeting(int day, int time, String studentId) {
        if (advisor.cancelMeeting(day, time, studentId)) {
            System.out.println("Meeting cancelled successfully");
            return true;
        } else {
            System.out.println("Meeting not found");
            return false;
        }
    }

    /**
     * @param day
     * @param time
     * @param studentId
     * @return
     */
    @Override
    public boolean addMeeting(int day, int time, String studentId) {
        if (advisor.addMeeting(day, time, studentId)) {
            System.out.println("Meeting scheduled successfully");
            return true;
        } else {
            System.out.println("Meeting already scheduled for this time");
            return false;
        }
    }

    @Override
    public void printSchedule() {
        advisor.printSchedule();
    }
}