package main;

import Interfaces.DirectoryInterface;
import users.Directory;
import helpers.FileUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectoryController implements DirectoryInterface {
    public static int ID_DIRECTORY = 0;
    public static int NAME_DIRECTORY = 1;
    public static int EMAIL_DIRECTORY = 2;
    private Directory idDirecotry = new Directory();
    private Directory nameDirectory = new Directory();
    private Directory emailDirectory = new Directory();
    private HashMap<String, ArrayList<Directory.EndOfWordData>> departmentDirectory;

    public DirectoryController() {
        this.idDirecotry = new Directory();
        this.nameDirectory = new Directory();
        this.emailDirectory = new Directory();
        this.departmentDirectory = new HashMap<>();
        List<String[]> students = FileUtils.readStructuredData("", "students.txt");
        List<String[]> instructors = FileUtils.readStructuredData("", "instructors.txt");
        for (String[] student : students) {
            insert(ID_DIRECTORY, student[0], student[1], student[2], student[3], "Stud");
            insert(NAME_DIRECTORY, student[0], student[1], student[2], student[3], "Stud");
            insert(EMAIL_DIRECTORY, student[0], student[1], student[2], student[3], "Stud");
        }
        for (String[] instructor : instructors) {
            insert(ID_DIRECTORY, instructor[0], instructor[1], instructor[2], instructor[3], instructor[4]);
            insert(NAME_DIRECTORY, instructor[0], instructor[1], instructor[2], instructor[3], instructor[4]);
            insert(EMAIL_DIRECTORY, instructor[0], instructor[1], instructor[2], instructor[3], instructor[4]);
        }

    }

    @Override
    public boolean insert(int type, String id, String name, String email, String dept) {
        Directory.EndOfWordData data = new Directory.EndOfWordData(name, id, email, dept);
        if (type == ID_DIRECTORY) {
            idDirecotry.insert(id, data);
        } else if (type == NAME_DIRECTORY) {
            nameDirectory.insert(name, data);
        } else if (type == EMAIL_DIRECTORY) {
            emailDirectory.insert(email, data);
        }
        return true;
    }

    @Override
    public Directory.EndOfWordData search(String word) {
        return null;
    }

    @Override
    public ArrayList<String> departmentSearch(String dept) {
        return null;
    }

    @Override
    public void populateDepartmentDirectory() {

    }
}
