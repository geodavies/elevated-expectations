package uk.ac.aston.dc2300.model.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a building which contains multiple elevators and floors for the elevators to visit. The
 * building also keeps log of the amount of complaints received by clients regarding elevator waiting times.
 *
 * @author George Davies
 * @since 04/04/17
 */
public class Building {

    private final Set<Elevator> elevators;
    private final List<Floor> floors;

    /**
     * @param elevators The set of elevators inside the building
     * @param floors The list of floors contained by the building
     */
    public Building(Set<Elevator> elevators, List<Floor> floors) {
        this.elevators = elevators;
        this.floors = floors;
    }

    public Set<Elevator> getElevators() {
        return elevators;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    /**
     * Gets all of the floors within the top section of the building
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
     * Collects all of the occupants from each of the floors and returns them
     *
     * @return occupants from all floors
     */
    public Set<BuildingOccupant> getAllOccupants() {
        Set<BuildingOccupant> occupants = new HashSet<>();
        for (Floor floor : floors) {
            occupants.addAll(floor.getOccupants());
        }
        return occupants;
    }

    public List<Elevator> getElevatorsOnFloor(Floor floor) {
        List<Elevator> elevatorsOnFloor = new ArrayList<>();
        for (Elevator elevator : elevators) {
            if (elevator.getCurrentFloor().equals(floor)) {
                elevatorsOnFloor.add(elevator);
            }
        }
        return elevatorsOnFloor;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        // Roof
        stringBuilder.append("\n    Lift Queue           Floor\n");
        stringBuilder.append("   +====+===============+==========+\n");

        List<Floor> buildingFloors = this.getFloors();
        for (int i = buildingFloors.size() - 1; i >= 0; i--) {
            Floor currentFloor = buildingFloors.get(i);

            StringBuilder queueStringBuilder = new StringBuilder();
            for (BuildingOccupant occupant : currentFloor.getElevatorQueue()) {
                queueStringBuilder.append(getLetterForBuildingOccupant(occupant));
            }

            StringBuilder floorStringBuilder = new StringBuilder();
            for (BuildingOccupant occupant : currentFloor.getOccupants()) {
                if (!currentFloor.getElevatorQueue().contains(occupant)) {
                    floorStringBuilder.append(getLetterForBuildingOccupant(occupant));
                }
            }

            StringBuilder elevatorStringBuilder = new StringBuilder();
            List<Elevator> elevatorsOnFloor = this.getElevatorsOnFloor(currentFloor);
            if (elevatorsOnFloor.size() > 0) {
                for (BuildingOccupant occupant : elevatorsOnFloor.get(0).getPassengers()) {
                    elevatorStringBuilder.append(getLetterForBuildingOccupant(occupant));
                }
            }

            String elevatorPointer = "";
            if (elevatorsOnFloor.size() > 0) {
                elevatorPointer = ">";
            }
            stringBuilder.append(String.format("%1s %1s|%-4s|%-15s|%10s|\n", i, elevatorPointer, elevatorStringBuilder.toString(), queueStringBuilder.toString(), floorStringBuilder.toString()));
        }
        stringBuilder.append("   +====+===============+==========+\n");
        return stringBuilder.toString();
    }

    private String getLetterForBuildingOccupant(BuildingOccupant buildingOccupant) {
        if (buildingOccupant instanceof Client) {
            return "C";
        } else if (buildingOccupant instanceof Developer) {
            return "D";
        } else if (buildingOccupant instanceof Employee) {
            return "E";
        } else if (buildingOccupant instanceof MaintenanceCrew) {
            return "M";
        } else {
            return "?";
        }
    }
}
