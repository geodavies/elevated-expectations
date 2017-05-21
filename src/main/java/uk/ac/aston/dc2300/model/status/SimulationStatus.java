package uk.ac.aston.dc2300.model.status;

import uk.ac.aston.dc2300.model.entity.Building;

/**
 * Created by dan on 20/05/2017.
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

    public int getTime() {
        return time;
    }

    public Building getBuilding() {
        return building;
    }

    public boolean isSimulationRunning() {
        return simulationRunning;
    }
}
