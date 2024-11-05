public class Assignment {
    private String assignmentID;
    private String title;
    private String dueDate;
    private Course course;

    // Constructor
    public Assignment(String assignmentID, String title, String dueDate, Course course) {
        this.assignmentID = assignmentID;
        this.title = title;
        this.dueDate = dueDate;
        this.course = course;
    }


    public String getAssignmentID() { return assignmentID; }
    public void setAssignmentID(String assignmentID) { this.assignmentID = assignmentID; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
}
