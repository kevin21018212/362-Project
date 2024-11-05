package main;

import main.Course;

public class Assignment {
    private String id;
    private String title;
    private String dueDate;
    private Course course;
    private String grade;

    public Assignment(String id, String title, String dueDate, Course course) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.course = course;
        this.grade = "Ungraded";
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDueDate() { return dueDate; }
    public Course getCourse() { return course; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    @Override
    public String toString() {
        return id + "," + title + "," + dueDate + "," + course.getId() + "," + grade;
    }
}
