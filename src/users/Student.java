package users;

import helpers.Display;
import helpers.User;
import helpers.Utils;
import main.*;
import helpers.FileUtils;

import java.io.*;
import java.util.*;

import static helpers.Display.displayMessage;
import static helpers.Display.displayStudentMenu;

public class Student extends User {
    private static final String[] STUDENT_HEADERS = {"ID", "Name", "Email", "Major", "Scholarships", "Tuition", "Advisor"};
    private static final String[] STUDENT_CLUBS = {"Soccer", "Baseball", "Poker", "Pickleball", "Tennis", "Finance", "Investing", "Hacking"};
    private String major;
    private String scholarshipAmount;
    private String tuitionAmount;

    private String studentClub1;
    private String studentClub2;

    private String advisor;

    private final int IN_STATE_TUITION_PER_CLASS = 800;
    private final int OUT_OF_STATE_TUITION_PER_CLASS = 1500;
    private final int IOWA_RESIDENT_SCHOLARSHIP = 200;
    private final int HEALTH_FEE = 100;
    private final int TECHNOLOGY_FEE = 300;
    private final int COST_PER_BOOK = 50;

    public Student(String id, String name, String email, String major, String scholarshipAmount, String tuitionAmount, String studentClub1, String studentClub2, String advisor) {
        super(id, name, email);
        this.major = major;
        this.scholarshipAmount = scholarshipAmount;
        this.tuitionAmount = tuitionAmount;
        this.studentClub1 = studentClub1;
        this.studentClub2 = studentClub2;
        this.advisor = advisor;
    }

    public String getAdvisor() {
        return advisor;
    }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getStudentClub1(){
        return studentClub1;
    }

    public String getStudentClub2(){
        return studentClub2;
    }

    public void setStudentClub1(String club){
        this.studentClub1 = club;
    }

    public void setStudentClub2(String club){
        this.studentClub2 = club;
    }


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

    public int getTotalUniversityBill(){
        int temp = getTuitionAmount() - getScholarshipAmount();
        return Math.max(temp, 0);
    }

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


    public void reserveStudyRoom() {
        Library.showAllRooms();
        System.out.println("Enter room name:");
        String roomName = System.console().readLine();

        Library.showRoomSchedule(roomName);
        System.out.println("Enter time to reserve:");
        String time = System.console().readLine();

        Library.reserveRoom(roomName, time, this.getId());
    }

    /**
     * Enrolls a student in a course (broken rn?)
     */
    public void enrollInCourse() {
        String courseId = Utils.getInput("Enter course ID: ");
        Course course = Course.findCourseById(courseId);
        if (course == null) {
            Display.displayMessage("Course not found");
            return;
        }
        // Validate enrollment
        if (course.isFull()) {
            Display.displayMessage("Course is full");
            return;
        }
        if (!Enrollment.checkPrerequisites(this.getId(), course)) {
            Display.displayMessage("Prerequisites not met");
            return;
        }
        for (Enrollment enrollment : Enrollment.loadEnrollments()) {
            if (enrollment.getStudentId().equals(this.id) && enrollment.getCourseId().equals(courseId)) {
                Display.displayMessage("You are already enrolled in this course.");
                return;
            }
        }
        // Create and save enrollment
        Enrollment enrollment = new Enrollment(this.getId(), courseId);
        Enrollment.saveEnrollment(enrollment);
        // Update course capacity
        course.incrementEnrollment();
        course.updateEnrollmentCount();
        Display.displayMessage("Successfully enrolled in " + course.getName());
    }

    /**
     * Initial display options for changing major, viewing major/department
     */
    public void changeMajorDisplay() {
        Major majorHelper = new Major();
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
                handleMajorChange(majorHelper);
                break;
            case "3":
                String department = majorHelper.getDepartmentByMajor(this.major);
                displayMessage("Your current Department is: " + department);
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
    private void handleMajorChange(Major majorHelper) {
        displayMessage("Your current Major is: " + getMajor());
        System.out.println("1. Type 1 to change your major.");
        System.out.println("2. Press 2 for No, go back.");
        String choice = Utils.getInput("\nAre you sure you want to change your major?");
        if (choice.equals("1")) {
            boolean selectingMajor = true;
            while (selectingMajor) {
                System.out.println("Changing your major...");
                List<String> departments = majorHelper.getAllDepartments();
                System.out.println("\nDepartments Available:");
                departments.forEach(Display::displayMessage);

                String departmentName = Utils.getInput("\nSelect a department or type 'back' to cancel:");

                if (departmentName.equalsIgnoreCase("back")) {
                    break;
                }

                majorHelper.displayMajors(departmentName);
                String selectedMajor = Utils.getInput("\nSelect a major or type 'back' for departments:");

                if (selectedMajor.equalsIgnoreCase("back")) {
                    continue;
                } else {
                    setMajor(selectedMajor);
                    updateStudentRecordInFile();
                    displayMessage("Your new Major is: " + getMajor());
                    selectingMajor = false;
                }
            }
        }
    }

    public void viewUniversityBillingOptions(){
        System.out.println("\nPlease Select an option regarding your University Bill or Scholarships:");
        displayMessage("1 View current University Tuition and Fees");
        displayMessage("2 View current awarded Scholarship amount.");
        displayMessage("3 Apply/Reapply for Tuition and Scholarships");
        displayMessage("4 View and Pay your University Bill");
        displayMessage("5 Return to Student Menu");
        String choice = Utils.getInput("Select an option: ");
        switch (choice) {
            case "1":
                System.out.println("\nYour current Tuition/Fees total: $" + tuitionAmount);
                if(tuitionAmount.equals("0")){
                    System.out.println("You must apply for tuition and scholarships, please press option 3.");
                }
                viewUniversityBillingOptions();
                break;
            case "2":
                System.out.println("\nYour current Scholarships total: $" + getScholarshipAmount());
                if(scholarshipAmount.equals("0")){
                    System.out.println("You must apply for tuition and scholarships, please press option 3.\n");
                }
                viewUniversityBillingOptions();
                break;
            case "3":
                viewUniversityBill();
                break;
            case "4":
                System.out.println("\nYour current Tuition/Fees total: $" + getTuitionAmount());
                System.out.println("Your current Scholarships total: $" + getScholarshipAmount());
                System.out.println("\nYour current University Bill Total: $" + getTotalUniversityBill());

                if(getTotalUniversityBill() == 0){
                    System.out.println("\nYou owe nothing for tuition, so there is no bill to pay! Congrats!");
                    break;
                } else {
                    payUniversityBillMenu();
                }
                break;
            case "5":
                displayStudentMenu();
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

    public void payUniversityBill(){
        System.out.println("\nYour current University Bill Total: $" + getTotalUniversityBill());
        displayMessage("\nPlease Select a Payment Method: ");
        displayMessage("1 Cash");
        displayMessage("2 Check");
        displayMessage("3 Credit/Debit Card");
        displayMessage("4 Venmo/Paypal");
        displayMessage("5 Go Back");
        String paymentMethod = Utils.getInput("\nPlease select an option (Ex: 3): ");

        switch (paymentMethod){
            case "1":
                int confirmPayment = Integer.parseInt(Utils.getInput("\nPlease Enter the amount you would like to pay:"));

                    if(confirmPayment <= getTotalUniversityBill() && (confirmPayment >=0)){
                        System.out.println("Thank you!");
                        System.out.println("Please deliver your cash to the Billing Center within 7 business days.");
                        handleUniversityBillPayment(confirmPayment);
                    } else {
                        System.out.println("That was not correct, please try again");
                        payUniversityBill();
                    }
                    break;
            case "2":
                System.out.println("You have selected to pay with check.");
                String rountingNumber = Utils.getInput("\nPlease enter your routing number: ");
                String accountNumber = Utils.getInput("\nPlease enter your account number: ");
                System.out.println("IF you would like to cancel, please enter 1 on the following prompt");
                int confirmPaymentCheck = Integer.parseInt(Utils.getInput("\nPlease Enter the amount you would like to pay:"));

                if(confirmPaymentCheck <= getTotalUniversityBill() && (confirmPaymentCheck >=0)){
                    System.out.println("Thank you!");
                    System.out.println("Your check has been confirmed");
                    handleUniversityBillPayment(confirmPaymentCheck);
                } else if(confirmPaymentCheck == 1){
                    System.out.println("You have cancelled your payment.");
                    payUniversityBill();
                }

                break;
            case "3":
                System.out.println("You have selected to pay with Credit/Debit Card.");
                String cardNumber = Utils.getInput("\nPlease enter your card number: ");
                String cardDate = Utils.getInput("\nPlease enter your card date (Ex: 10/12/27): ");
                String cardName = Utils.getInput("\nPlease enter the full name that appears on the card: ");
                String CVV = Utils.getInput("\nPlease enter your CVV: ");
                System.out.println("\nIf you would like to cancel, please enter 1 on the following prompt");
                int confirmPaymentCard = Integer.parseInt(Utils.getInput("\nPlease Enter the amount you would like to pay:"));

                if(confirmPaymentCard <= getTotalUniversityBill() && (confirmPaymentCard >=0)){
                    System.out.println("Thank you!");
                    System.out.println("Your card payment has been confirmed");
                    handleUniversityBillPayment(confirmPaymentCard);
                } else if(confirmPaymentCard == 1){
                    System.out.println("You have cancelled your payment.");
                    payUniversityBill();
                }
                break;
            case "4":
                System.out.println("You have selected to pay with Venmo/Paypal.");
                String username = Utils.getInput("\nPlease enter your username: ");
                System.out.println("\nIf you would like to cancel, please enter 1 on the following prompt");
                int confirmPaymentOnline = Integer.parseInt(Utils.getInput("\nPlease Enter the amount you would like to pay:"));

                if(confirmPaymentOnline <= getTotalUniversityBill() && (confirmPaymentOnline >=0)){
                    System.out.println("Thank you!");
                    handleUniversityBillPayment(confirmPaymentOnline);
                    System.out.println("Your card payment has been confirmed");
                } else if(confirmPaymentOnline == 1){
                    System.out.println("You have cancelled your payment.");
                    payUniversityBill();
                }
                break;
            case "5":
                payUniversityBillMenu();
                break;
            default:
                viewUniversityBillingOptions();
                break;
        }
    }

    public void payUniversityBillMenu(){
        displayMessage("\nPay University Bill Menu: ");
        displayMessage("1 Pay Bill");
        displayMessage("2 Cancel (Go Back)");

        String payBillMenuChoice = Utils.getInput("\nPlease select an option (Ex: 3): ");

        switch (payBillMenuChoice){
            case "1":
                payUniversityBill();
                break;//TODO - Edit tuition and scholarship values after payment
            case "2":
                viewUniversityBillingOptions();
                break;
            default:
                payUniversityBillMenu();
        }
    }

    public void handleUniversityBillPayment(int amount){
        int newStudentTuition = getTuitionAmount() - amount - getScholarshipAmount();

        System.out.println("Your new University Bill balance is: $" + newStudentTuition);

        //Set tuition amount to be the remaining total (if any)
        setTuitionAmount(Integer.toString(newStudentTuition));

        //Set apply for scholarship to 0 since the payment used their scholarship
        setScholarshipAmount("0");

        //update tuition and scholarship amount in students.txt file
        updateStudentRecordInFile();
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

    public void displayStudentExtracurricularMenu(){
        displayMessage("\nStudent Extracurricular Menu: ");
        displayMessage("1 View My Extracurricular Activities");
        displayMessage("2 Browse Available Extracurricular Activities");
        displayMessage("3 Join A New Extracurricular Activity");
        displayMessage("4 Drop an Extracurricular Activity");
        displayMessage("5 Return to Student Menu");
        String id = Utils.getInput("\nPlease select an option (Ex: 3): ");
        switch (id) {
            case "1":
                viewEnrolledExtracurriculars();
                break;
            case "2":
                browseExtracurricularActivities();
                break;
            case "3":
                joinExtracurricularActivity();
                break;
            case "4":
                dropExtracurricularActivity();
                break;
            case "5":
                displayStudentMenu();
                break;
            default:
                displayMessage("Something went wrong... returning to student menu.");
                displayStudentMenu();
        }
    }

    /**
     * Displays the enrolled extracurricular activities of the logged-in student.
     */
    public void viewEnrolledExtracurriculars(){
        if(studentClub2.equalsIgnoreCase("n/a") && studentClub1.equalsIgnoreCase("n/a")){
            System.out.println("You are not enrolled in any clubs.");
        } else {
            System.out.println("Your currently enrolled in: ");
        }
        if(!studentClub1.equalsIgnoreCase("n/a")){
            System.out.println(getStudentClub1());
        }
        if(!studentClub2.equalsIgnoreCase("n/a")){
            System.out.println(getStudentClub2());
        }
        //displayStudentExtracurricularMenu();
    }

    public void browseExtracurricularActivities(){
        displayAvailableExtracurriculars();
    }

    /**
     * Process of the logged-in student joining a club.
     * 1. Checks if student has open club slot
     * 2. Displays options of clubs to join (STUDENT_CLUBS)
     * 3. Validates entered club name to check if matches available options
     * 4. Updates either studentClub1 or studentClub2 depending on which slot is open
     */
    public void joinExtracurricularActivity(){
        if (!checkStudentExtracurricularAvailability()) {
            System.out.println("You are already enrolled in the maximum number of clubs. Please drop a club to join another.");
            displayStudentExtracurricularMenu();
            return;
        }

        System.out.println("Here are the available clubs to join: ");
        displayAvailableExtracurriculars();
        String clubChoice = Utils.getInput("\nPlease enter the name of the club you wish to join: ");

        // Validate if the input matches one of the available clubs
        boolean isValidClub = false;
        for (String club : STUDENT_CLUBS) {
            if (club.equalsIgnoreCase(clubChoice)) {
                isValidClub = true;
                break;
            }
        }

        if (!isValidClub) {
            System.out.println("Invalid club choice. Please select a valid club from the list.");
            joinExtracurricularActivity(); // Restart the process
        }

        // If valid, update the appropriate club slot using the helper function
        List<String[]> data = FileUtils.readStructuredData("", "students.txt");

        for (String[] row : data) {
            if (row[0].equals(this.id)) { // Locate the student's record by ID
                if ("n/a".equalsIgnoreCase(row[6])) {
                    String result = updateStudentClubs(this.id, "studentClub1", clubChoice);
                    setStudentClub1(clubChoice);
                    System.out.println(result);
                    break;
                } else if ("n/a".equalsIgnoreCase(row[7])) {
                    String result = updateStudentClubs(this.id, "studentClub2", clubChoice);
                    setStudentClub2(clubChoice);
                    System.out.println(result);
                    break;
                }
            }
        }
        displayStudentExtracurricularMenu();
    }

    /**
     * Checks if a student is able to join another club (aka has at least one open club slot)
     * Returns true if a student has an available club slot
     */
    public boolean checkStudentExtracurricularAvailability() {
        List<String[]> data = FileUtils.readStructuredData("", "students.txt");

        for (String[] row : data) {
            if (row[0].equals(this.id)) {
                // Check if either club slot is "n/a"
                return "n/a".equalsIgnoreCase(row[6]) || "n/a".equalsIgnoreCase(row[7]);
            }
        }
        return false;
    }

    /**
     * Displays available clubs to join.
     */
    public void displayAvailableExtracurriculars() {
        for (int i = 0; i < STUDENT_CLUBS.length; i++) {
            System.out.println((i + 1) + ". " + STUDENT_CLUBS[i]);
        }
    }

    /**
     * Walks user through process of dropping a club the logged-in student is enrolled in.
     */
    public void dropExtracurricularActivity() {
        viewEnrolledExtracurriculars();
        String clubChoice = Utils.getInput("\nPlease enter the name of the club you wish to drop: ");

        if (clubChoice.equalsIgnoreCase(studentClub1)) {
            String result = updateStudentClubs(this.id, "studentClub1", "n/a");
            setStudentClub1("n/a");
            displayMessage(result);
            displayStudentExtracurricularMenu();
        } else if (clubChoice.equalsIgnoreCase(studentClub2)) {
            String result = updateStudentClubs(this.id, "studentClub2", "n/a");
            setStudentClub2("n/a");
            displayMessage(result);
            displayStudentExtracurricularMenu();
        } else {
            System.out.println("That does not match your current clubs. Please check your spelling.");
            String retryChoice = Utils.getInput("\nEnter 1 to cancel or 2 to try again: ");
            switch (retryChoice) {
                case "1":
                    displayStudentExtracurricularMenu();
                    break;
                case "2":
                    dropExtracurricularActivity();
                    break;
                default:
                    displayMessage("Something went wrong... returning to student menu.");
                    displayStudentMenu();
            }
        }
    }

    /**
     * Updates the logged-in student's "clubToUpdate" with the specified string value of "newClubValue"
     * Will update to "n/a" if the student is dropping that club.
     *
     * @param studentId
     * @param clubToUpdate
     * @param newClubValue
     * @return String of either confirmation message or error message.
     */
    public String updateStudentClubs(String studentId, String clubToUpdate, String newClubValue) {
        List<String[]> data = FileUtils.readStructuredData("", "students.txt");
        List<String[]> updatedData = new ArrayList<>();
        boolean updated = false;

        for (String[] row : data) {
            if (row[0].equals(studentId)) {
                if ("studentClub1".equalsIgnoreCase(clubToUpdate)) {
                    row[6] = newClubValue;
                    updated = true;
                } else if ("studentClub2".equalsIgnoreCase(clubToUpdate)) {
                    row[7] = newClubValue;
                    updated = true;
                }
                updatedData.add(row);
            } else {
                updatedData.add(row);
            }
        }

        FileUtils.writeStructuredData("", "students.txt", STUDENT_HEADERS, updatedData);
        return updated ? "Club successfully updated." : "Failed to update the club. Please check your input.";
    }

    public void submitAssignment() {
        Course.loadCourses();
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
            String directory = "courses/" + courseId + "/";
            String fileName = "submissions.txt";
            List<String[]> existingSubmissions = FileUtils.readStructuredData(directory, fileName);

            // Add new submission
            existingSubmissions.add(submissionData);

            // Write back to file with headers
            FileUtils.writeStructuredData(directory, fileName,
                    new String[]{"SubmissionId", "AssignmentId", "StudentId", "Grade", "SubmittedDate"},
                    existingSubmissions);

            displayMessage("Assignment submitted successfully.");
        }
    }

    public static Student findStudentById(String id) {
        return DataAccess.findStudentById(id);
    }

    public void viewGrades() {
        String enrollmentsFile = "src/data/enrollments.txt"; // Path to enrollments file
        String coursesFolder = "src/data/courses";          // Path to main courses folder
        boolean hasCourses = false;

        Display.displayMessage("Your Grades:");

        try (BufferedReader enrollmentReader = new BufferedReader(new FileReader(enrollmentsFile))) {
            String enrollmentLine;
            while ((enrollmentLine = enrollmentReader.readLine()) != null) {
                // Parse enrollment data
                String[] enrollmentData = enrollmentLine.split("::");
                if (enrollmentData.length >= 2) {
                    String studentIdFromFile = enrollmentData[0].trim();
                    String courseId = enrollmentData[1].replace("##", "").trim();

                    // Check if the entry is for the current student
                    if (studentIdFromFile.equals(this.id)) {
                        // Paths for course-specific files
                        String courseFolder = coursesFolder + "/" + courseId;
                        String submissionsFile = courseFolder + "/submissions.txt";

                        // Initialize variables for course-grade calculation
                        double totalGrade = 0;
                        int numAssignments = 0;

                        // Store grades for individual assignments
                        Map<String, String> assignmentGrades = new LinkedHashMap<>(); // AssignmentID -> Grade

                        // Read submissions for the current course
                        try (BufferedReader submissionsReader = new BufferedReader(new FileReader(submissionsFile))) {
                            String submissionLine;
                            while ((submissionLine = submissionsReader.readLine()) != null) {
                                String[] submissionDetails = submissionLine.split("::");
                                if (submissionDetails.length >= 5) {
                                    String submissionStudentId = submissionDetails[2].trim();
                                    String assignmentId = submissionDetails[1].trim();
                                    String gradeStr = submissionDetails[3].trim();

                                    // Filter submissions for the current student
                                    if (submissionStudentId.equals(this.id)) {
                                        try {
                                            double grade = Double.parseDouble(gradeStr);
                                            totalGrade += grade; // Add to total grade
                                            numAssignments++;    // Increment assignment count

                                            assignmentGrades.put(assignmentId, gradeStr); // Store grade
                                        } catch (NumberFormatException e) {
                                            System.out.println("Invalid grade for submission: " + submissionDetails[0]);
                                        }
                                    }
                                }
                            }
                        } catch (FileNotFoundException e) {
                            Display.displayMessage("No submissions found for course " + courseId + ".");
                        }

                        // Display individual assignment grades
                        if (!assignmentGrades.isEmpty()) {
                            Display.displayMessage("\nCourse: " + courseId);
                            for (Map.Entry<String, String> entry : assignmentGrades.entrySet()) {
                                Display.displayMessage("   Assignment " + entry.getKey() + " - Grade: " + entry.getValue());
                            }

                            // Calculate and display the course average
                            double averageGrade = totalGrade / numAssignments;
                            String letterGrade = getLetterGrade(averageGrade);
                            Display.displayMessage("   Course Average: " + averageGrade + " (" + letterGrade + ")");
                            hasCourses = true;
                        } else {
                            Display.displayMessage("\nCourse: " + courseId);
                            Display.displayMessage("   No grades found for this course.");
                        }
                    }
                }
            }
        } catch (IOException e) {
            Display.displayMessage("Error reading enrollments or course files.");
            e.printStackTrace();
        }

        // If no courses are found for the student
        if (!hasCourses) {
            Display.displayMessage("You are not enrolled in any courses.");
        }
    }

    /**
     * Converts a numeric grade to a letter grade.
     *
     * @param grade Numeric grade (e.g., 85.0)
     * @return Corresponding letter grade (e.g., "A")
     */
    private String getLetterGrade(double grade) {
        if (grade >= 90) {
            return "A";
        } else if (grade >= 80) {
            return "B";
        } else if (grade >= 70) {
            return "C";
        } else if (grade >= 60) {
            return "D";
        } else {
            return "F";
        }
    }
    public void trackAcademicProgress() {
        //1. retrieve student major
        if (this.major == null || this.major.isEmpty()) {
            displayMessage("Major information is incomplete or missing. Please contact the registrar.");
            return; // Alternate Flow: Major Not Found
        }

        Major majorHelper = new Major();

        Course.allCourses = Course.loadCourses();
        // 2. fetch the list of student enrolled courses
        List<String> enrolledCourses = Enrollment.getEnrolledCourses(this.id);
        if (enrolledCourses.isEmpty()) {
            displayMessage("No enrollment data available. Please ensure your courses are properly recorded.");
            return; // Alternate Flow: Enrollment Data Missing
        }

        // 3. get all the required courses from the major
        List<String> requiredCourses = majorHelper.getCoursesByMajor(this.major);
        if (requiredCourses.isEmpty()) {
            displayMessage("Degree requirements are incomplete. Please contact your academic advisor.");
            return; // Alternate Flow: Incomplete Major Requirements
        }

        //4: remaining courses -> required - enrolled courses
        Set<String> remainingCourses = calculateRemainingCourses(requiredCourses, enrolledCourses);

        //5. display academic progress report
        displayAcademicProgressReport(enrolledCourses,remainingCourses);
    }

    private void displayAcademicProgressReport(List<String> enrolledCourses, Set<String> remainingCourses) {
        displayMessage("\n--- Academic Progress Report ---");
        displayMessage("Student Name: " + this.name);
        displayMessage("Student ID: " + this.id);
        displayMessage("Major: " + this.major);

        displayMessage("\nCompleted Courses:");
        if (enrolledCourses.isEmpty()) {
            displayMessage("   None");
        } else {
            for (String courseId : enrolledCourses) {
                Course course = Course.findCourseById(courseId);
                String courseName = (course != null) ? course.getName() : "Course Not Found";
                displayMessage("   " + courseId + ": " + courseName);
            }
        }

        displayMessage("\nRemaining Courses:");
        if (remainingCourses.isEmpty()) {
            displayMessage("   All major requirements have been completed. Congratulations!");
        } else {
            for (String courseId : remainingCourses) {
                Course course = Course.findCourseById(courseId);
                String courseName = (course != null) ? course.getName() : "Course Not Found";
                displayMessage("   " + courseId + ": " + courseName);
            }
        }
    }


    private Set<String> calculateRemainingCourses(List<String> requiredCourses, List<String> enrolledCourses) {
        System.out.println(requiredCourses);
        System.out.println(enrolledCourses);
        Set<String> completedCourses = new HashSet<>(enrolledCourses);

        Set<String> remainingCourses = new HashSet<>(requiredCourses);

        remainingCourses.removeAll(completedCourses);
        return remainingCourses;
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
    public void viewCourses() {
        String enrollmentsFile = "src/data/enrollments.txt"; // Path to enrollments file
        String coursesFile = "src/data/courses.txt"; // Path to courses file
        boolean hasCourses = false;

        Display.displayMessage("Your Enrolled Courses:");

        try (BufferedReader enrollmentReader = new BufferedReader(new FileReader(enrollmentsFile))) {
            String enrollmentLine;
            while ((enrollmentLine = enrollmentReader.readLine()) != null) {
                // Parse enrollment data
                String[] enrollmentData = enrollmentLine.split("::");
                if (enrollmentData.length >= 2) {
                    String studentIdFromFile = enrollmentData[0].trim();
                    String courseId = enrollmentData[1].replace("##", "").trim();

                    // Check if the entry is for the current student
                    if (studentIdFromFile.equals(this.id)) {
                        try (BufferedReader coursesReader = new BufferedReader(new FileReader(coursesFile))) {
                            String courseLine;
                            boolean courseFound = false;

                            while ((courseLine = coursesReader.readLine()) != null) {
                                // Parse course data
                                String[] courseData = courseLine.split("::");
                                if (courseData.length >= 5) {
                                    String courseIdFromFile = courseData[0].trim();
                                    String courseName = courseData[1].trim();
                                    String prerequisites = courseData[3].trim();

                                    if (courseIdFromFile.equals(courseId)) {
                                        Display.displayMessage(courseId + ": " + courseName);
                                        Display.displayMessage("   Prerequisites: " + (prerequisites.isEmpty() ? "None" : prerequisites));
                                        courseFound = true;
                                        break;
                                    }
                                }
                            }

                            if (!courseFound) {
                                Display.displayMessage(courseId + ": Course details not found in courses.txt.");
                            }
                        }
                        hasCourses = true;
                    }
                }
            }
        } catch (IOException e) {
            Display.displayMessage("Error reading enrollments or courses file.");
            e.printStackTrace();
        }

        // If no courses are found for the student
        if (!hasCourses) {
            Display.displayMessage("You are not enrolled in any courses.");
        }
    }
    public void submitFeedback() {
        // Prompt the student to input the course ID, rating, and comments
        String courseId = Utils.getInput("Enter the Course ID for feedback: ");
        String ratingStr = Utils.getInput("Enter your Rating (1-5): ");
        String comments = Utils.getInput("Enter your comments: ");

        try {
            // Convert the rating to an integer and validate it
            int rating = Integer.parseInt(ratingStr);
            if (rating < 1 || rating > 5) {
                throw new NumberFormatException("Rating must be between 1 and 5.");
            }

            // Define the path to the feedback file
            String feedbackFile = "src/data/feedback.txt";

            // Format the feedback entry (student ID, course ID, rating, comments)
            String feedbackEntry = this.id + "::" + courseId + "::" + rating + "::" + comments + "##";

            // Append the feedback entry to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(feedbackFile, true))) {
                writer.write(feedbackEntry);
                writer.newLine();
            }

            // Confirm to the student that their feedback was submitted
            Display.displayMessage("Thank you for your feedback!");
        } catch (NumberFormatException e) {
            // Handle invalid rating input
            Display.displayMessage("Invalid rating. Please enter a number between 1 and 5.");
        } catch (IOException e) {
            // Handle IO exceptions if there's an error with file writing
            Display.displayMessage("Error saving feedback. Please try again.");
        }
    }

    public void dropOut() {
        // Display a confirmation prompt
        String confirmation = Utils.getInput("Are you sure you want to drop out of the university? This action cannot be undone. Type 'yes' to confirm: ");

        if (!confirmation.equalsIgnoreCase("yes")) {
            Display.displayMessage("Dropout process canceled.");
            return;
        }

        // List of main files to modify
        String[] filesToUpdate = {
                "src/data/enrollments.txt",
                "src/data/feedback.txt",
                "src/data/students.txt"
        };

        // Path to the dropped out students file
        String droppedOutFile = "src/data/droppedOutStudents.txt";

        try (BufferedWriter droppedOutWriter = new BufferedWriter(new FileWriter(droppedOutFile, true))) {
            // Save the student information to droppedOutStudents.txt
            String droppedOutInfo = String.format("%s::%s::%s::%s##", this.id, this.name, this.major, this.email);
            droppedOutWriter.write(droppedOutInfo);
            droppedOutWriter.newLine();

            // Update main files
            for (String filePath : filesToUpdate) {
                File file = new File(filePath);
                File tempFile = new File(filePath + ".tmp");

                try (BufferedReader reader = new BufferedReader(new FileReader(file));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Skip lines containing the student's ID
                        if (!line.contains(this.id)) {
                            writer.write(line);
                            writer.newLine();
                        }
                    }
                }

                // Replace the original file with the updated temporary file
                if (!file.delete()) {
                    throw new IOException("Could not delete the original file: " + filePath);
                }
                if (!tempFile.renameTo(file)) {
                    throw new IOException("Could not rename the temporary file: " + tempFile.getAbsolutePath());
                }
            }

            // Handle submissions and assignments in course folders
            File coursesFolder = new File("src/data/courses");
            File[] courseFolders = coursesFolder.listFiles(File::isDirectory); // Get all course directories

            if (courseFolders != null) {
                for (File courseFolder : courseFolders) {
                    // Locate submissions.txt inside the course folder
                    File submissionsFile = new File(courseFolder, "submissions.txt");

                    if (submissionsFile.exists()) {
                        File tempFile = new File(courseFolder, "submissions.tmp");

                        try (BufferedReader reader = new BufferedReader(new FileReader(submissionsFile));
                             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                            String line;
                            while ((line = reader.readLine()) != null) {
                                // Skip lines containing the student's ID
                                if (!line.contains(this.id)) {
                                    writer.write(line);
                                    writer.newLine();
                                }
                            }
                        }

                        // Replace submissions.txt with the updated temporary file
                        if (!submissionsFile.delete()) {
                            throw new IOException("Could not delete the original file: " + submissionsFile.getAbsolutePath());
                        }
                        if (!tempFile.renameTo(submissionsFile)) {
                            throw new IOException("Could not rename the temporary file: " + tempFile.getAbsolutePath());
                        }
                    }
                }
            }

            // Notify the user of success
            Display.displayMessage("Your records have been removed. You have successfully dropped out of the university.");

        } catch (IOException e) {
            // Handle errors during file processing
            Display.displayMessage("An error occurred while processing your dropout request. Please try again.");
            e.printStackTrace();
        }
    }



}