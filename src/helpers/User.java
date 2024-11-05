package helpers;

public abstract class User {
    protected String id;
    protected String name;
    protected String email;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Display the user menu
    public abstract void displayMenu();

    // Common toString method for all users
    @Override
    public String toString() {
        return id + "," + name + "," + email;
    }


}
