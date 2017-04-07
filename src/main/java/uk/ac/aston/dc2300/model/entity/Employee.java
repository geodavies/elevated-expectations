package uk.ac.aston.dc2300.model.entity;

/**
 * This class represents an employee that works at the building
 *
 * @author George Davies
 * @since 04/04/17
 */
public class Employee extends BuildingOccupant {

    public Employee() {
        super(1, 0);
    }

    @Override
    public void callElevator(Floor currentFloor) {
        currentFloor.getElevatorQueue().addLast(this);
    }

    @Override
    public void getInElevator(Elevator elevator, Floor floor) {
        // Leave the queue
        floor.getElevatorQueue().remove(this);
        // Leave the floor
        floor.getOccupants().remove(this);
        // Get in the elevator
        elevator.addOccupant(this);
    }

}
