package uk.ac.aston.dc2300.model.entity;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uk.ac.aston.dc2300.utility.RandomUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * This class represents a developer that works in the building
 *
 * @author George Davies
 * @since 04/04/17
 */
public class Developer extends BuildingOccupant {

    private static final Logger LOGGER = LogManager.getLogger(Developer.class);

    public Developer() {
        super(1, 0);
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
        if (randomUtils.getBigDecimal().compareTo(probability) <= 0) {
            Floor currentFloor = building.getFloorContainingOccupant(this);
            // Assign developers a floor in the top half
            List<Floor> topHalfFloors = building.getTopHalfFloors();
            int randomFloorIndex = randomUtils.getIntInRange(0, topHalfFloors.size() - 1);
            while (randomFloorIndex == currentFloor.getFloorNumber()) {
                // If random floor is current floor try again
                randomFloorIndex = randomUtils.getIntInRange(0, topHalfFloors.size() - 1);
            }
            Floor destination = topHalfFloors.get(randomFloorIndex);
            setDestination(destination);
            LOGGER.debug(String.format("Developer on floor %s set destination floor %s", currentFloor.getFloorNumber(), destination.getFloorNumber()));
            callElevator(currentFloor);
        }
    }

}
