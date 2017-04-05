package uk.ac.aston.dc2300.model;

import java.util.HashSet;
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

    /**
     * @param maxCapacity the maximum amount of spaces available inside the elevator
     */
    public Elevator(int maxCapacity){
        this.maxCapacity = maxCapacity;
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

}
