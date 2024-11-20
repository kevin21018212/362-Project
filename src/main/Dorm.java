package main;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Dorm {
    private String dormName;
    private int capacity;
    private int occupiedRooms;
    private List<String> roomAssignments;
    private static final String DORM_FILE_PATH = "src/data/dorm.txt";
    
    
    public Dorm(String dormName, int capacity) {
        this.dormName = dormName;
        this.capacity = capacity;
        this.roomAssignments = new ArrayList<>();
    }
    

    public static List<Dorm> loadDormsFromFile(String filePath) {
        List<Dorm> dorms = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                line = line.replace("##", "");

                String[] parts = line.split("::");
                if (parts.length != 3) {
                    System.err.println("Invalid line format: " + line);
                    continue;
                }

                String dormName = parts[0];
                int capacity = Integer.parseInt(parts[1]);
                int occupiedRooms = Integer.parseInt(parts[2]);

                Dorm dorm = new Dorm(dormName, capacity);
                dorm.occupiedRooms = occupiedRooms; // Set occupied rooms

                dorms.add(dorm);
            }
        } catch (IOException e) {
            System.err.println("Error reading dorm file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in dorm file: " + e.getMessage());
        }
        return dorms;
    }

    // Methods
    public void assignRoom(String studentID) {
        if (occupiedRooms < capacity) {
            roomAssignments.add(studentID);
            occupiedRooms++;
            updateDormFile();
        } else {
            System.out.println("No available rooms in " + dormName);
            
        }
    }

    public boolean removeRoomAssignment(String studentID) {
        if (roomAssignments.contains(studentID)) {
            roomAssignments.remove(studentID);
            occupiedRooms--;
            updateDormFile();
            return true;
        } else {
            System.out.println("Student not found in this dorm.");
            return false;
        }
    }

    public int checkAvailability() {
        return capacity - occupiedRooms;
    }

    public List<String> getRoomAssignments() {
        return roomAssignments;
    }
    
    public String getDormName() {
    	return dormName;
    }

    @Override
    public String toString() {
        return dormName +
                ", Capacity: " + capacity +
                ", Occupied Rooms: " + occupiedRooms;
    }
    private void updateDormFile() {
        try {
            List<Dorm> dorms = loadDormsFromFile(DORM_FILE_PATH);

            for (Dorm dorm : dorms) {
                if (dorm.getDormName().equals(this.dormName)) {
                    dorm.occupiedRooms = this.occupiedRooms;
                    break;
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DORM_FILE_PATH))) {
                for (Dorm dorm : dorms) {
                    String line = dorm.dormName + "::" + dorm.capacity + "::" + dorm.occupiedRooms + "##";
                    writer.write(line);
                    writer.newLine();
                }
            }
            System.out.println("Dorm file successfully updated.");
        } catch (IOException e) {
            System.err.println("Error updating dorm file: " + e.getMessage());
        }
    }

}