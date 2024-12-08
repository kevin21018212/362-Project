package users;

import helpers.User;
import java.util.HashSet;

/**
 * Represents an academic advisor in the system.
 * Extends the User class to inherit basic user functionality.
 */
public class Advisor extends User {
    private String department;
    private HashSet<String> students;
    private HashSet<String> registrationHolds;
    private String[][] schedule;

    /** Enumeration of valid weekdays for scheduling */
    public static enum Days {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY}

    /** Available time slots for meetings */
    public static final String[] TIMES = {"9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"};

    /** Field definitions for advisor data storage */
    public static final String[] ADVISOR_FIELDS = {"id::name::email::department::[students]::[registrationHolds]::[schedule]"};

    private boolean canOverrideHold;

    /**
     * Constructs a new Advisor with specified details.
     * @param id The unique identifier for the advisor
     * @param name The advisor's full name
     * @param email The advisor's email address
     * @param department The department the advisor belongs to
     */
    public Advisor(String id, String name, String email, String department) {
        super(id, name, email);
        this.department = department;
        this.students = new HashSet<>();
        this.registrationHolds = new HashSet<>();
        this.schedule = new String[5][8];
        this.canOverrideHold = true;
    }

    /**
     * Gets the advisor's department.
     * @return The department name
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Gets the set of students assigned to this advisor.
     * @return HashSet of student IDs
     */
    public HashSet<String> getStudents() {
        return students;
    }

    /**
     * Gets the set of registration holds.
     * @return HashSet of student IDs with holds
     */
    public HashSet<String> getRegistrationHolds() {
        return registrationHolds;
    }

    /**
     * Gets the advisor's schedule matrix.
     * @return 2D array representing the weekly schedule
     */
    public String[][] getSchedule() {
        return schedule;
    }

    /**
     * Adds a registration hold for a student.
     * @param hold Student ID to place on hold
     */
    public void addRegistrationHold(String hold) {
        registrationHolds.add(hold);
    }

    /**
     * Adds a student to the advisor's list.
     * @param studentId ID of the student to add
     * @return true if student was added successfully, false if already exists
     */
    public boolean addStudent(String studentId) {
        if (students.contains(studentId)) {
            return false;
        }
        students.add(studentId);
        return true;
    }

    /**
     * Removes a student from the advisor's list.
     * @param studentId ID of the student to remove
     * @return true if student was removed successfully, false if not found
     */
    public boolean removeStudent(String studentId) {
        return students.remove(studentId);
    }

    /**
     * Schedules a meeting with a student.
     * @param day Day of the week (0-4)
     * @param time Time slot (0-7)
     * @param studentId ID of the student
     * @return true if meeting was scheduled successfully, false if slot is occupied or invalid
     */
    public boolean addMeeting(int day, int time, String studentId) {
        if (day < Days.MONDAY.ordinal()|| day > Days.FRIDAY.ordinal() || time < 0 || time >= 8) {
            return false;
        }
        if (schedule[day][time] != null) {
            return false;
        }
        schedule[day][time] = studentId;
        return true;
    }

    /**
     * Cancels a scheduled meeting.
     * @param day Day of the week (0-4)
     * @param time Time slot (0-7)
     * @param studentId ID of the student
     * @return true if meeting was cancelled successfully, false if not found or invalid
     */
    public boolean cancelMeeting(int day, int time, String studentId) {
        if (day < Days.MONDAY.ordinal()|| day > Days.FRIDAY.ordinal() || time < 0 || time >= 8) {
            return false;
        }
        if (!schedule[day][time].equals(studentId)) {
            return false;
        }
        schedule[day][time] = null;
        return true;
    }

    /**
     * Checks if the advisor has permission to override holds.
     * @return true if advisor can override holds, false otherwise
     */
    public boolean canOverrideHold() {
        return canOverrideHold;
    }
}