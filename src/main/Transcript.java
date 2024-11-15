package main;

import helpers.FileUtils;
import users.Student;
import users.DataAccess;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * Represents a student's academic transcript, containing course records, grades, and academic statistics.
 * This class manages the creation, calculation, and generation of official student transcripts.
 */
public class Transcript {
    private final Student student;
    private final List<Enrollment> enrollments;
    private double gpa;
    private int creditsCompleted;
    private int creditsInProgress;
    private Map<String, CourseRecord> courseRecords;
    private boolean generateTranscript;

    /** Maps letter grades to their corresponding grade point values */
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

    /**
     * Inner class representing a single course record in a student's transcript.
     */
    private static class CourseRecord {
        String courseId;
        String grade;
        String term;

        /**
         * Constructs a new CourseRecord with the specified details.
         * @param courseId The unique identifier of the course
         * @param grade The grade received in the course
         * @param term The academic term in which the course was taken
         */
        CourseRecord(String courseId, String grade, String term) {
            this.courseId = courseId;
            this.grade = grade;
            this.term = term;
        }
    }

    /**
     * Constructs a new Transcript for the specified student.
     * @param studentId The unique identifier of the student
     */
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

    /**
     * Loads all enrollments for the specified student.
     * @param studentId The unique identifier of the student
     * @return A list of enrollments for the student
     */
    private List<Enrollment> loadEnrollments(String studentId) {
        return Enrollment.loadEnrollments().stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    /**
     * Loads course records for the specified student from the grades file.
     * @param studentId The unique identifier of the student
     * @return A map of course IDs to CourseRecord objects
     */
    private Map<String, CourseRecord> loadCourseRecords(String studentId) {
        Map<String, CourseRecord> records = new HashMap<>();
        List<String[]> data = FileUtils.readStructuredData("", "grades.txt");

        for (String[] row : data) {
            if (row[0].equals(studentId)) {
                records.put(row[1], new CourseRecord(row[1], row[2], row[3]));
            }
        }
        if (records.isEmpty()) {
            System.out.println("No course records found for student ID: " + studentId);
            return null;
        }
        return records;
    }

    /**
     * Calculates academic statistics including GPA and credit totals.
     */
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
                    totalPoints += gradePoints * 3;
                    creditsCompleted += 3;
                }
            } else {
                creditsInProgress += 3;
            }
        }

        this.gpa = creditsCompleted > 0 ? totalPoints / creditsCompleted : 0.0;
    }

    /**
     * Generates a formatted transcript string containing all academic information.
     * @return A formatted string representing the complete transcript
     */
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

    /**
     * Checks if the transcript can be generated.
     * @return true if the transcript can be generated, false otherwise
     */
    public boolean isGenerateTranscript() {
        return generateTranscript;
    }
}