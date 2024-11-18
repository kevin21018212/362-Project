package helpers.displays;

import helpers.Utils;
import main.DirectoryController;
import users.Directory;

public class DirectoryDisplay {
    private DirectoryController directoryController;

    public DirectoryDisplay() {
        this.directoryController = new DirectoryController();
    }

    public void displayDirectoryMenu() {
        while (true) {
            System.out.println("\nDirectory Menu:");
            System.out.println("1: Search by ID");
            System.out.println("2: Search by Name");
            System.out.println("3: Search by Email");
            System.out.println("4: Search by Department");
            System.out.println("0: Exit");

            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    searchByID();
                    break;
                case "2":
                    searchByName();
                    break;
                case "3":
                    searchByEmail();
                    break;
                case "4":
                    searchByDepartment();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void searchByDepartment() {
        System.out.println("Search by Department");
        System.out.println("Available Departments:");
        for (String dept : directoryController.getDepartmentDirectory().keySet()) {
            System.out.println(dept);
        }
        String dept = Utils.getInput("Enter Department: ");
        for (Directory.EndOfWordData data : directoryController.getDepartmentDirectory().get(dept)) {
            System.out.println("ID: " + data.getId());
            System.out.println("Name: " + data.getName());
            System.out.println("Email: " + data.getEmail());
            System.out.println("Department: " + data.getDept());
        }
    }

    private void searchByEmail() {
        System.out.println("Search by Email");
        String email = Utils.getInput("Enter Email: ");
        Directory.EndOfWordData data = directoryController.search(email, DirectoryController.EMAIL_DIRECTORY);
        if (data != null) {
            System.out.println("ID: " + data.getId());
            System.out.println("Name: " + data.getName());
            System.out.println("Email: " + data.getEmail());
            System.out.println("Department: " + data.getDept());
        } else {
            System.out.println("Email not found");
        }
    }

    private void searchByName() {
    }

    private void searchByID() {
        System.out.println("Search by ID");
        String id = Utils.getInput("Enter ID: ");
        Directory.EndOfWordData data = directoryController.search(id, DirectoryController.ID_DIRECTORY);
        if (data != null) {
            System.out.println("ID: " + data.getId());
            System.out.println("Name: " + data.getName());
            System.out.println("Email: " + data.getEmail());
            System.out.println("Department: " + data.getDept());
        } else {
            System.out.println("ID not found");
        }
    }

}
