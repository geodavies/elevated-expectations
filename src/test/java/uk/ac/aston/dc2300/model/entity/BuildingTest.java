package uk.ac.aston.dc2300.model.entity;

import org.junit.Before;
import org.junit.Test;
import uk.ac.aston.dc2300.model.status.DeveloperCompany;
import uk.ac.aston.dc2300.utility.RandomUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private List<Elevator> elevators;

    /**
     * Setup before each test run for a basic building
     */
    @Before
    public void setup() throws Exception {
        floors = new ArrayList<>();
        for(int i = 0; i <= TOP_FLOOR; i++){
            floors.add(new Floor(i));
        }
        elevators = new ArrayList<>();
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
        assertEquals(topHalfFloors.get(0).getFloorNumber(), 3);
        assertEquals(topHalfFloors.get(1).getFloorNumber(), 4);
        assertEquals(topHalfFloors.get(2).getFloorNumber(), 5);
    }

    /**
     * Test to ensure the building sets the correct floors for the bottom half of the building
     */
    @Test
    public void getBottomHalfFloors() throws Exception {
        List<Floor> bottomHalfFloors = building.getBottomHalfFloors();
        assertEquals(bottomHalfFloors.size(), 3);
        assertEquals(bottomHalfFloors.get(0).getFloorNumber(), 0);
        assertEquals(bottomHalfFloors.get(1).getFloorNumber(), 1);
        assertEquals(bottomHalfFloors.get(2).getFloorNumber(), 2);
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
        List<BuildingOccupant> allBuildingOccupants = building.getAllOccupants();
        assertEquals(2, allBuildingOccupants.size());
        assertTrue(allBuildingOccupants.contains(employee));
        assertTrue(allBuildingOccupants.contains(developer));
    }

    /**
     * Test to ensure the building knows the number of elevators on each floor
     */
    @Test
    public void numberOfElevatorsOnFloor() {
        elevators.add(new Elevator(10, floors.get(2)));
        elevators.add(new Elevator(10, floors.get(2)));
        assertEquals(1, building.getElevatorsOnFloor(floors.get(0)).size());
        assertEquals(0, building.getElevatorsOnFloor(floors.get(1)).size());
        assertEquals(2, building.getElevatorsOnFloor(floors.get(2)).size());
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
        client1.setNewDestination(building, new RandomUtils(421), BigDecimal.ONE,0);
        client2.setNewDestination(building, new RandomUtils(421), BigDecimal.ONE,0);

        assertEquals(building.getClientComplaints(620), 2);
        // Check for complaints again to ensure clients can't complain twice
        assertEquals(building.getClientComplaints(620), 2);
    }

    /**
     * Test to ensure the building can contain multiple elevators
     */
    @Test
    public void multipleElevators() {
        // Add new elevator to building
        elevators.add(new Elevator(10, floors.get(0)));

        // Building now has two elevators
        assertEquals(building.getElevators().size(), 2);

    }

}
