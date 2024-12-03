package Interfaces;
import users.Directory.EndOfWordData;
import java.util.ArrayList;
import java.util.HashMap;

public interface DirectoryInterface {
    boolean insert(int type, String id, String name, String email, String dept);
    EndOfWordData search(String word, int type);
    ArrayList<EndOfWordData> searchImpartial(String word, int type);
    HashMap<String, ArrayList<EndOfWordData>> getDepartmentDirectory();
}