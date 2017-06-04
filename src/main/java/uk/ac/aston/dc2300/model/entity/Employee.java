package uk.ac.aston.dc2300.model.entity;

import uk.ac.aston.dc2300.utility.RandomUtils;

import java.math.BigDecimal;

/**
 * This class represents an employee that works at the building
 *
 * @author George Davies
 * @since 04/04/17
 */
public class Employee extends BuildingOccupant {

    /**
     * @param timeEntered The time the Employee entered the building
     */
    public Employee(int timeEntered) {
        super(1, timeEntered);
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
            // Assign employees any floor
            int numFloors = building.getFloors().size();
            int randomFloorIndex = randomUtils.getIntInRange(0, numFloors - 1);
            while (randomFloorIndex == currentFloor.getFloorNumber()) {
                // If random floor is current floor try again
                randomFloorIndex = randomUtils.getIntInRange(0, numFloors - 1);
            }
            Floor destination = building.getFloors().get(randomFloorIndex);
            setDestination(building.getFloors().get(randomFloorIndex));
            System.out.println(String.format("Employee on floor %s set destination floor %s", currentFloor.getFloorNumber(), destination.getFloorNumber()));
            callElevator(currentFloor, currentTime);
        }
    }

    @Override
    public void getInElevator(Elevator elevator, Floor floor, int currentTime) {
        // Reset the queue timer
        resetQueueTimer(currentTime);
        // Leave the queue
        floor.removeFromQueue(this);
        // Leave the floor
        floor.removeOccupant(this);
        // Get in the elevator
        elevator.addOccupant(this);
    }

}
