package uk.ac.aston.dc2300.model.status;

import java.util.ArrayList;

/**
 * Created by dan on 02/06/2017.
 */
public class SimulationStatistics {

    private ArrayList<Integer> waitTimes;
    private int numberOfComplaints;

    public SimulationStatistics(int numberOfComplaints) {
        this.numberOfComplaints = numberOfComplaints;
        waitTimes = new ArrayList<>();
    }

    public void addWaitTimes(ArrayList<Integer> waitTimes) {
        this.waitTimes.addAll(waitTimes);
    }

    /**
     * Stream the collection containing the wait times through
     * a reduce in order to reduce to a sum.
     *
     * Once we've got the sum, divide it by the number of
     * entries to return the mean
     * @return average time as an integer
     */
    public int getAverageTime() {
        int dataPoints = this.waitTimes.size();
        if (dataPoints > 0) {
            return (this.waitTimes
                    .stream()
                    .reduce(0, (sum, time) -> sum + time)) / this.waitTimes.size();
        } else {
            return 0;
        }
    }

    public int getNumberOfComplaints() {
        return this.numberOfComplaints;
    }

    public String getCSVHeaders() {
        return "Average Time (s),Number of complaints";
    }

    public String toCSV() {
        return getAverageTime() + "," + getNumberOfComplaints();
    }
}
