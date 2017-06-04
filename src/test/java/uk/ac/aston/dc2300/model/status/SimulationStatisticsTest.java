package uk.ac.aston.dc2300.model.status;

import org.junit.Before;
import org.junit.Test;
import uk.ac.aston.dc2300.model.entity.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the Simulation Statistics functions
 *
 * @author Scott Janes
 * @since 03/06/17
 */
public class SimulationStatisticsTest {

    private Floor groundFloor;
    private Elevator elevator;
    private List<Floor> floors;
    private SimulationStatistics simulationStatistics;

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

        // Setup simulation statistics
        simulationStatistics = new SimulationStatistics(2);
    }

    /**
     * Test to ensure the simulation statistics return the correct wait times for a single occupant with multiple wait times
     */
    @Test
    public void waitTimesCheckForSingleOccupant() {
        // Setup employee for test
        Employee employee = new Employee(0);
        // Add employee to elevator queue at time 10
        employee.callElevator(groundFloor, 10);
        // Employee enters elevator at time 110, employee wait time is 100
        employee.getInElevator(elevator, groundFloor, 110);

        // Remove employee from elevator
        elevator.removeOccupant(employee);

        // Add employee to elevator queue again at time 210
        employee.callElevator(groundFloor, 210);
        // Employee enters elevator again at time 255, employee wait time is 100, 45
        employee.getInElevator(elevator, groundFloor, 255);

        // Add wait time for employee
        simulationStatistics.addWaitTimes(employee.getWaitTimes());
        //Get list of wait times from statistics
        ArrayList<Integer> waitTimes = simulationStatistics.getWaitTimes();
        int firstWaitTime = waitTimes.get(0);
        int secondWaitTime = waitTimes.get(1);
        // First wait time is for employee which is 100
        assertEquals(firstWaitTime, 100);
        // Second wait time is for employees second time in queue which is 45
        assertEquals(secondWaitTime, 45);
    }

    /**
     * Test to ensure the simulation statistics return the correct wait times for multiple occupants
     */
    @Test
    public void waitTimesCheckForMultipleOccupants() {
        // Setup employee for test
        Employee employee = new Employee(0);
        // Setup developer for test
        Developer developer = new Developer(0, DeveloperCompany.GOGGLES);
        // Add employee to elevator queue at time 10
        employee.callElevator(groundFloor, 10);
        // Employee enters elevator at time 110, employee wait time is 100
        employee.getInElevator(elevator, groundFloor, 110);
        // Add developer to elevator queue at time 210
        developer.callElevator(groundFloor, 210);
        // Developer enters elevator at time 260, developer wait time is 50
        developer.getInElevator(elevator, groundFloor, 260);

        // Add wait time for occupants
        simulationStatistics.addWaitTimes(employee.getWaitTimes());
        simulationStatistics.addWaitTimes(developer.getWaitTimes());
        //Get list of wait times from statistics
        ArrayList<Integer> waitTimes = simulationStatistics.getWaitTimes();
        int firstWaitTime = waitTimes.get(0);
        int secondWaitTime = waitTimes.get(1);
        // First wait time is for employee which is 100
        assertEquals(firstWaitTime, 100);
        // Second wait time is for developer which is 50
        assertEquals(secondWaitTime, 50);
    }

    /**
     * Test to ensure the simulation statistics returns 0 for average wait time if no occupants have waited
     */
    @Test
    public void averageWaitTimesForNoOccupants() {
        assertEquals(simulationStatistics.getAverageTime(), 0);
    }

    /**
     * Test to ensure the simulation statistics return the correct average wait times for multiple occupants
     * Employee wait time 219
     * Developer wait time 584
     * Maintenance crew wait time 128
     * Client wait time 309
     * Average time should equal 1240 / 4 = 310
     */
    @Test
    public void averageWaitTimesForMultipleOccupants() {
        // Setup occupants for test
        Employee employee = new Employee(0);
        Developer developer = new Developer(0, DeveloperCompany.GOGGLES);
        MaintenanceCrew maintenanceCrew = new MaintenanceCrew(0, 600);
        Client client = new Client(0, 600);

        // Setup occupant wait times starting time for test
        employee.callElevator(groundFloor,0);
        developer.callElevator(groundFloor,0);
        maintenanceCrew.callElevator(groundFloor,0);
        client.callElevator(groundFloor,0);

        // Add employee to elevator at time 219 to end wait time
        employee.getInElevator(elevator,groundFloor, 219);
        // Reset elevator
        elevator.removeOccupant(employee);

        // Add developer to elevator at time 584 to end wait time
        developer.getInElevator(elevator,groundFloor, 584);
        // Reset elevator
        elevator.removeOccupant(developer);

        // Add maintenance crew to elevator at time 128 to end wait time
        maintenanceCrew.getInElevator(elevator,groundFloor, 128);
        // Reset elevator
        elevator.removeOccupant(maintenanceCrew);

        // Add client to elevator at time 309 to end wait time
        client.getInElevator(elevator,groundFloor, 309);

        //Add wait times for all occupants
        simulationStatistics.addWaitTimes(employee.getWaitTimes());
        simulationStatistics.addWaitTimes(developer.getWaitTimes());
        simulationStatistics.addWaitTimes(maintenanceCrew.getWaitTimes());
        simulationStatistics.addWaitTimes(client.getWaitTimes());

        //Average wait time is 310
        assertEquals(simulationStatistics.getAverageTime(), 310);
    }

    /**
     * Test to ensure the simulation statistics returns the correct string to CSV creation
     */
    @Test
    public void csvCreationString() {
        // Setup wait time for test
        Employee employee = new Employee(0);
        employee.callElevator(groundFloor,0);
        employee.getInElevator(elevator,groundFloor, 219);
        simulationStatistics.addWaitTimes(employee.getWaitTimes());

        // String for CSV creation equals 219 (average wait time), 2 (number of complaints added in setup)
        assertEquals(simulationStatistics.toCSV(), "219,2");
    }
}