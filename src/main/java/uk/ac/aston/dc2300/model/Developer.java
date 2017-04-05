package uk.ac.aston.dc2300.model;

/**
 * This class represents a developer that works in the building
 *
 * @author George Davies
 * @since 04/04/17
 */
public class Developer extends BuildingOccupant {

    /**
     * @param timeEntered the time in seconds the Developer entered the building following simulation start
     */
    public Developer(int timeEntered) {
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
