package uk.ac.aston.dc2300.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a building which contains multiple elevators and floors for the elevators to visit. The
 * building also keeps log of the amount of complaints received by clients regarding elevator waiting times.
 *
 * @author George Davies
 * @since 04/04/17
 */
public class Building {

    private final List<Elevator> elevators;
    private final List<Floor> floors;

    private int numberComplaints;

    /**
     * @param elevators The list of elevators inside the building
     * @param floors The list of floors contained by the building
     */
    public Building(List<Elevator> elevators, List<Floor> floors) {
        this.elevators = elevators;
        this.floors = floors;
        numberComplaints = 0;
    }

    /**
     * Gets all of the floors within the top section of the building
     *
     * If number of floors is odd, the middle floor will be prioritised to bottom half
     *
     * @return the list of floors in the top half of the building
     */
    public List<Floor> getTopHalfFloors() {
        // Get number of floors
        int numberOfFloors = floors.size();
        // Get middle floor (safe for odd numbers too)
        int middleFloorRoundingUp = numberOfFloors/2 + numberOfFloors%2;
        // Return sublist of floors
        return floors.subList(middleFloorRoundingUp, numberOfFloors);
    }

    /**
     * Gets all of the floors within the bottom section of the building
     *
     * If number of floors is odd, the middle floor will be prioritised to bottom half
     *
     * @return the list of floors in the bottom half of the building
     */
    public List<Floor> getBottomHalfFloors() {
        // Get number of floors
        int numberOfFloors = floors.size();
        // Get middle floor (safe for odd numbers too)
        int middleFloorRoundingUp = numberOfFloors/2 + numberOfFloors%2;
        // Return sublist of floors
        return floors.subList(0, middleFloorRoundingUp);
    }

    /**
     * Searches the building for the given occupant and returns the floor which contains them
     *
     * @param buildingOccupant the occupant to find
     * @return the floor containing that occupant
     */
    public Floor getFloorContainingOccupant(BuildingOccupant buildingOccupant) {
        // Loop through all the floors in the building
        for (Floor floor : floors) {
            // If the floor contains the occupant then return that floor
            if (floor.getOccupants().contains(buildingOccupant)) return floor;
        }
        return null;
    }

    /**
     * Returns a list of elevators on the specified floor
     *
     * @param floor the floor to search on
     * @return list of elevators on that floor
     */
    public List<Elevator> getElevatorsOnFloor(Floor floor) {
        List<Elevator> elevatorsOnFloor = new ArrayList<>();
        for (Elevator elevator : elevators) {
            if (elevator.getCurrentFloor().equals(floor)) {
                elevatorsOnFloor.add(elevator);
            }
        }
        return elevatorsOnFloor;
    }

    /**
     * Collects all of the occupants from each of the floors and returns them
     *
     * @return occupants from all floors
     */
    public List<BuildingOccupant> getAllOccupants() {
        List<BuildingOccupant> occupants = new ArrayList<>();
        occupants.addAll(getAllOccupantsOnFloors());
        occupants.addAll(getAllOccupantsInElevators());
        return occupants;
    }

    /**
     * Gets all occupants that are currently on a floor
     *
     * @return list of BuildingOccupants
     */
    public List<BuildingOccupant> getAllOccupantsOnFloors() {
        List<BuildingOccupant> occupants = new ArrayList<>();
        for (Floor floor : floors) {
            occupants.addAll(floor.getOccupants());
        }
        return occupants;
    }

    /**
     * Gets all the occupants that are currently in elevators
     *
     * @return list of BuildingOccupants
     */
    private List<BuildingOccupant> getAllOccupantsInElevators() {
        List<BuildingOccupant> occupants = new ArrayList<>();
        for (Elevator elevator : elevators) {
            occupants.addAll(elevator.getOccupants());
        }
        return occupants;
    }


    /**
     * Check if any of the clients would like to complain
     *
     * @param currentTime the current simulation time
     * @return the current number of complaints
     */
    public int getClientComplaints(int currentTime) {
        for(BuildingOccupant occupant : getAllOccupants()) {
            if (occupant instanceof Client) {
                Client client = (Client) occupant;
                if (client.wouldLikeToComplain(currentTime)) {
                    // Complain
                    numberComplaints++;
                    System.out.println("Client complaining and leaving, total complaints: " + numberComplaints);
                    // Leave
                    client.getReadyToLeave(getFloors().get(0), currentTime);
                }
            }
        }
        return numberComplaints;
    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public int getNumberComplaints() {
        return numberComplaints;
    }
}
