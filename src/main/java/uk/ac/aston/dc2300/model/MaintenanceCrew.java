package uk.ac.aston.dc2300.model;

/**
 * Created by George on 04/04/17.
 */
public class MaintenanceCrew extends BuildingOccupant {

    public MaintenanceCrew(int timeEntered) {
        super(4, timeEntered);
    }

    @Override
    void callElevator(Building building, Floor floor) {
        // TODO: Implement
    }

    @Override
    void getInElevator(Elevator elevator) {
        // TODO: Implement
    }

}
