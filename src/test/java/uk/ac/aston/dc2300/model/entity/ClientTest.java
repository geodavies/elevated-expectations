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
        Set<Elevator> elevators = new HashSet<>();
        elevators.add(new Elevator(4, floors.get(0)));

        // Setup building
        building = new Building(elevators, floors);

        // Setup client for tests
        client = new Client(0, 1200);

        // Setup seed
        randomUtils = new RandomUtils(420);
    }

    /**
     * Test to ensure the client can't complain if they have been waiting less than 10 minutes
     */
    @Test
    public void clientCantComplain() {
        // Setup for test
        groundFloor.addOccupant(client);
        // Client entered queue at 10 seconds
        client.setNewDestination(building, randomUtils, BigDecimal.ONE,0);
        // Client complains at 599 (9 minutes 59 seconds after entering queue)
        assertFalse(client.wouldLikeToComplain(599));
    }

    /**
     * Test to ensure the client can complain if they have been waiting 10 minutes or longer
     */
    @Test
    public void clientCanComplain() {
        // Setup for test
        groundFloor.addOccupant(client);
        // Client entered queue at 10 seconds
        client.setNewDestination(building, randomUtils, BigDecimal.ONE,0);
        // Client complains at 600 (10 minutes after entering queue)
        assertTrue(client.wouldLikeToComplain(600));
    }

    /**
     * Test to ensure the client can call the elevator
     */
    @Test
    public void clientCanCallElevator() {
        // Client calls elevator on ground floor
        client.callElevator(groundFloor, 10);
        // Client is in elevator queue for the ground floor
        assertTrue(groundFloor.getElevatorQueue().contains(client));
    }

    /**
     * Test to ensure the client can enter elevator
     */
    @Test
    public void clientCanEnterElevator() {
        // Add client to ground floor and elevator queue
        groundFloor.addOccupant(client);
        groundFloor.addToBackOfQueue(client);
        // Client enters elevator on ground floor
        client.getInElevator(elevator, groundFloor, 10);
        // Client is in elevator
        assertTrue(elevator.getOccupants().contains(client));
        assertEquals(groundFloor.getOccupants().size(), 0);
        assertEquals(groundFloor.getElevatorQueue().size(), 0);
    }

    /**
     * Test to ensure the client leaves the building
     */
    @Test
    public void clientIsReadyToLeave() {
        // Client is ready to leave the building
        client.getReadyToLeave(groundFloor, 1200);
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

        // Elevator is on floor 2 and client doesn't get out of elevator
        client.getOutElevatorIfAtDestination(elevator, floors.get(2), 20);
        assertTrue(elevator.getOccupants().contains(client));
        assertFalse(floors.get(2).getOccupants().contains(client));
        assertFalse(floors.get(2).getElevatorQueue().contains(client));
    }

    /**
     * Test to ensure the client has the correct time on entering the elevator queue
     */
    @Test
    public void clientSetQueueEntryTime() {
        // Client calls elevator on ground floor at time 1234
        client.callElevator(groundFloor, 1234);

        // Client entered elevator queue at time 1234
        assertEquals(client.getQueueEntryTime(), 1234);
    }
}