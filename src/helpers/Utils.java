package helpers;

public class Utils {

    public static String generateID(String prefix, int number) {
        return prefix + String.format("%03d", number);
    }

    //Future Use
    public static void displayMessage(String message) {
        System.out.println(message);
    }
}
