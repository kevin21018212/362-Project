import helpers.Display;
import helpers.Utils;

public class Main {
    public static void main(String[] args) {
            while (true) {
                System.out.println("\nWelcome to the University System");
                System.out.println("1. Student Login");
                System.out.println("2. Instructor Login");
                System.out.println("3. Faculty Login");
                System.out.println("4. Exit\n");
                String choice = Utils.getInput("Choose Option: ");

                switch (choice) {
                    case "1":
                        Display.displayStudentMenu();
                        break;
                    case "2":
                        Display.displayInstructorMenu();
                        break;
                    case "3":
                        //faculty?
                        break;
                    case "4":
                        System.out.println("End");
                        return;
                    default:
                        System.out.println("Bad Baka");
                }
            }
    }
}