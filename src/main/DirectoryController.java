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
        List<String[]> departments = FileUtils.readStructuredData("", "departments.txt");


    }

    @Override
    public boolean insert(String word, String name, String id, String email, String dept) {
        return false;
    }

    @Override
    public Directory.EndOfWordData search(String word) {
        return null;
    }

    @Override
    public ArrayList<String> departmentSearch(String dept) {
        return null;
    }
}
