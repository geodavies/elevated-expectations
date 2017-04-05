package uk.ac.aston.dc2300.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * This class represents a floor inside the building. Each building contains multiple occupants and may have a queue of
 * them waiting to ride the elevator
 *
 * @author George Davies
 * @since 04/04/17
 */
public class Floor {

    private Set<BuildingOccupant> occupants;

    private LinkedList<BuildingOccupant> elevatorQueue;

    public Floor() {
        elevatorQueue = new LinkedList<>();
        occupants = new HashSet<>();
    }

    public Set<BuildingOccupant> getOccupants() {
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

}
