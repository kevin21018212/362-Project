package Interfaces;

import main.StudentRegistration;

import java.util.Random;

public interface RegistrarInterface {
    void enrollNewStudent(String fullName, String dateOfBirth, String contactInfo,
                          String address, String programOfStudy, String academicTerm);
    void generateTranscript(String studentId);
    boolean validateStudentInfo(StudentRegistration registration);
    static String generateStudentId() {
        long timestamp = System.currentTimeMillis();
        Random random = new Random(timestamp);
        return String.format("%04d", random.nextInt(9999));
    }
    void swapCourseSection(String studentId, String courseId, String sectionId);

}

