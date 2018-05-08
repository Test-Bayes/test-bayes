package runner;

import edu.uw.cse.testbayes.Runner.IndividualClassRunner;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import runner.utilTestClasses.Test1;
import org.junit.Test;
import org.junit.runners.model.InitializationError;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

public class IndividualRunnerTests {

    @Test(expected = NullPointerException.class)
    public void testConstructorThrowsNull() throws InitializationError {
        IndividualClassRunner r = new IndividualClassRunner(null);
    }

    @Test(expected = InitializationError.class)
    public void testConstructorThrowsBadClass() throws InitializationError {
        IndividualClassRunner r = new IndividualClassRunner(IndividualClassRunner.class);
    }

    @Test
    public void testConstructorValid() throws InitializationError {
        IndividualClassRunner r = new IndividualClassRunner(Test1.class);
    }

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

    @Test
    // check if a log file was created within 1000ms of a run
    public void testLogsExist() {
        JUnitCore junit = new JUnitCore();
        Result result = junit.run(Test1.class);

        // get the minimum time difference between current time and file timestamp
        File mostRecent = getMostRecentLog();
        long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        String name = mostRecent.getName();
        long fileTimestamp = Long.parseLong(name.split("-")[0]);
        System.out.println(timestamp - fileTimestamp);
        assert(timestamp - fileTimestamp < 1000 ? true : false);
    }

    @Test
    public void testFailedTestHasNegativeRuntime() {
        JUnitCore junit = new JUnitCore();
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
            if (results[0].equals("a1")) {
                assert (Double.parseDouble(results[1]) < 0.0) ? true : false;
            }
        }
    }

    public File getMostRecentLog() {
        File logs = new File("log-data");
        long timestamp = new Timestamp(System.currentTimeMillis()).getTime() ;
        // get the minimum time difference between current time and file timestamp
        long min = Long.MAX_VALUE;
        File result = null;
        for (File log: logs.listFiles()) {
            String name = log.getName();
            long fileTimestamp = Long.parseLong(name.split("-")[0]);
            long oldMin = min;
            min = (timestamp - fileTimestamp) < min ? (timestamp - fileTimestamp): min;
            if (min != oldMin)
                result = log;
        }
        return result;
    }
}