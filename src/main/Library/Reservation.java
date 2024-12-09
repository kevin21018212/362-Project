package main.Library;

import helpers.FileUtils;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Reservation {
    private static final String DIRECTORY = "library/";
    private static final String RESERVATIONS_FILE = "reservations.txt";

    private String roomId;
    private String studentId;
    private LocalTime startTime;
    private Duration duration;

    public Reservation(String roomId, String studentId, LocalTime startTime, Duration duration) {
        this.roomId = roomId;
        this.studentId = studentId;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getStudentId() {
        return studentId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalTime getEndTime() {
        return startTime.plus(duration);
    }

    public boolean overlaps(LocalTime newStart, Duration newDuration) {
        LocalTime newEnd = newStart.plus(newDuration);
        return !newEnd.isBefore(startTime) && !newStart.isAfter(getEndTime());
    }

    // Static method to load all reservations
    public static List<Reservation> loadReservations() {
        List<String[]> reservationData = FileUtils.readStructuredData(DIRECTORY, RESERVATIONS_FILE);
        List<Reservation> reservations = new ArrayList<>();

        for (String[] row : reservationData) {
            try {
                String roomId = row[0].trim();
                String studentId = row[1].trim();
                LocalTime startTime = LocalTime.parse(row[2].trim().replace(".", ":"));
                Duration duration = Duration.ofMinutes(Long.parseLong(row[3].trim()));

                reservations.add(new Reservation(roomId, studentId, startTime, duration));
            } catch (Exception e) {
                System.err.println("Error parsing reservation: " + String.join("::", row));
                e.printStackTrace();
            }
        }
        return reservations;
    }

    // Static method to write reservations
    public static void saveReservations(List<Reservation> reservations) {
        List<String[]> data = new ArrayList<>();
        for (Reservation reservation : reservations) {
            data.add(new String[]{
                    reservation.getRoomId(),
                    reservation.getStudentId(),
                    reservation.getStartTime().toString().replace(":", "."),
                    String.valueOf(reservation.getDuration().toMinutes())
            });
        }
        FileUtils.writeStructuredData(DIRECTORY, RESERVATIONS_FILE, new String[]{"RoomID", "StudentID", "StartTime", "DurationMinutes"}, data);
    }

    @Override
    public String toString() {
        return "RoomID: " + roomId + ", StudentID: " + studentId + ", StartTime: " + startTime + ", Duration: " + duration.toMinutes() + " mins";
    }
}