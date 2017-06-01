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
 * Test class for the Maintenance Crew functions
 *
 * @author Scott Janes
 * @since 01/06/17
 */
public class MaintenanceCrewTest {

    private Floor groundFloor;
    private Elevator elevator;
    private MaintenanceCrew maintenanceCrew;
    private Set<Elevator> elevators;
    private Building building;
    private List<Floor> floors;

    // Set seed to be the same for multiple tests
    private static final RandomUtils randomUtils = new RandomUtils(420);

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
        elevators = new HashSet<>();
        elevators.add(new Elevator(4, floors.get(0)));

        // Setup building
        building = new Building(elevators, floors);

        // Setup maintenance crew for tests
        maintenanceCrew = new MaintenanceCrew(0, 1200);
    }

    /**
     * Test to ensure the maintenance crew can call the elevator
     */
    @Test
    public void maintenanceCrewCanCallElevator() {
        // Maintenance crew calls elevator on ground floor
        maintenanceCrew.callElevator(groundFloor);
        // Maintenance crew is in elevator queue for the ground floor
        assertTrue(groundFloor.getElevatorQueue().contains(maintenanceCrew));
    }

    /**
     * Test to ensure the maintenance crew can enter elevator
     */
    @Test
    public void maintenanceCrewCanEnterElevator() {
        // Maintenance crew enters elevator on ground floor
        maintenanceCrew.getInElevator(elevator, groundFloor);
        // Maintenance crew is in elevator
        assertTrue(elevator.getOccupants().contains(maintenanceCrew));
    }

    /**
     * Test to ensure on arrival the maintenance crew get's a destination of the top floor
     */
    @Test
    public void maintenanceCrewMovesToTopFloorOnArrival() {
        // Add maintenance crew to the ground floor
        groundFloor.addOccupant(maintenanceCrew);

        // Maintenance crew sets a destination
        maintenanceCrew.setNewDestination(building, randomUtils, BigDecimal.ONE, 0);

        // Maintenance crew's new destination is floor 2
        assertEquals(maintenanceCrew.getDestination().floorNumber, 5);
        assertEquals(maintenanceCrew.getDestination(), floors.get(5));
    }

    /**
     * Test to ensure the maintenance crew leaves the building after leave time expires
     */
    @Test
    public void maintenanceCrewLeavesBuildingAfterTimeExpired() {
        // Add maintenance crew to the ground floor
        groundFloor.addOccupant(maintenanceCrew);
        assertEquals(groundFloor.getOccupants().size(), 1);

        // Set maintenance crew destination to ground floor
        maintenanceCrew.setDestination(groundFloor);

        // Maintenance crew leaves building due to time expired
        maintenanceCrew.setNewDestination(building, randomUtils, BigDecimal.ONE, 1201);

        // Maintenance crew is no longer in the building
        assertEquals(groundFloor.getOccupants().size(), 0);
    }

    /**
     * Test to ensure the maintenance crew starts to leave the building after leave time expires by setting destination to ground floor
     */
    @Test
    public void maintenanceCrewSetsDestinationToGroundFloorAfterTimeExpired() {
        // Set maintenance crew destination to floor 1 and add maintenance crew to floor 1
        maintenanceCrew.setDestination(floors.get(1));
        floors.get(1).addOccupant(maintenanceCrew);
        assertTrue(floors.get(1).getOccupants().contains(maintenanceCrew));

        // Maintenance crew leaves building due to time expiring
        maintenanceCrew.setNewDestination(building, randomUtils, BigDecimal.ONE, 1201);

        // Maintenance crew sets destination to ground floor and enters elevator queue on floor 1
        assertEquals(maintenanceCrew.getDestination(), groundFloor);
        assertTrue(floors.get(1).getElevatorQueue().contains(maintenanceCrew));
    }

    /**
     * Test to ensure the maintenance crew leaves elevator on the correct floor
     */
    @Test
    public void maintenanceCrewGetsOutOfElevatorOnCorrectFloor() {
        // Set maintenance crew destination to floor 1
        maintenanceCrew.setDestination(floors.get(1));
        // Add Maintenance crew to elevator
        elevator.addOccupant(maintenanceCrew);
        assertTrue(elevator.getOccupants().contains(maintenanceCrew));

        // Elevator is on floor 1 and maintenance crew gets out of elevator
        maintenanceCrew.getOutElevatorIfAtDestination(elevator, floors.get(1), 20);
        assertFalse(elevator.getOccupants().contains(maintenanceCrew));
    }

    /**
     * Test to ensure the maintenance crew doesn't leave the elevator on the wrong floor
     */
    @Test
    public void maintenanceCrewDoesntGetOutOfElevatorIfOnWrongFloor() {
        // Set maintenance crew destination to floor 1
        maintenanceCrew.setDestination(floors.get(1));
        // Add maintenance crew to elevator
        elevator.addOccupant(maintenanceCrew);
        assertTrue(elevator.getOccupants().contains(maintenanceCrew));

        // Elevator is on floor 2 and maintenance crew doesn't get out of elevator
        maintenanceCrew.getOutElevatorIfAtDestination(elevator, floors.get(2), 20);
        assertTrue(elevator.getOccupants().contains(maintenanceCrew));
    }
}