package Interfaces;

public interface TranscriptInterface {
    boolean generateTranscript(String studentId);
    boolean validateStudentStatus(String studentId);
    float calculateGPA(String studentId);
//    List<Course> getCompletedCourses(String studentId);
}
