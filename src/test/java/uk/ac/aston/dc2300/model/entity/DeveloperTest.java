package uk.ac.aston.dc2300.model.entity;

import org.junit.Before;
import org.junit.Test;
import uk.ac.aston.dc2300.model.status.DeveloperCompany;
import uk.ac.aston.dc2300.utility.RandomUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the Client functions
 *
 * @author Scott Janes
 * @since 01/06/17
 */
public class DeveloperTest {

    private Floor groundFloor;
    private Elevator elevator;
    private Developer developer;
    private Set<Elevator> elevators;
    private Building building;
    private List<Floor> floors;

    // Set seed to be the same for multiple tests
    private static final RandomUtils randomUtils = new RandomUtils(420);

    /**
     * Setup before each test run for a generic test components
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
        elevators = new HashSet<>();
        elevators.add(new Elevator(4, floors.get(0)));

        // Setup building
        building = new Building(elevators, floors);

        // Setup developer for tests
        developer = new Developer(0, DeveloperCompany.GOGGLES);
    }

    /**
     * Test to ensure the developer can call the elevator
     */
    @Test
    public void developerCanCallElevator() {
        // Developer calls elevator on ground floor
        developer.callElevator(groundFloor);
        // Developer is in elevator queue for the ground floor
        assertTrue(groundFloor.getElevatorQueue().contains(developer));
    }

    /**
     * Test to ensure the developer can enter elevator
     */
    @Test
    public void developerCanEnterElevator() {
        // Developer enters elevator on ground floor
        developer.getInElevator(elevator, groundFloor);
        // Developer is in elevator
        assertTrue(elevator.getOccupants().contains(developer));
    }

    /**
     * Test to ensure the developer won't enter elevator if a developer from a rival company is present
     */
    @Test
    public void developerWontEnterElevator() {
        // Add developer from rival company to elevator
        Developer rivalDeveloper = new Developer(0, DeveloperCompany.MUGTOME);
        elevator.addOccupant(rivalDeveloper);
        // Developer enters elevator on ground floor
        developer.getInElevator(elevator, groundFloor);
        // Developer isn't in elevator
        assertFalse(elevator.getOccupants().contains(developer));
        //Rival developer is in elevator
        assertTrue(elevator.getOccupants().contains(rivalDeveloper));
    }

    /**
     * Test to ensure the developer can get a new destination
     */
    @Test
    public void developerGetsNewDestination() {
        // Add developer crew to the ground floor
        groundFloor.addOccupant(developer);

        // Developer sets a destination
        developer.setNewDestination(building, randomUtils, BigDecimal.ONE, 0);

        // Developer's new destination is floor 2
        assertEquals(developer.getDestination().floorNumber, 4);
        assertEquals(developer.getDestination(), floors.get(4));
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
    public void setDeveloperGetsOutOfElevatorOnCorrectFloor() {
        // Set developers destination to floor 1
        developer.setDestination(floors.get(1));
        // Add developer to elevator
        elevator.addOccupant(developer);
        assertTrue(elevator.getOccupants().contains(developer));

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
        assertTrue(elevator.getOccupants().contains(developer));

        // Elevator is on floor 2 and developer doesn't get out of elevator
        developer.getOutElevatorIfAtDestination(elevator, floors.get(2), 20);
        assertTrue(elevator.getOccupants().contains(developer));
    }
}