package uk.ac.aston.dc2300.model;

import java.util.List;
import java.util.Set;

/**
 * Created by George on 04/04/17.
 */
public class Building {

    private final Set<Elevator> elevators;

    private final List<Floor> floors;

    private int numComplaints;

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

    public void addComplaint() {
        this.numComplaints  = numComplaints + 1;
    }

    public int getNumComplaints() {
        return this.numComplaints;
    }

}
