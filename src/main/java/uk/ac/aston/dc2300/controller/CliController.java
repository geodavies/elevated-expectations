package uk.ac.aston.dc2300.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uk.ac.aston.dc2300.component.Simulation;
import uk.ac.aston.dc2300.model.configuration.SimulationConfiguration;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * An implementation of ApplicationController which allows command line interaction.
 *
 * @author George Davies
 * @since 05/04/17
 */
public class CliController implements ApplicationController {

    private static final Logger LOGGER = LogManager.getLogger(CliController.class);

    private final Simulation simulation;


    public CliController() {
        LOGGER.info("Initializing application in 'cli' mode");
        SimulationConfiguration simulationConfiguration = getConfigurationInput();
        simulation = new Simulation(simulationConfiguration);
    }

    @Override
    public void start() {

    }

    /**
     * Prompts the user to enter configuration settings and assembles them together into a SimulationConfiguration
     * object.
     *
     * @return a SimulationConfiguration from the input of the user
     */
    private SimulationConfiguration getConfigurationInput() {
        System.out.println("Please select simulation configuration settings");

        Scanner scanner = new Scanner(System.in);

        // Initialize variables
        BigDecimal floorChangeProbability = null;
        BigDecimal clientArrivalProbability = null;
        String seed;
        // Set these to -1 so application will error later if not set correctly
        int numEmployees = -1;
        int numDevelopers = -1;

        // Get floor change probability
        while (floorChangeProbability == null) {
            System.out.printf("Floor change probability (p) [0.01]: ");
            // Take user input
            String floorChangeProbabilityInput = scanner.nextLine();
            // If left empty, set to default value
            if (floorChangeProbabilityInput.isEmpty()) {
                floorChangeProbability = new BigDecimal(0.01);
            } else {
                try {
                    // Check value is valid decimal
                    BigDecimal validationDecimal = new BigDecimal(floorChangeProbabilityInput);
                    // Check value is between 0 and 1
                    if (validationDecimal.compareTo(BigDecimal.ZERO) < 0 || validationDecimal.compareTo(BigDecimal.ONE) > 0) {
                        System.out.println("Probability must be a decimal between 0 and 1");
                        continue;
                    }
                    floorChangeProbability = validationDecimal;
                } catch (NumberFormatException e) {
                    System.out.println("Probability must be a decimal between 0 and 1");
                }
            }
        }

        // Get client arrival probability
        while (clientArrivalProbability == null) {
            System.out.printf("Client arrival probability (q) [0.005]: ");
            // Get user input
            String clientArrivalProbablityInput = scanner.nextLine();
            // If left empty, set to default value
            if (clientArrivalProbablityInput.isEmpty()) {
                clientArrivalProbability = new BigDecimal(0.005);
            } else {
                try {
                    // Check value is valid decimal
                    BigDecimal validationDecimal = new BigDecimal(clientArrivalProbablityInput);
                    // Check value is between 0 and 1
                    if (validationDecimal.compareTo(BigDecimal.ZERO) < 0 || validationDecimal.compareTo(BigDecimal.ONE) > 0) {
                        System.out.println("Probability must be a decimal between 0 and 1");
                        continue;
                    }
                    clientArrivalProbability = validationDecimal;
                } catch (NumberFormatException e) {
                    System.out.println("Probability must be a decimal between 0 and 1");
                }
            }
        }

        // Get randomization seed
        System.out.printf("Randomization seed: ");
        seed = scanner.nextLine();
        if (seed.isEmpty()) seed = "Not so random";

        // Get number of employees
        while (numEmployees == -1) {
            System.out.printf("Number of employees [10]: ");
            // Get user input
            String numEmployeesInput = scanner.nextLine();
            // If left empty, set to default value
            if (numEmployeesInput.equals("")) {
                numEmployees = 10;
            } else {
                try {
                    // Check if valid int
                    int validationInt = Integer.parseInt(numEmployeesInput);
                    // Check greater than 0
                    if(validationInt < 0){
                        System.out.println("Must be greater than or equal to 0");
                        continue;
                    }
                    numEmployees = validationInt;
                } catch (NumberFormatException e) {
                    System.out.println("Must be a valid whole number");
                }
            }
        }

        // Get number of developers
        while (numDevelopers == -1) {
            System.out.printf("Number of developers [10]: ");
            // Get user input
            String numDevelopersInput = scanner.nextLine();
            // If left empty, set to default value
            if (numDevelopersInput.equals("")) {
                numDevelopers = 10;
            } else {
                try {
                    // Check if valid int
                    int validationInt = Integer.parseInt(numDevelopersInput);
                    // Check greater than 0
                    if(validationInt < 0){
                        System.out.println("Must be greater than or equal to 0");
                        continue;
                    }
                    numDevelopers = validationInt;
                } catch (NumberFormatException e) {
                    System.out.println("Must be a valid whole number");
                }
            }
        }

        return new SimulationConfiguration(floorChangeProbability, clientArrivalProbability, seed, numEmployees, numDevelopers);
    }

}
