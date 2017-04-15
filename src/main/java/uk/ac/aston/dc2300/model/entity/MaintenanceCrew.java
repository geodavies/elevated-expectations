package uk.ac.aston.dc2300.model.entity;

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
    public void callElevator(Floor currentFloor) {
        currentFloor.addToBackOfQueue(this);
    }

    @Override
    public void getInElevator(Elevator elevator, Floor floor) {
        // Leave the queue
        floor.removeFromQueue(this);
        // Leave the floor
        floor.removeOccupant(this);
        // Get in the elevator
        elevator.addOccupant(this);
    }

}
