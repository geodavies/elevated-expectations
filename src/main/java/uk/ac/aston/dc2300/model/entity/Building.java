package uk.ac.aston.dc2300.model.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a building which contains multiple elevators and floors for the elevators to visit. The
 * building also keeps log of the amount of complaints received by clients regarding elevator waiting times.
 *
 * @author George Davies
 * @since 04/04/17
 */
public class Building {

    private final Set<Elevator> elevators;

    private final List<Floor> floors;

    private int numComplaints;

    /**
     * @param elevators The set of elevators inside the building
     * @param floors The list of floors contained by the building
     */
    public Building(Set<Elevator> elevators, List<Floor> floors) {
        this.elevators = elevators;
        this.floors = floors;
    }

    public Set<Elevator> getElevators() {
        return elevators;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    /**
     * Increments the number of complaints received by one
     */
    public void addComplaint() {
        this.numComplaints  = numComplaints + 1;
    }

    public int getNumComplaints() {
        return this.numComplaints;
    }

    /**
     * Gets all of the floors within the top section of the building
     *
     * @return the list of floors in the top half of the building
     */
    public List<Floor> getTopHalfFloors() {
        // Get number of floors
        int numberOfFloors = floors.size();
        // Get middle floor (safe for odd numbers too)
        int middleFloorRoundingUp = numberOfFloors/2 + numberOfFloors%2;
        // Return sublist of floors
        return floors.subList(middleFloorRoundingUp, numberOfFloors);
    }

    /**
     * Gets all of the floors within the bottom section of the building
     *
     * @return the list of floors in the bottom half of the building
     */
    public List<Floor> getBottomHalfFloors() {
        // Get number of floors
        int numberOfFloors = floors.size();
        // Get middle floor (safe for odd numbers too)
        int middleFloorRoundingUp = numberOfFloors/2 + numberOfFloors%2;
        // Return sublist of floors
        return floors.subList(0, middleFloorRoundingUp);
    }

    /**
     * Searches the building for the given occupant and returns the floor which contains them
     *
     * @param buildingOccupant the occupant to find
     * @return the floor containing that occupant
     */
    public Floor getFloorContainingOccupant(BuildingOccupant buildingOccupant) {
        // Loop through all the floors in the building
        for (Floor floor : floors) {
            // If the floor contains the occupant then return that floor
            if (floor.getOccupants().contains(buildingOccupant)) return floor;
        }
        return null;
    }

    public Set<BuildingOccupant> getAllOccupants() {
        Set<BuildingOccupant> occupants = new HashSet<>();
        for (Floor floor : floors) {
            occupants.addAll(floor.getOccupants());
        }
        return occupants;
    }

}
