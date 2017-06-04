package uk.ac.aston.dc2300.model.entity;

import uk.ac.aston.dc2300.model.status.DeveloperCompany;
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

    private DeveloperCompany company;

    /**
     * @param timeEntered the time in seconds the Developer entered the building following simulation start
     * @param company the company the Developer works for
     */
    public Developer(int timeEntered, DeveloperCompany company) {
        super(1, timeEntered);
        this.company = company;
    }

    @Override
    public void callElevator(Floor currentFloor, int currentTime) {
        currentFloor.addToBackOfQueue(this);
        startQueueTimer(currentTime);
    }

    @Override
    public void setNewDestination(Building building, RandomUtils randomUtils, BigDecimal probability, int currentTime) {
        if (randomUtils.getBigDecimal().compareTo(probability) <= 0) {
            Floor currentFloor = building.getFloorContainingOccupant(this);
            // Assign developers a floor in the top half
            List<Floor> topHalfFloors = building.getTopHalfFloors();
            int randomFloorIndex = randomUtils.getIntInRange(0, topHalfFloors.size() - 1);
            while (topHalfFloors.get(randomFloorIndex).getFloorNumber() == currentFloor.getFloorNumber()) {
                // If random floor is current floor try again
                randomFloorIndex = randomUtils.getIntInRange(0, topHalfFloors.size() - 1);
            }
            Floor destination = topHalfFloors.get(randomFloorIndex);
            setDestination(destination);
            System.out.println(String.format("Developer on floor %s set destination floor %s", currentFloor.getFloorNumber(), destination.getFloorNumber()));
            callElevator(currentFloor, currentTime);
        }
    }

    @Override
    public void getInElevator(Elevator elevator, Floor floor, int currentTime) {
        // Leave the queue
        floor.removeFromQueue(this);
        // If we have rivals in the elevator.
        if (elevatorContainsRival(elevator)) {
            System.out.println("Developer rejecting elevator due to rival.");
            // Enter the back of the queue
            floor.addToBackOfQueue(this);
        } else {
            System.out.println("Developer accepting elevator due to no rivals.");
            // Leave the floor
            floor.removeOccupant(this);
            // Get in the elevator
            elevator.addOccupant(this);
            resetQueueTimer(currentTime);
        }
    }

    /**
     * Checks an elevator for any rivals
     *
     * @param elevator the elevator to search for a rival developer in
     * @return whether or not the elevator contains a rival
     */
    private boolean elevatorContainsRival(Elevator elevator) {
        for (BuildingOccupant passenger : elevator.getOccupants()) {
            if (passenger instanceof Developer && ((Developer) passenger).getCompany() != this.company) {
                return true;
            }
        }
        return false;
    }

    public DeveloperCompany getCompany() {
        return this.company;
    }

}
