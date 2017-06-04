package uk.ac.aston.dc2300.model.entity;

import org.junit.Before;
import org.junit.Test;
import uk.ac.aston.dc2300.model.status.DeveloperCompany;
import uk.ac.aston.dc2300.model.status.ElevatorDoorStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the Elevator functions
 *
 * @author Scott Janes
 * @since 18/04/17
 */

public class ElevatorTest {

    private Elevator elevator;
    private List<Floor> floors;
    private int topFloor;



    /**
     * Setup before each test run for a basic elevator
     */
    @Before
    public void setup() {
        floors = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            floors.add(new Floor(i));
        }
        elevator = new Elevator(4, floors.get(0));
        topFloor = floors.size() -1;
    }

    /**
     * Test to check that elevator doesn't move if no requests are waiting
     */
    @Test
    public void elevatorDoesntMoveIfFloorZeroAndNoRequests() {
        assertEquals(elevator.getCurrentFloor(), floors.get(0));
        elevator.moveIfRequested(floors);
        assertEquals(elevator.getCurrentFloor(), floors.get(0));
    }

    /**
     * Test to check elevator can load a passenger
     */
    @Test
    public void loadPassengers() {
        // Setup occupant for test
        Employee employee = new Employee(0);
        employee.setDestination(floors.get(1));
        floors.get(0).addToFrontOfQueue(employee);

        // Setup elevator for test
        elevator.updateDoorStatus(topFloor);
        elevator.updateDoorStatus(topFloor);

        // Elevator contains no occupants
        assertEquals(0, elevator.getOccupants().size());

        // Elevator loads occupant from elevator queue
        elevator.loadPassengers(topFloor, 10);
        // Elevator contains one occupant
        assertEquals(1, elevator.getOccupants().size());
    }

    /**
     * Test to check elevator can unload a passenger
     */
    @Test
    public void unloadPassenger() {
        // Setup occupant for test
        Employee employee = new Employee(0);
        employee.setDestination(floors.get(0));

        // Setup elevator for test
        elevator.addOccupant(employee);
        elevator.updateDoorStatus(topFloor);
        elevator.updateDoorStatus(topFloor);

        // Elevator contains one occupant
        assertEquals(1, elevator.getOccupants().size());

        // Elevator unloads occupant from elevator
        elevator.unloadPassengers(0);
        // Elevator contains no occupants
        assertEquals(0, elevator.getOccupants().size());
    }

    /**
     * Test to check elevator moves when a request is made
     */
    @Test
    public void elevatorMovesUpwardsOnRequest() {
        // Create new employee
        Employee employee = new Employee(0);
        // Set employee destination to floor 2
        employee.setDestination(floors.get(2));
        // Add employee to floor 0
        floors.get(0).addOccupant(employee);
        // Add employee to back of elevator queue
        floors.get(0).addToBackOfQueue(employee);
        // Ensure current floor is floor 0
        assertEquals(elevator.getCurrentFloor(), floors.get(0));
        // Elevator doors opening
        elevator.updateDoorStatus(topFloor);
        // Elevator doors opened
        elevator.updateDoorStatus(topFloor);
        // Elevator loads occupant from elevator queue
        elevator.loadPassengers(topFloor, 10);
        // Occupant loaded onto elevator
        elevator.updateDoorStatus(topFloor);
        // Elevator doors closing
        elevator.updateDoorStatus(topFloor);
        // Elevator doors closed
        elevator.updateDoorStatus(topFloor);
        // Elevator moves up one floor
        elevator.moveIfRequested(floors);
        // Ensure current floor is floor 1
        assertEquals(elevator.getCurrentFloor(), floors.get(1));
    }

    /**
     * Test to check elevator moves down to ground floor if no requests are waiting
     */
    @Test
    public void elevatorMovesDownwardsOnNoMoreRequests() {
        // Reset the elevator to start on floor 1
        elevator = new Elevator(4, floors.get(1));
        // Ensure current floor is floor 1
        assertEquals(elevator.getCurrentFloor(), floors.get(1));
        // Check if elevator requested if not go back to floor 0
        elevator.moveIfRequested(floors);
        // Ensure current floor is floor 0
        assertEquals(elevator.getCurrentFloor(), floors.get(0));
    }

    /**
     * Test to check that an employee can be added to the elevator
     */
    @Test
    public void elevatorAcceptsEmployee() {
        // Create new employee
        Employee employee = new Employee(0);
        // Set employee destination to floor 2
        employee.setDestination(floors.get(2));
        // Add employee to floor 0
        floors.get(0).addOccupant(employee);
        // Add employee to back of elevator queue
        floors.get(0).addToBackOfQueue(employee);
        // Elevator contains no occupants
        assertEquals(0, elevator.getOccupants().size());
        // Elevator queue contains one occupant
        assertEquals(1, floors.get(0).getElevatorQueue().size());
        // Elevator queue occupant is employee
        assertEquals(employee, floors.get(0).getElevatorQueue().get(0));
        // Elevator doors opening
        elevator.updateDoorStatus(topFloor);
        // Elevator doors opened
        elevator.updateDoorStatus(topFloor);
        // Elevator loads occupant from elevator queue
        elevator.loadPassengers(topFloor, 10);
        // Elevator contains one occupant
        assertEquals(1, elevator.getOccupants().size());
        // Elevator occupant is employee
        assertTrue(elevator.getOccupants().contains(employee));
        // Elevator queue contains no occupants
        assertEquals(0, floors.get(0).getElevatorQueue().size());
    }

    /**
     * Test to check that a client can be added to the elevator
     */
    @Test
    public void elevatorAcceptsDeveloper() {
        // Create new developer
        Developer developer = new Developer(0, DeveloperCompany.GOGGLES);
        // Set developer destination to floor 2
        developer.setDestination(floors.get(2));
        // Add developer to the floor 0
        floors.get(0).addOccupant(developer);
        // Add developer to back of elevator queue
        floors.get(0).addToBackOfQueue(developer);
        // Elevator contains no occupants
        assertEquals(0, elevator.getOccupants().size());
        // Elevator queue contains one occupant
        assertEquals(1, floors.get(0).getElevatorQueue().size());
        // Elevator queue occupant is developer
        assertEquals(developer, floors.get(0).getElevatorQueue().get(0));
        // Elevator doors opening
        elevator.updateDoorStatus(topFloor);
        // Elevator doors opened
        elevator.updateDoorStatus(topFloor);
        // Elevator loads occupant from elevator queue
        elevator.loadPassengers(topFloor, 10);
        // Elevator contains one occupants
        assertEquals(1, elevator.getOccupants().size());
        // Elevator occupant is developer
        assertTrue(elevator.getOccupants().contains(developer));
        // Elevator queue contains no occupants
        assertEquals(0, floors.get(0).getElevatorQueue().size());
    }

    /**
     * Test to check that a client can be added to the elevator
     */
    @Test
    public void elevatorAcceptsClient() {
        // Create new client
        Client client = new Client(0, 10);
        // Set client destination to floor 2
        client.setDestination(floors.get(2));
        // Add client to the floor 0
        floors.get(0).addOccupant(client);
        // Add client to back of elevator queue
        floors.get(0).addToBackOfQueue(client);
        // Elevator contains no occupants
        assertEquals(0, elevator.getOccupants().size());
        // Elevator queue contains one occupant
        assertEquals(1, floors.get(0).getElevatorQueue().size());
        // Elevator queue occupant is client
        assertEquals(client, floors.get(0).getElevatorQueue().get(0));
        // Elevator doors opening
        elevator.updateDoorStatus(topFloor);
        // Elevator doors opened
        elevator.updateDoorStatus(topFloor);
        // Elevator loads occupant from elevator queue
        elevator.loadPassengers(topFloor, 10);
        // Elevator contains one occupant
        assertEquals(1, elevator.getOccupants().size());
        // Elevator occupant is client
        assertTrue(elevator.getOccupants().contains(client));
        // Elevator queue contains no occupants
        assertEquals(0, floors.get(0).getElevatorQueue().size());
    }

    /**
     * Test to check that a maintenance crew can be added to the elevator
     */
    @Test
    public void elevatorAcceptsMaintenanceCrew() {
        // Create new maintenance crew
        MaintenanceCrew maintenanceCrew = new MaintenanceCrew(0, 10);
        // Set maintenance crew destination to floor 2
        maintenanceCrew.setDestination(floors.get(2));
        // Add maintenance crew to the floor 0
        floors.get(0).addOccupant(maintenanceCrew);
        // Add maintenance crew to back of elevator queue
        floors.get(0).addToBackOfQueue(maintenanceCrew);
        // Elevator contains no occupants
        assertEquals(0, elevator.getOccupants().size());
        // Elevator queue contains one occupant
        assertEquals(1, floors.get(0).getElevatorQueue().size());
        // Elevator queue occupant is maintenance crew
        assertEquals(maintenanceCrew, floors.get(0).getElevatorQueue().get(0));
        // Elevator doors opening
        elevator.updateDoorStatus(topFloor);
        // Elevator doors opened
        elevator.updateDoorStatus(topFloor);
        // Elevator loads occupant from elevator queue
        elevator.loadPassengers(topFloor, 10);
        // Elevator contains one occupant
        assertEquals(1, elevator.getOccupants().size());
        // Elevator occupant is maintenance crew
        assertTrue(elevator.getOccupants().contains(maintenanceCrew));
    }


    /**
     * Test to check that no more occupants can be added to elevator when elevator is at max capacity
     */
    @Test
    public void checkElevatorMaxCapacity() {
        // Create new maintenance crew
        MaintenanceCrew maintenanceCrew = new MaintenanceCrew(0, 10);
        maintenanceCrew.setDestination(floors.get(1));
        // Create new employee
        Employee employee = new Employee(0);
        employee.setDestination(floors.get(1));
        // Add maintenance crew to queue first and then employee
        floors.get(0).addToBackOfQueue(maintenanceCrew);
        floors.get(0).addToBackOfQueue(employee);

        // Elevator contains no occupants
        assertEquals(0, elevator.getOccupants().size());
        // Elevator queue contains two occupants
        assertEquals(2, floors.get(0).getElevatorQueue().size());
        // Elevator doors opening
        elevator.updateDoorStatus(topFloor);
        // Elevator doors opened
        elevator.updateDoorStatus(topFloor);
        // Elevator loads occupant from elevator queue
        elevator.loadPassengers(topFloor, 10);
        // Elevator contains one occupant
        assertEquals(1, elevator.getOccupants().size());
        // Elevator occupant is maintenance crew
        assertTrue(elevator.getOccupants().contains(maintenanceCrew));
        // Elevator queue contains employee due to elevator being full
        assertEquals(1, floors.get(0).getElevatorQueue().size());
        assertTrue(floors.get(0).getElevatorQueue().contains(employee));
    }

    /**
     * Test to check that a client has priority over other building occupants to move to front of queue
     */
    @Test
    public void ensureClientHasPriority() {
        // Create new maintenance crew
        MaintenanceCrew maintenanceCrew = new MaintenanceCrew(0, 10);
        maintenanceCrew.setDestination(floors.get(1));
        // Create new client
        Client client = new Client(0, 10);
        client.setDestination(floors.get(1));
        // Add maintenance crew to queue first and then client
        floors.get(0).addOccupant(maintenanceCrew);
        floors.get(0).addOccupant(client);
        maintenanceCrew.callElevator(floors.get(0), 10);
        client.callElevator(floors.get(0), 10);

        // Elevator contains no occupants
        assertEquals(0, elevator.getOccupants().size());
        // Elevator queue contains two occupants
        assertEquals(2, floors.get(0).getElevatorQueue().size());
        // Elevator doors opening
        elevator.updateDoorStatus(topFloor);
        // Elevator doors opened
        elevator.updateDoorStatus(topFloor);
        // Elevator loads client from elevator queue due to priority
        elevator.loadPassengers(topFloor, 10);
        // Elevator contains one occupant
        assertEquals(1, elevator.getOccupants().size());
        // Elevator occupant is client
        assertTrue(elevator.getOccupants().contains(client));
        // Elevator queue contains maintenance crew due to elevator not having enough space
        assertEquals(1, floors.get(0).getElevatorQueue().size());
        assertTrue(floors.get(0).getElevatorQueue().contains(maintenanceCrew));
    }

    /**
     * Test to check that developers from different companies don't enter the same lift
     */
    @Test
    public void developersFromDifferentCompaniesTest() {
        // Setup developers for test
        Developer gogglesDeveloper = new Developer(0, DeveloperCompany.GOGGLES);
        gogglesDeveloper.setDestination(floors.get(1));
        floors.get(0).addToFrontOfQueue(gogglesDeveloper);

        Developer mugtomeDeveloper = new Developer(0, DeveloperCompany.MUGTOME);
        mugtomeDeveloper.setDestination(floors.get(1));
        floors.get(0).addToBackOfQueue(mugtomeDeveloper);

        // Elevator contains no occupants
        assertEquals(0, elevator.getOccupants().size());
        // Elevator queue contains two occupants
        assertEquals(2, floors.get(0).getElevatorQueue().size());
        // Elevator doors opening
        elevator.updateDoorStatus(topFloor);
        // Elevator doors opened
        elevator.updateDoorStatus(topFloor);
        // Elevator loads gogglesDeveloper from elevator queue
        elevator.loadPassengers(topFloor, 10);
        // Elevator contains one occupant
        assertEquals(1, elevator.getOccupants().size());
        // Elevator occupant is gogglesDeveloper
        assertTrue(elevator.getOccupants().contains(gogglesDeveloper));
        // Elevator queue contains mugtomeDeveloper due to being developers from rival companies
        assertEquals(1, floors.get(0).getElevatorQueue().size());
        assertTrue(floors.get(0).getElevatorQueue().contains(mugtomeDeveloper));
    }

    /**
     * Test to check that the elevator doors show the right status
     */
    @Test
    public void elevatorDoorTest() {
        // Create new employee
        Employee employee = new Employee(0);
        // Set employee destination to floor 2
        employee.setDestination(floors.get(2));
        // Add employee to back of elevator queue
        floors.get(0).addToBackOfQueue(employee);
        // Elevator doors are closed
        assertEquals(elevator.getDoorStatus(), ElevatorDoorStatus.CLOSED);
        // Elevator doors opening
        elevator.updateDoorStatus(topFloor);
        assertEquals(elevator.getDoorStatus(), ElevatorDoorStatus.OPENING);
        // Elevator doors opened
        elevator.updateDoorStatus(topFloor);
        assertEquals(elevator.getDoorStatus(), ElevatorDoorStatus.OPEN);
        // Elevator loads occupant from elevator queue
        elevator.loadPassengers(topFloor, 10);
        // Occupant loaded onto elevator
        elevator.updateDoorStatus(topFloor);
        assertEquals(elevator.getDoorStatus(), ElevatorDoorStatus.OPEN);
        // Elevator doors closing
        elevator.updateDoorStatus(topFloor);
        assertEquals(elevator.getDoorStatus(), ElevatorDoorStatus.CLOSING);
        // Elevator doors closed
        elevator.updateDoorStatus(topFloor);
        assertEquals(elevator.getDoorStatus(), ElevatorDoorStatus.CLOSED);
    }

    /**
     * Test to ensure the maintenance crew can't enter elevator if elevator capacity is to small
     */
    @Test
    public void maintenanceCrewCantEnterElevatorIfCapacityToSmall() {
        // Setup maintenance crew for test
        MaintenanceCrew maintenanceCrew = new MaintenanceCrew(0, 1200);
        // Create elevator that is too small for maintenance crew
        Elevator smallElevator = new Elevator(3, floors.get(0));

        // Add maintenance crew to elevator queue
        floors.get(0).addToFrontOfQueue(maintenanceCrew);

        // Setup elevator for test
        smallElevator.updateDoorStatus(5);
        smallElevator.updateDoorStatus(5);

        // Elevator tries to load maintenance crew from elevator queue
        elevator.loadPassengers(5, 10);
        // Elevator doesn't contain maintenance crew
        assertFalse(smallElevator.getOccupants().contains(maintenanceCrew));
    }
}
