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
    private Developer developer;
    private Employee employee;
    private int currentFloor;


    /*
    Setup before each test ran for a basic elevator scenario
     */
    @Before
    public void setUp() throws Exception {
        floorList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            floorList.add(new Floor(i));
        }
        employee = new Employee();
        employee.setDestination(floorList.get(2));
        floorList.get(1).addToFrontOfQueue(employee);
        developer = new Developer();
        developer.setDestination(floorList.get(5));
        floorList.get(4).addToFrontOfQueue(developer);
        elevator = new Elevator(4, floorList.get(0));

        currentFloor = 0;
    }

    /*
    This test checks that the elevator can move in an upwards direction whilst picking up and dropping passengers off.
     */
    @Test
    public void moveIfRequested() throws Exception {
        assertEquals(elevator.getOccupants().size(), 0);
        //Tick 1 - move to floor 1
        currentFloor = 1;
        elevator.moveIfRequested(floorList);
        //Tick 2 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),1);
        //Tick 3 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 4 = elevator door open and add person to elevator
        elevator.updateElevatorStatus();
        elevator.loadPassengers();
        assertEquals(elevator.getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 5 = person added to elevator
        elevator.updateElevatorStatus();
        //Tick 6 = elevator doors closing
        elevator.updateElevatorStatus();
        //Tick 7 = elevator doors closed
        elevator.updateElevatorStatus();
        //Tick 8 = move to floor 2
        currentFloor = 2;
        elevator.moveIfRequested(floorList);
        //Tick 9 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 10 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 11 = elevator door open and unload person from elevator
        elevator.updateElevatorStatus();
        elevator.unloadPassengers();
        //Tick 12 = person removed from elevator
        elevator.updateElevatorStatus();
        assertEquals(elevator.getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 13 = elevator doors closing
        elevator.updateElevatorStatus();
        //Tick 14 = elevator doors closed
        elevator.updateElevatorStatus();
        //Tick 15 = move to floor 3
        currentFloor = 3;
        elevator.moveIfRequested(floorList);
        //Tick 16 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 17 = move to floor 4
        currentFloor = 4;
        elevator.moveIfRequested(floorList);
        //Tick 18 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),1);
        //Tick 19 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 20 = elevator door open and add person to elevator
        elevator.updateElevatorStatus();
        elevator.loadPassengers();
        assertEquals(elevator.getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 21 = person added to elevator
        elevator.updateElevatorStatus();
        //Tick 22 = elevator doors closing
        elevator.updateElevatorStatus();
        //Tick 23 = elevator doors closed
        elevator.updateElevatorStatus();
        //Tick 24 = move to floor 5
        currentFloor = 5;
        elevator.moveIfRequested(floorList);
        //Tick 24 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 25 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 26 = elevator doors open and unload person from elevator
        elevator.updateElevatorStatus();
        elevator.unloadPassengers();
        //Tick 27 = person removed from elevator
        elevator.updateElevatorStatus();
        assertEquals(elevator.getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
    }

    /*
   This test checks that the elevator pickup multiple passengers.
    */
    @Test
    public void pickUpMultiplePassengers() {
        Employee employee2 = new Employee();
        employee2.setDestination(floorList.get(2));
        floorList.get(1).addToBackOfQueue(employee2);

        //Tick 1 - move to floor 1
        currentFloor = 1;
        elevator.moveIfRequested(floorList);
        //Tick 2 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(elevator.getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),2);
        //Tick 3 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 4 = elevator door open and add persons to elevator
        elevator.updateElevatorStatus();
        elevator.loadPassengers();
        //Tick 5 = persons added to elevator
        elevator.updateElevatorStatus();
        assertEquals(elevator.getOccupants().size(), 2);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
    }

    /*
   This test checks that the elevator unload multiple passengers.
    */
    @Test
    public void unloadMultiplePassengers() {
        floorList.get(1).removeFromQueue(employee);
        Employee employee2 = new Employee();
        employee2.setDestination(floorList.get(2));
        elevator.addOccupant(employee);
        elevator.addOccupant(employee2);

        assertEquals(elevator.getOccupants().size(), 2);
        //Tick 1 - move to floor 1
        currentFloor = 1;
        elevator.moveIfRequested(floorList);
        //Tick 2 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        //Tick 3 = move to floor 2
        currentFloor = 2;
        elevator.moveIfRequested(floorList);
        //Tick 4 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 5 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 6 = elevator doors open and unload person from elevator
        elevator.updateElevatorStatus();
        elevator.unloadPassengers();
        //Tick 7 = person removed from elevator
        elevator.updateElevatorStatus();
        assertEquals(elevator.getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 2);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(), 0);
    }

    /*
   This test checks that the elevator carries on in the current direction until no more passengers are needed picking up or dropping off in the original direction.
    */
    @Test
    public void elevatorGoesInDirectionOfFirstPassenger(){
        floorList.get(1).getElevatorQueue().clear();
        Employee employee2 = new Employee();
        employee2.setDestination(floorList.get(3));
        floorList.get(1).addToBackOfQueue(employee2);
        floorList.get(4).removeFromQueue(developer);

        employee.setDestination(floorList.get(1));
        floorList.get(2).addToBackOfQueue(employee);

        assertEquals(elevator.getOccupants().size(), 0);
        //Tick 1 - move to floor 1
        currentFloor = 1;
        elevator.moveIfRequested(floorList);
        //Tick 2 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),1);
        //Tick 3 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 4 = elevator door open and add person to elevator
        elevator.updateElevatorStatus();
        elevator.loadPassengers();
        assertEquals(elevator.getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 5 = person added to elevator
        elevator.updateElevatorStatus();
        //Tick 6 = elevator doors closing
        elevator.updateElevatorStatus();
        //Tick 7 = elevator doors closed
        elevator.updateElevatorStatus();
        //Tick 8 = move to floor 2
        currentFloor = 2;
        elevator.moveIfRequested(floorList);
        //Tick 9 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),1);
        //Tick 10 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 11 = elevator door open and add person to elevator
        elevator.updateElevatorStatus();
        elevator.loadPassengers();
        assertEquals(elevator.getOccupants().size(), 2);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 12 = person added to elevator
        elevator.updateElevatorStatus();
        //Tick 13 = elevator doors closing
        elevator.updateElevatorStatus();
        //Tick 14 = elevator doors closed
        elevator.updateElevatorStatus();
        //Tick 15 = move to floor 3
        currentFloor = 3;
        elevator.moveIfRequested(floorList);
        //Tick 16 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(elevator.getOccupants().size(), 2);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 17 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 18 = elevator doors open and unload person from elevator
        elevator.updateElevatorStatus();
        elevator.unloadPassengers();
        //Tick 19 = person removed from elevator
        elevator.updateElevatorStatus();
        assertEquals(elevator.getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 20 = elevator doors closing
        elevator.updateElevatorStatus();
        //Tick 21 = elevator doors closed
        elevator.updateElevatorStatus();
        //Tick 22 = move to floor 2
        currentFloor = 2;
        elevator.moveIfRequested(floorList);
        //Tick 23 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(elevator.getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 24 = move to floor 1
        currentFloor = 1;
        elevator.moveIfRequested(floorList);
        //Tick 25 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(elevator.getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 26 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 27 = elevator doors open and unload person from elevator
        elevator.updateElevatorStatus();
        elevator.unloadPassengers();
        //Tick 28 = person removed from elevator
        elevator.updateElevatorStatus();
        assertEquals(elevator.getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
    }

    /*
   This test checks that the elevator is full and can#t pick up more passengers.
    */
    @Test
    public void elevatorFull(){
        Employee employee1 = new Employee();
        employee1.setDestination(floorList.get(2));
        elevator.addOccupant(employee1);
        Employee employee2 = new Employee();
        employee2.setDestination(floorList.get(2));
        elevator.addOccupant(employee2);
        Employee employee3 = new Employee();
        employee3.setDestination(floorList.get(2));
        elevator.addOccupant(employee3);
        Employee employee4 = new Employee();
        employee4.setDestination(floorList.get(2));
        elevator.addOccupant(employee4);

        assertEquals(elevator.getOccupants().size(), 4);
        //Tick 1 - move to floor 1
        currentFloor = 1;
        elevator.moveIfRequested(floorList);
        //Tick 2 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),1);
        //Tick 3 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 4 = elevator door open and add person to elevator
        elevator.updateElevatorStatus();
        elevator.loadPassengers();
        assertEquals(elevator.getOccupants().size(), 4);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),1);
        //Tick 5 = person not added to elevator
        elevator.updateElevatorStatus();
        //Tick 6 = elevator doors closing
        elevator.updateElevatorStatus();
        //Tick 7 = elevator doors closed
        elevator.updateElevatorStatus();
        //Tick 8 = move to floor 2
        currentFloor = 2;
        elevator.moveIfRequested(floorList);
        //Tick 9 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        //Check floor 1 still has person in elevator queue
        assertEquals(floorList.get(1).getElevatorQueue().size(),1);
    }

    /*
   This test checks that the client has priority over all other passenger types
    */
    @Test
    public void clientHasPriority() {
        MaintenanceCrew maintenanceCrew = new MaintenanceCrew(10);
        maintenanceCrew.setDestination(floorList.get(1));
        maintenanceCrew.callElevator(floorList.get(0));
        Client client = new Client(15);
        client.setDestination(floorList.get(3));
        client.callElevator(floorList.get(0));

        assertEquals(elevator.getOccupants().size(), 0);
        //Tick 1 - elevator doors opening
        currentFloor = 0;
        elevator.updateElevatorStatus();
        //Tick 2 = elevator doors open and add person to elevator
        elevator.updateElevatorStatus();
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),2);
        elevator.loadPassengers();
        assertEquals(elevator.getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),1);
        //Check client was given priority and moved to the front of the elevator queue and was the first to enter elevator
        assertTrue(elevator.getOccupants().contains(client));
        assertTrue(floorList.get(currentFloor).getOccupants().contains(maintenanceCrew));
    }

    /*
   This test checks that the elevator can handle all types of building occupants.
    */
    @Test
    public void allBuildingOccupantTypes() {
        MaintenanceCrew maintenanceCrew = new MaintenanceCrew(10);
        maintenanceCrew.setDestination(floorList.get(1));
        maintenanceCrew.callElevator(floorList.get(0));
        Client client = new Client(15);
        client.setDestination(floorList.get(3));
        client.callElevator(floorList.get(0));

        assertEquals(elevator.getOccupants().size(), 0);
        //Tick 1 - elevator doors opening
        currentFloor = 0;
        elevator.updateElevatorStatus();
        //Tick 2 = elevator doors open and add person to elevator
        elevator.updateElevatorStatus();
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),2);
        elevator.loadPassengers();
        assertEquals(elevator.getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),1);
        //Check client was given priority and moved to the front of the elevator queue and was the first to enter elevator
        assertTrue(elevator.getOccupants().contains(client));
        //Tick 5 = person added to elevator
        elevator.updateElevatorStatus();
        //Tick 6 = elevator doors closing
        elevator.updateElevatorStatus();
        //Tick 7 = elevator doors closed
        elevator.updateElevatorStatus();
        //Tick 8 = move to floor 1
        currentFloor = 1;
        elevator.moveIfRequested(floorList);
        //Tick 9 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),1);
        //Tick 10 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 11 = elevator door open and add person to elevator
        elevator.updateElevatorStatus();
        elevator.loadPassengers();
        assertEquals(elevator.getOccupants().size(), 2);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 12 = person added to elevator
        elevator.updateElevatorStatus();
        //Tick 13 = elevator doors closing
        elevator.updateElevatorStatus();
        //Tick 14 = elevator doors closed
        elevator.updateElevatorStatus();
        //Tick 15 = move to floor 2
        currentFloor = 2;
        elevator.moveIfRequested(floorList);
        //Tick 16 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 17 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 18 = elevator door open and unload person from elevator
        elevator.updateElevatorStatus();
        elevator.unloadPassengers();
        //Tick 19 = person removed from elevator
        elevator.updateElevatorStatus();
        assertEquals(elevator.getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 1);
        //Check to make sure the employee was the occupant that was removed from the elevator
        assertTrue(floorList.get(currentFloor).getOccupants().contains(employee));
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 20 = elevator doors closing
        elevator.updateElevatorStatus();
        //Tick 21 = elevator doors closed
        elevator.updateElevatorStatus();
        //Tick 22 = move to floor 3
        currentFloor = 3;
        elevator.moveIfRequested(floorList);
        //Tick 23 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        assertEquals(elevator.getOccupants().size(), 1);
        //Tick 24 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 25 = elevator door open, remove occupant from elevator
        elevator.updateElevatorStatus();
        elevator.unloadPassengers();
        assertEquals(elevator.getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Check to make sure the client was the occupant that was removed from the elevator
        assertTrue(floorList.get(currentFloor).getOccupants().contains(client));
        //Tick 26 = person removed from elevator
        elevator.updateElevatorStatus();
        //Tick 27 = elevator doors closing
        elevator.updateElevatorStatus();
        //Tick 28 = elevator doors closed
        elevator.updateElevatorStatus();
        //Tick 29 = move to floor 4
        currentFloor = 4;
        elevator.moveIfRequested(floorList);
        //Tick 30 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),1);
        //Tick 31 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 32 = elevator door open and add person to elevator
        elevator.updateElevatorStatus();
        elevator.loadPassengers();
        assertEquals(elevator.getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 33 = person added to elevator
        elevator.updateElevatorStatus();
        //Tick 34 = elevator doors closing
        elevator.updateElevatorStatus();
        //Tick 35 = elevator doors closed
        elevator.updateElevatorStatus();
        //Tick 36 = move to floor 5
        currentFloor = 5;
        elevator.moveIfRequested(floorList);
        //Tick 37 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 38 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 39 = elevator doors open and unload person from elevator
        elevator.updateElevatorStatus();
        elevator.unloadPassengers();
        //Tick 40 = person removed from elevator
        elevator.updateElevatorStatus();
        assertEquals(elevator.getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 41 = elevator doors closing
        elevator.updateElevatorStatus();
        //Tick 42 = elevator doors closed
        elevator.updateElevatorStatus();
        //Tick 43 = move to floor 4
        currentFloor = 4;
        elevator.moveIfRequested(floorList);
        //Tick 44 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 45 = move to floor 3
        currentFloor = 3;
        elevator.moveIfRequested(floorList);
        //Tick 46 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 47 = move to floor 2
        currentFloor = 2;
        elevator.moveIfRequested(floorList);
        //Tick 48 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 49 = move to floor 1
        currentFloor = 1;
        elevator.moveIfRequested(floorList);
        //Tick 50 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 51 = move to floor 0
        currentFloor = 0;
        elevator.moveIfRequested(floorList);
        //Tick 52 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        //Tick 53 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 54 = elevator doors open and add maintenance crew to elevator
        elevator.updateElevatorStatus();
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),1);
        elevator.loadPassengers();
        assertEquals(elevator.getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 55 = maintenance crew added to elevator
        elevator.updateElevatorStatus();
        //Tick 56 = elevator doors closing
        elevator.updateElevatorStatus();
        //Tick 57 = elevator doors closed
        elevator.updateElevatorStatus();
        //Tick 58 = move to floor 1
        currentFloor = 1;
        elevator.moveIfRequested(floorList);
        //Tick 59 = change movement status to stationary
        elevator.moveIfRequested(floorList);
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 0);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Tick 60 = elevator doors opening
        elevator.updateElevatorStatus();
        //Tick 61 = elevator door open and unload maintenance crew
        elevator.updateElevatorStatus();
        elevator.unloadPassengers();
        assertEquals(elevator.getCurrentFloor().getFloorNumber(), currentFloor);
        assertEquals(floorList.get(currentFloor).getOccupants().size(), 1);
        assertEquals(floorList.get(currentFloor).getElevatorQueue().size(),0);
        //Check Maintenance crew was the occupant removed from the elevator
        assertTrue(floorList.get(currentFloor).getOccupants().contains(maintenanceCrew));
        assertEquals(elevator.getOccupants().size(), 0);
    }

}