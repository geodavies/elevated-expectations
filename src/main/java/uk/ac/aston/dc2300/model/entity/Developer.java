package uk.ac.aston.dc2300.model.entity;

/**
 * This class represents a developer that works in the building
 *
 * @author George Davies
 * @since 04/04/17
 */
public class Developer extends BuildingOccupant {

    public Developer() {
        super(1, 0);
    }

    @Override
    public void callElevator(Floor currentFloor) {
        currentFloor.getElevatorQueue().addLast(this);
    }

    @Override
    public void getInElevator(Elevator elevator) {
        // TODO: Implement
    }

}
