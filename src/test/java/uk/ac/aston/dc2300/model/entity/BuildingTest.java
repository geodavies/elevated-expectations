package uk.ac.aston.dc2300.model.entity;

import org.junit.Before;
import org.junit.Test;
import uk.ac.aston.dc2300.model.status.DeveloperCompany;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the Building functions
 *
 * @author Scott Janes
 * @since 18/04/17
 */
public class BuildingTest {

    private Building building;

    private Developer developer;

    private Employee employee;

    private static int TOP_FLOOR = 5;

    private List<Floor> floors;

    private Set<Elevator> elevators;

    /**
     * Setup before each test run for a basic building
     */
    @Before
    public void setup() throws Exception {
        floors = new ArrayList<>();
        for(int i = 0; i <= TOP_FLOOR; i++){
            floors.add(new Floor(i));
        }
        elevators = new HashSet<>();
        elevators.add(new Elevator(4, floors.get(0)));
        building = new Building(elevators, floors);
        developer = new Developer(0, DeveloperCompany.GOGGLES);
        employee = new Employee(0);
        floors.get(0).addOccupant(employee);
        floors.get(TOP_FLOOR).addOccupant(developer);
    }

    /**
     * Test to ensure the building sets the correct floors for the top half of the building
     */
    @Test
    public void getTopHalfFloors() throws Exception {
        List<Floor> topHalfFloors = building.getTopHalfFloors();
        assertEquals(topHalfFloors.size(), 3);
        assertEquals(topHalfFloors.get(0).floorNumber, 3);
        assertEquals(topHalfFloors.get(1).floorNumber, 4);
        assertEquals(topHalfFloors.get(2).floorNumber, 5);
    }

    /**
     * Test to ensure the building sets the correct floors for the bottom half of the building
     */
    @Test
    public void getBottomHalfFloors() throws Exception {
        List<Floor> bottomHalfFloors = building.getBottomHalfFloors();
        assertEquals(bottomHalfFloors.size(), 3);
        assertEquals(bottomHalfFloors.get(0).floorNumber, 0);
        assertEquals(bottomHalfFloors.get(1).floorNumber, 1);
        assertEquals(bottomHalfFloors.get(2).floorNumber, 2);
    }

    /**
     * Test to ensure the building states the correct floor for building occupant
     */
    @Test
    public void getFloorContainingOccupant() throws Exception {
        Floor devFloor = building.getFloorContainingOccupant(developer);
        assertEquals(devFloor.getFloorNumber(), TOP_FLOOR);
        assertEquals(devFloor.getOccupants().size(), 1);
        assertEquals(devFloor.getElevatorQueue().size(), 0);


        Floor empFloor = building.getFloorContainingOccupant(employee);
        assertEquals(empFloor.getFloorNumber(), 0);
        assertEquals(empFloor.getOccupants().size(), 1);
        assertEquals(empFloor.getElevatorQueue().size(), 0);
    }

    /**
     * Test to ensure the building gets all the current building occupants
     */
    @Test
    public void getAllOccupants() {
        Set<BuildingOccupant> allBuildingOccupants = building.getAllOccupants();
        assertEquals(2, allBuildingOccupants.size());
        assertTrue(allBuildingOccupants.contains(employee));
        assertTrue(allBuildingOccupants.contains(developer));
    }

    /**
     * Test to ensure the building knows which floor the elevator is on
     */
    @Test
    public void elevatorOnFloorOnGroundFloor() {
        List<Elevator> elevatorsOnGroundFloor = building.getElevatorsOnFloor(floors.get(0));
        assertEquals(1, elevatorsOnGroundFloor.size());
    }

    /**
     * Test to ensure the building knows how many complaints are being made
     */
    @Test
    public void numberOfComplaints() {
        // Building currently has no complaints
        assertEquals(building.getNumberComplaints(), 0);

        // Add two clients that will complain
        Client client1 = new Client(0, 1200);
        Client client2 = new Client(0, 1200);
        floors.get(0).addOccupant(client1);
        floors.get(0).addOccupant(client2);
        floors.get(0).addToFrontOfQueue(client1);
        floors.get(0).addToFrontOfQueue(client2);
        client1.setQueueEnterTime(0);
        client2.setQueueEnterTime(0);

        assertEquals(building.getClientComplaints(620), 2);
    }

    /**
     * Test to ensure the building knows which floor the elevator is on
     */
    @Test
    public void multipleElevators() {
        // Add new elevator to building
        elevators.add(new Elevator(10, floors.get(0)));

        // Building now has two elevators
        assertEquals(building.getElevators().size(), 2);
    }

}