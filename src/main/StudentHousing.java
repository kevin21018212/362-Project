package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import helpers.Utils;

public class StudentHousing {
    private static final String DORM_FILE_PATH = "src/data/dorm.txt";
    private static final String STUDENT_HOUSING_PATH = "src/data/studentHousing.txt";
    private static final List<Dorm> DORMS = Dorm.loadDormsFromFile(DORM_FILE_PATH);
    private static final String[] SPECIAL_ACCOMODATIONS = {"Wheelchair accessible room", "Service animal allowed", "Religious Accommodations"};
    public enum RoomType {
        SINGLE,
        DOUBLE,
        SUITE
    }
	
	private String studentID;
    private String term;
    private RoomType roomPreference;
    private Dorm dormPreference;
    //private String mealPlan;
    private List<String> specialAccommodations;
    private String applicationStatus;

    public StudentHousing(String studentID) {
        this.studentID = studentID;
        this.term = "Spring 2024";
        this.specialAccommodations = new ArrayList<>();
        //this.mealPlan = mealPlan;
        this.applicationStatus = "Pending"; // Default status
    }
    
    public void apply() {
    	if(!isStudentIdRegistered(studentID)) {
    		boolean hasDorm = false;
        	String SAs = "";
        	System.out.println("Starting Student Housing Application for student: " + studentID);
        	while(!hasDorm) {
        		System.out.println("Select a Dorm Building:");
        		for(int i = 0; i < DORMS.size(); i++) {
        			System.out.println((i + 1) + ". " + DORMS.get(i).toString());
        		}
        		Scanner in = new Scanner(System.in);
        		int input = in.nextInt();
        		if(DORMS.get(input - 1).checkAvailability() >= 1) {
        			Dorm dormInput = DORMS.get(input - 1);
            		dormInput.assignRoom(studentID);
            		this.dormPreference = dormInput;
            		hasDorm = true;
            		System.out.println("Selected: " + dormInput.getDormName());
        		}
        		else {
        			System.out.println("No space left in " + DORMS.get(input - 1).getDormName() + ". Please pick a different one");
        		}
        	}
        	System.out.println("Room Preference: \n"
        			+ "1. Single\n"
        			+ "2. Double\n"
        			+ "3. Suite");
        	Scanner in = new Scanner(System.in);
            String roomPref = in.nextLine();
            switch (roomPref) {
            case "1":
            	this.roomPreference = RoomType.SINGLE;
            	break;
            case "2":
            	this.roomPreference = RoomType.DOUBLE;
            	break;
            case "3":
            	this.roomPreference = RoomType.SUITE;
            	break;
            default:
            	this.roomPreference = RoomType.SINGLE;
            	break;
            }
            System.out.println("Do you require any special accommodations?");
            for(int i = 0; i < SPECIAL_ACCOMODATIONS.length; i++) {
            	System.out.println((i + 1) + ". " + SPECIAL_ACCOMODATIONS[i]);
            }
            System.out.println("4. None");
            String SAInput = in.nextLine();
            switch (SAInput) {
            case "1":
            	addSpecialAccommodation(SPECIAL_ACCOMODATIONS[0]);
            	break;
            case "2":
            	addSpecialAccommodation(SPECIAL_ACCOMODATIONS[1]);
            	break;
            case "3":
            	addSpecialAccommodation(SPECIAL_ACCOMODATIONS[2]);
            	break;
            case "4":
            	System.out.println("No special accommodations");
            	break;
            default:
            	break;
            }
            reviewApplication();
    		
    	}
    	else {
    		System.out.println("Student: " + studentID + " already applied");
    	}
    	
        
    }
    
    public void reviewApplication() {
        System.out.println("\nYour Application for term " + term + ":");
        System.out.println("Student ID: " + studentID);
        System.out.println("Dorm preference: " + dormPreference);
        System.out.println("Room preference: " + roomPreference);
        System.out.println("Special Accommodation(s): " + getSpecialAccommodations().toString());
        String input = Utils.getInput("Submit Application? (y/n): \n");
        if(input.equals("y")) {
        	submitApplication();
        }
        else{
        	System.out.println("Application cancelled");
        }
        
    }
    
    public void submitApplication() {
    	System.out.println("Application submitted");
    	String filePath = "src/data/studentHousing.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String accommodations = specialAccommodations.isEmpty() ? "None" : String.join(", ", specialAccommodations);
            writer.write(studentID + "::" +
                    dormPreference.getDormName() + "::" +
                    roomPreference + "::" +
                    accommodations + "##");
            writer.newLine();
            System.out.println("Application saved to file.");
        } catch (IOException e) {
            System.err.println("Error saving application to file: " + e.getMessage());
        }
    }
    
    public void addSpecialAccommodation(String accommodation) {
        specialAccommodations.add(accommodation);
        System.out.println("Added special accommodation: " + accommodation);
    }
    
    public List<String> getSpecialAccommodations() {
        return specialAccommodations;
    }
    
    public String checkApplicationStatus() {
        return "Application status for " + studentID + ": " + applicationStatus;
    }
    
    public void updateApplicationStatus(String status) {
        this.applicationStatus = status;
        System.out.println("Application status updated to: " + status);
    }
    
    public static boolean isStudentIdRegistered(String studentId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_HOUSING_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                line = line.replace("##", "");

                String[] parts = line.split("::");
                if (parts.length > 0) {
                    String existingStudentId = parts[0];
                    if (existingStudentId.equals(studentId)) {
                        return true; // Student ID already exists
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return false; // Student ID not found
    }
}