package uk.ac.aston.dc2300.component;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uk.ac.aston.dc2300.model.configuration.SimulationConfiguration;
import uk.ac.aston.dc2300.model.entity.*;

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

    private final BigDecimal FLOOR_CHANGE_PROBABILITY;

    private final BigDecimal CLIENT_ARRIVAL_PROBABILITY;

    private final String SEED;

    private final Building BUILDING;

    private static final Logger LOGGER = LogManager.getLogger(Simulation.class);

    public Simulation(SimulationConfiguration simulationConfiguration){

        LOGGER.info("Creating simulation...");

        // Create floor(s)
        List<Floor> floors = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Floor floor = new Floor();
            floors.add(floor);
        }

        // Create elevator(s) and put in ground floor
        Elevator elevator = new Elevator(4, floors.get(0));
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

        // Set randomization seed
        SEED = simulationConfiguration.getSeed();

        // Create and set Building
        BUILDING = new Building(elevatorSet, floors);

        LOGGER.info("Finished creating simulation");

    }

    public void start() {
        // TODO: Implement
    }

}
