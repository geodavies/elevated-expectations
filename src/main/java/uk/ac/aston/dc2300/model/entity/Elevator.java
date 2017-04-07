package uk.ac.aston.dc2300.model.entity;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private final Set<BuildingOccupant> occupants;

    private final int maxCapacity;

    private static final Logger LOGGER = LogManager.getLogger(Elevator.class);

    /**
     * @param maxCapacity the maximum amount of spaces available inside the elevator
     * @param currentFloor the floor at which the elevator is to start
     */
    public Elevator(int maxCapacity, Floor currentFloor){
        this.maxCapacity = maxCapacity;
        this.currentFloor = currentFloor;
        this.previousFloor = currentFloor;
        occupants = new HashSet<>();
    }

    public Set<BuildingOccupant> getOccupants() {
        return occupants;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Adds a BuildingOccupant to the elevator
     *
     * @param buildingOccupant the BuildingOccupant to be added
     */
    public void addOccupant(BuildingOccupant buildingOccupant) {
        occupants.add(buildingOccupant);
    }

    /**
     * Removes a BuildingOccupant from the elevator
     *
     * @param buildingOccupant the BuildingOccupant to be removed
     */
    public void removeOccupant(BuildingOccupant buildingOccupant) {
        occupants.remove(buildingOccupant);
    }

    public void moveIfRequested(List<Floor> floors) {

        if (currentFloor.getFloorNumber() == 0) { // The elevator is currently on the ground floor
            // If there's anyone waiting above current floor move up
            if (anyoneWaiting(getFloorsAbove(floors))) {
                moveUp(floors);
            }
        } else if (currentFloor.getFloorNumber() == floors.size() - 1) { // The elevator is currently on the top floor
            // If there's anyone waiting below current floor move down
            if (anyoneWaiting(getFloorsAbove(floors))) {
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

    private List<Floor> getFloorsAbove(List<Floor> floors) {
        return floors.subList(currentFloor.getFloorNumber() + 1, floors.size());
    }

    private List<Floor> getFloorsBelow(List<Floor> floors) {
        return floors.subList(0, currentFloor.getFloorNumber());
    }

    private boolean anyoneWaiting(List<Floor> floors) {
        boolean requestAbove = false;
        for (Floor floor : floors) {
            if (!floor.getElevatorQueue().isEmpty()) {
                requestAbove = true;
                break;
            }
        }
        return requestAbove;
    }

    private void moveUp(List<Floor> floors) {
        previousFloor = currentFloor;
        currentFloor = floors.get(currentFloor.getFloorNumber() + 1);
        LOGGER.info(String.format("Elevator moved up from floor %s to floor %s", previousFloor.getFloorNumber(), currentFloor.getFloorNumber()));
    }

    private void moveDown(List<Floor> floors) {
        previousFloor = currentFloor;
        currentFloor = floors.get(currentFloor.getFloorNumber() - 1);
        LOGGER.info(String.format("Elevator moved down from floor %s to floor %s", previousFloor.getFloorNumber(), currentFloor.getFloorNumber()));
    }

}
