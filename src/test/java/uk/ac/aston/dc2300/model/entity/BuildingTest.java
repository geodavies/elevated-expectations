package uk.ac.aston.dc2300.model.entity;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by dev on 18/04/17.
 */
public class BuildingTest {

    private Building building;

    private Developer developer;

    private Employee employee;

    private int topFloor;



    @Before
    public void setUp() throws Exception {
        List<Floor> floorList = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            floorList.add(new Floor(i));
        }
        Set<Elevator> elevatorSet = new HashSet<>();
        elevatorSet.add(new Elevator(4, floorList.get(0)));
        building = new Building(elevatorSet, floorList);
        developer = new Developer();
        employee = new Employee();
        topFloor = floorList.size() -1;
        floorList.get(0).addOccupant(employee);
        floorList.get(topFloor).addOccupant(developer);
    }

    @Test
    public void getTopHalfFloors() throws Exception {
        List<Floor> newFloorList = building.getTopHalfFloors();
        assertEquals(newFloorList.size(), 3);
    }

    @Test
    public void getBottomHalfFloors() throws Exception {
        List<Floor> newFloorList = building.getBottomHalfFloors();
        assertEquals(newFloorList.size(), 3);
    }

    @Test
    public void getFloorContainingOccupant() throws Exception {
        Floor devFloor = building.getFloorContainingOccupant(developer);
        assertEquals(devFloor.getFloorNumber(), topFloor);
        assertEquals(devFloor.getOccupants().size(), 1);
        assertEquals(devFloor.getElevatorQueue().size(), 0);


        Floor empFloor = building.getFloorContainingOccupant(employee);
        assertEquals(empFloor.getFloorNumber(), 0);
        assertEquals(empFloor.getOccupants().size(), 1);
        assertEquals(empFloor.getElevatorQueue().size(), 0);
    }

}