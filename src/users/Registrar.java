package users;

import helpers.User;
import helpers.FileUtils;
import helpers.Display;
import main.StudentRegistration;
import main.Transcript;
import Interfaces.RegistrarInterface;

import java.util.Random;

public class Registrar extends User implements RegistrarInterface {

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

        Student newStudent = new Student(studentId, fullName, contactInfo);
        FileUtils.writeToFile("", "students.txt", newStudent.toString());

        // Save additional student information
        String studentInfo = String.join(",", studentId, dateOfBirth, address,
                programOfStudy, academicTerm);
        FileUtils.writeToFile("registrar", "student_details.txt", studentInfo);

        Display.displayMessage("Student successfully enrolled with ID: " + studentId);
    }

    @Override
    public void generateTranscript(String studentId) {
        Student student = DataAccess.findStudentById(studentId);
        if (student == null) {
            Display.displayMessage("Student not found.");
            return;
        }

        Transcript transcript = new Transcript(studentId);
        String transcriptContent = transcript.generateTranscript();

        // Save transcript to file
        String fileName = "transcripts/" + studentId + "_" + System.currentTimeMillis() + ".txt";
        FileUtils.writeToFile("registrar", fileName, transcriptContent);

        Display.displayMessage("Transcript generated successfully.");
        Display.displayMessage(transcriptContent);
    }

    @Override
    public boolean validateStudentInfo(StudentRegistration registration) {
        return registration.validateInformation();
    }

    @Override
    public String generateStudentId() {
        long timestamp = System.currentTimeMillis();
        Random random = new Random(timestamp);
        return random.nextInt(9999)+"";
    }





}
