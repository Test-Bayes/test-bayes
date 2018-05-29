package runner;

import edu.uw.cse.testbayes.fileio.LogWriter;
import edu.uw.cse.testbayes.runner.TestBayesIndividualClassRunner;
import edu.uw.cse.testbayes.utils.BayesFileNameUtils;
import edu.uw.cse.testbayes.utils.LoggerUtils;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runners.model.InitializationError;
import runner.utilTestClasses.Test1;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * This class tests the Test Bayes individual class runner
 */
public class BayesRunnerTest {

    /**
     * Verify that individual class runner throws an exception for null class
     * @throws InitializationError indicates runner class is null
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorThrowsNull() throws InitializationError {
        TestBayesIndividualClassRunner r = new TestBayesIndividualClassRunner(null);
    }

    /**
     * verify that only test classes will be run
     * @throws InitializationError indicates invalid test class
     */
    @Test(expected = InitializationError.class)
    public void testConstructorThrowsBadClass() throws InitializationError {
        TestBayesIndividualClassRunner r = new TestBayesIndividualClassRunner(TestBayesIndividualClassRunner.class);
    }

    /**
     * verify a proper constructor call to individual class runner
     * @throws InitializationError indicates invalid test class
     */
    @Test
    public void testConstructorValid() throws InitializationError {
        TestBayesIndividualClassRunner r = new TestBayesIndividualClassRunner(Test1.class);
    }

    /**
     * verify that the shuffle method can find a new ordering
     */
    @Test(timeout = 5000)
    public void testShuffle() {
        Method[] ms = this.getClass().getDeclaredMethods();
        while (true) {
            ArrayList<Method> shuffleMs = TestBayesIndividualClassRunner.shuffle(ms);
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
    public void testLogsExist() throws InterruptedException {
        LogWriter.forceNewFile();
        long before = new Timestamp(System.currentTimeMillis()).getTime();
        LoggerUtils.info("Time before: " + before);
        JUnitCore junit = new JUnitCore();
        System.setProperty("A1_FAIL_FOR_TEST", "true");
        junit.run(Test1.class);
        long after = new Timestamp(System.currentTimeMillis()).getTime();
        LoggerUtils.info("Time after: " + after);

        File mostRecent = getMostRecentLog();
        String name = mostRecent.getName();
        long fileTimestamp = Long.parseLong(name.split("-")[0]);
        LoggerUtils.info((fileTimestamp - before) + ", " + (after - fileTimestamp));
        assertTrue(before <= fileTimestamp && fileTimestamp <= after);
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
            s.close();
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
        s.close();
    }

    /**
     * Gets the most recently created log file
     * @return A file object representing the most recently created log file
     */
    public File getMostRecentLog() {

        File directory = new File(BayesFileNameUtils.getDirectoryName());
        File[] fileArray = directory.listFiles();

        Map<String, File> fileMap = new TreeMap<String, File>(Collections.reverseOrder());
        if (fileArray == null) {
            return null;
        }
        for (File file : fileArray) {
            if (file.isFile()) {
                fileMap.put(file.toString(), file);
            }
        }

        for(String filename : fileMap.keySet()) {
            return fileMap.get(filename);
        }
        return null;
    }
}