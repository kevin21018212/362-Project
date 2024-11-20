package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MealPlan {
	private static final String MEALPLAN_FILE_PATH = "src/data/mealPlan.txt";
    private String planName;
    private int mealsPerWeek;
    private String studentID;

    public MealPlan(String planName, int mealsPerWeek, String studentID) {
        this.planName = planName;
        this.mealsPerWeek = mealsPerWeek;
        this.studentID = studentID;
    }
    
    public static void chooseMealPlan(String studentId) {
    	if(!isStudentIdRegistered(studentId)) {
    		
    		MealPlan basic = new MealPlan("Basic", 10, studentId);
        	MealPlan standard = new MealPlan("Standard", 20, studentId);
        	MealPlan premium = new MealPlan("Premium", 30, studentId);
        	
        	List<MealPlan> mealPlans = new ArrayList<>();
        	
        	mealPlans.add(basic);
        	mealPlans.add(standard);
        	mealPlans.add(premium);
        	
        	System.out.println("Available Meal Plans: ");
        	for(int i = 0; i < mealPlans.size(); i++) {
        		System.out.println(i+1 + " "+ mealPlans.get(i).getPlanDetails());
        	}
        	Scanner in = new Scanner(System.in);
            int chosenPlan = in.nextInt();
            switch(chosenPlan) {
            case 1:
            	basic.activatePlan();
            	break;
            case 2:
            	standard.activatePlan();
            	break;
            case 3:
            	premium.activatePlan();
            	break;
            default:
            	break;
            }
    		
    	}
    	else {
    		System.out.println("Student: " + studentId + " already activated a meal plan");
    	}
    	
    }

    public void activatePlan() {
    	System.out.println(planName + " Meal plan activated for student ID: " + studentID);
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEALPLAN_FILE_PATH, true))) {
            String line = studentID + "::" + planName + "::" + mealsPerWeek + "##";
            writer.write(line);
            writer.newLine();
            System.out.println("Meal plan saved to file: " + MEALPLAN_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error saving meal plan to file: " + e.getMessage());
        }
    }

    public void updatePlanDetails(String newPlanName, double newCost, int newMealsPerWeek) {
        this.planName = newPlanName;
        this.mealsPerWeek = newMealsPerWeek;
        System.out.println("Meal plan updated: " + planName);
    }

    public String getPlanDetails() {
        return "Meal Plan: " + planName +
               " Meals per Week: " + mealsPerWeek;
    }
    
    public static boolean isStudentIdRegistered(String studentId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(MEALPLAN_FILE_PATH))) {
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

