package main.Library;

import helpers.FileUtils;

import main.Room;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Library {
    private static final String DIRECTORY = "library/";
    private static final String ROOMS_FILE = "rooms.txt";
    private static final String RESERVATIONS_FILE = "reservations.txt";

    // Load all rooms from rooms.txt
    public static List<Room> loadRooms() {
        List<String[]> roomData = FileUtils.readStructuredData(DIRECTORY, ROOMS_FILE);
        List<Room> rooms = new ArrayList<>();

        for (String[] row : roomData) {
            String id = row[0];
            int capacity = Integer.parseInt(row[1]);
            String[] majorsArray = row[2].replace("[", "").replace("]", "").split(", ");
            List<String> allowedMajors = List.of(majorsArray);
            rooms.add(new Room(id, capacity, allowedMajors));
        }
        return rooms;
    }

    // Load all reservations from reservations.txt
    public static List<Reservation> loadReservations() {
        List<String[]> reservationData = FileUtils.readStructuredData(DIRECTORY, RESERVATIONS_FILE);
        List<Reservation> reservations = new ArrayList<>();

        for (String[] row : reservationData) {
            try {
                // Parse room ID, student ID, start time, and duration
                String roomId = row[0].trim();
                String studentId = row[1].trim();
                String startTimeRaw = row[2].trim().replace(".", ":"); // Replace . with : for parsing
                LocalTime startTime = LocalTime.parse(startTimeRaw); // Parse fixed start time
                Duration duration = Duration.ofMinutes(Long.parseLong(row[3].trim()));

                // Add a valid reservation to the list
                reservations.add(new Reservation(roomId, studentId, startTime, duration));
            } catch (Exception e) {
                System.err.println("Error parsing reservation: " + String.join("::", row));
                e.printStackTrace();
            }
        }

        return reservations;
    }


    // Show all rooms
    public static void showAllRooms() {
        List<Room> rooms = loadRooms();
        for (Room room : rooms) {
            System.out.println(room);
        }
    }

    // Show room schedule
    public static void showRoomSchedule(String roomId) {
        List<Reservation> reservations = loadReservations();
        System.out.println("Schedule for Room: " + roomId);

        boolean hasReservations = false;
        for (Reservation reservation : reservations) {
            if (reservation.getRoomId().equalsIgnoreCase(roomId)) {
                System.out.println("Reserved: " + reservation.getStartTime() + " - " + reservation.getEndTime());
                hasReservations = true;
            }
        }

        if (!hasReservations) {
            System.out.println("No reservations found for this room.");
        }
    }





    // Reserve a room
    public static void reserveRoom(String roomId, String startTimeStr, int durationMinutes, String studentId) {
        List<Room> rooms = loadRooms();
        List<Reservation> reservations = loadReservations();

        // Validate room ID
        boolean roomExists = rooms.stream().anyMatch(room -> room.getId().equalsIgnoreCase(roomId));
        if (!roomExists) {
            System.out.println("Invalid room ID. Please select a valid room.");
            return;
        }

        // Parse start time from human-friendly format
        LocalTime startTime;
        try {
            startTime = LocalTime.parse(startTimeStr.trim());
        } catch (Exception e) {
            System.out.println("Invalid time format. Please use HH:mm.");
            return;
        }

        Duration duration = Duration.ofMinutes(durationMinutes);

        // Check for overlapping reservations
        for (Reservation reservation : reservations) {
            if (reservation.getRoomId().equalsIgnoreCase(roomId) && reservation.overlaps(startTime, duration)) {
                System.out.println("Time slot overlaps with an existing reservation!");
                return;
            }
        }

        // Add the new reservation
        reservations.add(new Reservation(roomId, studentId, startTime, duration));
        List<String[]> updatedReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            updatedReservations.add(new String[]{
                    reservation.getRoomId(),
                    reservation.getStudentId(),
                    reservation.getStartTime().toString().replace(":", "."), // Convert to file format
                    String.valueOf(reservation.getDuration().toMinutes())
            });
        }
        FileUtils.writeStructuredData(DIRECTORY, RESERVATIONS_FILE, new String[]{"RoomID", "StudentID", "StartTime", "DurationMinutes"}, updatedReservations);
        System.out.println("Room reserved successfully!");
    }
}
