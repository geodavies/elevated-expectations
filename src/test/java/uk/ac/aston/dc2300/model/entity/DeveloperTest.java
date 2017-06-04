package uk.ac.aston.dc2300.model.entity;

import org.junit.Before;
import org.junit.Test;
import uk.ac.aston.dc2300.model.status.DeveloperCompany;
import uk.ac.aston.dc2300.utility.RandomUtils;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Test class for the Developer functions
 *
 * @author Scott Janes
 * @since 01/06/17
 */
public class DeveloperTest {

    private Floor groundFloor;
    private Elevator elevator;
    private Developer developer;
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

        // Setup developer for tests
        developer = new Developer(0, DeveloperCompany.GOGGLES);

        // Setup seed
        randomUtils = new RandomUtils(420);
    }

    /**
     * Test to ensure the developer can call the elevator
     */
    @Test
    public void developerCanCallElevator() {
        // Developer calls elevator on ground floor
        developer.callElevator(groundFloor, 10);
        // Developer is in elevator queue for the ground floor
        assertTrue(groundFloor.getElevatorQueue().contains(developer));
    }

    /**
     * Test to ensure the developer can enter elevator
     */
    @Test
    public void developerCanEnterElevator() {
        // Add developer to ground floor and elevator queue
        groundFloor.addOccupant(developer);
        groundFloor.addToBackOfQueue(developer);
        // Developer enters elevator on ground floor
        developer.getInElevator(elevator, groundFloor, 10);
        // Client is in elevator
        assertTrue(elevator.getOccupants().contains(developer));
        assertEquals(groundFloor.getOccupants().size(), 0);
        assertEquals(groundFloor.getElevatorQueue().size(), 0);
    }

    /**
     * Test to ensure the developer won't enter elevator if a developer from a rival company is present and developer will move to back of the queue
     */
    @Test
    public void developerWontEnterElevator() {
        // Add developer to front of queue
        floors.get(0).addToFrontOfQueue(developer);
        // Add employee to back of queue
        floors.get(0).addToBackOfQueue(new Employee(0));
        // Developer is at front of queue
        LinkedList<BuildingOccupant> elevatorQueue = floors.get(0).getElevatorQueue();
        assertTrue(elevatorQueue.get(0) instanceof Developer);

        // Add developer from rival company to elevator
        Developer rivalDeveloper = new Developer(0, DeveloperCompany.MUGTOME);
        elevator.addOccupant(rivalDeveloper);
        // Developer enters elevator on ground floor
        developer.getInElevator(elevator, groundFloor, 10);
        // Developer isn't in elevator
        assertFalse(elevator.getOccupants().contains(developer));
        //Rival developer is in elevator
        assertTrue(elevator.getOccupants().contains(rivalDeveloper));
        // Developer was moved to back of queue
        elevatorQueue = floors.get(0).getElevatorQueue();
        assertFalse(elevatorQueue.get(0) instanceof Developer);
        assertTrue(elevatorQueue.get(1) instanceof Developer);
    }

    /**
     * Test to ensure the developer can get a new destination
     */
    @Test
    public void developerGetsNewDestination() {
        // Add developer to the ground floor
        groundFloor.addOccupant(developer);

        // Developer sets a destination
        developer.setNewDestination(building, randomUtils, BigDecimal.ONE, 0);

        // Developer's new destination is floor 4
        assertEquals(developer.getDestination().floorNumber, 4);
        assertEquals(developer.getDestination(), floors.get(4));
    }

    /**
     * Test to ensure the developer can get a new destination other than current floor
     */
    @Test
    public void developerGetsNewDestinationNotSameFloor() {
        // Add developer to the fourth floor as that is what is returned from getting new destination (see test above)
        floors.get(4).addOccupant(developer);

        // Developer sets a destination
        developer.setNewDestination(building, randomUtils, BigDecimal.ONE, 0);

        // Developer's new destination is floor 3
        assertEquals(developer.getDestination().floorNumber, 3);
        assertEquals(developer.getDestination(), floors.get(3));
    }

    /**
     * Test to ensure the developer doesn't get a new destination as not probable to move
     */
    @Test
    public void developerDoesntGetNewDestination() {
        // Add developer crew to the ground floor
        groundFloor.addOccupant(developer);
        developer.setDestination(floors.get(1));

        // Developer sets a destination
        developer.setNewDestination(building, new RandomUtils(1), BigDecimal.ZERO, 0);

        // Developer's destination stays the same
        assertEquals(developer.getDestination().floorNumber, 1);
    }

    /**
     * Test to ensure the developer leaves elevator on the correct floor
     */
    @Test
    public void developerGetsOutOfElevatorOnCorrectFloor() {
        // Set developers destination to floor 1
        developer.setDestination(floors.get(1));
        // Add developer to elevator
        elevator.addOccupant(developer);

        // Elevator is on floor 1 and developer gets out of elevator
        developer.getOutElevatorIfAtDestination(elevator, floors.get(1), 20);
        assertFalse(elevator.getOccupants().contains(developer));
    }

    /**
     * Test to ensure the developer doesn't leave the elevator on the wrong floor
     */
    @Test
    public void developerDoesntGetOutOfElevatorIfOnWrongFloor() {
        // Set developer destination to floor 1
        developer.setDestination(floors.get(1));
        // Add developer to elevator
        elevator.addOccupant(developer);

        // Elevator is on floor 2 and developer doesn't get out of elevator
        developer.getOutElevatorIfAtDestination(elevator, floors.get(2), 20);
        assertTrue(elevator.getOccupants().contains(developer));
        assertFalse(floors.get(2).getOccupants().contains(developer));
        assertFalse(floors.get(2).getElevatorQueue().contains(developer));
    }

    /**
     * Test to ensure the developer has the correct time on entering the elevator queue
     */
    @Test
    public void developerSetQueueEntryTime() {
        // Developer calls elevator on ground floor at time 1234
        developer.callElevator(groundFloor, 1234);

        // Developer entered elevator queue at time 1234
        assertEquals(developer.getQueueEntryTime(), 1234);
    }
}