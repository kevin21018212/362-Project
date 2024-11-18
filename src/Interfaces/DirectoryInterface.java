package Interfaces;

import users.Directory.EndOfWordData;

import java.util.ArrayList;

public interface DirectoryInterface {
    boolean insert(String word, String name, String id, String email, String dept);
    EndOfWordData search(String word);
    ArrayList<String> departmentSearch(String dept);
}
