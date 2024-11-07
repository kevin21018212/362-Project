package main;

import users.Student;
import users.DataAccess;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Transcript {
    private final Student student;
    private final List<Enrollment> enrollments;
    private double gpa;
    private int creditsCompleted;
    private int creditsInProgress;

    public Transcript(String studentId) {
        this.student = DataAccess.findStudentById(studentId);
        this.enrollments = Enrollment.loadEnrollments().stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .collect(Collectors.toList());
        calculateStats();
    }

    private void calculateStats() {
        double totalPoints = 0;
        creditsCompleted = 0;
        creditsInProgress = 0;

        for (Enrollment enrollment : enrollments) {
            Course course = Course.findCourseById(enrollment.getCourseId());
            if (course != null) {
                //TODO
                // Add logic for GPA calculation based on grades
                creditsCompleted += 3; // Assuming 3 credits per course
            }
        }

        this.gpa = creditsCompleted > 0 ? totalPoints / creditsCompleted : 0.0;
    }

    public String generateTranscript() {
        StringBuilder transcript = new StringBuilder();
        transcript.append("OFFICIAL TRANSCRIPT\n\n");
        transcript.append("Student Information:\n");
        transcript.append("ID: ").append(student.getId()).append("\n");
        transcript.append("Name: ").append(student.getName()).append("\n");
        transcript.append("Email: ").append(student.getEmail()).append("\n\n");

        transcript.append("Course History:\n");
        for (Enrollment enrollment : enrollments) {
            Course course = Course.findCourseById(enrollment.getCourseId());
            if (course != null) {
                transcript.append(course.getId())
                        .append(" - ")
                        .append(course.getName())
                        .append("\n");
            }
        }

        transcript.append("\nAcademic Summary:\n");
        transcript.append("GPA: ").append(String.format("%.2f", gpa)).append("\n");
        transcript.append("Credits Completed: ").append(creditsCompleted).append("\n");
        transcript.append("Credits In Progress: ").append(creditsInProgress).append("\n");

        return transcript.toString();
    }
}