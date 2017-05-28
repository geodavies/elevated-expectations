package uk.ac.aston.dc2300.controller;

import uk.ac.aston.dc2300.component.Simulation;
import uk.ac.aston.dc2300.model.configuration.SimulationConfiguration;
import uk.ac.aston.dc2300.model.entity.*;
import uk.ac.aston.dc2300.model.status.SimulationStatus;
import uk.ac.aston.dc2300.utility.CliUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * An implementation of ApplicationController which allows command line interaction.
 *
 * @author George Davies
 * @since 05/04/17
 */
public class CliController implements ApplicationController {

    private final Simulation simulation;

    private boolean simulationRunning;

    public CliController() {
        System.out.println("Initializing application in 'cli' mode");
        SimulationConfiguration simulationConfiguration = getConfigurationInput();
        simulation = new Simulation(simulationConfiguration);
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

        /*
            Log collected values.
        */
        System.out.println("[CLI] Collected following values from input");
        System.out.println("[CLI] EmpChange: " + empFloorChangeProbability);
        System.out.println("[CLI] ClientArrive: " + clientArrivalProbability);
        System.out.println("[CLI] RandSeed: " + seed);
        System.out.println("[CLI] NumEmp: " + numEmployees);
        System.out.println("[CLI] NumDev: " + numDevelopers);
        System.out.println("[CLI] NumFloors: " + numFloors);
        System.out.println("[CLI] ElevatorCapacity: " + elevatorCapacity);
        System.out.println("[CLI] SimulationTime: " + simulationTime);

        // Create some space to improve legibility
        System.out.print("\n");

        return new SimulationConfiguration(empFloorChangeProbability, clientArrivalProbability, seed, numEmployees,
                numDevelopers, numFloors, elevatorCapacity, simulationTime);
    }

    @Override
    public void start() {
        simulationRunning = true;
        SimulationStatus currentStatus = null;
        while (simulationRunning) {
            currentStatus = simulation.tick();

            displayBuilding(currentStatus.getBuilding());

            simulationRunning = currentStatus.isSimulationRunning();
        }
        System.out.println(String.format("Simulation Completed at time: %s ", currentStatus.getTime()));
    }

    private void displayBuilding(Building building) {
        StringBuilder stringBuilder = new StringBuilder();

        // Roof
        stringBuilder.append("\n    Lift Queue           Floor\n");
        stringBuilder.append("   +====+===============+==========+\n");

        List<Floor> buildingFloors = building.getFloors();
        for (int i = buildingFloors.size() - 1; i >= 0; i--) {
            Floor currentFloor = buildingFloors.get(i);

            StringBuilder queueStringBuilder = new StringBuilder();
            for (BuildingOccupant occupant : currentFloor.getElevatorQueue()) {
                queueStringBuilder.append(getLetterForBuildingOccupant(occupant));
            }

            StringBuilder floorStringBuilder = new StringBuilder();
            for (BuildingOccupant occupant : currentFloor.getOccupants()) {
                if (!currentFloor.getElevatorQueue().contains(occupant)) {
                    floorStringBuilder.append(getLetterForBuildingOccupant(occupant));
                }
            }

            StringBuilder elevatorStringBuilder = new StringBuilder();
            List<Elevator> elevatorsOnFloor = building.getElevatorsOnFloor(currentFloor);
            if (elevatorsOnFloor.size() > 0) {
                for (BuildingOccupant occupant : elevatorsOnFloor.get(0).getPassengers()) {
                    elevatorStringBuilder.append(getLetterForBuildingOccupant(occupant));
                }
            }

            String elevatorPointer = "";
            if (elevatorsOnFloor.size() > 0) {
                elevatorPointer = ">";
            }
            stringBuilder.append(String.format("%1s %1s|%-4s|%-15s|%10s|\n", i, elevatorPointer, elevatorStringBuilder.toString(), queueStringBuilder.toString(), floorStringBuilder.toString()));
        }
        stringBuilder.append("   +====+===============+==========+\n");
        System.out.println(stringBuilder.toString());
    }

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
