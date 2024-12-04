package main;

import Interfaces.AdvisorInterface;
import users.Advisor;
import helpers.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class AdvisorController implements AdvisorInterface {
    private Advisor advisor;

    public AdvisorController(String id) {
        this.advisor = getAdvisorFromData(id);

    }

    @Override
    public Advisor getAdvisorFromData(String id) {
        List<String[]> advisorData = FileUtils.readStructuredData("advisor", "advisors.txt");
        for (String[] advisor : advisorData) {
            if (advisor[0].equals(id)) {
                Advisor newAdvisor = new Advisor(
                        advisor[0],  // id
                        advisor[1],  // name
                        advisor[2],  // email
                        advisor[3]   // department
                );

                // Parse and add students if the data exists and isn't empty
                if (advisor.length > 4 && advisor[4] != null && !advisor[4].isEmpty()) {
                    String[] students = advisor[4].substring(0, advisor[4].length()).split(",");
                    for (String studentId : students) {
                        if (!studentId.trim().isEmpty()) {
                            newAdvisor.addStudent(studentId.trim());
                        }
                    }
                }

                // Parse and add registration holds if the data exists and isn't empty
                if (advisor.length > 5 && advisor[5] != null && !advisor[5].isEmpty()) {
                    String[] holds = advisor[5].substring(1, advisor[5].length() - 1).split(",");
                    for (String hold : holds) {
                        if (!hold.trim().isEmpty()) {
                            newAdvisor.addRegistrationHold(hold.trim());
                        }
                    }
                }

                // Parse and add schedule if the data exists and isn't empty
                if (advisor.length > 6 && advisor[6] != null && !advisor[6].isEmpty()) {
                    String[] schedule = advisor[6].substring(1, advisor[6].length() - 1).split(",");
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (!schedule[i * 8 + j].trim().isEmpty())
                                newAdvisor.addMeeting(i, j, schedule[i * 8 + j]);
                        }
                    }
                }

                return newAdvisor;
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

    /**
     *
     */
    @Override
    public void saveToData() {
        List<String[]> advisorData = new ArrayList<>();
        advisorData.add(new String[]{
                advisor.getId(),
                advisor.getName(),
                advisor.getEmail(),
                advisor.getDepartment(),
                String.join(",", advisor.getStudents()),
                String.join(",", advisor.getRegistrationHolds())
        });
        String[] schedule = new String[5 * 8];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                schedule[i * 8 + j] = String.join(",", advisor.getSchedule()[i][j]);
            }
        }
        advisorData.add(schedule);
        FileUtils.writeStructuredData("advisors", "advisors.txt", Advisor.ADVISOR_FIELDS, advisorData);

    }

    /**
     * @param message
     */
    @Override
    public void messageStudent(String message) {

    }

    /**
     * @param message
     */
    @Override
    public void messageAdvisor(String message) {

    }

    @Override
    public void printSchedule() {
        //TODO
    }

    /**
     * @return
     */
    @Override
    public Advisor getAdvisor() {
        return this.advisor;
    }
}