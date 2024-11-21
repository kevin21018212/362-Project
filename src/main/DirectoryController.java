package main;

import Interfaces.DirectoryInterface;
import users.Directory;
import users.Directory.EndOfWordData;
import helpers.FileUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Controller class for managing different types of directories (ID, Name, Email, Department).
 * Implements the DirectoryInterface for directory operations.
 */
public class DirectoryController implements DirectoryInterface {
    /** Constant for ID directory type */
    public static int ID_DIRECTORY = 0;
    /** Constant for Name directory type */
    public static int NAME_DIRECTORY = 1;
    /** Constant for Email directory type */
    public static int EMAIL_DIRECTORY = 2;

    private Directory idDirecotry;
    private Directory nameDirectory;
    private Directory emailDirectory;
    private HashMap<String, ArrayList<Directory.EndOfWordData>> departmentDirectory;

    /**
     * Constructor initializes directories and loads data from files.
     * Creates separate directories for IDs, names, and emails.
     * Loads both student and instructor data from respective files.
     */
    public DirectoryController() {
        this.idDirecotry = new Directory();
        this.nameDirectory = new Directory();
        this.emailDirectory = new Directory();
        this.departmentDirectory = new HashMap<>();
        List<String[]> students = FileUtils.readStructuredData("", "students.txt");
        List<String[]> instructors = FileUtils.readStructuredData("", "instructors.txt");

        // Insert student data
        for (String[] student : students) {
            insert(ID_DIRECTORY, student[0], student[1], student[2], "STUD");
            insert(NAME_DIRECTORY, student[0], student[1], student[2], "STUD");
            insert(EMAIL_DIRECTORY, student[0], student[1], student[2], "STUD");
            if (!this.departmentDirectory.containsKey("STUD"))
                this.departmentDirectory.put("STUD", new ArrayList<>());
            this.departmentDirectory.get("STUD").add(new Directory.EndOfWordData(student[1], student[0], student[2], "STUD"));
        }

        // Insert instructor data
        for (String[] instructor : instructors) {
            insert(ID_DIRECTORY, instructor[0], instructor[1], instructor[2], instructor[3]);
            insert(NAME_DIRECTORY, instructor[0], instructor[1], instructor[2], instructor[3]);
            insert(EMAIL_DIRECTORY, instructor[0], instructor[1], instructor[2], instructor[3]);
            if (!this.departmentDirectory.containsKey(instructor[3]))
                this.departmentDirectory.put(instructor[3], new ArrayList<>());
            this.departmentDirectory.get(instructor[3]).add(new Directory.EndOfWordData(instructor[1], instructor[0], instructor[2], instructor[3]));
        }
    }

    /**
     * Inserts a new entry into the specified directory type.
     * @param type The type of directory (ID, NAME, or EMAIL)
     * @param id The ID of the person
     * @param name The name of the person
     * @param email The email of the person
     * @param dept The department of the person
     * @return true if insertion is successful
     */
    @Override
    public boolean insert(int type, String id, String name, String email, String dept) {
        Directory.EndOfWordData data = new Directory.EndOfWordData(name, id, email, dept);
        if (type == ID_DIRECTORY) {
            idDirecotry.insert(id, data);
        } else if (type == NAME_DIRECTORY) {
            nameDirectory.insert(name.toUpperCase(), data);
        } else if (type == EMAIL_DIRECTORY) {
            emailDirectory.insert(email.toUpperCase(), data);
        }
        return true;
    }

    /**
     * Searches for an exact match in the specified directory type.
     * @param word The search term
     * @param type The type of directory to search in
     * @return EndOfWordData object if found, null otherwise
     */
    @Override
    public EndOfWordData search(String word, int type) {
        if (type == ID_DIRECTORY) {
            return idDirecotry.search(word);
        } else if (type == NAME_DIRECTORY) {
            return nameDirectory.search(word.toUpperCase());
        } else if (type == EMAIL_DIRECTORY) {
            return emailDirectory.search(word.toUpperCase());
        }
        return null;
    }

    /**
     * Performs a partial search in the specified directory type.
     * @param word The partial search term
     * @param type The type of directory to search in
     * @return ArrayList of EndOfWordData objects matching the partial search
     */
    @Override
    public ArrayList<EndOfWordData> searchImpartial(String word, int type) {
        if (type == NAME_DIRECTORY) {
            return nameDirectory.searchPartial(word.toUpperCase());
        } else if (type == EMAIL_DIRECTORY) {
            return emailDirectory.searchPartial(word.toUpperCase());
        }
        return null;
    }

    /**
     * Gets the department directory.
     * @return HashMap containing department-wise directory data
     */
    @Override
    public HashMap<String, ArrayList<EndOfWordData>> getDepartmentDirectory() {
        return departmentDirectory;
    }
}