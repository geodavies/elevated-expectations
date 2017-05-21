package uk.ac.aston.dc2300.model.entity;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the Elevator functions
 *
 * @author Scott Janes
 * @since 18/04/17
 */

public class ElevatorTest {

    private Elevator elevator;
    private List<Floor> floorList;

    /*
    Setup before each test run for a basic elevator
     */
    @Before
    public void setup() {
        floorList = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            floorList.add(new Floor(i));
        }
        elevator = new Elevator(4, floorList.get(0));
    }

    @Test
    public void elevatorDoesntMoveIfFloorZeroAndNoRequests() {
        assertEquals(elevator.getCurrentFloor(), floorList.get(0));
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor(), floorList.get(0));
    }

    @Test
    public void elevatorMovesUpwardsOnRequest() {
        // Create new occupant for elevator
        Employee employee = new Employee();
        // Set occupant destination to floor 2
        employee.setDestination(floorList.get(2));
        // Add occupant to the floor 0
        floorList.get(0).addOccupant(employee);
        // Add occupant to back of elevator queue
        floorList.get(0).addToBackOfQueue(employee);
        // Ensure current floor is floor 0
        assertEquals(elevator.getCurrentFloor(), floorList.get(0));
        // Elevator should start to open doors
        elevator.updateElevatorStatus();
        // Elevator should finish opening doors
        elevator.updateElevatorStatus();
        // Elevator should load the occupant
        elevator.loadPassengers();
        // Occupant loaded onto elevator
        elevator.updateElevatorStatus();
        // Elevator should start to close doors
        elevator.updateElevatorStatus();
        // Elevator should finish closing doors
        elevator.updateElevatorStatus();
        // Elevator should move up a floor
        elevator.moveIfRequested(floorList);
        // Elevator should be on floor 1
        assertEquals(elevator.getCurrentFloor(), floorList.get(1));
    }

    @Test
    public void elevatorMovesDownwardsOnNoMoreRequests() {
        // Reset the elevator
        elevator = new Elevator(4, floorList.get(1));
        // Ensure current floor is floor 1
        assertEquals(elevator.getCurrentFloor(), floorList.get(1));
        // Check if elevator requested if not go back to floor 0
        elevator.moveIfRequested(floorList);
        // Ensure current floor is floor 0
        assertEquals(elevator.getCurrentFloor(), floorList.get(0));
    }

    @Test
    public void elevatorAcceptsEmployee() {
        // Create new employee
        Employee employee = new Employee();
        // Set employee destination to floor 2
        employee.setDestination(floorList.get(2));
        // Add occupant to the floor 0
        floorList.get(0).addOccupant(employee);
        // Add employee to back of elevator queue
        floorList.get(0).addToBackOfQueue(employee);
        // Elevator should be empty
        assertEquals(0, elevator.getOccupants().size());
        // Elevator queue should contain one person
        assertEquals(1, floorList.get(0).getElevatorQueue().size());
        // Elevator should start to open doors
        elevator.updateElevatorStatus();
        // Elevator should finish opening doors
        elevator.updateElevatorStatus();
        // Elevator should load the employee
        elevator.loadPassengers();
        // Elevator should have one person in
        assertEquals(1, elevator.getOccupants().size());
        // Person should be instance of Employee
        assertTrue(elevator.getOccupants().contains(employee));
        // Queue should be empty
        assertEquals(0, floorList.get(0).getElevatorQueue().size());
    }

    @Test
    public void elevatorAcceptsDeveloper() {
        // Create new developer
        Developer developer = new Developer();
        // Set developer destination to floor 2
        developer.setDestination(floorList.get(2));
        // Add occupant to the floor 0
        floorList.get(0).addOccupant(developer);
        // Add developer to back of elevator queue
        floorList.get(0).addToBackOfQueue(developer);
        // Elevator should be empty
        assertEquals(0, elevator.getOccupants().size());
        // Elevator queue should contain one person
        assertEquals(1, floorList.get(0).getElevatorQueue().size());
        // Elevator should start to open doors
        elevator.updateElevatorStatus();
        // Elevator should finish opening doors
        elevator.updateElevatorStatus();
        // Elevator should load the employee
        elevator.loadPassengers();
        // Elevator should have one person in
        assertEquals(1, elevator.getOccupants().size());
        // Person should be developer
        assertTrue(elevator.getOccupants().contains(developer));
        // Queue should be empty
        assertEquals(0, floorList.get(0).getElevatorQueue().size());
    }

    @Test
    public void elevatorAcceptsClient() {
        // Create new client
        Client client = new Client(0);
        // Set client destination to floor 2
        client.setDestination(floorList.get(2));
        // Add occupant to the floor 0
        floorList.get(0).addOccupant(client);
        // Add client to back of elevator queue
        floorList.get(0).addToBackOfQueue(client);
        // Elevator should be empty
        assertEquals(0, elevator.getOccupants().size());
        // Elevator queue should contain one person
        assertEquals(1, floorList.get(0).getElevatorQueue().size());
        // Elevator should start to open doors
        elevator.updateElevatorStatus();
        // Elevator should finish opening doors
        elevator.updateElevatorStatus();
        // Elevator should load the client
        elevator.loadPassengers();
        // Elevator should have one person in
        assertEquals(1, elevator.getOccupants().size());
        // Person should be client
        assertTrue(elevator.getOccupants().contains(client));
        // Queue should be empty
        assertEquals(0, floorList.get(0).getElevatorQueue().size());
    }

    @Test
    public void elevatorAcceptsMaintenanceCrew() {
        // Create new maintenance crew
        MaintenanceCrew maintenanceCrew = new MaintenanceCrew(0);
        // Set maintenance crew destination to floor 2
        maintenanceCrew.setDestination(floorList.get(2));
        // Add occupant to the floor 0
        floorList.get(0).addOccupant(maintenanceCrew);
        // Add maintenance crew to back of elevator queue
        floorList.get(0).addToBackOfQueue(maintenanceCrew);
        // Elevator should be empty
        assertEquals(0, elevator.getOccupants().size());
        // Elevator queue should contain one person
        assertEquals(1, floorList.get(0).getElevatorQueue().size());
        // Elevator should start to open doors
        elevator.updateElevatorStatus();
        // Elevator should finish opening doors
        elevator.updateElevatorStatus();
        // Elevator should load the maintenance crew
        elevator.loadPassengers();
        // Elevator should have one person in
        assertEquals(1, elevator.getOccupants().size());
        // Person should be maintenance crew
        assertTrue(elevator.getOccupants().contains(maintenanceCrew));
    }

    @Test
    public void checkElevatorMaxCapacity() {
        // Create new maintenance crew
        MaintenanceCrew maintenanceCrew = new MaintenanceCrew(0);
        maintenanceCrew.setDestination(floorList.get(1));
        // Create new employee
        Employee employee = new Employee();
        employee.setDestination(floorList.get(1));
        // Add maintenance crew to queue first and then employee
        floorList.get(0).addToBackOfQueue(maintenanceCrew);
        floorList.get(0).addToBackOfQueue(employee);

        // Elevator should be empty
        assertEquals(0, elevator.getOccupants().size());
        // Elevator queue should contain two people
        assertEquals(2, floorList.get(0).getElevatorQueue().size());
        // Elevator should start to open doors
        elevator.updateElevatorStatus();
        // Elevator should finish opening doors
        elevator.updateElevatorStatus();
        // Elevator should load the maintenance crew
        elevator.loadPassengers();
        // Elevator should have one person in
        assertEquals(1, elevator.getOccupants().size());
        // Person should be maintenance crew
        assertTrue(elevator.getOccupants().contains(maintenanceCrew));
        // Queue should contain the employee due to elevator being full
        assertEquals(1, floorList.get(0).getElevatorQueue().size());
        assertTrue(floorList.get(0).getElevatorQueue().contains(employee));
    }

    @Test
    public void ensureClientHasPriority() {
        // Create new maintenance crew
        MaintenanceCrew maintenanceCrew = new MaintenanceCrew(0);
        maintenanceCrew.setDestination(floorList.get(1));
        // Create new client
        Client client = new Client(0);
        client.setDestination(floorList.get(1));
        // Add maintenance crew to queue first and then client
        floorList.get(0).addToBackOfQueue(maintenanceCrew);
        floorList.get(0).addToBackOfQueue(client);

        // Elevator should be empty
        assertEquals(0, elevator.getOccupants().size());
        // Elevator queue should contain two people
        assertEquals(2, floorList.get(0).getElevatorQueue().size());
        // Elevator should start to open doors
        elevator.updateElevatorStatus();
        // Elevator should finish opening doors
        elevator.updateElevatorStatus();
        // Elevator should load the client due to priority
        elevator.loadPassengers();
        // Elevator should have one person in
        assertEquals(1, elevator.getOccupants().size());
        // Person should be client
        assertTrue(elevator.getOccupants().contains(client));
        // Queue should contain the maintenance crew due to elevator not having enough space
        assertEquals(1, floorList.get(0).getElevatorQueue().size());
        assertTrue(floorList.get(0).getElevatorQueue().contains(maintenanceCrew));
    }
}
