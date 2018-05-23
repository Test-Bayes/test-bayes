package edu.uw.cse.testbayes.fileio;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Map;

/**
 * Stores the data of a single Log File
 */
public class LogData {

    /**
     * The data stored with each test's method signature being the key and it's runtime being the double. The sign of
     * the double signifies whether the test failed or passed
     */
    private Map<String, Double> data;

    /**
     * Whether or not this test was a complete run
     */
    private boolean complete;

    /**
     * Creates a new LogData object with no data
     */
    public LogData() {
        this(null, false);
    }

    /**
     * Creates a new LogData object with the given data and completeness state
     *
     * @param data data from the log file
     * @param complete completeness of the test run that this log corresponds to
     */
    public LogData(Map<String, Double> data, boolean complete) {
        this.data = data;
        this.complete = complete;
    }

    /**
     * Gets the data of the log file
     *
     * @return Map with each test's method signature being the key and it's runtime being the double. The sign of
     * the double signifies whether the test failed or passed
     */
    public Map<String, Double> getData() {
        return data;
    }

    /**
     * Sets the data of the log file
     *
     * @param data Map with each test's method signature being the key and it's runtime being the double. The sign of
     * the double signifies whether the test failed or passed
     */
    public void setData(Map<String, Double> data) {
        this.data = data;
    }

    /**
     * Checks whether this run was a complete run
     *
     * @return true if and only if the test is complete. False otherwise
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Sets whether this run was a complete run
     *
     * @param complete true if and only if the test is complete. False otherwise
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /**
     * Verifies if the LogData is equal to the given/other Object
     *
     * @param o Other Object against which equality is verified
     * @return
     *          true if the given Object represents a LogData equivalent to this LogData;
     *          false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogData logData = (LogData) o;
        return complete == logData.complete &&
                Objects.equal(data, logData.data);
    }

    /**
     * Calculates the Hash Code for the given LogData\
     *
     * @return Returns the calculated Hash Code
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(data, complete);
    }

    /**
     * Returns a String representation of the object
     * @return String showing the data and the completiton state of the log
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("data", data)
                .add("complete", complete)
                .toString();
    }
}
