package Interfaces;

public interface RegistrarInterface {
    boolean validateRegistrar(String employeeId);
    boolean assignDegreeProgram(String studentId, String programId);
    boolean processEnrollment(String studentId, String courseId);
    boolean generateStudentDocuments(String studentId);
    void enrollNewStudent(String fullName, String dateOfBirth, String contactInfo,
                          String address, String programOfStudy, String academicTerm);
    void generateTranscript(String studentId);
    boolean validateStudentDocuments(String studentId);
    String generateStudentId();

}

