package main;

import Interfaces.DirectoryInterface;
import users.Directory;
import users.Directory.EndOfWordData;
import helpers.FileUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectoryController implements DirectoryInterface {
    public static int ID_DIRECTORY = 0;
    public static int NAME_DIRECTORY = 1;
    public static int EMAIL_DIRECTORY = 2;
    private Directory idDirecotry;
    private Directory nameDirectory;
    private Directory emailDirectory;
    private HashMap<String, ArrayList<Directory.EndOfWordData>> departmentDirectory;

    public DirectoryController() {
        this.idDirecotry = new Directory();
        this.nameDirectory = new Directory();
        this.emailDirectory = new Directory();
        this.departmentDirectory = new HashMap<>();
        List<String[]> students = FileUtils.readStructuredData("", "students.txt");
        List<String[]> instructors = FileUtils.readStructuredData("", "instructors.txt");
        for (String[] student : students) {
            insert(ID_DIRECTORY, student[0], student[1], student[2], "Stud");
            insert(NAME_DIRECTORY, student[0], student[1], student[2], "Stud");
            insert(EMAIL_DIRECTORY, student[0], student[1], student[2], "Stud");
        }
        for (String[] instructor : instructors) {
            insert(ID_DIRECTORY, instructor[0], instructor[1], instructor[2], instructor[3]);
            insert(NAME_DIRECTORY, instructor[0], instructor[1], instructor[2], instructor[3]);
            insert(EMAIL_DIRECTORY, instructor[0], instructor[1], instructor[2], instructor[3]);
            this.departmentDirectory.put(instructor[3], new ArrayList<>());
            this.departmentDirectory.get(instructor[3]).add(new Directory.EndOfWordData(instructor[1], instructor[0], instructor[2], instructor[3]));
        }

    }

    @Override
    public boolean insert(int type, String id, String name, String email, String dept) {
        Directory.EndOfWordData data = new Directory.EndOfWordData(name, id, email, dept);
        if (type == ID_DIRECTORY) {
            idDirecotry.insert(id, data);
        } else if (type == NAME_DIRECTORY) {
            nameDirectory.insert(name.toLowerCase(), data);
        } else if (type == EMAIL_DIRECTORY) {
            emailDirectory.insert(email.toLowerCase(), data);
        }
        return true;
    }

    @Override
    public EndOfWordData search(String word, int type) {
        if (type == ID_DIRECTORY) {
            return idDirecotry.search(word);
        } else if (type == NAME_DIRECTORY) {
            return nameDirectory.search(word);
        } else if (type == EMAIL_DIRECTORY) {
            return emailDirectory.search(word);
        }
        return null;
    }

    @Override
    public ArrayList<String[]> departmentSearch(String dept) {
        if (this.departmentDirectory.containsKey(dept)) {
            ArrayList<String[]> result = new ArrayList<>();
            for (Directory.EndOfWordData data : this.departmentDirectory.get(dept)) {
                result.add(new String[]{data.getId(), data.getName(), data.getEmail()});
            }
            return result;
        }
        return null;
    }

    @Override
    public void populateDepartmentDirectory() {

    }

    @Override
    public ArrayList<EndOfWordData> searchImpartial(String word, int type) {
        if (type == NAME_DIRECTORY) {
            return nameDirectory.searchPartial(word);
        } else if (type == EMAIL_DIRECTORY) {
            return emailDirectory.searchPartial(word);
        }
        return null;
    }

    public HashMap<String, ArrayList<EndOfWordData>> getDepartmentDirectory() {
        return departmentDirectory;
    }
}
