package uk.ac.aston.dc2300.model.configuration;

import java.math.BigDecimal;

/**
 * This class holds data about how the simulation will start, this will be passed from the user interfaces to the
 * simulation upon initialization to configure it according to user parameters.
 *
 * @author George Davies
 * @since 05/04/17
 */
public class SimulationConfiguration {

    private final BigDecimal FLOOR_CHANGE_PROBABILITY;

    private final BigDecimal CLIENT_ARRIVAL_PROBABILITY;

    private final long SEED;

    private final int NUM_EMPLOYEES;

    private final int NUM_DEVELOPERS;

    private final int NUM_FLOORS;

    private final int ELEVATOR_CAPACITY;

    private final int SIMULATION_TIME;

    /**
     * @param employeeFloorChangeProbability the probability each tick that an employee or developer will change their
     *                                       current
     * @param clientArrivalProbability the probability each tick that a client will arrive at the building
     * @param seed the seed used to generate the randomness
     * @param numEmployees number of employees to begin simulation with
     * @param numDevelopers number of developers to begin the simulation with
     * @param numFloors number of floors within the building that will be simulated
     * @param elevatorCapacity capacity of each elevator in the building
     */
    public SimulationConfiguration(BigDecimal employeeFloorChangeProbability, BigDecimal clientArrivalProbability,
                                   long seed, int numEmployees, int numDevelopers, int numFloors, int elevatorCapacity,
                                   int simulationTime) {
        this.FLOOR_CHANGE_PROBABILITY = employeeFloorChangeProbability;
        this.CLIENT_ARRIVAL_PROBABILITY = clientArrivalProbability;
        this.SEED = seed;
        this.NUM_EMPLOYEES = numEmployees;
        this.NUM_DEVELOPERS = numDevelopers;
        this.NUM_FLOORS = numFloors;
        this.ELEVATOR_CAPACITY = elevatorCapacity;
        this.SIMULATION_TIME = simulationTime;
    }

    public BigDecimal getFloorChangeProbability() {
        return FLOOR_CHANGE_PROBABILITY;
    }

    public BigDecimal getclientArrivalProbability() {
        return CLIENT_ARRIVAL_PROBABILITY;
    }

    public long getSeed() {
        return SEED;
    }

    public int getNumEmployees() {
        return NUM_EMPLOYEES;
    }

    public int getnumDevelopers() {
        return NUM_DEVELOPERS;
    }

    public int getnumFloors() {
        return NUM_FLOORS;
    }

    public int getElevatorCapacity() {
        return ELEVATOR_CAPACITY;
    }

    public int getsimulationTime() {
        return SIMULATION_TIME;
    }
}
