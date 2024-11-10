package main;

import helpers.FileUtils;
import users.Student;
import users.DataAccess;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class Transcript {
    private final Student student;
    private final List<Enrollment> enrollments;
    private double gpa;
    private int creditsCompleted;
    private int creditsInProgress;
    private Map<String, CourseRecord> courseRecords;
    private boolean generateTranscript;

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

    private static class CourseRecord {
        String courseId;
        String grade;
        String term;

        CourseRecord(String courseId, String grade, String term) {
            this.courseId = courseId;
            this.grade = grade;
            this.term = term;
        }
    }

    public Transcript(String studentId) {
        this.student = DataAccess.findStudentById(studentId);
        this.enrollments = loadEnrollments(studentId);
        this.courseRecords = loadCourseRecords(studentId);
        if (student == null || enrollments.isEmpty() || courseRecords.isEmpty()) {
            System.out.println("Error: Student or course records not found.");
            this.generateTranscript = false;
            return;
        }
        calculateStats();
        this.generateTranscript = true;
    }

    private List<Enrollment> loadEnrollments(String studentId) {
        return Enrollment.loadEnrollments().stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    private Map<String, CourseRecord> loadCourseRecords(String studentId) {
        Map<String, CourseRecord> records = new HashMap<>();
        List<String> lines = FileUtils.readFromFile("", "grades.txt");

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts[0].equals(studentId)) {
                records.put(parts[1], new CourseRecord(parts[1], parts[2], parts[3]));
            }
        }
        if (records.isEmpty()) {
            System.out.println("No course records found for student ID: " + studentId);
            return null;
        }
        return records;
    }

    private void calculateStats() {
        double totalPoints = 0;
        creditsCompleted = 0;
        creditsInProgress = 0;

        for (Enrollment enrollment : enrollments) {
            String courseId = enrollment.getCourseId();
            CourseRecord record = courseRecords.get(courseId);

            if (record != null && record.grade != null) {
                Double gradePoints = GRADE_POINTS.get(record.grade);
                if (gradePoints != null) {
                    totalPoints += gradePoints * 3; // 3 credits per course
                    creditsCompleted += 3;
                }
            } else {
                creditsInProgress += 3;
            }
        }

        this.gpa = creditsCompleted > 0 ? totalPoints / creditsCompleted : 0.0;
    }

    public String generateTranscript() {
        if (!generateTranscript) {
            return "Error: Transcript generation failed.";
        }
        StringBuilder transcript = new StringBuilder();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Header
        transcript.append("OFFICIAL TRANSCRIPT\n");
        transcript.append("Generated: ").append(timestamp).append("\n\n");

        // Student Information
        transcript.append("STUDENT INFORMATION\n");
        transcript.append("==================\n");
        transcript.append("ID: ").append(student.getId()).append("\n");
        transcript.append("Name: ").append(student.getName()).append("\n");
        transcript.append("Email: ").append(student.getEmail()).append("\n\n");

        // Course History
        transcript.append("COURSE HISTORY\n");
        transcript.append("=============\n");
        Map<String, List<CourseRecord>> termRecords = new TreeMap<>();

        for (Enrollment enrollment : enrollments) {
            String courseId = enrollment.getCourseId();
            CourseRecord record = courseRecords.get(courseId);
            String term = record != null ? record.term : "Current Term";

            termRecords.computeIfAbsent(term, k -> new ArrayList<>())
                    .add(new CourseRecord(courseId,
                            record != null ? record.grade : "IP",
                            term));
        }

        for (Map.Entry<String, List<CourseRecord>> entry : termRecords.entrySet()) {
            transcript.append("\nTerm: ").append(entry.getKey()).append("\n");
            for (CourseRecord record : entry.getValue()) {
                transcript.append(String.format("%-8s %2s credits  Grade: %s\n",
                        record.courseId,
                        "3",
                        record.grade));
            }
        }

        // Academic Summary
        transcript.append("\nACADEMIC SUMMARY\n");
        transcript.append("================\n");
        transcript.append(String.format("Cumulative GPA: %.2f\n", gpa));
        transcript.append(String.format("Credits Completed: %d\n", creditsCompleted));
        transcript.append(String.format("Credits In Progress: %d\n", creditsInProgress));
        transcript.append(String.format("Total Credits: %d\n", creditsCompleted + creditsInProgress));

        // Notes
        transcript.append("\nNOTES\n");
        transcript.append("=====\n");
        transcript.append("IP = In Progress\n");

        return transcript.toString();
    }

    public void saveTranscript() {
        String fileName = String.format("transcripts/%s_%s.txt",
                student.getId(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));
        FileUtils.writeToFile("registrar", fileName, generateTranscript());
    }
}