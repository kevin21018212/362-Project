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
            List<String> allowedMajors = List.of(row[2].replace("[", "").replace("]", "").split(", "));
            rooms.add(new Room(id, capacity, allowedMajors));
        }
        return rooms;
    }

    // Show accessible and inaccessible rooms for a student
    public static void showAllRooms(String studentId) {
        Student student = validateStudent(studentId);
        if (student == null) return;

        String major = student.getMajor();
        List<Room> rooms = loadRooms();

        displayRooms(rooms, major, true);
        displayRooms(rooms, major, false);
    }

    //shorter way to display rooms
    private static void displayRooms(List<Room> rooms, String major, boolean accessible) {
        String title = accessible ? "Accessible Rooms:" : "Inaccessible Rooms:";
        System.out.println("\n" + title);
        for (Room room : rooms) {
            if (room.isMajorAllowed(major) == accessible) {
                System.out.println(room);
            }
        }
    }

    //finds a room given a Iid
    public static Room findRoomById(List<Room> rooms, String roomId) {
        for (Room room : rooms) {
            if (room.getId().equalsIgnoreCase(roomId)) {
                return room;
            }
        }
        return null;
    }

    // Show books grouped by enrollment status
    public static void showBooks(String studentId, List<Book> books) {
        List<String> enrolledCourses = Enrollment.getEnrolledCourses(studentId);
        displayBooks(books, enrolledCourses, true);
        displayBooks(books, enrolledCourses, false);
    }

    //shorer way to display books
    private static void displayBooks(List<Book> books, List<String> enrolledCourses, boolean enrolled) {
        String title = enrolled ? "Books for Enrolled Courses:" : "Books for Non-Enrolled Courses:";
        System.out.println("\n" + title);
        for (Book book : books) {
            if (enrolledCourses.contains(book.getCourseId()) == enrolled) {
                System.out.println(book);
            }
        }
    }

    // Show books checked out by a student
    public static void showCheckedOutBooks(String studentId, List<Book> books) {
        List<Book> checkedOutBooks = getCheckedOutBooks(studentId, books);
        if (checkedOutBooks.isEmpty()) {
            System.out.println("No books currently checked out.");
        } else {
            System.out.println("Books Checked Out by " + studentId + ":");
            checkedOutBooks.forEach(System.out::println);
        }
    }

    // Checkout a single book
    public static void checkoutBook(String studentId, String bookId, List<Book> books) {
        if (!canCheckoutMoreBooks(studentId, books)) return;

        Book book = findBookById(books, bookId);
        if (book != null && book.isAvailable()) {
            book.checkout(studentId);
            Book.saveBooks(books);
            System.out.println("Book checked out successfully!");
        } else {
            System.out.println("Book is not available for checkout or not found.");
        }
    }

    // Return a book
    public static void returnBook(String studentId, String bookId, List<Book> books) {
        Book book = findBookById(books, bookId);
        if (book != null && book.getCheckedOutBy().contains(studentId)) {
            book.returnBook(studentId);
            Book.saveBooks(books);
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("Book not found or not checked out by you.");
        }
    }

    // Auto checkout books for all enrolled courses
    public static void autoCheckout(String studentId, List<Book> books) {
        List<String> enrolledCourses = Enrollment.getEnrolledCourses(studentId);
        int currentCheckedOutCount = (int) books.stream()
                .filter(book -> book.getCheckedOutBy().contains(studentId))
                .count();

        for (Book book : books) {
            if (enrolledCourses.contains(book.getCourseId()) && book.isAvailable() && currentCheckedOutCount < 5) {
                book.checkout(studentId);
                currentCheckedOutCount++;
            }
        }

        Book.saveBooks(books);
        System.out.println("Books for your required courses have been checked out.");
    }

    // Helper methods
    private static Student validateStudent(String studentId) {
        Student student = DataAccess.findStudentById(studentId);
        if (student == null) {
            System.out.println("Invalid student ID!");
        }
        return student;
    }

    private static boolean canCheckoutMoreBooks(String studentId, List<Book> books) {
        int count = (int) books.stream()
                .filter(book -> book.getCheckedOutBy().contains(studentId))
                .count();
        if (count >= 5) {
            System.out.println("You have already checked out the maximum number of books.");
            return false;
        }
        return true;
    }

    private static Book findBookById(List<Book> books, String bookId) {
        return books.stream()
                .filter(book -> book.getId().equalsIgnoreCase(bookId))
                .findFirst()
                .orElse(null);
    }

    private static List<Book> getCheckedOutBooks(String studentId, List<Book> books) {
        return books.stream()
                .filter(book -> book.getCheckedOutBy().contains(studentId))
                .toList();
    }

}
