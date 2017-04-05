package uk.ac.aston.dc2300.model;

/**
 * This class represents a maintenance crew that may come to visit the building. A maintenance crew will take up 4
 * spaces in the elevator.
 *
 * @author George Davies
 * @since 04/04/17
 */
public class MaintenanceCrew extends BuildingOccupant {

    /**
     * the time in seconds the MaintenanceCrew entered the building following simulation start
     */
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
