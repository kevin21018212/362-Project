package Interfaces;

import users.Directory.EndOfWordData;

import java.util.ArrayList;

public interface DirectoryInterface {
    boolean insert(int type, String id, String name, String email, String dept);
    EndOfWordData search(String word);
    ArrayList<String[]> departmentSearch(String dept);
    void populateDepartmentDirectory();
}
