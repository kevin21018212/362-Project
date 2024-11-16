package Interfaces;

import main.StudentRegistration;

public interface RegistrarInterface {
    void enrollNewStudent(String fullName, String dateOfBirth, String contactInfo,
                          String address, String programOfStudy, String academicTerm);
    void generateTranscript(String studentId);
    boolean validateStudentInfo(StudentRegistration registration);
    String generateStudentId();
    void swapCourseSection(String studentId, String courseId, String sectionId);

}

