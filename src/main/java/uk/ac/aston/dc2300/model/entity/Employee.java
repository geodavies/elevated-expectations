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
    void callElevator(Building building, Floor floor) {
        // TODO: Implement
    }

    @Override
    void getInElevator(Elevator elevator) {
        // TODO: Implement
    }

}
