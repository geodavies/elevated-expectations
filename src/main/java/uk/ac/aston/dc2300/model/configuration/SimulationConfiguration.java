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

    private final BigDecimal employeeFloorChangeProbability;

    private final BigDecimal clientArrivalProbability;

    private final long seed;

    private final int numEmployees;

    private final int numDevelopers;

    /**
     * @param employeeFloorChangeProbability the probability each tick that an employee or developer will change their
     *                                       current
     * @param clientArrivalProbability the probability each tick that a client will arrive at the building
     * @param seed the seed used to generate the randomness
     * @param numEmployees number of employees to begin simulation with
     * @param numDevelopers number of developers to begin the simulation with
     */
    public SimulationConfiguration(BigDecimal employeeFloorChangeProbability, BigDecimal clientArrivalProbability,
                                   long seed, int numEmployees, int numDevelopers) {
        this.employeeFloorChangeProbability = employeeFloorChangeProbability;
        this.clientArrivalProbability = clientArrivalProbability;
        this.seed = seed;
        this.numEmployees = numEmployees;
        this.numDevelopers = numDevelopers;
    }

    public BigDecimal getEmployeeFloorChangeProbability() {
        return employeeFloorChangeProbability;
    }

    public BigDecimal getClientArrivalProbability() {
        return clientArrivalProbability;
    }

    public long getSeed() {
        return seed;
    }

    public int getNumEmployees() {
        return numEmployees;
    }

    public int getNumDevelopers() {
        return numDevelopers;
    }

}
