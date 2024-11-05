public class Assignment {
    private String assignmentID;
    private String title;
    private String dueDate;
    private Course course;

    public Assignment(String assignmentID, String title, String dueDate, Course course) {
        this.assignmentID = assignmentID;
        this.title = title;
        this.dueDate = dueDate;
        this.course = course;
    }
    public String getAssignmentID() { return assignmentID; }
    public String getTitle() { return title; }
    public String getDueDate() { return dueDate; }
    public Course getCourse() { return course; }

    public String toString() {
        return assignmentID + "," + title + "," + dueDate + "," + course.getCourseID();
    }
}
