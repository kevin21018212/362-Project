package main;

import helpers.FileUtils;
import users.Student;
import users.DataAccess;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transcript {
    private final Student student;
    private final List<Enrollment> enrollments;
    private double gpa;
    private int creditsCompleted;
    private int creditsInProgress;
    private Map<String, String> grades;
    private static final Map<String, Double> GRADE_POINTS = new HashMap<>();

    static {
        GRADE_POINTS.put("A", 4.0);
        GRADE_POINTS.put("A-", 3.7);
        GRADE_POINTS.put("B+", 3.3);
        GRADE_POINTS.put("B", 3.0);
        GRADE_POINTS.put("B-", 2.7);
        GRADE_POINTS.put("C+", 2.3);
        GRADE_POINTS.put("C", 2.0);
        GRADE_POINTS.put("C-", 1.7);
        GRADE_POINTS.put("D+", 1.3);
        GRADE_POINTS.put("D", 1.0);
        GRADE_POINTS.put("F", 0.0);
    }

    public Transcript(String studentId) {
        this.student = DataAccess.findStudentById(studentId);
        this.enrollments = Enrollment.loadEnrollments().stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .collect(Collectors.toList());
        this.grades = loadGrades(studentId);
        calculateStats();
    }

    private Map<String, String> loadGrades(String studentId) {
        Map<String, String> gradeMap = new HashMap<>();
        List<String> lines = FileUtils.readFromFile("", "grades.txt");
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts[0].equals(studentId)) {
                gradeMap.put(parts[1], parts[2]); // courseId -> grade
            }
        }
        return gradeMap;
    }

    private void calculateStats() {
        double totalPoints = 0;
        creditsCompleted = 0;
        creditsInProgress = 0;

        for (Enrollment enrollment : enrollments) {
            Course course = Course.findCourseById(enrollment.getCourseId());
            if (course != null) {
                String grade = grades.get(course.getId());
                if (grade != null) {
                    Double gradePoints = GRADE_POINTS.get(grade);
                    if (gradePoints != null) {
                        totalPoints += gradePoints * 3; // 3 credits per course
                        creditsCompleted += 3;
                    }
                } else {
                    creditsInProgress += 3;
                }
            }
        }

        this.gpa = creditsCompleted > 0 ? totalPoints / creditsCompleted : 0.0;
    }

    public String generateTranscript() {
        StringBuilder transcript = new StringBuilder();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        transcript.append("OFFICIAL TRANSCRIPT\n");
        transcript.append("Generated: ").append(timestamp).append("\n\n");

        transcript.append("STUDENT INFORMATION\n");
        transcript.append("==================\n");
        transcript.append("ID: ").append(student.getId()).append("\n");
        transcript.append("Name: ").append(student.getName()).append("\n");
        transcript.append("Email: ").append(student.getEmail()).append("\n\n");

        transcript.append("COURSE HISTORY\n");
        transcript.append("=============\n");
        for (Enrollment enrollment : enrollments) {
            Course course = Course.findCourseById(enrollment.getCourseId());
            if (course != null) {
                String grade = grades.getOrDefault(course.getId(), "IP");
                transcript.append(String.format("%-8s %-35s %2s credits  Grade: %s\n",
                        course.getId(),
                        course.getName(),
                        "3",
                        grade));
            }
        }

        transcript.append("\nACADEMIC SUMMARY\n");
        transcript.append("================\n");
        transcript.append(String.format("Cumulative GPA: %.2f\n", gpa));
        transcript.append(String.format("Credits Completed: %d\n", creditsCompleted));
        transcript.append(String.format("Credits In Progress: %d\n", creditsInProgress));
        transcript.append(String.format("Total Credits: %d\n", creditsCompleted + creditsInProgress));

        transcript.append("\nNOTES\n");
        transcript.append("=====\n");
        transcript.append("IP = In Progress\n");
        transcript.append("This transcript is unofficial unless signed by the registrar\n");

        // Placeholder for university bill
        transcript.append("\nFINANCIAL STATUS\n");
        transcript.append("===============\n");
        transcript.append("Please contact the Bursar's office for current account status.\n");

        return transcript.toString();
    }

    public void saveTranscript() {
        String fileName = String.format("transcripts/%s_%s.txt",
                student.getId(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));
        FileUtils.writeToFile("registrar", fileName, generateTranscript());
    }
}