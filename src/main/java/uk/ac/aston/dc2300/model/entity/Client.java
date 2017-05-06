package uk.ac.aston.dc2300.model.entity;

/**
 * This class represents a client visiting the building
 *
 * @author George Davies
 * @since 04/04/17
 */
public class Client extends BuildingOccupant {

    /**
     * @param timeEntered the time in seconds the Client entered the building following simulation start
     */
    public Client(int timeEntered) {
        super(1, timeEntered);
    }

    @Override
    public void callElevator(Floor currentFloor) {
        currentFloor.addToFrontOfQueue(this);
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

    @Override
    public void reassignDestination() {
        // TODO: Implement
    }

}
