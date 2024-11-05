package users;

import helpers.FileUtils;
import helpers.User;
import helpers.Utils;
import main.Assignment;
import main.Course;
import main.Enrollment;

import java.util.ArrayList;
import java.util.List;

public class Instructor extends User {

    public Instructor(String id, String name, String email) {
        super(id, name, email);
    }

    public static Instructor findInstructorById(String id) {
        return DataAccess.findInstructorById(id);
    }

    public void gradeAssignments() {
        String courseId = Utils.getInput("Enter Course ID: ");
        Course course = Course.findCourseById(courseId);
        if (course != null && course.getInstructorId().equals(this.id)) {
            List<Assignment> assignments = Assignment.loadAssignments(courseId);
            if (assignments.isEmpty()) {
                Utils.displayMessage("No assignments found for this course.");
                return;
            }

            Utils.displayMessage("Available Assignments:");
            for (int i = 0; i < assignments.size(); i++) {
                Assignment assignment = assignments.get(i);
                Utils.displayMessage((i + 1) + ". " + assignment.getId() + ": " + assignment.toString());
            }

            int assignmentChoice = Integer.parseInt(Utils.getInput("Choose an assignment number to grade: ")) - 1;
            if (assignmentChoice < 0 || assignmentChoice >= assignments.size()) {
                Utils.displayMessage("Invalid choice.");
                return;
            }

            Assignment selectedAssignment = assignments.get(assignmentChoice);
            checkMissingSubmissions(courseId, selectedAssignment.getId());
            gradeSelectedAssignment(courseId, selectedAssignment.getId());
        } else {
            Utils.displayMessage("Course not found or you are not the instructor for this course.");
        }
    }

    private void gradeSelectedAssignment(String courseId, String assignmentId) {
        String fileName = "courses/" + courseId + "/submissions.txt";
        List<String> submissions = FileUtils.readFromFile("", fileName);
        List<String> updatedSubmissions = new ArrayList<>();

        for (String submission : submissions) {
            String[] data = submission.split(",");
            if (data[1].equals(assignmentId)) {
                String studentId = data[2];
                String grade = data[3];
                String submittedDate = data[4];

                if (grade.equals("Not Graded")) {
                    Utils.displayMessage("Student ID: " + studentId + ", Submitted on: " + submittedDate);
                    boolean isLate = checkIfLate(courseId, assignmentId, submittedDate);
                    if (isLate) {
                        Utils.displayMessage("This submission is late.");
                    }

                    String newGrade = Utils.getInput("Enter grade for this assignment: ");
                    data[3] = newGrade;
                }
            }
            updatedSubmissions.add(String.join(",", data));
        }

        FileUtils.OverwriteFile("", fileName, updatedSubmissions);
        Utils.displayMessage("Grading completed for assignment ID: " + assignmentId);
    }


    private void checkMissingSubmissions(String courseId, String assignmentId) {
        List<Enrollment> enrollments = Enrollment.loadEnrollments();
        List<String> enrolledStudentIds = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getCourseId().equals(courseId)) {
                enrolledStudentIds.add(enrollment.getStudentId());
            }
        }

        List<String> submissions = FileUtils.readFromFile("", "courses/" + courseId + "/submissions.txt");
        List<String> studentsWhoSubmitted = new ArrayList<>();

        for (String submission : submissions) {
            String[] data = submission.split(",");
            if (data[1].equals(assignmentId)) {
                studentsWhoSubmitted.add(data[2]);
            }
        }

        Utils.displayMessage("Checking for missing submissions...");
        for (String studentId : enrolledStudentIds) {
            if (!studentsWhoSubmitted.contains(studentId)) {
                Utils.displayMessage("Missing submission for Student ID: " + studentId + " for Assignment ID: " + assignmentId);
            }
        }
    }



    private boolean checkIfLate(String courseId, String assignmentId, String submittedDate) {
        Assignment assignment = Assignment.findAssignmentById(courseId, assignmentId);
        if (assignment != null) {
            String dueDate = assignment.getDueDate();
            return submittedDate.compareTo(dueDate) > 0;
        }
        return false;
    }


}

