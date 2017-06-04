package uk.ac.aston.dc2300.model.status;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds and processes wait times and number of complaints
 *
 * @author Dan Cotton
 * @since 02/06/17
 */
public class SimulationStatistics {

    private List<Integer> waitTimes;

    private int numberOfComplaints;

    public SimulationStatistics(int numberOfComplaints) {
        this.numberOfComplaints = numberOfComplaints;
        waitTimes = new ArrayList<>();
    }

    /**
     * Adds the given list of wait times to this
     *
     * @param waitTimes the wait times to add
     */
    public void addWaitTimes(List<Integer> waitTimes) {
        this.waitTimes.addAll(waitTimes);
    }

    /**
     * Gets the average waiting time
     *
     * @return average time as an integer
     */
    public int getAverageTime() {
        if (waitTimes.size() > 0) {
            // Stream the collection containing the wait times through a reduce in order to reduce to a sum
            // Once we've got the sum, divide it by the number of entries to return the mean
            return (this.waitTimes
                    .stream()
                    .reduce(0, (sum, time) -> sum + time)) / this.waitTimes.size();
        } else {
            return 0;
        }
    }

    /**
     * Gets the field titles as a comma separated String
     *
     * @return the field titles
     */
    public String getCSVHeaders() {
        return "Average Time (s),Number of complaints";
    }

    /**
     * Converts the current field values into a comma separated String
     *
     * @return the field values
     */
    public String toCSV() {
        return getAverageTime() + "," + getNumberOfComplaints();
    }

    public List<Integer> getWaitTimes() {
        return waitTimes;
    }

    public int getNumberOfComplaints() {
        return this.numberOfComplaints;
    }
}
