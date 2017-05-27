package uk.ac.aston.dc2300;

import uk.ac.aston.dc2300.controller.ApplicationController;
import uk.ac.aston.dc2300.controller.CliController;
import uk.ac.aston.dc2300.controller.GuiController;

/**
 * ElevatedExpectationApplication is the main application class which contains the entry point of the application.
 *
 * The responsibility of this class is to parse the application arguments and initialize the controllers based on
 * this information.
 *
 * This class will read the first argument passed at runtime to determine how to initialise the application.
 * Currently the following modes are accepted:
 *
 * cli - Runs in command line interface mode
 * gui - Runs as a graphical user interface
 *
 * @author George Davies
 * @since 04/04/17
 */
public class ElevatedExpectationsApplication {

    private static ApplicationController applicationController;

    public static void main(String[] args) {

        System.out.println("Starting Elevated Expectations");

        try{
            String runningMode = args[0];
            if (runningMode.equalsIgnoreCase("cli")) {
                applicationController = new CliController();
            } else if (runningMode.equalsIgnoreCase("gui")) {
                applicationController = new GuiController();
            } else {
                System.out.println("Unable to start interface. Unexpected parameter, please specify either 'cli' or 'gui' mode");
                System.exit(1);
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Unable to start interface. No parameters found, please specify either 'cli' or 'gui' mode");
            System.exit(1);
        }

        applicationController.start();

    }

}
