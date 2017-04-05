package uk.ac.aston.dc2300.model;

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

}
