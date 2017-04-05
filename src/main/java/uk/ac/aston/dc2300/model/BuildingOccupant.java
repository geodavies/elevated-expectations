package uk.ac.aston.dc2300.model;

/**
 * <p>
 *     This class represents an entity that moves around within the building such as a person or a maintenance crew.
 *     Each entity stores the time they entered the building, the size of the entity (amount of space they take up in
 *     an elevator) and the destination their current destination.
 * </p>
 *
 * @author George Davies
 * @since 04/04/17
 */
public abstract class BuildingOccupant {

    private final int timeEntered;

    private final int occupantSize;

    private Floor destination;

    /**
     * @param occupantSize the number of spaces the occupant takes in the elevator
     * @param timeEntered the time in seconds the occupant entered the building following simulation start
     */
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

    /**
     * Notifies the building that this occupant is waiting to ride the elevator
     *
     * @param building the building containing the occupant
     * @param floor the floor the occupant want to go to
     */
    abstract void callElevator(Building building, Floor floor);

    /**
     * Moves the occupant from their current floor into the elevator
     *
     * @param elevator the elevator to move the occupant to
     */
    abstract void getInElevator(Elevator elevator);

    /**
     * Moves the occupant from the elevator to the current floor the elevator is at
     *
     * @param elevator the elevator to move the occupant from
     */
    public void getOutElevator(Elevator elevator) {
        // TODO: Implement
    }

}
