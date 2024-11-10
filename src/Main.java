import helpers.Display;
import helpers.Utils;

public class Main {
    public static void main(String[] args) {
            while (true) {
                System.out.println("\nWelcome to the University System");
                System.out.println("1. Student Login");
                System.out.println("2. Instructor Login");
                System.out.println("3. Faculty Login");
                System.out.println("4. Registrar Login");
                System.out.println("5. Exit\n");
                String choice = Utils.getInput("Choose Option: ");

                switch (choice) {
                    case "1":
                        Display.displayStudentMenu();
                        break;
                    case "2":
                        Display.displayInstructorMenu();
                        break;
                    case "3":
                        //TODO @Kev
                        break;
                    case "4":
                        Display.displayRegistrarMenu();
                        break;
                    case "5":
                        System.out.println("End");
                        return;

                    default:
                        System.out.println("Bad Baka");
                }
            }
    }
}