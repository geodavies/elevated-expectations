package uk.ac.aston.dc2300.controller;

import uk.ac.aston.dc2300.component.Simulation;
import uk.ac.aston.dc2300.model.configuration.SimulationConfiguration;
import uk.ac.aston.dc2300.model.entity.*;
import uk.ac.aston.dc2300.model.status.SimulationStatistics;
import uk.ac.aston.dc2300.model.status.SimulationStatus;
import uk.ac.aston.dc2300.utility.CliUtils;
import uk.ac.aston.dc2300.utility.FileUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An implementation of ApplicationController which allows command line interaction.
 *
 * @author George Davies
 * @since 05/04/17
 */
public class CliController implements ApplicationController {

    private final Simulation simulation;
    private final SimulationConfiguration simulationConfiguration;

    private SimulationStatus currentStatus;

    public CliController() {
        System.out.println("Initializing application in 'cli' mode");
        simulationConfiguration = getConfigurationInput();
        simulation = new Simulation(simulationConfiguration);
    }

    /**
     * Prompts the user to enter configuration settings and assembles them together into a SimulationConfiguration
     * object.
     *
     * @return a SimulationConfiguration from the input of the user
     */
    private SimulationConfiguration getConfigurationInput() {
        System.out.println("\n=== Please select simulation configuration settings ===\n");

        CliUtils cliMediator = new CliUtils();

        // Initialize variables

        // Get floor change probability
        BigDecimal empFloorChangeProbability = cliMediator.readBigDecimalViaCli("Floor Change probability (p)", new BigDecimal("0.01"));

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

        // Get simulation time
        int simulationTime = cliMediator.readIntegerViaCli("Time to run simulation (s)", 28800);

        // Create some space to improve legibility
        System.out.println();

        return new SimulationConfiguration(empFloorChangeProbability, clientArrivalProbability, seed, numEmployees,
                numDevelopers, numFloors, elevatorCapacity, simulationTime);
    }

    @Override
    public void start() {
        currentStatus = new SimulationStatus(null, 0, true);

        System.out.println("Simulation configuration complete\n");

        printUsageInstructions();

        while (true) {
            String userInput = getUserInput();

            if (userInput.equalsIgnoreCase("/quit")) {
                System.out.println("Goodbye!");
                System.exit(0);
            } else {
                handleCommand(userInput);
            }
        }
    }

    /**
     * Prints the usage instructions for the CLI to STDOUT
     */
    private void printUsageInstructions() {
        System.out.println("Commands:");
        System.out.println("/step [ticks] : Steps through the simulation the specified amount of ticks eg. /step 5");
        System.out.println("/goto [ticks] : Goes to the given future tick of the simulation eg. /goto 78");
        System.out.println("/end          : Runs the simulation to the end");
        System.out.println("/stats [file] : Saves statistics in CSV format to the specified file");

        System.out.println();
    }

    /**
     * Prompts the user for CLI input with '>' mark
     *
     * @return the input of the user
     */
    private String getUserInput() {
        Scanner reader = new Scanner(System.in);
        System.out.print(">");
        return reader.nextLine();
    }

    /**
     * Reads the input provided by the user, matches to a command and actions that command if found.
     *
     * @param userInput the input of the user
     */
    private void handleCommand(String userInput) {
        System.out.println(); // Print some space to improve legibility

        Pattern commandPattern = Pattern.compile("(?i)^/(step|goto|end|stats)( (.+))?$"); // "/[command] [optional string]"
        Matcher commandMatcher = commandPattern.matcher(userInput);

        if (commandMatcher.matches()) { // If command is valid
            // Make the command lower case for switch
            String command = commandMatcher.group(1).toLowerCase();
            String parameter = commandMatcher.group(3);
            switch (command) {
                case "step":
                    if (parameter != null) {
                        try {
                            int steps = Integer.parseInt(parameter);
                            performStepCommand(steps);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid step parameter, please provide a valid number");
                        }
                    } else {
                        System.out.println("Please provide a valid number parameter");
                    }
                    break;
                case "goto":
                    if (parameter != null) {
                        try {
                            int tick = Integer.parseInt(parameter);
                            performGotoCommand(tick);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid tick parameter, please provide a valid number");
                            break;
                        }
                    } else {
                        System.out.println("Please provide a valid number parameter");
                    }
                    break;
                case "end":
                    performEndCommand();
                    break;
                case "stats":
                    if (parameter != null) {
                        performStatsCommand(parameter);
                    } else {
                        System.out.println("Please provide a filename to save to");
                    }
                    break;
            }
        } else { // Command not matched
            System.out.println("Invalid command!");
        }
    }

    private void performStepCommand(int steps) {
        for (int i = 0; i<steps; i++) {  // Tick for the number of steps provided by the user
            currentStatus = simulation.tick();
        }
        displayBuilding(currentStatus.getBuilding()); // Output building diagram on target tick
    }

    private void performGotoCommand(int tick) {
        if (currentStatus.getTime() / 10 < tick) { // Assert the target tick is in the future
            while (true) { // Keep ticking until tick reached or end of simulation
                currentStatus = simulation.tick();
                if (currentStatus.getTime() / 10 == tick) {
                    break;
                } else if (!currentStatus.isSimulationRunning()) {
                    break;
                }
            }
            displayBuilding(currentStatus.getBuilding()); // Output building diagram on target tick
        } else {
            System.out.println("Please choose a tick in the future");
        }
    }

    private void performEndCommand() {
        while (currentStatus.isSimulationRunning()) { // Keep ticking until the end of simulation
            currentStatus = simulation.tick();
        }
        displayBuilding(currentStatus.getBuilding()); // Output building diagram on target tick
    }

    private void performStatsCommand(String filename) {
        FileUtils fileUtils = new FileUtils(new File(filename));
        SimulationStatistics stats = simulation.getStatistics();
        try {
            fileUtils.writeToFile(simulationConfiguration.toCSV() + "," + stats.toCSV(),simulationConfiguration.getCSVHeaders() + "," +  stats.getCSVHeaders());
            System.out.println("File saved to: " + filename);
        } catch (IOException e) {
            System.out.println("File writing failed, please try again");
        }
    }

    /**
     * Creates a graphical text representation of the provided building and prints to STDOUT
     *
     * @param building the building to draw
     */
    private void displayBuilding(Building building) {
        StringBuilder buildingStringBuilder = new StringBuilder();

        // Draw top
        buildingStringBuilder.append("\n    Lift Queue                     Floor\n");
        buildingStringBuilder.append("   +====+=========================+====================+\n");

        List<Floor> buildingFloors = building.getFloors();
        for (int i = buildingFloors.size() - 1; i >= 0; i--) { // Draw each floor one by one from top to bottom
            Floor currentFloor = buildingFloors.get(i);

            // Draw the queue occupants
            StringBuilder queueStringBuilder = new StringBuilder();
            for (BuildingOccupant occupant : currentFloor.getElevatorQueue()) {
                queueStringBuilder.append(getLetterForBuildingOccupant(occupant));
            }

            // Draw the floor occupants
            StringBuilder floorStringBuilder = new StringBuilder();
            for (BuildingOccupant occupant : currentFloor.getOccupants()) {
                if (!currentFloor.getElevatorQueue().contains(occupant)) { // Only draw occupants that aren't in the queue
                    floorStringBuilder.append(getLetterForBuildingOccupant(occupant));
                }
            }

            // Draw the elevator occupants
            StringBuilder elevatorStringBuilder = new StringBuilder();
            List<Elevator> elevatorsOnFloor = building.getElevatorsOnFloor(currentFloor);
            if (elevatorsOnFloor.size() > 0) { // If there is one or more elevators on that floor
                // Draw each elevator occupant in the first elevator (multiple elevators not supported in CLI)
                for (BuildingOccupant occupant : elevatorsOnFloor.get(0).getOccupants()) {
                    elevatorStringBuilder.append(getLetterForBuildingOccupant(occupant));
                }
            }

            // Add a mark next to floor if there's an elevator at it
            String elevatorPointer = (elevatorsOnFloor.size() > 0) ? ">" : "";

            // Format and add the floor to the building
            buildingStringBuilder.append(String.format("%1s %1s|%-4s|%-25s|%20s|\n", i, elevatorPointer,
                    elevatorStringBuilder.toString(), queueStringBuilder.toString(), floorStringBuilder.toString()));
        }

        // Draw bottom
        buildingStringBuilder.append("   +====+=========================+====================+\n");

        // Print out the constructed building
        System.out.println(buildingStringBuilder.toString());
    }

    /**
     * Gets the letter associated with the occupant type
     * C = Client
     * D = Developer
     * E = Employee
     * M = MaintenanceCrew
     * ? = Unknown
     *
     * @param buildingOccupant the occupant to represent
     * @return the representation of the occupant
     */
    private String getLetterForBuildingOccupant(BuildingOccupant buildingOccupant) {
        if (buildingOccupant instanceof Client) {
            return "C";
        } else if (buildingOccupant instanceof Developer) {
            return "D";
        } else if (buildingOccupant instanceof Employee) {
            return "E";
        } else if (buildingOccupant instanceof MaintenanceCrew) {
            return "M";
        } else {
            return "?";
        }
    }


}
