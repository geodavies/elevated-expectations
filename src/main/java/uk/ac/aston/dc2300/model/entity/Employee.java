package uk.ac.aston.dc2300.model.entity;

/**
 * This class represents an employee that works at the building
 *
 * @author George Davies
 * @since 04/04/17
 */
public class Employee extends BuildingOccupant {

    /**
     * @param timeEntered the time in seconds the Employee entered the building following simulation start
     */
    public Employee(int timeEntered) {
        super(1, timeEntered);
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
