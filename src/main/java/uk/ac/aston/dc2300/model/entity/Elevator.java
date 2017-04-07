package uk.ac.aston.dc2300.model.entity;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This class represents an elevator from inside the building. Each elevator stores its current floor, the floor it was
 * previously at, the OCCUPANTS contained and the maximum capacity (maximum number of OCCUPANTS allowed in at one time).
 *
 * @author George Davies
 * @since 04/04/17
 */
public class Elevator {

    private Floor previousFloor;

    private Floor currentFloor;

    private final Set<BuildingOccupant> OCCUPANTS;

    private final int MAX_CAPACITY;

    private static final Logger LOGGER = LogManager.getLogger(Elevator.class);

    /**
     * @param MAX_CAPACITY the maximum amount of spaces available inside the elevator
     * @param currentFloor the floor at which the elevator is to start
     */
    public Elevator(int MAX_CAPACITY, Floor currentFloor){
        this.MAX_CAPACITY = MAX_CAPACITY;
        this.currentFloor = currentFloor;
        this.previousFloor = currentFloor;
        OCCUPANTS = new HashSet<>();
    }

    public Set<BuildingOccupant> getOccupants() {
        return OCCUPANTS;
    }

    public int getMaxCapacity() {
        return MAX_CAPACITY;
    }

    /**
     * Adds a BuildingOccupant to the elevator
     *
     * @param buildingOccupant the BuildingOccupant to be added
     */
    public void addOccupant(BuildingOccupant buildingOccupant) {
        OCCUPANTS.add(buildingOccupant);
    }

    /**
     * Removes a BuildingOccupant from the elevator
     *
     * @param buildingOccupant the BuildingOccupant to be removed
     */
    public void removeOccupant(BuildingOccupant buildingOccupant) {
        OCCUPANTS.remove(buildingOccupant);
    }

    /**
     * Determines where the elevator should move to next based on specification rules and then moves the elevator either
     * up or down.
     *
     * @param floors list of floors in the building
     */
    public void moveIfRequested(List<Floor> floors) {

        if (currentFloor.getFloorNumber() != 0 && !anyoneWaiting(floors)) { // There's nobody waiting return to ground floor
            moveDown(floors);
        } else if (currentFloor.getFloorNumber() == 0) { // The elevator is currently on the ground floor
            // If there's anyone waiting above current floor move up
            if (anyoneWaiting(getFloorsAbove(floors))) {
                moveUp(floors);
            }
        } else if (currentFloor.getFloorNumber() == floors.size() - 1) { // The elevator is currently on the top floor
            // If there's anyone waiting below current floor move down
            if (anyoneWaiting(getFloorsBelow(floors))) {
                moveDown(floors);
            }
        } else if(currentFloor.getFloorNumber() > previousFloor.getFloorNumber()){ // If the elevator last moved up
            // If there's anyone waiting above then continue moving up
            if (anyoneWaiting(getFloorsAbove(floors))) {
                moveUp(floors);
            } else {
                // Otherwise move down if there's people below
                if (anyoneWaiting(getFloorsBelow(floors))) {
                    moveDown(floors);
                }
            }
        } else if (currentFloor.getFloorNumber() < previousFloor.getFloorNumber()) { // If the elevator last moved down
            // If there's anyone waiting below then continue moving down
            if (anyoneWaiting(getFloorsBelow(floors))) {
                moveDown(floors);
            } else {
                // Otherwise move up if there's people above
                if (anyoneWaiting(getFloorsAbove(floors))) {
                    moveUp(floors);
                }
            }
        }

    }

    /**
     * Gets any passengers from the current floor and puts them into the elevator if there's enough space and rules are
     * met
     */
    public void loadPassengers() {
        // Create a copy of elevator queue so it can be modified in the loop
        LinkedList<BuildingOccupant> elevatorQueue = new LinkedList<>(currentFloor.getElevatorQueue());
        for (BuildingOccupant buildingOccupant : elevatorQueue) {
            // If the elevator is full then stop loading
            if (MAX_CAPACITY == getUsedCapacity()) {
                LOGGER.info("Elevator is full, won't load any more passengers");
                break;
            }
            if (getUsedCapacity() + buildingOccupant.getSize() <= MAX_CAPACITY) {
                // Command the passenger to get in the elevator
                buildingOccupant.getInElevator(this, currentFloor);
                LOGGER.info(String.format("Picked up new passenger going to floor %s", buildingOccupant.getDestination().getFloorNumber()));
            }
        }
    }

    /**
     * Gets any passengers currently in the elevator that have a destination of the current floor and moves them onto
     * that floor.
     */
    public void unloadPassengers() {
        // Create a copy of the occupants set so it can be modified in the loop
        Set<BuildingOccupant> elevatorOccupants = new HashSet<>(OCCUPANTS);
        for (BuildingOccupant buildingOccupant : elevatorOccupants) {
            if (buildingOccupant.getDestination().equals(currentFloor)) {
                // Get out of the elevator
                removeOccupant(buildingOccupant);
                // Get onto the floor
                currentFloor.addOccupant(buildingOccupant);
                LOGGER.info(String.format("Let out passenger on floor %s", currentFloor.getFloorNumber()));
            }
        }
    }

    /**
     *  Returns a sublist of the floors passed but only includes floors above the current elevator location.
     *
     * @param floors collection of all the floors in the building
     * @return a sublist of floors above current floor
     */
    private List<Floor> getFloorsAbove(List<Floor> floors) {
        return floors.subList(currentFloor.getFloorNumber() + 1, floors.size());
    }

    /**
     *  Returns a sublist of the floors passed but only includes floors below the current elevator location.
     *
     * @param floors collection of all the floors in the building
     * @return a sublist of floors below current floor
     */
    private List<Floor> getFloorsBelow(List<Floor> floors) {
        return floors.subList(0, currentFloor.getFloorNumber());
    }

    /**
     * Checks if anyone is waiting to be collected or travel to the given floors
     *
     * @param floors the floors to check
     * @return boolean status true=passengersWaiting, false=noneWaiting
     */
    private boolean anyoneWaiting(List<Floor> floors) {
        boolean waiting = false;
        for (Floor floor : floors) {
            // If there's someone queuing on that floor
            if (!floor.getElevatorQueue().isEmpty()) {
                waiting = true;
                break;
            }
            for (BuildingOccupant buildingOccupant : OCCUPANTS) {
                // If someone currently in the lift wants to go to that floor
                if (buildingOccupant.getDestination().equals(floor)) {
                    waiting = true;
                    break;
                }
            }
        }
        return waiting;
    }

    /**
     * Moves the elevator up
     *
     * @param floors collection of floors in the building
     */
    private void moveUp(List<Floor> floors) {
        previousFloor = currentFloor;
        // Increment current floor
        currentFloor = floors.get(currentFloor.getFloorNumber() + 1);
        LOGGER.info(String.format("Elevator moved up from floor %s to floor %s", previousFloor.getFloorNumber(), currentFloor.getFloorNumber()));
    }

    /**
     * Moves the elevator down
     *
     * @param floors collection of floors in the building
     */
    private void moveDown(List<Floor> floors) {
        previousFloor = currentFloor;
        // Decrement current floor
        currentFloor = floors.get(currentFloor.getFloorNumber() - 1);
        LOGGER.info(String.format("Elevator moved down from floor %s to floor %s", previousFloor.getFloorNumber(), currentFloor.getFloorNumber()));
    }

    /**
     * Gets the current amount of space that is being used up in the elevator
     *
     * @return int of used capacity
     */
    private int getUsedCapacity() {
        int usedCapacity = 0;
        for (BuildingOccupant buildingOccupant : OCCUPANTS) {
            usedCapacity += buildingOccupant.getSize();
        }
        return usedCapacity;
    }
}
