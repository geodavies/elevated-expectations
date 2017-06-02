package uk.ac.aston.dc2300.component;

import uk.ac.aston.dc2300.model.configuration.SimulationConfiguration;
import uk.ac.aston.dc2300.model.entity.*;
import uk.ac.aston.dc2300.model.status.DeveloperCompany;
import uk.ac.aston.dc2300.model.status.SimulationStatus;
import uk.ac.aston.dc2300.utility.RandomUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Simulation class composes the main component of the application this class is responsible for managing the
 * application state and controlling fundamental processes such as randomization and timekeeping.
 *
 * @author George Davies
 * @since 05/04/17
 */
public class Simulation {

    private final DeveloperCompany[] COMPANIES = new DeveloperCompany[]{DeveloperCompany.GOGGLES, DeveloperCompany.MUGTOME};

    private final BigDecimal MAINTENANCE_CREW_ARRIVAL_PROBABILITY = BigDecimal.valueOf(0.005);

    private final BigDecimal FLOOR_CHANGE_PROBABILITY;

    private final BigDecimal CLIENT_ARRIVAL_PROBABILITY;

    private final Building BUILDING;

    private final RandomUtils RANDOM_UTILS;

    private final int SIMULATION_RUN_TIME;

    private int currentTime = 0;

    /**
     * Constructor for Simulation. Requires a populated configuration object.
     *
     * @param simulationConfiguration configuration for simulation to run from
     */
    public Simulation(SimulationConfiguration simulationConfiguration) {

        System.out.println("Creating simulation");

        // Create floor(s)
        List<Floor> floors = new ArrayList<>();
        for (int i = 0; i < simulationConfiguration.getNumFloors(); i++) {
            Floor floor = new Floor(i);
            floors.add(floor);
        }

        // Create elevator(s) and put in ground floor
        Elevator elevator = new Elevator(simulationConfiguration.getElevatorCapacity(), floors.get(0));
        List<Elevator> elevatorSet = new ArrayList<>();
        elevatorSet.add(elevator);

        // Create employee(s) and put in ground floor
        for (int i = 0; i < simulationConfiguration.getNumEmployees(); i++) {
            Employee employee = new Employee(currentTime);
            floors.get(0).addOccupant(employee);
        }

        // Create developer(s) and put in ground floor
        for (int i = 0; i < simulationConfiguration.getNumDevelopers(); i++) {
            Developer developer = new Developer(currentTime, COMPANIES[i % 2]);
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

        // Initialise simulation state
        initialise();

    }

    /**
     * Sets the initial destinations of the building occupants and makes them call the elevator
     */
    private void initialise() {
        System.out.println("Setting initial occupant destinations...");
        // Get all the occupants on the ground floor
        Floor groundFloor = BUILDING.getFloors().get(0);
        List<BuildingOccupant> initialOccupants = groundFloor.getOccupants();
        for (BuildingOccupant buildingOccupant : initialOccupants) {
            // Give them new destination floors
            buildingOccupant.setNewDestination(BUILDING, RANDOM_UTILS, BigDecimal.ONE, currentTime);
        }
    }

    public SimulationStatus tick() {
        System.out.println(String.format("Time: %s", currentTime));
        randomlyReassignDestinations();
        checkForArrivingClients(currentTime);
        checkForArrivingMaintenanceCrew(currentTime);
        updateElevatorStatuses();
        unloadElevators();
        loadElevators();
        moveElevators();

        BUILDING.getClientComplaints(currentTime);

        SimulationStatus currentStatus = new SimulationStatus(BUILDING, currentTime, currentTime < SIMULATION_RUN_TIME);
        currentTime += 10;

        return currentStatus;
    }

    /**
     * Randomly (against given probability) creates a client, sets their destination, adds them
     * to a floor and makes them call the elevator for the floor they're on.
     */
    private void checkForArrivingClients(int currentTime) {
        // Only execute following code if random is in range of probability
        if (RANDOM_UTILS.getBigDecimal().compareTo(CLIENT_ARRIVAL_PROBABILITY) <= 0) {
            // Generate random leaving time between 10 and 30 minutes, change to seconds.
            int leaveAfterArrivalTime = RANDOM_UTILS.getIntInRange(10, 30) * 60;
            Client arrivingClient = new Client(currentTime, leaveAfterArrivalTime);
            Floor groundFloor = BUILDING.getFloors().get(0);
            // Put client into building (ground floor)
            groundFloor.addOccupant(arrivingClient);
            // Assign initial destination floor
            arrivingClient.setNewDestination(BUILDING, RANDOM_UTILS, null, currentTime);
        }
    }

    /**
     * Randomly (against given probability) creates a Maintenance Crew, sets their destination, adds them
     * to a floor and makes them call the elevator for the floor they're on.
     */
    private void checkForArrivingMaintenanceCrew(int currentTime) {
        // Only execute following code if random is in range of probability
        if (RANDOM_UTILS.getBigDecimal().compareTo(MAINTENANCE_CREW_ARRIVAL_PROBABILITY) <= 0) {
            // Generate random leaving time between 20 and 40 minutes, change to seconds.
            int leaveAfterArrivalTime = RANDOM_UTILS.getIntInRange(20, 40) * 60;
            MaintenanceCrew arrivingMaintenanceCrew = new MaintenanceCrew(currentTime, leaveAfterArrivalTime);
            Floor groundFloor = BUILDING.getFloors().get(0);
            // Put crew into building (ground floor)
            groundFloor.addOccupant(arrivingMaintenanceCrew);
            // Assign initial destination floor
            arrivingMaintenanceCrew.setNewDestination(BUILDING, RANDOM_UTILS, null, currentTime);
        }
    }

    /**
     * Will call all occupants that are on their destination floors to set new destinations providing each of their
     * individual setNewDestination method implementation requirements are met.
     */
    private void randomlyReassignDestinations() {
        List<BuildingOccupant> buildingOccupants = BUILDING.getAllOccupants();
        for (BuildingOccupant occupant : buildingOccupants) {
            Floor currentFloor = BUILDING.getFloorContainingOccupant(occupant);
            // If occupant current floor is also their destination
            if (currentFloor.equals(occupant.getDestination())) {
                occupant.setNewDestination(BUILDING, RANDOM_UTILS, FLOOR_CHANGE_PROBABILITY, currentTime);
            }
        }
    }

    /**
     * This commands each elevator to look at their current occupants and those waiting outside and will open/close the
     * doors where needed.
     */
    private void updateElevatorStatuses() {
        for (Elevator elevator : BUILDING.getElevators()) {
            elevator.updateDoorStatus(BUILDING.getFloors().size() - 1);
        }
    }

    /**
     * Moves any BuildingOccupants at their destination onto the floor
     */
    private void unloadElevators() {
        for (Elevator elevator : BUILDING.getElevators()) {
            elevator.unloadPassengers(currentTime);
        }
    }

    /**
     * Moves any queuing BuildingOccupants into the elevators
     */
    private void loadElevators() {
        for (Elevator elevator : BUILDING.getElevators()) {
            elevator.loadPassengers(BUILDING.getFloors().size() - 1);
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
