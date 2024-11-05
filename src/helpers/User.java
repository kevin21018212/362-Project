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


    public String getUserID() { return userID; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public abstract void displayInfo();
}

