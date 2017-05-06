package uk.ac.aston.dc2300.model.entity;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uk.ac.aston.dc2300.utility.RandomUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * This class represents a maintenance crew that may come to visit the building. A maintenance crew will take up 4
 * spaces in the elevator.
 *
 * @author George Davies
 * @since 04/04/17
 */
public class MaintenanceCrew extends BuildingOccupant {

    private int leaveAfterArrivalTime;

    private static final Logger LOGGER = LogManager.getLogger(MaintenanceCrew.class);

    /**
     * @param timeEntered the time in seconds the MaintenanceCrew entered the building following simulation start
     * @param leaveAfterArrivalTime the time in seconds the MaintenanceCrew will stay at their destination floor before leaving
     */
    public MaintenanceCrew(int timeEntered, int leaveAfterArrivalTime) {
        super(4, timeEntered);
        this.leaveAfterArrivalTime = leaveAfterArrivalTime;
    }

    @Override
    public void callElevator(Floor currentFloor) {
        currentFloor.addToBackOfQueue(this);
    }

    @Override
    public void getInElevator(Elevator elevator, Floor floor) {
        // Leave the queue
        floor.removeFromQueue(this);
        // Leave the floor
        floor.removeOccupant(this);
        // Get in the elevator
        elevator.addOccupant(this);
    }

    @Override
    public void setNewDestination(Building building, RandomUtils randomUtils, BigDecimal probability, int currentTime) {
        Floor currentFloor = building.getFloorContainingOccupant(this);
        Floor groundFloor = building.getFloors().get(0);
        // If the client has just arrived in the building
        if (currentFloor.equals(groundFloor) && timeEntered == currentTime) {
            // Assign maintenance workers to the top floor
            List<Floor> floors = building.getFloors();
            this.setDestination(floors.get(floors.size() - 1));
            LOGGER.debug(String.format("Maintenance Crew arrived on floor %s set destination floor %s", currentFloor.getFloorNumber(), floors.size() - 1));
            callElevator(currentFloor);
        } else if (currentFloor.equals(groundFloor) && destination.equals(groundFloor)) {
            leaveBuilding(currentFloor);
            LOGGER.info("Maintenance Crew has left the building");
        } else if (destination.equals(currentFloor) && currentTime >= destinationArrivalTime + leaveAfterArrivalTime) {
            // Set destination to ground floor to leave
            setDestination(groundFloor);
            LOGGER.debug(String.format("Maintenance Crew on floor %s set destination floor %s", currentFloor.getFloorNumber(), destination.getFloorNumber()));
            callElevator(currentFloor);
        }
    }

}
