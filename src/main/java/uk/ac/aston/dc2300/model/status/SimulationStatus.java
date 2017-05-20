package uk.ac.aston.dc2300.model.status;

import uk.ac.aston.dc2300.model.entity.Building;

/**
 * Created by dan on 20/05/2017.
 */
public class SimulationStatus {

    private Building buildingState;

    private int time;

    private boolean isSimulationRunning;

    public SimulationStatus(Building buildingState, int time, boolean isSimulationRunning) {
        this.buildingState = buildingState;
        this.time = time;
        this.isSimulationRunning = isSimulationRunning;
    }

    public int getTime() {
        return time;
    }

    public Building getBuildingState() {
        return buildingState;
    }

    public boolean isSimulationRunning() {
        return isSimulationRunning;
    }
}
