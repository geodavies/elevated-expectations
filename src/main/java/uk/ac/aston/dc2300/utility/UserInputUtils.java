package uk.ac.aston.dc2300.utility;

import java.util.Scanner;

/**
 * This class contains methods to simplify getting user input from the command line
 *
 * @author George Davies
 * @since 05/04/17
 */
public class UserInputUtils {

    private static Scanner scanner = new Scanner(System.in);

    public static String getInput() {
        return scanner.nextLine();
    }

}
