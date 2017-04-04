package uk.ac.aston.dc2300.model;

/**
 * Created by George on 04/04/17.
 */
public abstract class BuildingOccupant {

    private final int timeEntered;

    private final int occupantSize;

    private Floor destination;

    public BuildingOccupant(int occupantSize, int timeEntered){
        this.occupantSize = occupantSize;
        this.timeEntered = timeEntered;
    }

    public int getTimeEntered() {
        return timeEntered;
    }

    public int getOccupantSize() {
        return occupantSize;
    }

    public Floor getDestination() {
        return destination;
    }

    public void setDestination(Floor destination) {
        this.destination = destination;
    }

    abstract void callElevator(Building building, Floor floor);

    abstract void getInElevator(Elevator elevator);

    public void getOutElevator(Elevator elevator) {
        // TODO: Implement
    }

}
