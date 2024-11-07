package helpers;

import java.util.Scanner;

public class Utils {

    public static String generateId(String prefix, int number) {
        return prefix + String.format("%03d", number);
    }

    // Future use @TODO

    public static String getInput(String prompt) {
        System.out.print(prompt);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
