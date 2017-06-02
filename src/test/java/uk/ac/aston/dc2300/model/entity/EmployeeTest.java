package uk.ac.aston.dc2300.model.entity;

import org.junit.Before;
import org.junit.Test;
import uk.ac.aston.dc2300.utility.RandomUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test class for the Employee functions
 *
 * @author Scott Janes
 * @since 01/06/17
 */
public class EmployeeTest {

    private Floor groundFloor;
    private Elevator elevator;
    private Employee employee;
    private Building building;
    private List<Floor> floors;
    private RandomUtils randomUtils;

    /**
     * Setup before each test run for generic test components
     */
    @Before
    public void setup() {
        // Setup floors in building
        floors = new ArrayList<>();
        for(int i = 0; i <= 5; i++){
            floors.add(new Floor(i));
        }

        // Setup ground floor
        groundFloor = floors.get(0);
        // Setup an elevator for tests
        elevator = new Elevator(4, groundFloor);

        // Setup all elevators in building
        List<Elevator> elevators = new ArrayList<>();
        elevators.add(new Elevator(4, floors.get(0)));

        // Setup building
        building = new Building(elevators, floors);

        // Setup employee for tests
        employee = new Employee(0);

        // Setup seed
        randomUtils = new RandomUtils(420);
    }

    /**
     * Test to ensure the employee can call the elevator
     */
    @Test
    public void employeeCanCallElevator() {
        // Employee calls elevator on ground floor
        employee.callElevator(groundFloor);
        // Employee is in elevator queue for the ground floor
        assertTrue(groundFloor.getElevatorQueue().contains(employee));
    }

    /**
     * Test to ensure the employee can enter elevator
     */
    @Test
    public void employeeCanEnterElevator() {
        // Employee enters elevator on ground floor
        employee.getInElevator(elevator, groundFloor);
        // Employee is in elevator
        assertTrue(elevator.getOccupants().contains(employee));
    }

    /**
     * Test to ensure the employee can get a new destination
     */
    @Test
    public void employeeGetsNewDestination() {
        // Add employee crew to the ground floor
        groundFloor.addOccupant(employee);

        // Employee sets a destination
        employee.setNewDestination(building, randomUtils, BigDecimal.ONE, 0);

        // Employee's new destination is floor 1
        assertEquals(employee.getDestination().floorNumber, 1);
        assertEquals(employee.getDestination(), floors.get(1));
    }

    /**
     * Test to ensure the employee can get a new destination other than current floor
     */
    @Test
    public void employeeGetsNewDestinationNotSameFloor() {
        // Add employee to the fourth floor as that is what is returned from getting new destination (see test above)
        floors.get(1).addOccupant(employee);

        // Employee sets a destination
        employee.setNewDestination(building, randomUtils, BigDecimal.ONE, 0);

        // Employee's new destination is floor 3
        assertEquals(employee.getDestination().floorNumber, 3);
        assertEquals(employee.getDestination(), floors.get(3));
    }

    /**
     * Test to ensure the employee doesn't get a new destination as not probable to move
     */
    @Test
    public void employeeDoesntGetNewDestination() {
        // Add employee crew to the ground floor
        groundFloor.addOccupant(employee);
        employee.setDestination(floors.get(1));

        // Employee sets a destination
        employee.setNewDestination(building, new RandomUtils(1), BigDecimal.ZERO, 0);

        // Employee's destination stays the same
        assertEquals(employee.getDestination().floorNumber, 1);
    }

    /**
     * Test to ensure the employee leaves elevator on the correct floor
     */
    @Test
    public void employeeGetsOutOfElevatorOnCorrectFloor() {
        // Set employees destination to floor 1
        employee.setDestination(floors.get(1));
        // Add employee to elevator
        elevator.addOccupant(employee);

        // Elevator is on floor 1 and employee gets out of elevator
        employee.getOutElevatorIfAtDestination(elevator, floors.get(1), 20);
        assertFalse(elevator.getOccupants().contains(employee));
    }

    /**
     * Test to ensure the employee doesn't leave the elevator on the wrong floor
     */
    @Test
    public void employeeDoesntGetOutOfElevatorIfOnWrongFloor() {
        // Set employee destination to floor 1
        employee.setDestination(floors.get(1));
        // Add employee to elevator
        elevator.addOccupant(employee);

        // Elevator is on floor 2 and employee doesn't get out of elevator
        employee.getOutElevatorIfAtDestination(elevator, floors.get(2), 20);
        assertTrue(elevator.getOccupants().contains(employee));
        assertFalse(floors.get(2).getOccupants().contains(employee));
        assertFalse(floors.get(2).getElevatorQueue().contains(employee));
    }
}