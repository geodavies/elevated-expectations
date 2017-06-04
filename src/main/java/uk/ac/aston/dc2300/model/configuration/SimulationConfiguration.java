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

    private final BigDecimal EMP_FLOOR_CHANGE_PROBABILITY;

    private final BigDecimal CLIENT_ARRIVAL_PROBABILITY;

    private final long SEED;

    private final int NUM_EMPLOYEES;

    private final int NUM_DEVELOPERS;

    private final int NUM_FLOORS;

    private final int ELEVATOR_CAPACITY;

    private final int SIMULATION_TIME;

    /**
     * @param empFloorChangeProbability the probability each tick that an employee or developer will change their current floor
     * @param clientArrivalProbability the probability each tick that a client will arrive at the building
     * @param seed the seed used to generate the randomness
     * @param numEmployees number of employees to begin simulation with
     * @param numDevelopers number of developers to begin the simulation with
     * @param numFloors number of floors within the building that will be simulated
     * @param elevatorCapacity capacity of each elevator in the building
     * @param simulationTime the amount of time the simulation will run for in seconds
     */
    public SimulationConfiguration(BigDecimal empFloorChangeProbability, BigDecimal clientArrivalProbability,
                                   long seed, int numEmployees, int numDevelopers, int numFloors, int elevatorCapacity,
                                   int simulationTime) {
        EMP_FLOOR_CHANGE_PROBABILITY = empFloorChangeProbability;
        CLIENT_ARRIVAL_PROBABILITY = clientArrivalProbability;
        SEED = seed;
        NUM_EMPLOYEES = numEmployees;
        NUM_DEVELOPERS = numDevelopers;
        NUM_FLOORS = numFloors;
        ELEVATOR_CAPACITY = elevatorCapacity;
        SIMULATION_TIME = simulationTime;
    }

    public BigDecimal getEmpFloorChangeProbability() {
        return EMP_FLOOR_CHANGE_PROBABILITY;
    }

    public BigDecimal getClientArrivalProbability() {
        return CLIENT_ARRIVAL_PROBABILITY;
    }

    public long getSeed() {
        return SEED;
    }

    public int getNumEmployees() {
        return NUM_EMPLOYEES;
    }

    public int getNumDevelopers() {
        return NUM_DEVELOPERS;
    }

    public int getNumFloors() {
        return NUM_FLOORS;
    }

    public int getElevatorCapacity() {
        return ELEVATOR_CAPACITY;
    }

    public int getSimulationTime() {
        return SIMULATION_TIME;
    }

    /**
     * Gets the field titles as a comma separated String
     *
     * @return the field titles
     */
    public String getCSVHeaders() {
        String[] headers = new String[]{
            "Floor Change Probability",
            "Client Arrival Probability",
            "Random Seed",
            "Num Employees",
            "Num Developers",
            "Num Floors",
            "Elevator Capacity",
            "Simulation Runtime"
        };
        return String.join(",", headers);
    }

    /**
     * Converts the current field values into a comma separated String
     *
     * @return the field values
     */
    public String toCSV() {
        String[] headers = new String[]{
            getEmpFloorChangeProbability() + "",
            getClientArrivalProbability() + "",
            getSeed() + "",
            getNumEmployees() + "",
            getNumDevelopers() + "",
            getNumFloors() + "",
            getElevatorCapacity() + "",
            getSimulationTime() + ""
        };
        return String.join(",", headers);
    }
}
