package main;

import helpers.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private static final String DIRECTORY = "library/";
    private static final String ROOMS_FILE = "rooms.txt";
    private static final String RESERVATIONS_FILE = "reservations.txt";

    // Show all rooms
    public static void showAllRooms() {
        List<String[]> rooms = FileUtils.readStructuredData(DIRECTORY, ROOMS_FILE);
        System.out.println("Available Rooms:");
        for (String[] room : rooms) {
            System.out.println("Room: " + room[0] + ", Capacity: " + room[1]);
        }
    }

    // Show available and booked times for a room
    public static void showRoomSchedule(String roomName) {
        List<String[]> reservations = FileUtils.readStructuredData(DIRECTORY, RESERVATIONS_FILE);
        List<String> bookedTimes = new ArrayList<>();

        System.out.println("Schedule for Room: " + roomName);
        for (String[] reservation : reservations) {
            if (reservation[0].equalsIgnoreCase(roomName)) {
                bookedTimes.add(reservation[1]);
            }
        }

        System.out.println("Booked Times: " + bookedTimes);
    }

    // Reserve a room
    public static void reserveRoom(String roomName, String time, String studentId) {
        List<String[]> reservations = FileUtils.readStructuredData(DIRECTORY, RESERVATIONS_FILE);

        // Check if the time is already booked
        for (String[] reservation : reservations) {
            if (reservation[0].equalsIgnoreCase(roomName) && reservation[1].equals(time)) {
                System.out.println("Time slot already booked!");
                return;
            }
        }

        // Add the new reservation
        reservations.add(new String[]{roomName, time, studentId});
        FileUtils.writeStructuredData(DIRECTORY, RESERVATIONS_FILE, new String[]{"RoomName", "Time", "StudentID"}, reservations);
        System.out.println("Room reserved successfully!");
    }
}
