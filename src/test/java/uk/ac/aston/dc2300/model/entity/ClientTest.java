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
 * Test class for the Elevator functions
 *
 * @author Scott Janes
 * @since 01/06/17
 */
public class ClientTest {


    private Floor groundFloor;
    private Elevator elevator;
    private Client client;

    @Before
    public void setup() {
        // Setup a single floor
        groundFloor = new Floor(0);
        // Setup an elevator for tests
        elevator = new Elevator(4, groundFloor);
        // Setup client for tests
        client = new Client(0, 1200);
    }

    @Test
    public void clientCantComplain() {
        // Client entered queue at 10 seconds
        client.setQueueEnterTime(10);
        // Client complains at 609 (9 minutes 59 seconds after entering queue
        assertFalse(client.wouldLikeToComplain(609));
    }

    @Test
    public void clientCanComplain() {
        // Client entered queue at 10 seconds
        client.setQueueEnterTime(10);
        // Client complains at 610 (10 minutes after entering queue
        assertTrue(client.wouldLikeToComplain(610));
    }

    @Test
    public void clientCanCallElevator() {
        // Client calls elevator on ground floor
        client.callElevator(groundFloor);
        // Client is in elevator queue for the ground floor
        assertTrue(groundFloor.getElevatorQueue().contains(client));
    }

    @Test
    public void clientCanEnterElevator() {
        // Client enters elevator on ground floor
        client.getInElevator(elevator, groundFloor);
        // Client is in elevator
        assertTrue(elevator.getOccupants().contains(client));
    }

    @Test
    public void clientIsReadyToLeave() {
        // Client is ready to leave the building
        client.getReadyToLeave(groundFloor);
        // Client's destination is the ground floor
        assertEquals(client.getDestination(), groundFloor);
    }

    @Test
    public void clientMovesToFloor() {
        //Setup building for test
        List<Floor> floors = new ArrayList<>();
        for(int i = 0; i <= 5; i++){
            floors.add(new Floor(i));
        }
        Set<Elevator> elevators = new HashSet<>();
        elevators.add(new Elevator(4, floors.get(0)));
        Building building = new Building(elevators, floors);

        //Setup random utils for test
        RandomUtils randomUtils = new RandomUtils(420);

        floors.get(0).addOccupant(client);

        client.setNewDestination(building, randomUtils, BigDecimal.ONE, 0);
        System.out.println(client.getDestination().floorNumber);
    }
}