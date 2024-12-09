package main.Library;

import helpers.FileUtils;
import users.DataAccess;
import users.Student;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private static final String DIRECTORY = "library/";
    private static final String ROOMS_FILE = "rooms.txt";

    // Load all rooms
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

    // Show all rooms, differentiating between accessible and inaccessible
    public static void showAllRooms(String studentId) {
        Student student = DataAccess.findStudentById(studentId);
        if (student == null) {
            System.out.println("Invalid student ID!");
            return;
        }

        String major = student.getMajor();
        List<Room> rooms = loadRooms();

        System.out.println("Accessible Rooms:");
        for (Room room : rooms) {
            if (room.isMajorAllowed(major)) {
                System.out.println(room);
            }
        }

        System.out.println("\nInaccessible Rooms:");
        for (Room room : rooms) {
            if (!room.isMajorAllowed(major)) {
                System.out.println(room);
            }
        }
    }
}
