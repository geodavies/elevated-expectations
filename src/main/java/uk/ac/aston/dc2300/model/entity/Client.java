package uk.ac.aston.dc2300.model.entity;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uk.ac.aston.dc2300.utility.RandomUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * This class represents a client visiting the building
 *
 * @author George Davies
 * @since 04/04/17
 */
public class Client extends BuildingOccupant {

    private int leaveAfterArrivalTime;

    private static final Logger LOGGER = LogManager.getLogger(Client.class);

    /**
     * @param timeEntered the time in seconds the Client entered the building following simulation start
     */
    public Client(int timeEntered, int leaveAfterArrivalTime) {
        super(1, timeEntered);
        this.leaveAfterArrivalTime = leaveAfterArrivalTime;
    }

    @Override
    public void callElevator(Floor currentFloor) {
        currentFloor.addToFrontOfQueue(this);
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
            // Assign client a floor in the bottom half
            List<Floor> bottomHalfFloors = building.getBottomHalfFloors();
            int randomFloorIndex = randomUtils.getIntInRange(0, bottomHalfFloors.size() - 1);
            while (randomFloorIndex == currentFloor.getFloorNumber()) {
                // If random floor is current floor try again
                randomFloorIndex = randomUtils.getIntInRange(0, bottomHalfFloors.size() - 1);
            }
            Floor destination = bottomHalfFloors.get(randomFloorIndex);
            setDestination(destination);
            LOGGER.debug(String.format("Client arrived on floor %s set destination floor %s", currentFloor.getFloorNumber(), destination.getFloorNumber()));
            callElevator(currentFloor);
        } else if (currentFloor.equals(groundFloor) && destination.equals(groundFloor)) {
            leaveBuilding(currentFloor);
            LOGGER.info("Client has left the building");
        } else if (destination.equals(currentFloor) && currentTime >= destinationArrivalTime + leaveAfterArrivalTime) {
            // Set destination to ground floor to leave
            setDestination(groundFloor);
            LOGGER.debug(String.format("Client on floor %s set destination floor %s", currentFloor.getFloorNumber(), destination.getFloorNumber()));
            callElevator(currentFloor);
        }
    }
}
