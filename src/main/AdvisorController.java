package main;

import Interfaces.AdvisorInterface;
import helpers.Display;
import helpers.Utils;
import users.Advisor;
import helpers.FileUtils;
import users.Student;
import java.util.ArrayList;
import java.util.List;
import static users.Advisor.TIMES;

/**
 * Controller class for managing advisor-related operations in the academic system.
 * Implements the AdvisorInterface to provide advisor management functionality.
 */
public class AdvisorController implements AdvisorInterface {
    private Advisor advisor;
    private static final double GPA_THRESHOLD = 2.0;
    private static final String HOLD_RELEASE_MESSAGE = "Your registration hold has been released.";
    private static final String HOLD_DENIED_GPA_MESSAGE = "Your registration hold cannot be released due to GPA requirements.";
    private static final String HOLD_DENIED_BALANCE_MESSAGE = "Your registration hold cannot be released due to outstanding balance.";

    /**
     * Constructs an AdvisorController with the specified advisor ID.
     * @param id The unique identifier of the advisor
     */
    public AdvisorController(String id) {
        this.advisor = getAdvisorFromData(id);
    }

    /**
     * Retrieves advisor information from the data storage.
     * @param id The unique identifier of the advisor to retrieve
     * @return Advisor object if found, null otherwise
     */
    @Override
    public Advisor getAdvisorFromData(String id) {
        List<String[]> advisorData = FileUtils.readStructuredData("advisor", "advisors.txt");
        for (String[] advisor : advisorData) {
            if (advisor[0].equals(id)) {
                Advisor newAdvisor = new Advisor(
                        advisor[0], // id
                        advisor[1], // name
                        advisor[2], // email
                        advisor[3]  // department
                );

                if (advisor.length > 4 && advisor[4] != null && !advisor[4].isEmpty()) {
                    String[] students = advisor[4].substring(0, advisor[4].length()).split(",");
                    for (String studentId : students) {
                        if (!studentId.trim().isEmpty()) {
                            newAdvisor.addStudent(studentId.trim());
                        }
                    }
                }

                if (advisor.length > 5 && advisor[5] != null && !advisor[5].isEmpty()) {
                    String[] holds = advisor[5].substring(0, advisor[5].length()).split(",");
                    for (String hold : holds) {
                        if (!hold.trim().isEmpty()) {
                            newAdvisor.addRegistrationHold(hold.trim());
                        }
                    }
                }

                if (advisor.length > 6 && advisor[6] != null && !advisor[6].isEmpty()) {
                    String[] schedule = advisor[6].substring(0, advisor[6].length()).split(",");
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
     * Adds multiple students to an advisor's list.
     * @param advisor The advisor to whom students will be added
     * @param students List of student IDs to be added
     */
    @Override
    public void addStudents(Advisor advisor, ArrayList<String> students) {
        for (String student : students) {
            advisor.addStudent(student);
        }
        saveToData();
    }

    /**
     * Removes a student from the advisor's list.
     * @param studentId The ID of the student to remove
     * @return true if student was successfully removed, false otherwise
     */
    @Override
    public boolean removeStudent(String studentId) {
        if (!advisor.getStudents().contains(studentId)) {
            System.out.println("Student not found");
            return false;
        }
        advisor.removeStudent(studentId);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                if (advisor.getSchedule()[i][j] != null && advisor.getSchedule()[i][j].equals(studentId)) {
                    advisor.getSchedule()[i][j] = null;
                }
            }
        }
        saveToData();
        return true;
    }

    @Override
    public String getAllStudents(boolean hold) {
        if (!hold) {
            return String.join(", ", advisor.getStudents());
        } else {
            return String.join(", ", advisor.getRegistrationHolds());
        }
    }

    /**
     * Schedules a meeting between advisor and student.
     * @param day The day of the week (0-4)
     * @param time The time slot (0-7)
     * @param studentId The ID of the student
     * @return true if meeting was successfully scheduled, false otherwise
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
            messageAdvisor("Meeting with "+studentId,
                    "Meeting at " + Advisor.Days.values()[day] + " " + time + ":00 scheduled for student " + studentId);
            messageStudent(studentId,"Meeting with Advisor",
                    "Meeting with advisor on " + Advisor.Days.values()[day] + " at " + time + ":00 scheduled");
            saveToData();
            return true;
        } else {
            System.out.println("Meeting already scheduled for this time");
            return false;
        }
    }

    /**
     * Cancels a scheduled meeting.
     * @param day The day of the week (0-4)
     * @param time The time slot (0-7)
     * @param studentId The ID of the student
     * @return true if meeting was successfully cancelled, false otherwise
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
            messageAdvisor("Meeting with"+studentId,
                    "Meeting on " + Advisor.Days.values()[day] + " " + time + ":00 cancelled for student " + studentId);
            messageStudent(studentId,"Meeting with Advisor",
                    "Meeting on " + Advisor.Days.values()[day] + " at " + time + ":00 cancelled");
            saveToData();
            return true;
        } else {
            System.out.println("Meeting not found");
            return false;
        }
    }

    /**
     * Saves the current advisor data to storage.
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
        String[] schedule = new String[40];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                schedule[i * 8 + j] = advisor.getSchedule()[i][j] != null ? advisor.getSchedule()[i][j] : "-";
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
        FileUtils.writeStructuredData("advisor", "advisors.txt", Advisor.ADVISOR_FIELDS, advisorData);
    }

    /**
     * Sends a message to a student.
     * @param ID The student's ID
     * @param subject The message subject
     * @param message The message content
     */
    @Override
    public void messageStudent(String ID, String subject, String message) {
        InboxController ic = new InboxController("-1", "DO NOT REPLY");
        ic.sendMessage(ID, subject, message);
    }

    /**
     * Sends a message to the advisor.
     * @param subject The message subject
     * @param message The message content
     */
    @Override
    public void messageAdvisor(String subject, String message) {
        InboxController ic = new InboxController("-1", "DO NOT REPLY");
        ic.sendMessage(advisor.getId(), subject, message);
    }

    /**
     * Checks if a student has a valid appointment.
     * @param studentId The ID of the student to check
     * @return true if student has a valid appointment, false otherwise
     */
    @Override
    public boolean hasValidAppointment(String studentId) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                if (advisor.getSchedule()[i][j] != null && advisor.getSchedule()[i][j].equals(studentId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Attempts to release a registration hold for a student.
     * @param student The student whose hold should be released
     * @return true if hold was successfully released, false otherwise
     */
    @Override
    public boolean releaseRegistrationHold(Student student) {
        if (!this.advisor.canOverrideHold()) {
            System.out.println("You do not have permission to remove holds.");
            return false;
        }
        if (!this.advisor.getStudents().contains(student.getId())) {
            System.out.println("Student is not assigned to this advisor.");
            return false;
        }
        if (!this.advisor.getRegistrationHolds().contains(student.getId())) {
            System.out.println("Student does not have a registration hold.");
            return false;
        }

        if (!hasValidAppointment(student.getId())) {
            System.out.println("Student does not have an appointment.");
            System.out.println("Would you like to...\n1. Exit and schedule an appointment\n2. Exit and have system notify student to make an appointment\n3. Override hold");
            String choice = Utils.getInput("Enter choice: ");
            switch (choice) {
                case "1": return false;
                case "2":
                    messageStudent(student.getId(),"You Have a Registration Hold", "Please schedule an appointment with your advisor.");
                    return false;
                case "3": return overrideRegistrationHold(student);
                default: System.out.println("Invalid option");
            }
            return false;
        }

        if (student.getGPA() < GPA_THRESHOLD) {
            Display.displayMessage("Cannot release hold: GPA below threshold (" + student.getGPA() + ")");
            messageStudent(student.getId(),"You Have a Registration Hold", HOLD_DENIED_GPA_MESSAGE);
            String choice = Utils.getInput("Override hold? (Y/N)");
            if (choice.equalsIgnoreCase("Y")) {
                return overrideRegistrationHold(student);
            }
            return false;
        }

        if (student.getTuitionAmount() != 0) {
            Display.displayMessage("Cannot release hold: Outstanding balance exists");
            messageStudent(student.getId(),"You Have a Registration Hold", HOLD_DENIED_BALANCE_MESSAGE);
            String choice = Utils.getInput("Override hold? (Y/N)");
            if (choice.equalsIgnoreCase("Y")) {
                return overrideRegistrationHold(student);
            }
            return false;
        }

        String choice = Utils.getInput("Release registration hold? (Y/N)");
        if (choice.equalsIgnoreCase("Y")) {
            this.advisor.getRegistrationHolds().remove(student.getId());
            saveToData();
            return true;
        }
        return false;
    }

    /**
     * Overrides a registration hold for a student.
     * @param student The student whose hold should be overridden
     * @return true if hold was successfully overridden, false otherwise
     */
    public boolean overrideRegistrationHold(Student student) {
        if (!this.advisor.canOverrideHold()) {
            Display.displayMessage("You do not have permission to override holds.");
            return false;
        }
        if (!this.advisor.getRegistrationHolds().contains(student.getId())) {
            Display.displayMessage("Student does not have a registration hold.");
            return false;
        }
        Display.displayMessage("Are you sure you want to override the hold? (Y/N)");
        String choice = Utils.getInput("Release registration hold? (Y/N)");
        if (choice.equalsIgnoreCase("Y")) {
            this.advisor.getRegistrationHolds().remove(student.getId());
            saveToData();
            return true;
        }
        return false;
    }

    /**
     * Gets the advisor object associated with this controller.
     * @return The Advisor object
     */
    @Override
    public Advisor getAdvisor() {
        return this.advisor;
    }

    /**
     * Prints the advisor's schedule in a formatted table.
     */
    @Override
    public void printSchedule() {
        System.out.print("              ");
        for (String time : TIMES) {
            System.out.printf("%-8s", time);
        }
        System.out.println();
        int i = 1;
        for (Advisor.Days day : Advisor.Days.values()) {
            System.out.printf(i +". "+"%-9s", day);
            for (int timeSlot = 0; timeSlot < TIMES.length; timeSlot++) {
                String meeting = advisor.getSchedule()[day.ordinal()][timeSlot];
                String display = meeting != null ? meeting : "-";
                int padding = (8 - display.length()) / 2;
                System.out.print(" ".repeat(padding) + display + " ".repeat(8 - display.length() - padding));
            }
            i += 1;
            System.out.println();
        }
    }
}