package uk.ac.aston.dc2300.model.entity;

import java.util.*;

/**
 * This class represents a floor inside the building. Each building contains multiple occupants and may have a queue of
 * them waiting to ride the elevator
 *
 * @author George Davies
 * @since 04/04/17
 */
public class Floor {

    int floorNumber;

    private List<BuildingOccupant> occupants;

    private LinkedList<BuildingOccupant> elevatorQueue;

    /**
     * @param floorNumber the number of the floor
     */
    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        elevatorQueue = new LinkedList<>();
        occupants = new ArrayList<>();
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<BuildingOccupant> getOccupants() {
        return occupants;
    }

    public LinkedList<BuildingOccupant> getElevatorQueue() {
        return elevatorQueue;
    }

    /**
     * Adds a BuildingOccupant to the floor
     *
     * @param buildingOccupant the BuildingOccupant to be added
     */
    public void addOccupant(BuildingOccupant buildingOccupant) {
        occupants.add(buildingOccupant);
    }

    /**
     * Removes a BuildingOccupant from the floor
     *
     * @param buildingOccupant the BuildingOccupant to be removed
     */
    public void removeOccupant(BuildingOccupant buildingOccupant) {
        occupants.remove(buildingOccupant);
    }

    /**
     * Adds a BuildingOccupant to the front of the elevator queue
     *
     * @param buildingOccupant BuildingOccupant to be added
     */
    public void addToFrontOfQueue(BuildingOccupant buildingOccupant) {
        elevatorQueue.addFirst(buildingOccupant);
    }

    /**
     * Adds a BuildingOccupant to the back of the elevator queue
     *
     * @param buildingOccupant BuildingOccupant to be added
     */
    public void addToBackOfQueue(BuildingOccupant buildingOccupant) {
        elevatorQueue.addLast(buildingOccupant);
    }

    /**
     * Removes a BuildingOccupant from the elevator queue
     *
     * @param buildingOccupant BuildingOccupant to be removed
     */
    public void removeFromQueue(BuildingOccupant buildingOccupant) {
        elevatorQueue.remove(buildingOccupant);
    }

    /**
     * Checks if anyone is waiting to to be collected from here
     *
     * @return boolean true=someone waiting, false=nobody waiting
     */
    public boolean isAnyoneWaiting() {
        return !elevatorQueue.isEmpty();
    }

}
