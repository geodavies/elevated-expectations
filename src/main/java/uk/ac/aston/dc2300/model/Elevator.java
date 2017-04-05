package uk.ac.aston.dc2300.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by George on 04/04/17.
 */
public class Elevator {

    private Floor previousFloor;

    private Floor currentFloor;

    private final Set<BuildingOccupant> occupants;

    private final int maxCapacity;

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

    public void addOccupant(BuildingOccupant buildingOccupant) {
        occupants.add(buildingOccupant);
    }

    public void removeOccupant(BuildingOccupant buildingOccupant) {
        occupants.remove(buildingOccupant);
    }

}
