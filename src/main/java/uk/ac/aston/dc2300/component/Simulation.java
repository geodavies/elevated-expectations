package uk.ac.aston.dc2300.component;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uk.ac.aston.dc2300.model.configuration.SimulationConfiguration;
import uk.ac.aston.dc2300.model.entity.*;
import uk.ac.aston.dc2300.utility.RandomUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simulation class composes the main component of the application this class is responsible for managing the
 * application state and performing fundamental processes such as randomization and timekeeping.
 *
 * @author George Davies
 * @since 05/04/17
 */
public class Simulation {

    private final BigDecimal MAINTENANCE_CREW_ARRIVAL_PROBABILITY = BigDecimal.valueOf(0.005);

    private final BigDecimal FLOOR_CHANGE_PROBABILITY;

    private final BigDecimal CLIENT_ARRIVAL_PROBABILITY;

    private final Building BUILDING;

    private final RandomUtils RANDOM_UTILS;

    private final int SIMULATION_RUN_TIME;

    private int currentTime = 0;

    private static final Logger LOGGER = LogManager.getLogger(Simulation.class);

    public Simulation(SimulationConfiguration simulationConfiguration){

        LOGGER.info("Creating simulation...");

        // Create floor(s)
        List<Floor> floors = new ArrayList<>();
        for (int i = 0; i < simulationConfiguration.getNumFloors(); i++) {
            Floor floor = new Floor(i);
            floors.add(floor);
        }

        // Create elevator(s) and put in ground floor
        Elevator elevator = new Elevator(simulationConfiguration.getElevatorCapacity(), floors.get(0));
        Set<Elevator> elevatorSet = new HashSet<>();
        elevatorSet.add(elevator);

        // Create employee(s) and put in ground floor
        for (int i = 0; i < simulationConfiguration.getNumEmployees(); i++) {
            Employee employee = new Employee();
            floors.get(0).addOccupant(employee);
        }

        // Create developer(s) and put in ground floor
        for (int i = 0; i < simulationConfiguration.getNumDevelopers(); i++) {
            Developer developer = new Developer();
            floors.get(0).addOccupant(developer);
        }

        // Set floor change probability
        FLOOR_CHANGE_PROBABILITY = simulationConfiguration.getEmpFloorChangeProbability();

        // Set client arrival probability
        CLIENT_ARRIVAL_PROBABILITY = simulationConfiguration.getClientArrivalProbability();

        // Set random seed
        RANDOM_UTILS = new RandomUtils(simulationConfiguration.getSeed());

        // Create and set Building
        BUILDING = new Building(elevatorSet, floors);

        // Set time to run simulation for
        SIMULATION_RUN_TIME = simulationConfiguration.getSimulationTime();

        LOGGER.info("Finished creating simulation");

    }

    /**
     * Begins the simulation
     */
    public void start() {

        setInitialDestinations();

        while (currentTime <= SIMULATION_RUN_TIME) {
            LOGGER.debug(String.format("Time: %s", currentTime));

            checkForArrivingClients(currentTime);
            checkForArrivingMaintenanceCrew(currentTime);
            updateElevatorStatuses();
            unloadElevators();
            loadElevators();
            moveElevators();

            currentTime += 10;
        }

    }

    /**
     * Randomly (against given probability) creates a client, sets their destination, adds them
     * to a floor and makes them call the elevator for the floor they're on.
     */
    private void checkForArrivingClients(int time) {
        // #34 Randomly spawn clients
        if (RANDOM_UTILS.getBigDecimal().compareTo(CLIENT_ARRIVAL_PROBABILITY) <= 0){
            Client arrivingClient = new Client(time);
            Floor groundFloor = BUILDING.getFloors().get(0);

            assignNewDestination(arrivingClient, groundFloor);
            groundFloor.addOccupant(arrivingClient);

            arrivingClient.callElevator(groundFloor);

            LOGGER.info("Client Arriving on Floor 0 with destination: " + BUILDING.getFloors().indexOf(arrivingClient.getDestination()));
        }
    }

    /**
     * Randomly (against given probability) creates a Maintenance Crew, sets their destination, adds them
     * to a floor and makes them call the elevator for the floor they're on.
     */
    private void checkForArrivingMaintenanceCrew(int time) {
        // #34 Randomly spawn clients
        if (RANDOM_UTILS.getBigDecimal().compareTo(MAINTENANCE_CREW_ARRIVAL_PROBABILITY) <= 0){
            MaintenanceCrew arrivingMaintenanceCrew = new MaintenanceCrew(time);
            Floor groundFloor = BUILDING.getFloors().get(0);

            assignNewDestination(arrivingMaintenanceCrew, groundFloor);
            groundFloor.addOccupant(arrivingMaintenanceCrew);

            arrivingMaintenanceCrew.callElevator(groundFloor);

            LOGGER.info("Maintenance Crew Arriving on Floor 0 with destination: " + BUILDING.getFloors().indexOf(arrivingMaintenanceCrew.getDestination()));
        }
    }

    /**
     * Sets the initial destinations of the building occupants and makes them call the elevator
     */
    private void setInitialDestinations() {

        LOGGER.info("Setting initial occupant destinations...");

        // Get all the occupants on the ground floor
        Floor groundFloor = BUILDING.getFloors().get(0);
        Set<BuildingOccupant> initialOccupants = groundFloor.getOccupants();
        for (BuildingOccupant buildingOccupant : initialOccupants) {
            // Give them new destination floors
            assignNewDestination(buildingOccupant, BUILDING.getFloorContainingOccupant(buildingOccupant));
            buildingOccupant.callElevator(groundFloor);
        }

        LOGGER.info("Finished setting destinations");

    }

    /**
     * Assigns a new destination floor for the occupant to go to based on their type
     *
     * @param buildingOccupant the occupant to be assigned a new destination
     */
    private void assignNewDestination(BuildingOccupant buildingOccupant, Floor currentFloor) {
        int currentFloorNumber = currentFloor.getFloorNumber();
        if (buildingOccupant instanceof Employee) {
            // Assign employees any floor
            int numFloors = BUILDING.getFloors().size();
            int randomFloorIndex = RANDOM_UTILS.getIntInRange(0, numFloors - 1);
            while (randomFloorIndex == currentFloorNumber) {
                // If random floor is current floor try again
                randomFloorIndex = RANDOM_UTILS.getIntInRange(0, numFloors - 1);
            }
            buildingOccupant.setDestination(BUILDING.getFloors().get(randomFloorIndex));
        } else if (buildingOccupant instanceof Developer) {
            // Assign developers a floor in the top half
            List<Floor> topHalfFloors = BUILDING.getTopHalfFloors();
            int randomFloorIndex = RANDOM_UTILS.getIntInRange(0, topHalfFloors.size() - 1);
            while (randomFloorIndex == currentFloorNumber) {
                // If random floor is current floor try again
                randomFloorIndex = RANDOM_UTILS.getIntInRange(0, topHalfFloors.size() - 1);
            }
            buildingOccupant.setDestination(topHalfFloors.get(randomFloorIndex));
        } else if (buildingOccupant instanceof Client) {
            // Assign clients a floor in the bottom half
            List<Floor> bottomHalfFoors = BUILDING.getBottomHalfFloors();
            int randomFloorIndex = RANDOM_UTILS.getIntInRange(0, bottomHalfFoors.size() - 1);
            while (randomFloorIndex == currentFloorNumber) {
                // If random floor is current floor try again
                randomFloorIndex = RANDOM_UTILS.getIntInRange(0, bottomHalfFoors.size() - 1);
            }
            buildingOccupant.setDestination(bottomHalfFoors.get(randomFloorIndex));
        } else if (buildingOccupant instanceof MaintenanceCrew) {
            // Assign maintenance workers to the top floor
            List<Floor> floors = BUILDING.getFloors();
            buildingOccupant.setDestination(floors.get(floors.size() - 1));
        }
    }

    /**
     * This commands each elevator to look at their current occupants and those waiting outside and will open/close the
     * doors where needed.
     */
    private void updateElevatorStatuses() {
        for (Elevator elevator : BUILDING.getElevators()) {
            elevator.updateElevatorStatus();
        }
    }

    /**
     * Moves any BuildingOccupants at their destination onto the floor
     */
    private void unloadElevators() {
        for (Elevator elevator : BUILDING.getElevators()) {
            elevator.unloadPassengers();
        }
    }

    /**
     * Moves any queuing BuildingOccupants into the elevators
     */
    private void loadElevators() {
        for (Elevator elevator : BUILDING.getElevators()) {
            elevator.loadPassengers();
        }
    }

    /**
     * Moves the elevators to their next positions
     */
    private void moveElevators() {
        for (Elevator elevator : BUILDING.getElevators()) {
            elevator.moveIfRequested(BUILDING.getFloors());
        }
    }

}
