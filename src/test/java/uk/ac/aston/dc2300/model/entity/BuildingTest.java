package uk.ac.aston.dc2300.model.entity;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

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

    /**
     * Setup before each test run for a basic building
     */
    @Before
    public void setup() throws Exception {
        List<Floor> floors = new ArrayList<>();
        for(int i = 0; i <= TOP_FLOOR; i++){
            floors.add(new Floor(i));
        }
        Set<Elevator> elevators = new HashSet<>();
        elevators.add(new Elevator(4, floors.get(0)));
        building = new Building(elevators, floors);
        developer = new Developer();
        employee = new Employee();
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

}