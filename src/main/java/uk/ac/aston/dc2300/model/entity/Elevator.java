package uk.ac.aston.dc2300.model.entity;

import uk.ac.aston.dc2300.model.status.ElevatorDoorStatus;
import uk.ac.aston.dc2300.model.status.ElevatorMovementStatus;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static uk.ac.aston.dc2300.model.status.ElevatorDoorStatus.*;
import static uk.ac.aston.dc2300.model.status.ElevatorMovementStatus.MOVING;
import static uk.ac.aston.dc2300.model.status.ElevatorMovementStatus.STATIONARY;

/**
 * This class represents an elevator from inside the building. Each elevator stores its current floor, the floor it was
 * previously at, the occupants contained and the maximum capacity (maximum number of occupants allowed in at one time).
 *
 * @author George Davies
 * @since 04/04/17
 */
public class Elevator {

    private Floor previousFloor;

    private Floor currentFloor;

    private Set<BuildingOccupant> currentOccupants;

    private Set<BuildingOccupant> occupantsLastTick;
    private final int MAX_CAPACITY;

    private ElevatorDoorStatus doorStatus;

    private ElevatorMovementStatus movementStatus;

    /**
     * @param maxCapacity the maximum amount of spaces available inside the elevator
     * @param currentFloor the floor at which the elevator is to start
     */
    public Elevator(int maxCapacity, Floor currentFloor){
        MAX_CAPACITY = maxCapacity;
        this.currentFloor = currentFloor;
        previousFloor = currentFloor;
        currentOccupants = new HashSet<>();
        occupantsLastTick = new HashSet<>();
        doorStatus = CLOSED;
        movementStatus = STATIONARY;
    }

    /**
     * Adds a BuildingOccupant to the elevator.
     *
     * @param buildingOccupant the BuildingOccupant to be added
     */
    public void addOccupant(BuildingOccupant buildingOccupant) {
        currentOccupants.add(buildingOccupant);
    }

    /**
     * Removes a BuildingOccupant from the elevator.
     *
     * @param buildingOccupant the BuildingOccupant to be removed
     */
    public void removeOccupant(BuildingOccupant buildingOccupant) {
        currentOccupants.remove(buildingOccupant);
    }

    /**
     * Determines where the elevator should move to next based on specification rules and then moves the elevator either
     * up or down.
     *
     * @param floors list of floors in the building
     */
    public void moveIfRequested(List<Floor> floors) {
        if (movementStatus.equals(MOVING)) {
            movementStatus = STATIONARY;
        } else {
            if (currentFloor.getFloorNumber() != 0 && !anyoneWaitingForFloors(floors)) { // There's nobody waiting return to ground floor
                moveDown(floors);
            } else if (currentFloor.getFloorNumber() == 0) { // The elevator is currently on the ground floor
                // If there's anyone waiting above current floor move up
                if (anyoneWaitingForFloors(getFloorsAbove(floors))) {
                    moveUp(floors);
                }
            } else if(currentFloor.getFloorNumber() > previousFloor.getFloorNumber()){ // If the elevator last moved up
                // If there's anyone waiting above then continue moving up
                if (anyoneWaitingForFloors(getFloorsAbove(floors))) {
                    moveUp(floors);
                } else {
                    // Otherwise move down if there's people below
                    if (anyoneWaitingForFloors(getFloorsBelow(floors))) {
                        moveDown(floors);
                    }
                }
            } else if (currentFloor.getFloorNumber() < previousFloor.getFloorNumber()) { // If the elevator last moved down
                // If there's anyone waiting below then continue moving down
                if (anyoneWaitingForFloors(getFloorsBelow(floors))) {
                    moveDown(floors);
                } else {
                    // Otherwise move up if there's people above
                    if (anyoneWaitingForFloors(getFloorsAbove(floors))) {
                        moveUp(floors);
                    }
                }
            }
        }
    }

    /**
     * Gets any passengers currently in the elevator.
     * @return Passengers
     */
    public Set<BuildingOccupant> getPassengers(){
        return currentOccupants;
    }

    /**
     * Gets any passengers from the current floor and puts them into the elevator if there's enough space and rules are
     * met.
     */
    public void loadPassengers(int topFloorNumber) {
        if (doorStatus.equals(OPEN)) {
            // Create a copy of the occupants to allow for concurrent modification
            LinkedList<BuildingOccupant> elevatorQueue = new LinkedList<>(currentFloor.getElevatorQueue());
            for (BuildingOccupant buildingOccupant : elevatorQueue) {
                int usedCapacity = getUsedCapacity();
                // If the elevator is full then stop loading
                if (MAX_CAPACITY == usedCapacity) {
                    break;
                }
                if (usedCapacity + buildingOccupant.getSize() <= MAX_CAPACITY &&
                        travellingInCorrectDirection(buildingOccupant, topFloorNumber)) {
                    loadPassenger(buildingOccupant);
                }
            }
        }
    }

    private void loadPassenger(BuildingOccupant buildingOccupant) {
        buildingOccupant.getInElevator(this, currentFloor);
        System.out.println(String.format("Picked up new passenger going to floor %s", buildingOccupant.getDestination().getFloorNumber()));
    }

    private boolean travellingInCorrectDirection(BuildingOccupant buildingOccupant, int topFloorNumber) {
        if (currentFloor.getFloorNumber() != 0 && currentFloor.getFloorNumber() != topFloorNumber) {
            // Check direction of travel
            if (previousFloor.getFloorNumber() < currentFloor.getFloorNumber()) {
                // Elevator going up
                if (buildingOccupant.getDestination().getFloorNumber() > currentFloor.getFloorNumber()) {
                    // Elevator going in same direction as destination, get in
                    return true;
                }
            } else if (previousFloor.getFloorNumber() > currentFloor.getFloorNumber()) {
                // Elevator going down
                if (buildingOccupant.getDestination().getFloorNumber() < currentFloor.getFloorNumber()) {
                    // Elevator going in same direction as destination, get in
                    return true;
                }
            }
        } else {
            // Occupant is on top or bottom floor, so always get in
            return true;
        }
        return false;
    }

    /**
     * Gets any passengers currently in the elevator that have a destination of the current floor and moves them onto
     * that floor.
     *
     * @param currentTime The current time of the simulation
     */
    public void unloadPassengers(int currentTime) {
        if (doorStatus.equals(OPEN)) {
            // Create a copy of the occupants to allow for concurrent modification
            Set<BuildingOccupant> elevatorOccupants = new HashSet<>(currentOccupants);
            for (BuildingOccupant buildingOccupant : elevatorOccupants) {
                buildingOccupant.getOutElevatorIfAtDestination(this, currentFloor, currentTime);
            }
        }
    }

    /**
     * This method looks at destinations of passengers and queue on current floor and determine whether the doors need
     * to be opened or closed.
     */
    public void updateDoorStatus(int topFloorNumber) {
        switch (doorStatus) {
            case OPENING:
                // If the doors are opening, finish opening them
                doorStatus = OPEN;
                System.out.println("Elevator doors are now open");
                break;
            case CLOSING:
                // If the doors are closing, finish closing them
                doorStatus = CLOSED;
                System.out.println("Elevator doors are now closed");
                break;
            case OPEN:
                // If the doors are open and nobody entered last tick then begin closing doors
                if (currentOccupants.equals(occupantsLastTick)) closeDoors();
                break;
            case CLOSED:
                // Check if anyone is waiting outside elevator
                boolean occupantsWaitingToEnter = false;
                for (BuildingOccupant buildingOccupant : currentFloor.getElevatorQueue()) {
                    if (travellingInCorrectDirection(buildingOccupant, topFloorNumber)) {
                        occupantsWaitingToEnter = true;
                        break;
                    }
                }

                // If the doors are closed, the elevator is stationary and people are waiting to get in or out then begin opening doors
                if (movementStatus.equals(STATIONARY) && (anyPassengerDestinationCurrentFloor() || occupantsWaitingToEnter)) openDoors();

        }
        // Make the previous stored tick become this one
        occupantsLastTick.clear();
        occupantsLastTick.addAll(currentOccupants);
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
    private boolean anyoneWaitingForFloors(List<Floor> floors) {
        boolean waiting = false;
        for (Floor floor : floors) {
            // If there's someone queuing on that floor
            if (floor.isAnyoneWaiting()) {
                waiting = true;
                break;
            }
            for (BuildingOccupant buildingOccupant : currentOccupants) {
                // If someone currently in the elevator wants to go to that floor
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
        if (doorStatus.equals(CLOSED)) {
            movementStatus = MOVING;
            previousFloor = currentFloor;
            // Increment current floor
            currentFloor = floors.get(currentFloor.getFloorNumber() + 1);
            System.out.println(String.format("Elevator moving up from floor %s to floor %s", previousFloor.getFloorNumber(), currentFloor.getFloorNumber()));
        }
    }

    /**
     * Moves the elevator down
     *
     * @param floors collection of floors in the building
     */
    private void moveDown(List<Floor> floors) {
        if (doorStatus.equals(CLOSED)) {
            movementStatus = MOVING;
            previousFloor = currentFloor;
            // Decrement current floor
            currentFloor = floors.get(currentFloor.getFloorNumber() - 1);
            System.out.println(String.format("Elevator moving down from floor %s to floor %s", previousFloor.getFloorNumber(), currentFloor.getFloorNumber()));
        }
    }

    /**
     * Gets the current amount of space that is being used up in the elevator
     *
     * @return int of used capacity
     */
    private int getUsedCapacity() {
        int usedCapacity = 0;
        for (BuildingOccupant buildingOccupant : currentOccupants) {
            usedCapacity += buildingOccupant.getSize();
        }
        return usedCapacity;
    }

    /**
     * Returns whether there are passengers waiting to get off at current floor
     *
     * @return boolean true=passengers waiting, false=no passengers waiting
     */
    private boolean anyPassengerDestinationCurrentFloor() {
        for (BuildingOccupant buildingOccupant : currentOccupants) {
            if (buildingOccupant.getDestination().equals(currentFloor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the doorStatus to opening which takes one tick to complete
     */
    private void openDoors() {
        System.out.println("Opening elevator doors");
        doorStatus = OPENING;
    }

    /**
     * Sets the doorStatus to closing which takes one tick to complete
     */
    private void closeDoors() {
        System.out.println("Closing elevator doors");
        doorStatus = CLOSING;
    }

    public Floor getCurrentFloor() {
        return currentFloor;
    }

}
