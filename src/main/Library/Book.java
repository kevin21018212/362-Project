package main.Library;

import helpers.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private static final String BOOKS_FILE = "books.txt";
    private static final String DIRECTORY = "library/";

    private String id;
    private int amount;
    private String courseId;
    private List<String> checkedOutBy;

    public Book(String id, int amount, String courseId, List<String> checkedOutBy) {
        this.id = id;
        this.amount = amount;
        this.courseId = courseId;
        this.checkedOutBy = new ArrayList<>(checkedOutBy);
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public String getCourseId() {
        return courseId;
    }

    public List<String> getCheckedOutBy() {
        return checkedOutBy;
    }

    public boolean isAvailable() {
        return checkedOutBy.size() < amount;
    }

    public void checkout(String studentId) {
        if (isAvailable() && !checkedOutBy.contains(studentId)) {
            checkedOutBy.add(studentId);
        }
    }

    public void returnBook(String studentId) {
        checkedOutBy.remove(studentId);
    }

    // Load all books from the file
    public static List<Book> loadBooks() {
        List<String[]> bookData = FileUtils.readStructuredData(DIRECTORY, BOOKS_FILE);
        List<Book> books = new ArrayList<>();

        for (String[] row : bookData) {
            String id = row[0];
            int amount = Integer.parseInt(row[1]);
            String courseId = row[2];
            String[] checkedOutRaw = row[3].replace("[", "").replace("]", "").split(", ");
            List<String> checkedOutBy = checkedOutRaw.length == 1 && checkedOutRaw[0].isEmpty() ? new ArrayList<>() : List.of(checkedOutRaw);
            books.add(new Book(id, amount, courseId, checkedOutBy));
        }
        return books;
    }

    // Save books to the file
    public static void saveBooks(List<Book> books) {
        List<String[]> data = new ArrayList<>();
        for (Book book : books) {
            data.add(new String[]{
                    book.getId(),
                    String.valueOf(book.getAmount()),
                    book.getCourseId(),
                    book.getCheckedOutBy().toString()
            });
        }
        FileUtils.writeStructuredData(DIRECTORY, BOOKS_FILE, new String[]{"BookID", "Amount", "CourseID", "CheckedOutBy"}, data);
    }

    @Override
    public String toString() {
        return "BookID: " + id + ", Amount: " + amount + ", Course: " + courseId + ", CheckedOut: " + checkedOutBy;
    }
}
