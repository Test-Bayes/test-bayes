package runner;

import edu.uw.cse.testbayes.runner.IndividualClassRunner;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runners.model.InitializationError;
import runner.utilTestClasses.Test1;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Authors: Steven Austin, Ethan Mayer
 * This class tests the individual class runner
 */
public class IndividualRunnerTests {

    /**
     * Verify that individual class runner throws an exception for null class
     * @throws InitializationError indicates runner class is null
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorThrowsNull() throws InitializationError {
        IndividualClassRunner r = new IndividualClassRunner(null);
    }

    /**
     * verify that only test classes will be run
     * @throws InitializationError indicates invalid test class
     */
    @Test(expected = InitializationError.class)
    public void testConstructorThrowsBadClass() throws InitializationError {
        IndividualClassRunner r = new IndividualClassRunner(IndividualClassRunner.class);
    }

    /**
     * verify a proper constructor call to individual class runner
     * @throws InitializationError indicates invalid test class
     */
    @Test
    public void testConstructorValid() throws InitializationError {
        IndividualClassRunner r = new IndividualClassRunner(Test1.class);
    }

    /**
     * verify that the shuffle method can find a new ordering
     */
    @Test(timeout = 5000)
    public void testShuffle() {
        Method[] ms = this.getClass().getDeclaredMethods();
        while (true) {
            ArrayList<Method> shuffleMs = IndividualClassRunner.shuffle(ms);
            for (int i = 0; i < ms.length; i++) {
                if (!ms[i].equals(shuffleMs.get(i))) {
                    return;  // Found good shuffle
                }
            }
        }
    }

    /**
     * verify that a log file was created within 5000ms of a test run
     */
    @Test
    public void testLogsExist() {
        JUnitCore junit = new JUnitCore();
        System.setProperty("A1_FAIL_FOR_TEST", "true");
        Result result = junit.run(Test1.class);

        // get the minimum time difference between current time and file timestamp
        File mostRecent = getMostRecentLog();
        long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        String name = mostRecent.getName();
        long fileTimestamp = Long.parseLong(name.split("-")[0]);
        System.out.println(timestamp - fileTimestamp);
        assert(timestamp - fileTimestamp < 5000 ? true : false);
    }

    /**
     * verify that failing tests have negative runtime to indicate failure
     */
    @Test
    public void testFailedTestHasNegativeRuntime() {
        JUnitCore junit = new JUnitCore();
        System.setProperty("A1_FAIL_FOR_TEST", "true");
        Result result = junit.run(Test1.class);
        Scanner s = null;
        try {
            s = new Scanner(getMostRecentLog());
        } catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }

        String[] contents = s.nextLine().split(" ");
        for (String test: contents) {
            String[] results = test.split(",");
            if (results[0].equals("public void runner.utilTestClasses.Test1.a1() throws java.lang.InterruptedException")) {
                assert (Double.parseDouble(results[1]) < 0.0) ? true : false;
            }
        }
    }

    /**
     * Gets the most recently created log file
     * @return A file object representing the most recently created log file
     */
    public File getMostRecentLog() {
        File logs = new File("log-data");
        long timestamp = new Timestamp(System.currentTimeMillis()).getTime() ;
        // get the minimum time difference between current time and file timestamp
        long min = Long.MAX_VALUE;
        File result = null;
        for (File log: logs.listFiles()) {
            if (log.isFile()) {
                String name = log.getName();
                long fileTimestamp = Long.parseLong(name.split("-")[0]);
                long oldMin = min;
                min = (timestamp - fileTimestamp) < min ? (timestamp - fileTimestamp) : min;
                if (min != oldMin)
                    result = log;
            }
        }
        System.out.println("Result: " + result);
        return result;
    }
}