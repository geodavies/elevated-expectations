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
    private int queueEnterTime;

    /**
     * @param timeEntered the time in seconds the Client entered the building following simulation start
     * @param leaveAfterArrivalTime the time in seconds the Client will stay at their destination floor before leaving
     */
    public Client(int timeEntered, int leaveAfterArrivalTime) {
        super(1, timeEntered);
        this.leaveAfterArrivalTime = leaveAfterArrivalTime;
        this.queueEnterTime = -1;
    }

    public boolean wouldLikeToComplain(int currentTime) {
        if (queueEnterTime > -1 && (currentTime - queueEnterTime) >= 600) {
            return true;
        } else {
            return false;
        }
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
        // Leaving queue
        queueEnterTime = -1;
    }

    public void getReadyToLeave(Floor ground) {
        setDestination(ground);
        queueEnterTime = -1;
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
            System.out.println(String.format("Client arrived on floor %s set destination floor %s", currentFloor.getFloorNumber(), destination.getFloorNumber()));
            callElevator(currentFloor);
            // Entered queue, start the clock
            queueEnterTime = currentTime;
        } else if (currentFloor.equals(groundFloor) && destination.equals(groundFloor)) {
            leaveBuilding(currentFloor);
            System.out.println("Client has left the building");
        } else if (destination.equals(currentFloor) && currentTime >= destinationArrivalTime + leaveAfterArrivalTime) {
            // Set destination to ground floor to leave
            setDestination(groundFloor);
            System.out.println(String.format("Client on floor %s set destination floor %s", currentFloor.getFloorNumber(), destination.getFloorNumber()));
            callElevator(currentFloor);
            // Entered queue, start the clock
            queueEnterTime = currentTime;
        }
    }

    public void setQueueEnterTime(int queueEnterTime) {
        this.queueEnterTime = queueEnterTime;
    }

}
