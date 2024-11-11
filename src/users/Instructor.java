package users;

import helpers.Display;
import helpers.FileUtils;
import helpers.User;
import helpers.Utils;
import main.Assignment;
import main.Course;
import main.Enrollment;
import main.Submission;

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
        // Display all available courses before prompting for a course ID
        Course.displayAllCourses();

        String courseId = Utils.getInput("Enter Course ID: ");
        Course course = Course.findCourseById(courseId);

        if (course != null && course.getInstructorId().equals(this.id)) {
            List<Assignment> assignments = Assignment.loadAssignments(courseId);
            if (assignments.isEmpty()) {
                Display.displayMessage("No assignments found for this course.");
                return;
            }

            Display.displayMessage("Available Assignments:");
            for (int i = 0; i < assignments.size(); i++) {
                Assignment assignment = assignments.get(i);
                Display.displayMessage((i + 1) + ". " + assignment);
            }

            int assignmentChoice = Integer.parseInt(Utils.getInput("\nChoose an assignment number to grade: ")) - 1;
            if (assignmentChoice < 0 || assignmentChoice >= assignments.size()) {
                Display.displayMessage("Invalid choice.");
                return;
            }

            Assignment selectedAssignment = assignments.get(assignmentChoice);
            checkMissingSubmissions(courseId, selectedAssignment);
            gradeSelectedAssignment(courseId, selectedAssignment);
        } else {
            Display.displayMessage("Course not found or you are not the instructor for this course.");
        }
    }


    private void gradeSelectedAssignment(String courseId, Assignment assignment) {
        List<Submission> submissions = Submission.loadSubmissions(courseId, assignment.getId());
        if (submissions.isEmpty()) {
            Display.displayMessage("No submissions found for this assignment.");
            return;
        }

        List<String[]> updatedSubmissions = new ArrayList<>();
        for (Submission submission : submissions) {
            String[] submissionData = new String[5];
            submissionData[0] = submission.getId();
            submissionData[1] = submission.getAssignmentId();
            submissionData[2] = submission.getStudentId();
            submissionData[3] = submission.getGrade();
            submissionData[4] = submission.getSubmittedDate();

            if ("Not Graded".equals(submission.getGrade())) {
                Display.displayMessage("Student ID: " + submission.getStudentId() +
                        ", Submitted on: " + submission.getSubmittedDate());
                if (isLate(assignment.getDueDate(), submission.getSubmittedDate())) {
                    Display.displayMessage("This submission is late.\n");
                }
                submissionData[3] = Utils.getInput("Enter grade for this assignment: ");
            }
            updatedSubmissions.add(submissionData);
        }

        // In gradeSelectedAssignment method
        String fileName = assignment.getId()+"_graded_submissions.txt";
        List<String[]> updatedSubmissionData = new ArrayList<>();

        for (Submission submission : submissions) {
            String[] data = {
                    submission.getId() + "::" +
                    submission.getAssignmentId() + "::" +
                    submission.getStudentId() + "::" +
                    submission.getGrade() + "::" +
                    submission.getSubmittedDate() + "##"
            };
            updatedSubmissionData.add(data);
        }

        FileUtils.writeStructuredData("courses", fileName,
                new String[]{"SubmissionID::AssignmentID::StudentID::Grade::SubmittedDate##"},
                updatedSubmissionData);
        Display.displayMessage("Grading completed for assignment ID: " + assignment.getId());
    }

    private void checkMissingSubmissions(String courseId, Assignment assignment) {
        List<Enrollment> enrollments = Enrollment.loadEnrollments();
        List<String> enrolledStudentIds = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getCourseId().equals(courseId)) {
                enrolledStudentIds.add(enrollment.getStudentId());
            }
        }

        List<String[]> submissions = Assignment.getSubmissions(courseId, assignment.getId());
        List<String> studentsWhoSubmitted = new ArrayList<>();
        for (String[] submission : submissions) {
            studentsWhoSubmitted.add(submission[2]);
        }

        Display.displayMessage("Checking for missing submissions...\n");
        for (String studentId : enrolledStudentIds) {
            if (!studentsWhoSubmitted.contains(studentId)) {
                Display.displayMessage("Missing submission for Student ID: " + studentId + " for Assignment ID: " + assignment.getId());
            }
        }
    }

    private boolean isLate(String dueDate, String submittedDate) {
        return submittedDate.compareTo(dueDate) > 0;
    }
}