package main.Library;

import users.DataAccess;
import users.Student;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private String id;
    private int capacity;
    private List<String> allowedMajors;

    public Room(String id, int capacity, List<String> allowedMajors) {
        this.id = id;
        this.capacity = capacity;
        this.allowedMajors = allowedMajors;
    }

    public String getId() {
        return id;
    }

    public boolean isMajorAllowed(String major) {
        return allowedMajors.contains("Open") || allowedMajors.contains(major);
    }

    // Show room schedule
    public void showSchedule(List<Reservation> reservations) {
        System.out.println("Schedule for Room: " + id);
        boolean hasReservations = false;

        for (Reservation reservation : reservations) {
            if (reservation.getRoomId().equalsIgnoreCase(id)) {
                System.out.println("Reserved: " + reservation.getStartTime() + " - " + reservation.getEndTime());
                hasReservations = true;
            }
        }

        if (!hasReservations) {
            System.out.println("No reservations found for this room.");
        }
    }

    // Reserve room
    public void reserveRoom(String studentId, String startTimeStr, int durationMinutes) {
        // Find the student
        Student student = DataAccess.findStudentById(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        // Check if the student's major is allowed
        if (!isMajorAllowed(student.getMajor())) {
            System.out.println("Student's major is not allowed to reserve this room.");
            return;
        }

        // Parse start time
        LocalTime startTime;
        try {
            startTime = LocalTime.parse(startTimeStr.trim());
        } catch (Exception e) {
            System.out.println("Invalid time format. Please use HH:mm.");
            return;
        }

        Duration duration = Duration.ofMinutes(durationMinutes);
        List<Reservation> reservations = Reservation.loadReservations();

        // Check for overlapping reservations
        for (Reservation reservation : reservations) {
            if (reservation.getRoomId().equalsIgnoreCase(id) && reservation.overlaps(startTime, duration)) {
                System.out.println("Time slot overlaps with an existing reservation!");
                return;
            }
        }

        // Add and save the new reservation
        reservations.add(new Reservation(id, studentId, startTime, duration));
        Reservation.saveReservations(reservations);
        System.out.println("Room reserved successfully!");
    }

    @Override
    public String toString() {
        return "Room: " + id + ", Capacity: " + capacity + ", Allowed Majors: " + allowedMajors;
    }
}
