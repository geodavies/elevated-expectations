package uk.ac.aston.dc2300.model.entity;

import uk.ac.aston.dc2300.model.status.ElevatorDirection;
import uk.ac.aston.dc2300.utility.RandomUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    private List<Integer> waitTimes;

    private int queueEntryTime;

    /**
     * @param occupantSize the number of spaces the occupant takes in the elevator
     * @param timeEntered the time in seconds the occupant entered the building following simulation start
     */
    public BuildingOccupant(int occupantSize, int timeEntered) {
        this.occupantSize = occupantSize;
        this.timeEntered = timeEntered;
        this.waitTimes = new ArrayList<>();
        this.queueEntryTime = -1;
    }

    /**
     * Notifies the building that this occupant is waiting to ride the elevator
     *
     * @param currentFloor the floor the occupant is calling from
     */
    public abstract void callElevator(Floor currentFloor, int currentTime);

    /**
     * Sets a new destination for the occupant providing all of the conditions to change floor are met first, this could
     * include random probability of floor change or whether they've been at the destination for the required amount of
     * time.
     *
     * @param building The building that contains the occupant
     * @param randomUtils Random utility class to generate numbers from
     * @param probability The probability that the occupant will set the new destination
     * @param currentTime The current time in seconds
     */
    public abstract void setNewDestination(Building building, RandomUtils randomUtils, BigDecimal probability, int currentTime);

    /**
     * Moves the occupant from their current floor into the elevator
     *
     * @param elevator the elevator to move the occupant to
     * @param floor the floor to move from
     */
    public abstract void getInElevator(Elevator elevator, Floor floor, int currentTime);

    /**
     * Checks if the the given floor is the destination of the occupant and will move them to that floor if it is
     *
     * @param elevator the elevator to move the occupant from
     * @param floor the floor to move the occupant to
     * @param currentTime the current simulation time
     */
    public void getOutElevatorIfAtDestination(Elevator elevator, Floor floor, int currentTime) {
        if (floor.equals(destination)) {
            getOutElevator(elevator, floor, currentTime);
        }
    }

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
     * Checks whether the occupant is travelling in the same direction the elevator is going
     *
     * @param elevator the elevator to check
     * @return same direction or not
     */
    public boolean travellingInSameDirection(Elevator elevator, List<Floor> floors) {
        ElevatorDirection elevatorDirection = elevator.whichDirectionNext(floors);

        if (elevatorDirection == ElevatorDirection.UP) {
            // If we're moving in the same direction
            if (destination.getFloorNumber() > elevator.getCurrentFloor().getFloorNumber()) {
                return true;
            }
            // Or it's about to change direction
            else if (!elevator.shouldElevatorTravelToFloors(elevator.getFloorsAbove(floors))) {
                return true;
            }
        } else if (elevatorDirection == ElevatorDirection.DOWN) {
            // If we're moving in the same direction
            if (destination.getFloorNumber() < elevator.getCurrentFloor().getFloorNumber()) {
                return true;
            }
            // Or it's about to change direction
            else if (!elevator.shouldElevatorTravelToFloors(elevator.getFloorsBelow(floors))) {
                return true;
            }
        }
        // if we're not moving up or down - jump in, noone's waiting
        return true;
    }

    /**
     * Records the queue entry time
     *
     * @param currentTime the current simulation time
     */
    protected void startQueueTimer(int currentTime) {
        this.queueEntryTime = currentTime;
    }

    /**
     * Resets the queue timer and adds the total wait time to the list
     *
     * @param currentTime the current simulation time
     */
    protected void resetQueueTimer(int currentTime) {
        if (queueEntryTime >= 0) {
            waitTimes.add(currentTime - queueEntryTime);
        }
        queueEntryTime = -1;
    }

    /**
     * Removes the occupant from the building
     *
     * @param currentFloor the current floor the occupant is on (should be ground)
     */
    protected void leaveBuilding(Floor currentFloor) {
        currentFloor.removeOccupant(this);
    }

    /**
     * Enter the building specified and sign in
     * @param building the building to enter
     */
    public void enterBuilding(Building building) {
        building.signIn(this);
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

    public int getQueueEntryTime() {
        return queueEntryTime;
    }

    public List<Integer> getWaitTimes() {
        return waitTimes;
    }

}
