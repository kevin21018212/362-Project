package users;

import helpers.User;
import helpers.FileUtils;
import helpers.Display;
import main.StudentRegistration;
import main.Transcript;
import Interfaces.RegistrarInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a Registrar user in the academic system responsible for managing student records,
 * enrollment, and transcript generation.
 */
public class Registrar extends User implements RegistrarInterface {
    /** Headers for the students.txt file */
    private static final String[] STUDENT_HEADERS = {"ID::Name::Email::Major##"};

    public void displayAllStudents(){
        List<String[]> students = FileUtils.readStructuredData("", "students.txt");
        Display.displayMessage("All Students:");
        Display.displayMessage("ID::Name::Email::Major");
        for (String[] student : students) {
            Display.displayMessage(student[0]+"::"+student[1]+"::"+student[2]+"::"+student[3]);
        }
    }

    /** Headers for the student_details.txt file */
    private static final String[] STUDENT_DETAILS_HEADERS = {
            "StudentID::DateOfBirth::Address::ProgramOfStudy::AcademicTerm##"
    };

    /**
     * Constructs a new Registrar with the specified credentials.
     *
     * @param id The unique identifier for the registrar
     * @param name The registrar's full name
     * @param email The registrar's email address
     */
    public Registrar(String id, String name, String email) {
        super(id, name, email);
    }

    /**
     * Enrolls a new student in the system with complete registration information.
     * Creates entries in both students.txt and student_details.txt.
     *
     * @param fullName Student's full name
     * @param dateOfBirth Student's date of birth (YYYY-MM-DD format)
     * @param contactInfo Student's email address
     * @param address Student's physical address
     * @param programOfStudy Student's chosen program/major
     * @param academicTerm Current academic term (e.g., FALL2024)
     */
    @Override
    public void enrollNewStudent(String fullName, String dateOfBirth, String contactInfo,
                                 String address, String programOfStudy, String academicTerm) {
        StudentRegistration registration = new StudentRegistration(
                fullName, dateOfBirth, contactInfo, address, programOfStudy, academicTerm
        );

        if (!validateStudentInfo(registration)) {
            Display.displayMessage("Invalid or incomplete student information.");
            return;
        }

        String studentId = generateStudentId();
        if (DataAccess.findStudentById(studentId) != null) {
            Display.displayMessage("Error: Student ID already exists.");
            return;
        }

        // Create student record in students.txt
        String[] studentData = {
                studentId + "::" + fullName + "::" + contactInfo + "::" + programOfStudy + "##"
        };
        List<String[]> existingStudents = FileUtils.readStructuredData("", "students.txt");
        existingStudents.add(studentData);
        FileUtils.writeStructuredData("", "students.txt", STUDENT_HEADERS, existingStudents);

        // Create detailed record in student_details.txt
        String[] detailsData = {
                studentId + "::" + dateOfBirth + "::" + address + "::" +
                        programOfStudy + "::" + academicTerm + "##"
        };
        List<String[]> existingDetails = FileUtils.readStructuredData("registrar", "student_details.txt");
        existingDetails.add(detailsData);
        FileUtils.writeStructuredData("registrar", "student_details.txt",
                STUDENT_DETAILS_HEADERS, existingDetails);

        Display.displayMessage("Student successfully enrolled with ID: " + studentId);
    }

    /**
     * Generates an official transcript for a student.
     * Creates a timestamped transcript file in the registrar/transcripts directory.
     *
     * @param studentId The ID of the student requesting the transcript
     */
    @Override
    public void generateTranscript(String studentId) {
        Student student = DataAccess.findStudentById(studentId);
        if (student == null) {
            Display.displayMessage("Student not found.");
            return;
        }

        Transcript transcript = new Transcript(studentId);
        String transcriptContent = transcript.generateTranscript();
        if (!transcript.isGenerateTranscript()) {
            Display.displayMessage("Error: Transcript could not be generated.");
            return;
        }

        // Create timestamped filename
        String timestamp = java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "transcripts/" + studentId + "_" + timestamp + ".txt";

        // Ensure transcripts directory exists
        new File("src/data/registrar/transcripts").mkdirs();

        // Save transcript with proper format
        List<String[]> transcriptData = new ArrayList<>();
        transcriptData.add(new String[]{"Type::Content##"});
        transcriptData.add(new String[]{"Content::" + transcriptContent + "##"});
        FileUtils.writeStructuredData("registrar", fileName,
                new String[]{"Type::Content##"}, transcriptData);

        Display.displayMessage("Transcript generated and saved to: " + fileName);
        Display.displayMessage("\nTranscript Content:\n" + transcriptContent);
    }

    /**
     * Validates student registration information.
     *
     * @param registration The StudentRegistration object to validate
     * @return true if all required information is present and valid
     */
    @Override
    public boolean validateStudentInfo(StudentRegistration registration) {
        return registration.validateInformation();
    }

    /**
     * Generates a unique student ID.
     *
     * @return A 4-digit random student ID
     */
    @Override
    public String generateStudentId() {
        long timestamp = System.currentTimeMillis();
        Random random = new Random(timestamp);
        return String.format("%04d", random.nextInt(9999));
    }

    @Override
    public void swapCourseSection(String studentId, String courseId, String sectionId) {

    }
}