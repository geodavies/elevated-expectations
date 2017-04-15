package uk.ac.aston.dc2300.component;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uk.ac.aston.dc2300.model.configuration.SimulationConfiguration;
import uk.ac.aston.dc2300.model.entity.*;
import uk.ac.aston.dc2300.utility.RandomUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Simulation class composes the main component of the application this class is responsible for managing the
 * application state and performing fundamental processes such as randomization and timekeeping.
 *
 * @author George Davies
 * @since 05/04/17
 */
public class Simulation {

    private final BigDecimal FLOOR_CHANGE_PROBABILITY;

    private final BigDecimal CLIENT_ARRIVAL_PROBABILITY;

    private final Building BUILDING;

    private final RandomUtils RANDOM_UTILS;

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
        FLOOR_CHANGE_PROBABILITY = simulationConfiguration.getEmployeeFloorChangeProbability();

        // Set client arrival probability
        CLIENT_ARRIVAL_PROBABILITY = simulationConfiguration.getClientArrivalProbability();

        // Set random seed
        RANDOM_UTILS = new RandomUtils(simulationConfiguration.getSeed());

        // Create and set Building
        BUILDING = new Building(elevatorSet, floors);

        LOGGER.info("Finished creating simulation");

    }

    /**
     * Begins the simulation
     */
    public void start() {

        setInitialDestinations();

        // TODO: Set this to loop while simulation is running
        for (int i = 0; i < 500; i++) {
            loadElevators();
            moveElevators();
            unloadElevators();
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
            assignNewDestination(buildingOccupant);
            buildingOccupant.callElevator(groundFloor);
        }

        LOGGER.info("Finished setting destinations");

    }

    /**
     * Assigns a new destination floor for the occupant to go to based on their type
     *
     * @param buildingOccupant the occupant to be assigned a new destination
     */
    private void assignNewDestination(BuildingOccupant buildingOccupant) {
        if (buildingOccupant instanceof Employee) {
            // Assign employees any floor
            int numFloors = BUILDING.getFloors().size();
            int randomFloor = RANDOM_UTILS.getIntInRange(0, numFloors - 1);
            buildingOccupant.setDestination(BUILDING.getFloors().get(randomFloor));
        } else if (buildingOccupant instanceof Developer) {
            // Assign developers a floor in the top half
            List<Floor> topHalfFloors = BUILDING.getTopHalfFloors();
            int randomFloorIndex = RANDOM_UTILS.getIntInRange(0, topHalfFloors.size() - 1);
            buildingOccupant.setDestination(topHalfFloors.get(randomFloorIndex));
        } else if (buildingOccupant instanceof Client) {
            // Assign clients a floor in the bottom half
            List<Floor> bottomHalfFoors = BUILDING.getBottomHalfFloors();
            int randomFloorIndex = RANDOM_UTILS.getIntInRange(0, bottomHalfFoors.size() - 1);
            buildingOccupant.setDestination(bottomHalfFoors.get(randomFloorIndex));
        } else if (buildingOccupant instanceof MaintenanceCrew) {
            // Assign maintenance workers to the top floor
            List<Floor> floors = BUILDING.getFloors();
            buildingOccupant.setDestination(floors.get(floors.size() - 1));
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

    /**
     * Moves any queuing BuildingOccupants into the elevators
     */
    private void loadElevators() {
        for (Elevator elevator : BUILDING.getElevators()) {
            elevator.loadPassengers();
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

}
