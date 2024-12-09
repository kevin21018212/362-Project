package main.Library;

import helpers.FileUtils;
import main.Enrollment;
import users.DataAccess;
import users.Student;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private static final String DIRECTORY = "library/";
    private static final String ROOMS_FILE = "rooms.txt";

    // Load all rooms
    public static List<Room> loadRooms() {
        List<String[]> roomData = FileUtils.readStructuredData(DIRECTORY, ROOMS_FILE);
        List<Room> rooms = new ArrayList<>();

        for (String[] row : roomData) {
            String id = row[0];
            int capacity = Integer.parseInt(row[1]);
            String[] majorsArray = row[2].replace("[", "").replace("]", "").split(", ");
            List<String> allowedMajors = List.of(majorsArray);
            rooms.add(new Room(id, capacity, allowedMajors));
        }
        return rooms;
    }

    // Show all rooms, differentiating between accessible and inaccessible
    public static void showAllRooms(String studentId) {
        Student student = DataAccess.findStudentById(studentId);
        if (student == null) {
            System.out.println("Invalid student ID!");
            return;
        }

        String major = student.getMajor();
        List<Room> rooms = loadRooms();

        System.out.println("Accessible Rooms:");
        for (Room room : rooms) {
            if (room.isMajorAllowed(major)) {
                System.out.println(room);
            }
        }

        System.out.println("\nInaccessible Rooms:");
        for (Room room : rooms) {
            if (!room.isMajorAllowed(major)) {
                System.out.println(room);
            }
        }
    }


    //Show all books
    public static void showBooks(String studentId) {
        List<Book> books = Book.loadBooks();
        List<String> enrolledCourses = Enrollment.getEnrolledCourses(studentId);

        System.out.println("Books for Enrolled Courses:");
        for (Book book : books) {
            if (enrolledCourses.contains(book.getCourseId())) {
                System.out.println(book);
            }
        }

        System.out.println("\nBooks for Non-Enrolled Courses:");
        for (Book book : books) {
            if (!enrolledCourses.contains(book.getCourseId())) {
                System.out.println(book);
            }
        }
    }
    //Show a students checkout book
    public static void showCheckedOutBooks(String studentId) {
        List<Book> books = Book.loadBooks();

        System.out.println("Books Checked Out by " + studentId + ":");
        boolean hasBooks = false;
        for (Book book : books) {
            if (book.getCheckedOutBy().contains(studentId)) {
                System.out.println(book);
                hasBooks = true;
            }
        }

        if (!hasBooks) {
            System.out.println("No books currently checked out.");
        }
    }

    //Checkout a single book
    public static void checkoutBook(String studentId, String bookId) {
        List<Book> books = Book.loadBooks();
        int currentCheckedOutCount = (int) books.stream().filter(book -> book.getCheckedOutBy().contains(studentId)).count();

        if (currentCheckedOutCount >= 5) {
            System.out.println("You have already checked out the maximum number of books.");
            return;
        }

        for (Book book : books) {
            if (book.getId().equalsIgnoreCase(bookId)) {
                if (book.isAvailable()) {
                    book.checkout(studentId);
                    Book.saveBooks(books);
                    System.out.println("Book checked out successfully!");
                } else {
                    System.out.println("Book is not available for checkout.");
                }
                return;
            }
        }

        System.out.println("Book not found.");
    }

    // Return a book
    public static void returnBook(String studentId, String bookId) {
        List<Book> books = Book.loadBooks();

        for (Book book : books) {
            if (book.getId().equalsIgnoreCase(bookId) && book.getCheckedOutBy().contains(studentId)) {
                book.returnBook(studentId);
                Book.saveBooks(books);
                System.out.println("Book returned successfully!");
                return;
            }
        }

        System.out.println("Book not found or not checked out by you.");
    }

    // Automatically checkout books for all enrolled courses
    public static void autoCheckout(String studentId) {
        List<Book> books = Book.loadBooks();
        List<String> enrolledCourses = Enrollment.getEnrolledCourses(studentId);
        int currentCheckedOutCount = (int) books.stream().filter(book -> book.getCheckedOutBy().contains(studentId)).count();

        for (Book book : books) {
            if (enrolledCourses.contains(book.getCourseId()) && book.isAvailable() && currentCheckedOutCount < 5) {
                book.checkout(studentId);
                currentCheckedOutCount++;
            }
        }

        Book.saveBooks(books);
        System.out.println("Books for your required courses have been checked out.");
    }
}
