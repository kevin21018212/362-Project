package main;

import Interfaces.AdvisorInterface;
import users.Advisor;
import helpers.FileUtils;

import java.util.ArrayList;
import java.util.List;

import static users.Advisor.TIMES;

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
                    String[] holds = advisor[5].substring(0, advisor[5].length()).split(",");
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
                            if (!schedule[i * 8 + j].equals("-"))
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
        saveToData();
    }

    /**
     * @param id
     */
    @Override
    public void addSchedule(String id) {

    }

    /**
     * @param day
     * @param time
     * @param studentId
     * @return
     */
    @Override
    public boolean addMeeting(int day, int time, String studentId) {
        if (!advisor.getStudents().contains(studentId)) {
            System.out.println("Student not found");
            return false;
        }
        if (advisor.addMeeting(day, time, studentId)) {
            time += 9;
            System.out.println("Meeting scheduled successfully");
            messageAdvisor("Meeting with "+studentId, "Meeting at " + Advisor.Days.values()[day] + " " + time + ":00 scheduled for student " + studentId);
            messageStudent(studentId,"Meeting with Advisor", "Meeting with advisor on " + Advisor.Days.values()[day] + " at " + time + ":00 scheduled");
            saveToData();
            return true;
        } else {
            System.out.println("Meeting already scheduled for this time");
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
    public boolean cancelMeeting(int day, int time, String studentId) {
        if (!advisor.getStudents().contains(studentId)) {
            System.out.println("Student not found");
            return false;
        }
        if (advisor.cancelMeeting(day, time, studentId)) {
            time += 9;
            System.out.println("Meeting cancelled successfully");
            messageAdvisor("Meeting with"+studentId, "Meeting on " + Advisor.Days.values()[day] + " " + time + ":00 cancelled for student " + studentId);
            messageStudent(studentId,"Meeting with Advisor", "Meeting on " + Advisor.Days.values()[day] + " at " + time + ":00 cancelled");
            saveToData();
            return true;
        } else {
            System.out.println("Meeting not found");
            return false;
        }
    }

    /**
     *
     */
    @Override
    public void saveToData() {
        List<String[]> advisorData = FileUtils.readStructuredData("advisor", "advisors.txt");
        for (int i = 0; i < advisorData.size(); i++) {
            if (advisorData.get(i)[0].equals(advisor.getId())) {
                advisorData.remove(i);
                break;
            }
        }
        String[] schedule = new String[5 * 8];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                if (advisor.getSchedule()[i][j] != null) {
                    schedule[i * 8 + j] = String.join(",", advisor.getSchedule()[i][j]);
                } else {
                    schedule[i * 8 + j] = String.join(",", "-");
                }
            }
        }
        advisorData.add(new String[]{
                advisor.getId(),
                advisor.getName(),
                advisor.getEmail(),
                advisor.getDepartment(),
                String.join(",", advisor.getStudents()),
                String.join(",", advisor.getRegistrationHolds()),
                String.join(",", schedule)
        });

//        advisorData.add(stringSchedule);
        FileUtils.writeStructuredData("advisor", "advisors.txt", Advisor.ADVISOR_FIELDS, advisorData);

    }

    /**
     * @param message
     */
    @Override
    public void messageStudent(String ID, String subject, String message) {
        InboxController ic = new InboxController("-1", "DO NOT REPLY");
        ic.sendMessage(ID, subject, message);
    }

    /**
     * @param message
     */
    @Override
    public void messageAdvisor(String subject, String message) {
        InboxController ic = new InboxController("-1", "DO NOT REPLY");
        ic.sendMessage(advisor.getId(), subject, message);
    }

    @Override
    public void printSchedule() {
        // Print header with times
        System.out.print("              ");
        for (String time : TIMES) {
            System.out.printf("%-8s", time);
        }
        System.out.println();

        // Print schedule for each day
        int i = 1;
        for (Advisor.Days day : Advisor.Days .values()) {
            System.out.printf(i +". "+"%-9s", day);
            for (int timeSlot = 0; timeSlot < TIMES.length; timeSlot++) {
                String meeting = advisor.getSchedule()[day.ordinal()][timeSlot];
                // Center the meeting ID or dash in an 8-character space
                String display = meeting != null ? meeting : "-";
                int padding = (8 - display.length()) / 2;
                System.out.print(" ".repeat(padding) + display + " ".repeat(8 - display.length() - padding));
            }
            i += 1;
            System.out.println();
        }
    }

    /**
     * @return
     */
    @Override
    public Advisor getAdvisor() {
        return this.advisor;
    }
}