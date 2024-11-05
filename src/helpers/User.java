package helpers;

public abstract class User {
    protected String userID;
    protected String name;
    protected String email;

    public User(String userID, String name, String email) {
        this.userID = userID;
        this.name = name;
        this.email = email;
    }

    public abstract void displayInfo();
}
