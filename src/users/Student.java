package users;

import helpers.Display;
import helpers.User;
import helpers.Utils;
import main.Assignment;
import main.Course;
import main.Enrollment;
import main.Submission;
import helpers.FileUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static helpers.Display.displayMessage;

public class Student extends User {
    private static final String[] STUDENT_HEADERS = {"ID", "Name", "Email", "Major", "Scholarships", "Tuition"};
    private String major;
    private String scholarshipAmount;
    private String tuitionAmount;

    private final int IN_STATE_TUITION_PER_CLASS = 800;
    private final int OUT_OF_STATE_TUITION_PER_CLASS = 1500;
    private final int IOWA_RESIDENT_SCHOLARSHIP = 200;
    private final int HEALTH_FEE = 100;
    private final int TECHNOLOGY_FEE = 300;
    private final int COST_PER_BOOK = 50;

    public Student(String id, String name, String email, String major, String scholarshipAmount, String tuitionAmount) {
        super(id, name, email);
        this.major = major;
        this.scholarshipAmount = scholarshipAmount;
        this.tuitionAmount = tuitionAmount;
    }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public int getScholarshipAmount() {
        List<String[]> data = FileUtils.readStructuredData("", "students.txt");

        for (String[] row : data) {
            // Check if this row is the current student's row by matching ID
            if (row[0].equals(this.id)) {
                return Integer.parseInt(row[4]);
            }
        }

        return 0;
    }

    public void setScholarshipAmount(String amount) { this.scholarshipAmount = amount; }
    public int getTuitionAmount(){return Integer.parseInt(tuitionAmount);}
    public void setTuitionAmount(String amount) { this.tuitionAmount = amount; }


    public int getStudentID(){
        return Integer.parseInt(id);
    }

    @Override
    public String toString() {
        return id + "::" + name + "::" + email + "::" + major + "::" + scholarshipAmount + "::" + tuitionAmount;
    }

    /**
     * Updates the student records in the students.txt file
     * Currently used for updating major, scholarship amount, and tuition amount
     */
    public void updateStudentRecordInFile() {
        List<String[]> data = FileUtils.readStructuredData("", "students.txt");
        List<String[]> updatedData = new ArrayList<>();

        // Convert tuition and scholarship amounts to strings to match file format
        String tuition = this.tuitionAmount != null ? this.tuitionAmount : "0";
        String scholarship = this.scholarshipAmount != null ? this.scholarshipAmount : "0";

        for (String[] row : data) {
            // Check if the row corresponds to the current student by ID
            if (row[0].equals(this.id)) {
                // Update the row with the current student's info
                updatedData.add(new String[]{this.id, this.name, this.email, this.major, scholarship, tuition});
            } else {
                updatedData.add(row);
            }
        }

        FileUtils.writeStructuredData("", "students.txt", STUDENT_HEADERS, updatedData);
    }


    public List<String> getMajors(String department) {
        List<String[]> data = FileUtils.readStructuredData("", "majors.txt");
        List<String> majors = new ArrayList<>();

        for (String[] row : data) {
            if (row.length > 0 && row[0].trim().equalsIgnoreCase(department)) {
                if (row.length > 1) {
                    String majorsStr = row[1].trim();
                    // Remove brackets and split by comma
                    majorsStr = majorsStr.replaceAll("[\\[\\]]", ""); // Remove [ and ]
                    String[] majorArray = majorsStr.split(",");

                    // Add each major to the list, trimming whitespace
                    for (String major : majorArray) {
                        majors.add(major.trim());
                    }
                }
                break;
            }
        }
        return majors;
    }

    /**
     * @return a List of the available departments
     */
    public List<String> getAllDepartments() {
        List<String[]> data = FileUtils.readStructuredData("", "departments.txt");
        List<String> departmentNames = new ArrayList<>();

        for (String[] row : data) {
            if (row.length > 0) {
                departmentNames.add(row[0].trim());
            }
        }
        return departmentNames;
    }

    /**
     * @return A simple string of the department the student's major is a part of
     */
    public String getStudentDepartment() {
        List<String[]> data = FileUtils.readStructuredData("", "majors.txt");

        for (String[] row : data) {
            if (row.length > 1) {
                String departmentName = row[0].trim();
                String majorsList = row[1].trim();

                // Remove the square brackets around the majors list and split by comma
                majorsList = majorsList.replace("[", "").replace("]", "");
                String[] majors = majorsList.split(",");

                // Check if the current student's major is in the majors array
                for (String major : majors) {
                    if (major.trim().equalsIgnoreCase(this.major)) {
                        return departmentName;
                    }
                }
            }
        }

        return "Department not found";
    }


    /**
     * Enrolls a student in a course (broken rn?)
     */
    public void enrollInCourse() {
        String courseId = Utils.getInput("Enter course ID: ");
        Course course = Course.findCourseById(courseId);

        if (course == null) {
            displayMessage("Course not found");
            return;
        }

        // Validate enrollment
        if (course.isFull()) {
            displayMessage("Course is full");
            return;
        }

        if (!Enrollment.checkPrerequisites(this.getId(), course)) {
            displayMessage("Prerequisites not met");
            return;
        }

        // Create and save enrollment
        Enrollment enrollment = new Enrollment(this.getId(), courseId);
        Enrollment.saveEnrollment(enrollment);

        // Update course capacity
        course.incrementEnrollment();
        course.updateEnrollmentCount();

        displayMessage("Successfully enrolled in " + course.getName());
    }

    /**
     * Initial display options for changing major, viewing major/department
     */
    public void changeMajorDisplay() {
        System.out.println("1. View Major");
        System.out.println("2. Change Major");
        System.out.println("3. View Department");
        System.out.println("4. Go Back\n");

        String num = Utils.getInput("\nChoose an Option to Proceed:");
        switch(num) {
            case "1":
                displayMessage("Your current Major is: " + getMajor());
                break;
            case "2":
                handleMajorChange();
                break;
            case "3":
                displayMessage("Your current Department is: " + getStudentDepartment());
                break;
            case "4":
                Display.displayStudentMenu();
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    /**
     * Handles the logic and user walkthrough of changing the logged-in student's major.
     * Also updates the students.txt file with any changes to their major
     */
    private void handleMajorChange() {
        displayMessage("Your current Major is: " + getMajor());
        System.out.println("1. Type 1 to change your major.");
        System.out.println("2. Press 2 for No, go back.");

        String choice = Utils.getInput("\nAre you sure you want to change your major?");
        if (choice.equals("1")) {
            boolean selectingMajor = true;
            while (selectingMajor) {
                System.out.println("Changing your major...");
                displayDepartmentNames();
                String departmentName = Utils.getInput("\nSelect a department or type 'back' to cancel:");

                if (departmentName.equalsIgnoreCase("back")) {
                    break;
                }

                displayMajors(departmentName);
                String selectedMajor = Utils.getInput("\nSelect a major or type 'back' for departments:");

                if (selectedMajor.equals("1")) {
                    selectingMajor = false;
                } else if (!selectedMajor.equalsIgnoreCase("back")) {
                    setMajor(selectedMajor);
                    updateStudentRecordInFile();
                    displayMessage("Your new Major is: " + getMajor());
                    selectingMajor = false;
                }
            }
        }
    }

    /**
     * Displays a list of all the available majors for the specified majors
     * @param department
     */
    public void displayMajors(String department) {
        List<String> majors = getMajors(department);
        System.out.println("Majors in " + department + " Department:");
        majors.forEach(System.out::println);
    }

    /**
     * Displays a list of all the available departments
     */
    public void displayDepartmentNames() {
        System.out.println("\nDepartments Available:");
        getAllDepartments().forEach(Display::displayMessage);
    }

    public void viewUniversityBillingOptions(){
        System.out.println("\nPlease Select an option regarding your University Bill or Scholarships:");
        displayMessage("1 View current University Tuition and Fees");
        displayMessage("2 View current scholarship amount.");
        displayMessage("3 Apply/Reapply for Tuition and Scholarships");
        String choice = Utils.getInput("Select an option: ");
        switch (choice) {
            case "1":
                System.out.println("\nYour current Tuition/Fees total: $" + tuitionAmount);
                if(tuitionAmount.equals("0")){
                    System.out.println("You must apply for tuition and scholarships, please press option 3.");
                    viewUniversityBillingOptions();
                }
                break;
            case "2":
                System.out.println("\nYour current Scholarships total: $" + getScholarshipAmount());
                if(scholarshipAmount.equals("0")){
                    System.out.println("You must apply for tuition and scholarships, please press option 3.\n");
                    viewUniversityBillingOptions();
                }
                break;
            case "3":
                viewUniversityBill();
                break;
            default:
                displayMessage("\nIncorrect Input, Please try again");
                viewUniversityBillingOptions();
                break;
        }
    }

    /**
     * Walks the user through their tuition and scholarship costs.
     * Prints out the total amount owed for the logged-in student.
     */
    public void viewUniversityBill() {
        //Tuition calculation, printing, and file updating
        System.out.println("\nUniversity Bill: Fall2024");

        System.out.println("\nTuition");
        int classCount = getClassCountFromFile(getStudentID());
        int tempTuition = calculateTuition(classCount);
        tuitionAmount = Integer.toString(tempTuition);

        if(!tuitionAmount.equals("0")){
            System.out.println("Total Tuition: $" + tuitionAmount);
        } else {
            System.out.println("You are not enrolled in any classes.");
        }

        //Fees and Books costs
        System.out.println("\nHealth Fee: $" + HEALTH_FEE);
        System.out.println("Technology Fee: $" + TECHNOLOGY_FEE);
        int bookCost = classCount * COST_PER_BOOK;
        System.out.println("Books/Materials: $" + bookCost);

        //Calculate total cost of tuition + fees
        int totalFees = HEALTH_FEE + TECHNOLOGY_FEE + bookCost;
        int totalCost = tempTuition + totalFees;
        setTuitionAmount(Integer.toString(totalCost));
        System.out.println("\nTotal Costs: $" + totalCost);

        // Scholarship calculations, printing, and file updating
        int tempScholarshipAmount = calculateScholarshipAmount();
        updateStudentRecordInFile();
        System.out.println("Your student record has been updated!\n");

        //Print out Final Bill
        System.out.println("Final Bill:");
        System.out.println("Total Costs = $" + totalCost);
        System.out.println("Total Scholarships Earned = $" + tempScholarshipAmount);

        //ensure amount owed cannot be negative
        int amountOwed = totalCost - tempScholarshipAmount;
        if(amountOwed < 0){
            amountOwed = 0;
            System.out.println("Congrats! You owe $" + amountOwed + " because your scholarships cover your costs!");
        } else {
            System.out.println("You owe: $" + amountOwed);
        }
    }

    /**
     * @param classCount
     * @return the integer amount of tuition the student will owe. Also UPDATES the student.txt file with the result
     */
    public int calculateTuition(int classCount) {
        String isInState = Utils.getInput("\nAre you an Iowa resident? (yes/no): ");

        int tuitionPerClass = OUT_OF_STATE_TUITION_PER_CLASS;

        if(isInState.equalsIgnoreCase("yes")){
            tuitionPerClass = IN_STATE_TUITION_PER_CLASS;
        }

        int totalTuition = classCount * tuitionPerClass;

        System.out.println("Classes enrolled: " + classCount);
        System.out.println("Tuition per class: $" + tuitionPerClass);

        return totalTuition;
    }

    /**
     * @return The integer amount of scholarships the student has earned based on GPA and ACT score.
     * Updates the students.txt file with the new amount
     */
    public int calculateScholarshipAmount(){
        final int HS_GPA_TOP = 400;
        final int HS_GPA_MED = 200;
        final int HS_GPA_SMALL = 100;
        final int ACT_TOP = 200;
        final int ACT_MED = 100;
        final int ACT_SMALL = 100;
        int runningTotal = 0;

        //Calculate in-state scholarship
        System.out.println("\nScholarships:");
        String isInState = Utils.getInput("Are you an Iowa resident? (yes/no): ");

        if(isInState.equalsIgnoreCase("yes")){
            runningTotal += IOWA_RESIDENT_SCHOLARSHIP;
        }

        //calculate high school GPA scholarship
        System.out.println("Please enter your high school GPA such as '3.5'.");
        float highSchoolGPA = Float.parseFloat(Utils.getInput("\nGPA: "));

        if(highSchoolGPA >= 3.8){
            System.out.println("Congrats! You have earned the top GPA Scholarship!");
            runningTotal += HS_GPA_TOP;
        } else if(highSchoolGPA < 3.8 && highSchoolGPA >= 3.3){
            System.out.println("Congrats! You have earned the medium GPA Scholarship!");
            runningTotal += HS_GPA_MED;
        } else if(highSchoolGPA < 3.3 && highSchoolGPA >= 3.0){
            System.out.println("Congrats! You have earned a small GPA Scholarship!");
            runningTotal += HS_GPA_SMALL;
        } else {
            System.out.println("Sorry! You do not qualify for a GPA Scholarship!");
        }

        //calculate ACT-based scholarship
        System.out.println("\nPlease enter your ACT score such as '25'.");
        int studentACT = Integer.parseInt(Utils.getInput("ACT Score: "));
        if(studentACT >= 32){
            System.out.println("Congrats! You have earned the top ACT Scholarship!");
            runningTotal += ACT_TOP;
        } else if(studentACT < 32 && studentACT >= 27){
            System.out.println("Congrats! You have earned the medium ACT Scholarship!");
            runningTotal += ACT_MED;
        } else if(studentACT < 27 && studentACT >= 24){
            System.out.println("Congrats! You have earned a small ACT Scholarship!");
            runningTotal += ACT_SMALL;
        } else {
            System.out.println("Sorry! You do not qualify for a ACT Scholarship!");
        }

        System.out.println("\nThank you for applying for Academic Scholarships!");
        System.out.println("You have earned a total of: $" + runningTotal);
        setScholarshipAmount(Integer.toString(runningTotal));

        return runningTotal;
    }

    /**
     * @param studentId
     * @return The integer amount (Ex: 2) of classes the logged-in student is enrolled in.
     * Used to calculate various metrics for tuition and fees
     */
    public int getClassCountFromFile(int studentId) {
        int classCount = 0;

        // Read structured data from enrollments.txt
        List<String[]> data = FileUtils.readStructuredData("", "enrollments.txt");

        // Iterate through each row of data
        for (String[] row : data) {
            if (row.length > 1) {
                // Check if the StudentId matches the specified studentId
                if (row[0].trim().equals(String.valueOf(studentId))) {
                    classCount++;
                }
            }
        }

        return classCount;
    }

    public void submitAssignment() {
        // Display all enrolled courses for the student
        Enrollment.displayAllEnrolledCourses(this.id);

        // Ask for course ID
        String courseId = Utils.getInput("\nEnter Course ID from the list above: ");
        Course course = Course.findCourseById(courseId);
        if (course == null) {
            displayMessage("Invalid Course ID.");
            return;
        }

        // Load assignments and display those not yet submitted
        List<Assignment> assignments = Assignment.loadAssignments(courseId);
        boolean hasUnsubmittedAssignments = false;
        System.out.println("Assignments not yet submitted:");

        for (Assignment assignment : assignments) {
            if (!Submission.isSubmitted(courseId, this.id, assignment.getId())) {
                System.out.println(assignment);
                hasUnsubmittedAssignments = true;
            }
        }

        if (!hasUnsubmittedAssignments) {
            displayMessage("All assignments have been submitted for this course.");
            return;
        }

        // Ask for assignment ID
        String assignmentId = Utils.getInput("\nEnter Assignment ID: ");

        if (Submission.isSubmitted(courseId, this.id, assignmentId)) {
            displayMessage("This assignment has already been submitted.");
        } else {
            // Create submission data
            String submissionId = "sub_" + System.currentTimeMillis();
            String submittedDate = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ISO_DATE);

            // Create submission data array
            String[] submissionData = {
                    submissionId,
                    assignmentId,
                    this.id,
                    "Not Graded",
                    submittedDate
            };

            // Read existing submissions
            String fileName = "courses/submissions.txt";
            List<String[]> existingSubmissions = FileUtils.readStructuredData("", fileName);

            // Add new submission
            existingSubmissions.add(submissionData);

            // Write back to file with headers
            FileUtils.writeStructuredData("", fileName,
                    new String[]{"SubmissionId", "AssignmentId", "StudentId", "Grade", "SubmittedDate"},
                    existingSubmissions);

            displayMessage("Assignment submitted successfully.");
        }
    }

    public static Student findStudentById(String id) {
        return DataAccess.findStudentById(id);
    }

    public void viewGrades() {
        String filePath = "src/data/grades.txt"; // Path to the grades file
        boolean hasGrades = false;

        Display.displayMessage("Your Current Grades:");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String studentIdFromFile = data[0].trim();
                    String courseId = data[1].trim();
                    String grade = data[2].trim();

                    // Check if the entry is for the current student
                    if (studentIdFromFile.equals(this.id)) {
                        Display.displayMessage(courseId + ": " + grade);
                        hasGrades = true;
                    }
                }
            }
        } catch (IOException e) {
            Display.displayMessage("Error reading grades file.");
            e.printStackTrace();
        }

        // If no grades are found for the student
        if (!hasGrades) {
            Display.displayMessage("No grades available for your enrolled courses.");
        }
    }


    public void applyForGraduation() {
        // Sample requirements for graduation
        final int MINIMUM_CREDITS = 120; // Minimum credits required for graduation
        final double MINIMUM_GPA = 2.0; // Minimum GPA required for graduation

        // Assume methods `getTotalCredits()` and `getGPA()` are implemented to fetch these details
        int totalCredits = getTotalCredits();
        double gpa = getGPA();

        if (totalCredits >= MINIMUM_CREDITS && gpa >= MINIMUM_GPA) {
            displayMessage("Congratulations! You are eligible to apply for graduation.");
            // Code to mark the application status, such as writing to a file or database
            markGraduationApplication();
            displayMessage("Your graduation application has been submitted successfully.");
        } else {
            displayMessage("You are not eligible to apply for graduation.");
            if (totalCredits < MINIMUM_CREDITS) {
                displayMessage("You need at least " + (MINIMUM_CREDITS - totalCredits) + " more credits to be eligible.");
            }
            if (gpa < MINIMUM_GPA) {
                displayMessage("Your GPA must be at least " + MINIMUM_GPA + " to be eligible.");
            }
        }
    }

    public int getTotalCredits() {
        // Placeholder - Implement logic to calculate total credits based on courses completed
        return 120; // Example return value for eligibility
    }

    public double getGPA() {
        // Placeholder - Implement logic to calculate GPA based on course grades
        return 3.0; // Example return value for eligibility
    }

    public void markGraduationApplication() {
        // Implement logic to mark graduation application status
        displayMessage("Graduation application status has been updated in the system.");
    }
}