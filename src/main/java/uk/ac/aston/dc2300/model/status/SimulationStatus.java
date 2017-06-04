package uk.ac.aston.dc2300.model.status;

import uk.ac.aston.dc2300.model.entity.Building;

/**
 * Contains information about a single simulation tick
 *
 * @author Dan Cotton
 * @since 20/05/17
 */
public class SimulationStatus {

    private Building building;

    private int time;

    private boolean simulationRunning;

    public SimulationStatus(Building building, int time, boolean simulationRunning) {
        this.building = building;
        this.time = time;
        this.simulationRunning = simulationRunning;
    }

    public Building getBuilding() {
        return building;
    }

    public int getTime() {
        return time;
    }

    public boolean isSimulationRunning() {
        return simulationRunning;
    }
}
