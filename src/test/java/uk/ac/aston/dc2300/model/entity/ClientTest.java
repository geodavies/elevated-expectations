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
 * Test class for the Client functions
 *
 * @author Scott Janes
 * @since 01/06/17
 */
public class ClientTest {

    private Floor groundFloor;
    private Elevator elevator;
    private Client client;
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

        // Setup client for tests
        client = new Client(0, 1200);
    }

    /**
     * Test to ensure the client can't complain if they have been waiting less than 10 minutes
     */
    @Test
    public void clientCantComplain() {
        // Client entered queue at 10 seconds
        client.setQueueEnterTime(10);
        // Client complains at 609 (9 minutes 59 seconds after entering queue
        assertFalse(client.wouldLikeToComplain(609));
    }

    /**
     * Test to ensure the client can complain if they have been waiting 10 minutes or longer
     */
    @Test
    public void clientCanComplain() {
        // Client entered queue at 10 seconds
        client.setQueueEnterTime(10);
        // Client complains at 610 (10 minutes after entering queue
        assertTrue(client.wouldLikeToComplain(610));
    }

    /**
     * Test to ensure the client can call the elevator
     */
    @Test
    public void clientCanCallElevator() {
        // Client calls elevator on ground floor
        client.callElevator(groundFloor);
        // Client is in elevator queue for the ground floor
        assertTrue(groundFloor.getElevatorQueue().contains(client));
    }

    /**
     * Test to ensure the client can enter elevator
     */
    @Test
    public void clientCanEnterElevator() {
        // Client enters elevator on ground floor
        client.getInElevator(elevator, groundFloor);
        // Client is in elevator
        assertTrue(elevator.getOccupants().contains(client));
    }

    /**
     * Test to ensure the client leaves the building
     */
    @Test
    public void clientIsReadyToLeave() {
        // Client is ready to leave the building
        client.getReadyToLeave(groundFloor);
        // Client's destination is the ground floor
        assertEquals(client.getDestination(), groundFloor);
    }

    /**
     * Test to ensure on arrival the client get's a destination of floor 2
     */
    @Test
    public void clientMovesToFloorOnArrivalFloorTwo() {
        // Add client to the ground floor
        groundFloor.addOccupant(client);

        // Client sets a destination
        client.setNewDestination(building, randomUtils, BigDecimal.ONE, 0);

        // Client's new destination is floor 2
        assertEquals(client.getDestination().floorNumber, 2);
        assertEquals(client.getDestination(), floors.get(2));
    }

    /**
     * Test to ensure on arrival the client get's a different destination if seed is different
     */
    @Test
    public void clientMovesToFloorOnArrivalFloorOne() {
        // Add client to the ground floor
        groundFloor.addOccupant(client);

        // Client sets a destination
        client.setNewDestination(building, new RandomUtils(421), BigDecimal.ONE, 0);

        // Client's new destination is floor 2
        assertEquals(client.getDestination().floorNumber, 1);
        assertEquals(client.getDestination(), floors.get(1));
    }

    /**
     * Test to ensure the client leaves the building after leave time expires
     */
    @Test
    public void clientLeavesBuildingAfterTimeExpired() {
        // Add client to the ground floor
        groundFloor.addOccupant(client);
        assertEquals(groundFloor.getOccupants().size(), 1);

        // Set client destination to ground floor
        client.setDestination(groundFloor);

        // Client leaves building due to time expired
        client.setNewDestination(building, randomUtils, BigDecimal.ONE, 1201);

        // Client is no longer in the building
        assertEquals(groundFloor.getOccupants().size(), 0);
    }

    /**
     * Test to ensure the client starts to leave the building after leave time expires by setting destination to ground floor
     */
    @Test
    public void clientSetsDestinationToGroundFloorAfterTimeExpired() {
        // Set client destination to floor 1 and add client to floor 1
        client.setDestination(floors.get(1));
        floors.get(1).addOccupant(client);
        assertTrue(floors.get(1).getOccupants().contains(client));

        // Client leaves building due to time expiring
        client.setNewDestination(building, randomUtils, BigDecimal.ONE, 1201);

        // Client sets destination to ground floor and enters elevator queue on floor 1
        assertEquals(client.getDestination(), groundFloor);
        assertTrue(floors.get(1).getElevatorQueue().contains(client));
    }

    /**
     * Test to ensure the client leaves elevator on the correct floor
     */
    @Test
    public void clientGetsOutOfElevatorOnCorrectFloor() {
        // Set client destination to floor 1
        client.setDestination(floors.get(1));
        // Add client to elevator
        elevator.addOccupant(client);
        assertTrue(elevator.getOccupants().contains(client));

        // Elevator is on floor 1 and client gets out of elevator
        client.getOutElevatorIfAtDestination(elevator, floors.get(1), 20);
        assertFalse(elevator.getOccupants().contains(client));
    }

    /**
     * Test to ensure the client doesn't leave the elevator on the wrong floor
     */
    @Test
    public void clientDoesntGetOutOfElevatorIfOnWrongFloor() {
        // Set client destination to floor 1
        client.setDestination(floors.get(1));
        // Add client to elevator
        elevator.addOccupant(client);
        assertTrue(elevator.getOccupants().contains(client));

        // Elevator is on floor 2 and client doesn't get out of elevator
        client.getOutElevatorIfAtDestination(elevator, floors.get(2), 20);
        assertTrue(elevator.getOccupants().contains(client));
    }
}