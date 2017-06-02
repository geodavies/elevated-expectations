package uk.ac.aston.dc2300.model.status;

import java.util.ArrayList;

/**
 * Created by dan on 02/06/2017.
 */
public class SimulationStatistics {

    private ArrayList<Integer> waitTimes;

    public SimulationStatistics() {
        waitTimes = new ArrayList<>();
    }

    public void addWaitTimes(ArrayList<Integer> waitTimes) {
        this.waitTimes.addAll(waitTimes);
    }

    public ArrayList<Integer> getWaitTimes() {
        return this.waitTimes;
    }

    public int getAverageTime() {
        return (this.waitTimes
                .stream()
                .reduce(0, (sum, time) -> sum + time)) / this.waitTimes.size();
    }

}
