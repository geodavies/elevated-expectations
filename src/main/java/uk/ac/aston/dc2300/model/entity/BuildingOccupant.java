package uk.ac.aston.dc2300.model.entity;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * This class represents an entity that moves around within the building such as a person or a maintenance crew.
 * Each entity stores the time they entered the building, the size of the entity (amount of space they take up in
 * an elevator) and the destination their current destination.
 *
 * @author George Davies
 * @since 04/04/17
 */
public abstract class BuildingOccupant {

    private final int timeEntered;

    private final int occupantSize;

    private Floor destination;

    private static final Logger LOGGER = LogManager.getLogger(BuildingOccupant.class);

    /**
     * @param occupantSize the number of spaces the occupant takes in the elevator
     * @param timeEntered the time in seconds the occupant entered the building following simulation start
     */
    public BuildingOccupant(int occupantSize, int timeEntered) {
        this.occupantSize = occupantSize;
        this.timeEntered = timeEntered;
    }

    public int getTimeEntered() {
        return timeEntered;
    }

    public int getSize() {
        return occupantSize;
    }

    public Floor getDestination() {
        return destination;
    }

    public void setDestination(Floor destination) {
        this.destination = destination;
    }

    /**
     * Notifies the building that this occupant is waiting to ride the elevator
     *
     * @param currentFloor the floor the occupant is calling from
     */
    public abstract void callElevator(Floor currentFloor);

    /**
     * Moves the occupant from their current floor into the elevator
     *
     * @param elevator the elevator to move the occupant to
     * @param floor the floor to move from
     */
    public abstract void getInElevator(Elevator elevator, Floor floor);

    /**
     * Moves the occupant from the elevator to the given floor
     *
     * @param elevator the elevator to move the occupant from
     * @param floor the floor to move the occupant to
     */
    private void getOutElevator(Elevator elevator, Floor floor) {
        // Remove from elevator
        elevator.removeOccupant(this);
        // Add to floor
        floor.addOccupant(this);
        LOGGER.info(String.format("Passenger got out of elevator at floor %s", floor.getFloorNumber()));
    }

    /**
     * Checks if the the given floor is the destination of the occupant and will move them to that floor if it is
     *
     * @param elevator the elevator to move the occupant from
     * @param floor the floor to move the occupant to
     */
    public void getOutElevatorIfAtDestination(Elevator elevator, Floor floor) {
        if (floor.equals(destination)) {
            getOutElevator(elevator, floor);
        }
    }

}
