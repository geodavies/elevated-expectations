package uk.ac.aston.dc2300.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uk.ac.aston.dc2300.component.Simulation;
import uk.ac.aston.dc2300.model.configuration.SimulationConfiguration;
import uk.ac.aston.dc2300.utility.CliUtils;

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
        simulation.start();
    }

    /**
     * Prompts the user to enter configuration settings and assembles them together into a SimulationConfiguration
     * object.
     *
     * @return a SimulationConfiguration from the input of the user
     */
    private SimulationConfiguration getConfigurationInput() {
        System.out.println("\nPlease select simulation configuration settings");

        Scanner scanner = new Scanner(System.in);

        CliUtils cliMediator = new CliUtils();

        // Initialize variables
        BigDecimal floorChangeProbability = null;
        BigDecimal clientArrivalProbability = null;
        // Set these to -1 so application will error later if not set correctly
        long seed = -1;
        int numEmployees = -1;
        int numDevelopers = -1;
        int numFloors = -1;
        int elevatorCapacity = -1;

        // Get floor change probability
        floorChangeProbability = cliMediator.readBigDecimalViaCli("Floor Change probability (p)", new BigDecimal("0.01"));

        // Get client arrival probability
        clientArrivalProbability = cliMediator.readBigDecimalViaCli("Client arrival probability (q) ",  new BigDecimal("0.005"));

        // Get randomization seed
        seed = cliMediator.readLongViaCli("Randomization seed", 420);

        // Get number of employees
        numEmployees = cliMediator.readIntegerViaCli("Number of employees in building", 10);

        // Get number of developers
        numDevelopers = cliMediator.readIntegerViaCli("Number of developers in building", 10);

        // Get number of floors
        numFloors = cliMediator.readIntegerViaCli("Number of floors in building", 6);

        // Get elevator capacities
        elevatorCapacity = cliMediator.readIntegerViaCli("Max Capacity of Elevators", 4);

        // Create some space to improve legibility
        System.out.print("Your chosen capacity: " + elevatorCapacity);
        System.out.print("\n");

        return new SimulationConfiguration(floorChangeProbability, clientArrivalProbability, seed, numEmployees, numDevelopers, numFloors, elevatorCapacity);
    }

}
