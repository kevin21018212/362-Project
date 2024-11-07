package Interfaces;

public interface RegistrarInterface {
    boolean validateRegistrar(String employeeId);
    boolean assignDegreeProgram(String studentId, String programId);
    boolean processEnrollment(String studentId, String courseId);
    boolean generateStudentDocuments(String studentId);
}
