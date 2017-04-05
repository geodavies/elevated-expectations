package uk.ac.aston.dc2300.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by George on 04/04/17.
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

    public void addOccupant(BuildingOccupant buildingOccupant) {
        occupants.add(buildingOccupant);
    }

    public void removeOccupant(BuildingOccupant buildingOccupant) {
        occupants.remove(buildingOccupant);
    }

}
