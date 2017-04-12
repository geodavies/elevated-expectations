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

        CliUtils cliMediator = new CliUtils();

        // Initialize variables
        // Get floor change probability
        BigDecimal floorChangeProbability = cliMediator.readBigDecimalViaCli("Floor Change probability (p)", new BigDecimal("0.01"));

        // Get client arrival probability
        BigDecimal clientArrivalProbability = cliMediator.readBigDecimalViaCli("Client arrival probability (q) ",  new BigDecimal("0.005"));

        // Get randomization seed
        long seed = cliMediator.readLongViaCli("Randomization seed", 420);

        // Get number of employees
        int numEmployees = cliMediator.readIntegerViaCli("Number of employees in building", 10);

        // Get number of developers
        int numDevelopers = cliMediator.readIntegerViaCli("Number of developers in building", 10);

        // Get number of floors
        int numFloors = cliMediator.readIntegerViaCli("Number of floors in building", 6);

        // Get elevator capacities
        int elevatorCapacity = cliMediator.readIntegerViaCli("Max Capacity of Elevators", 4);

        /*
            Log collected values.
        */
        LOGGER.debug("[CLI] Collected following values from input");
        LOGGER.debug("[CLI] EmpChange: " + floorChangeProbability);
        LOGGER.debug("[CLI] ClientArrive: " + clientArrivalProbability);
        LOGGER.debug("[CLI] RandSeed: " + seed);
        LOGGER.debug("[CLI] NumEmp: " + numEmployees);
        LOGGER.debug("[CLI] NumDev: " + numDevelopers);
        LOGGER.debug("[CLI] NumFloors: " + numFloors);
        LOGGER.debug("[CLI] ElevatorCapacity: " + elevatorCapacity);

        // Create some space to improve legibility
        System.out.print("\n");

        return new SimulationConfiguration(floorChangeProbability, clientArrivalProbability, seed, numEmployees, numDevelopers, numFloors, elevatorCapacity);
    }

}
