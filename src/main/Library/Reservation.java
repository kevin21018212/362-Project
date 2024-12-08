package main.Library;

import java.time.Duration;
import java.time.LocalTime;

public class Reservation {
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

    @Override
    public String toString() {
        return "RoomID: " + roomId + ", StudentID: " + studentId + ", StartTime: " + startTime + ", Duration: " + duration.toMinutes() + " mins";
    }
}
