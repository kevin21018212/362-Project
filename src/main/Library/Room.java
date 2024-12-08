package main;

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

    public int getCapacity() {
        return capacity;
    }

    public List<String> getAllowedMajors() {
        return allowedMajors;
    }

    public boolean isMajorAllowed(String major) {
        return allowedMajors.contains("Open") || allowedMajors.contains(major);
    }

    @Override
    public String toString() {
        return "Room: " + id + ", Capacity: " + capacity + ", Allowed Majors: " + allowedMajors;
    }
}

