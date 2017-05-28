package uk.ac.aston.dc2300.model.entity;

import uk.ac.aston.dc2300.utility.RandomUtils;

import java.math.BigDecimal;

/**
 * This class represents an entity that moves around within the building such as a person or a maintenance crew.
 * Each entity stores the time they entered the building, the size of the entity (amount of space they take up in
 * an elevator) and the destination their current destination.
 *
 * @author George Davies
 * @since 04/04/17
 */
public abstract class BuildingOccupant {

    private final int occupantSize;

    protected final int timeEntered;

    protected Floor destination;
    protected int destinationArrivalTime;

    /**
     * @param occupantSize the number of spaces the occupant takes in the elevator
     * @param timeEntered the time in seconds the occupant entered the building following simulation start
     */
    public BuildingOccupant(int occupantSize, int timeEntered) {
        this.occupantSize = occupantSize;
        this.timeEntered = timeEntered;
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
     * Sets a new destination for the occupant providing all of the conditions to change floor are met first, this could
     * include random probability of floor change or whether they've been at the destination for the required amount of
     * time.
     *
     * @param building The building that contains the occupant
     * @param randomUtils Random utility class to generate numbers from
     * @param probability The probability that the occupant will change floor
     * @param currentTime The current time in seconds
     */
    public abstract void setNewDestination(Building building, RandomUtils randomUtils, BigDecimal probability, int currentTime);

    /**
     * Moves the occupant from the elevator to the given floor
     *
     * @param elevator the elevator to move the occupant from
     * @param floor the floor to move the occupant to
     */
    private void getOutElevator(Elevator elevator, Floor floor, int currentTime) {
        // Remove from elevator
        elevator.removeOccupant(this);
        // Add to floor
        floor.addOccupant(this);
        // Update destination arrival time to now
        destinationArrivalTime = currentTime;
        System.out.println(String.format("Passenger got out of elevator at floor %s", floor.getFloorNumber()));
    }

    /**
     * Checks if the the given floor is the destination of the occupant and will move them to that floor if it is
     *  @param elevator the elevator to move the occupant from
     * @param floor the floor to move the occupant to
     * @param currentTime
     */
    public void getOutElevatorIfAtDestination(Elevator elevator, Floor floor, int currentTime) {
        if (floor.equals(destination)) {
            getOutElevator(elevator, floor, currentTime);
        }
    }

    protected void leaveBuilding(Floor currentFloor) {
        currentFloor.removeOccupant(this);
    }
}
