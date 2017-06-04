package uk.ac.aston.dc2300.model.entity;

import org.junit.Before;
import org.junit.Test;
import uk.ac.aston.dc2300.model.status.DeveloperCompany;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the Elevator functions
 *
 * @author Scott Janes
 * @since 29/05/17
 */
public class FloorTest {

    private List<Floor> floors;

    /**
     * Setup before each test run for a basic elevator
     */
    @Before
    public void setup() {
        floors = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            floors.add(new Floor(i));
        }
    }

    /**
     * Test to check occupant is added to floor correctly
     */
    @Test
    public void addOccupantToFloor() {
        // Create new employee
        Employee employee = new Employee(0);
        // Get ground floor
        Floor groundFloor = floors.get(0);
        // Floor contains no occupants
        assertEquals(0, groundFloor.getOccupants().size());
        // Add occupant to ground floor
        groundFloor.addOccupant(employee);
        // Floor contains one occupant
        assertEquals(1, groundFloor.getOccupants().size());
    }

    /**
     * Test to check occupant is removed from floor correctly
     */
    @Test
    public void removeOccupantFromFloor() {
        // Create new employee
        Employee employee = new Employee(0);
        // Get ground floor
        Floor groundFloor = floors.get(0);
        // Add occupant to ground floor
        groundFloor.addOccupant(employee);
        // Floor contains one occupant
        assertEquals(1, groundFloor.getOccupants().size());
        // Remove occupant from ground floor
        groundFloor.removeOccupant(employee);
        // Floor contains no occupants
        assertEquals(0, groundFloor.getOccupants().size());
    }

    /**
     * Test to check occupant is added to the back of the elevator queue correctly
     */
    @Test
    public void addOccupantToBackOfQueue() {
        // Create new employee
        Employee employee = new Employee(0);
        // Create new developer
        Developer developer = new Developer(0, DeveloperCompany.GOGGLES);
        // Get ground floor
        Floor groundFloor = floors.get(0);
        // Add employee to elevator queue
        groundFloor.addToBackOfQueue(employee);
        // Elevator queue contains one occupant
        assertEquals(1, groundFloor.getElevatorQueue().size());
        // Elevator queue contains employee
        assertEquals(groundFloor.getElevatorQueue().get(0), employee);
        // Add developer to back of queue behind employee
        groundFloor.addToBackOfQueue(developer);

        List<BuildingOccupant> elevatorQueue = groundFloor.getElevatorQueue();
        // Employee is first in elevator queue
        assertEquals(elevatorQueue.get(0), employee);
        // Developer is last in elevator queue
        assertEquals(elevatorQueue.get(1), developer);
    }

    /**
     * Test to check occupant is added to the front of the elevator queue correctly
     */
    @Test
    public void addOccupantToFrontOfQueue() {
        // Create new employee
        Employee employee = new Employee(0);
        // Create new developer
        Developer developer = new Developer(0, DeveloperCompany.GOGGLES);
        // Get ground floor
        Floor groundFloor = floors.get(0);
        // Add employee to elevator queue
        groundFloor.addToFrontOfQueue(employee);
        // Elevator queue contains one occupant
        assertEquals(1, groundFloor.getElevatorQueue().size());
        // Elevator queue contains employee
        assertEquals(groundFloor.getElevatorQueue().get(0), employee);
        // Add developer to front of queue before employee
        groundFloor.addToFrontOfQueue(developer);

        List<BuildingOccupant> elevatorQueue = groundFloor.getElevatorQueue();
        // Developer is first in elevator queue
        assertEquals(elevatorQueue.get(0), developer);
        // Employee is last in elevator queue
        assertEquals(elevatorQueue.get(1), employee);
    }

    /**
     * Test to check occupant is removed from elevator queue correctly
     */
    @Test
    public void removeOccupantFromElevatorQueue() {
        // Create new employee
        Employee employee = new Employee(0);
        // Get ground floor
        Floor groundFloor = floors.get(0);
        // Add employee to elevator queue
        groundFloor.addToBackOfQueue(employee);
        // Elevator queue contains one occupant
        assertEquals(1, groundFloor.getElevatorQueue().size());
        // Remove occupant from elevator queue
        groundFloor.removeFromQueue(employee);
        // Elevator queue contains no occupants
        assertEquals(0, groundFloor.getElevatorQueue().size());
    }

    /**
     * Test to check occupant is waiting for elevator
     */
    @Test
    public void occupantIsWaitingForElevator() {
        // Create new employee
        Employee employee = new Employee(0);
        // Get ground floor
        Floor groundFloor = floors.get(0);
        // Add employee to elevator queue
        groundFloor.addToBackOfQueue(employee);
        // Occupant is waiting for elevator
        assertTrue(groundFloor.isAnyoneWaiting());
    }

    /**
     * Test to check occupant isn't waiting for elevator
     */
    @Test
    public void occupantIsntWaitingForElevator() {
        // Create new employee
        Employee employee = new Employee(0);
        // Add occupant to floor 1 elevator queue
        floors.get(1).addToFrontOfQueue(employee);
        // Occupant isn't waiting for elevator on floor 0
        assertFalse(floors.get(0).isAnyoneWaiting());
    }

    /**
     * Test to check all occupants are added to elevator queue in correct order
     * Order will be instance of client, employee, developer, maintenance crew
     * Client will be added last
     */
    @Test
    public void allOccupantTypes() {
        // Setup occupants
        Employee employee = new Employee(0);
        Developer developer = new Developer(0, DeveloperCompany.GOGGLES);
        MaintenanceCrew maintenanceCrew = new MaintenanceCrew(0, 1200);
        Client client = new Client(0, 1200);

        Floor groundFloor = floors.get(0);

        // All occupants call elevator
        employee.callElevator(groundFloor, 10);
        developer.callElevator(groundFloor, 10);
        maintenanceCrew.callElevator(groundFloor, 10);
        client.callElevator(groundFloor, 10);

        List<BuildingOccupant> elevatorQueue = floors.get(0).getElevatorQueue();
        assertTrue(elevatorQueue.get(0) instanceof Client);
        assertTrue(elevatorQueue.get(1) instanceof Employee);
        assertTrue(elevatorQueue.get(2) instanceof Developer);
        assertTrue(elevatorQueue.get(3) instanceof MaintenanceCrew);
    }
}
