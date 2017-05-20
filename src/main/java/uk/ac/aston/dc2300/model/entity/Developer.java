package uk.ac.aston.dc2300.model.entity;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uk.ac.aston.dc2300.model.status.DeveloperCompany;
import uk.ac.aston.dc2300.utility.RandomUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * This class represents a developer that works in the building
 *
 * @author George Davies
 * @since 04/04/17
 */
public class Developer extends BuildingOccupant {

    private DeveloperCompany company;
    private static final Logger LOGGER = LogManager.getLogger(Developer.class);

    /**
     * @param timeEntered the time in seconds the Developer entered the building following simulation start
     */
    public Developer(int timeEntered, DeveloperCompany company) {
        super(1, timeEntered);
        this.company = company;
    }

    public DeveloperCompany getCompany() {
        return this.company;
    }

    /**
     * Checks an elevator for any rivals - returns boolean result.
     * @param elevator the elevator to search for a rival developer in
     */
    private boolean elevatorContainsRival(Elevator elevator) {
        for (BuildingOccupant passenger : elevator.getPassengers()) {
            if (passenger instanceof Developer && ((Developer) passenger).getCompany() != this.company) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void callElevator(Floor currentFloor) {
        currentFloor.addToBackOfQueue(this);
    }

    @Override
    public void getInElevator(Elevator elevator, Floor floor) {
        // Leave the queue
        floor.removeFromQueue(this);
        // If we have rivals in the elevator.
        if (elevatorContainsRival(elevator)) {
            LOGGER.debug("Developer rejecting elevator due to rival.");
            // Enter the back of the queue
            floor.addToBackOfQueue(this);
        } else {
            LOGGER.debug("Developer accepting elevator due to no rivals.");
            // Leave the floor
            floor.removeOccupant(this);
            // Get in the elevator
            elevator.addOccupant(this);
        }
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
