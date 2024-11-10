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

public class Registrar extends User implements RegistrarInterface {
    private static final String[] STUDENT_HEADERS = {"ID", "Name", "Email", "Major"};
    private static final String[] STUDENT_DETAILS_HEADERS = {
            "StudentID", "DateOfBirth", "Address", "ProgramOfStudy", "AcademicTerm"
    };

    public Registrar(String id, String name, String email) {
        super(id, name, email);
    }

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

        // Create new student data
        String[] studentData = {studentId, fullName, contactInfo, programOfStudy};
        List<String[]> existingStudents = FileUtils.readStructuredData("", "students.txt");
        existingStudents.add(studentData);
        FileUtils.writeStructuredData("", "students.txt", STUDENT_HEADERS, existingStudents);

        // Save additional student information
        String[] detailsData = {
                studentId, dateOfBirth, address, programOfStudy, academicTerm
        };
        List<String[]> existingDetails = FileUtils.readStructuredData("registrar", "student_details.txt");
        existingDetails.add(detailsData);
        FileUtils.writeStructuredData("registrar", "student_details.txt",
                STUDENT_DETAILS_HEADERS, existingDetails);

        Display.displayMessage("Student successfully enrolled with ID: " + studentId);
    }

    @Override
    public void generateTranscript(String studentId) {
        Student student = DataAccess.findStudentById(studentId);
        if (student == null) {
            Display.displayMessage("Student not found.");
            return;
        }

        // Create and generate transcript
        Transcript transcript = new Transcript(studentId);
        String transcriptContent = transcript.generateTranscript();
        if (!transcript.isGenerateTranscript()) {
            Display.displayMessage("Error: Transcript could not be generated.");
            return;
        }

        // Save transcript to file with timestamp
        String timestamp = java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "transcripts/" + studentId + "_" + timestamp + ".txt";

        // Ensure directory exists
        new File("src/data/registrar/transcripts").mkdirs();

        // Save transcript with headers
        List<String[]> transcriptData = new ArrayList<>();
        transcriptData.add(new String[]{"Content", transcriptContent});
        FileUtils.writeStructuredData("registrar", fileName,
                new String[]{"Type", "Content"}, transcriptData);

        Display.displayMessage("Transcript generated and saved to: " + fileName);
        Display.displayMessage("\nTranscript Content:\n" + transcriptContent);
    }

    @Override
    public boolean validateStudentInfo(StudentRegistration registration) {
        return registration.validateInformation();
    }

    @Override
    public String generateStudentId() {
        long timestamp = System.currentTimeMillis();
        Random random = new Random(timestamp);
        return String.format("%04d", random.nextInt(9999));
    }
}