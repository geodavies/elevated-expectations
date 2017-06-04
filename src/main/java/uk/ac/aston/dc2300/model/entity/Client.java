package uk.ac.aston.dc2300.model.entity;

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
    private boolean hasLeaveIntent;

    /**
     * @param timeEntered the time in seconds the Client entered the building following simulation start
     * @param leaveAfterArrivalTime the time in seconds the Client will stay at their destination floor before leaving
     */
    public Client(int timeEntered, int leaveAfterArrivalTime) {
        super(1, timeEntered);
        this.leaveAfterArrivalTime = leaveAfterArrivalTime;
        this.hasLeaveIntent = false;
    }

    @Override
    public void callElevator(Floor currentFloor, int currentTime) {
        currentFloor.addToFrontOfQueue(this);
        startQueueTimer(currentTime);
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
            Floor destination = bottomHalfFloors.get(randomFloorIndex);
            setDestination(destination);
            System.out.println(String.format("Client arrived on floor %s set destination floor %s", currentFloor.getFloorNumber(), destination.getFloorNumber()));
            // If we're already there - lets not join the queue!
            if (currentFloor != destination) {
                callElevator(currentFloor, currentTime);
            }
        } else if (currentFloor.equals(groundFloor) && hasLeaveIntent) {
            // If they're on the ground floor and they have the intent to leave. Leave the building.
            leaveBuilding(currentFloor);
            System.out.println("Client has left the building");
        } else if (destination.equals(currentFloor) && currentTime >= destinationArrivalTime + leaveAfterArrivalTime) { // If they're at their destination and they want to leave
            // Set destination to ground floor to leave
            setDestination(groundFloor);
            System.out.println(String.format("Client on floor %s set destination floor %s", currentFloor.getFloorNumber(), destination.getFloorNumber()));
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

    /**
     * Returns whether or not the Client is currently wanting to complain
     *
     * @param currentTime the current simulation time
     * @return to complain or not to complain (that is the question)
     */
    public boolean wouldLikeToComplain(int currentTime) {
        return getQueueEntryTime() > -1 && (currentTime - getQueueEntryTime()) >= 600;
    }

    /**
     * Sets the Client destination to ground floor and resets queue timer
     *
     * @param ground the ground floor of the building
     * @param currentTime the current simulation time
     */
    public void getReadyToLeave(Floor ground, int currentTime) {
        setDestination(ground);
        resetQueueTimer(currentTime);
        hasLeaveIntent = true;
    }

}
