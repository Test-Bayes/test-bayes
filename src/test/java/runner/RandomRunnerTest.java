package runner;

import edu.uw.cse.testbayes.runner.RandomIndividualClassRunner;
import edu.uw.cse.testbayes.runner.TestBayesIndividualClassRunner;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import runner.utilTestClasses.Test1;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * This class tests the Test Bayes individual class runner
 */
public class RandomRunnerTest {

    /**
     * Verify that individual class runner throws an exception for null class
     * @throws InitializationError indicates runner class is null
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorThrowsNull() throws InitializationError {
        RandomIndividualClassRunner r = new RandomIndividualClassRunner(null);
    }

    /**
     * verify that only test classes will be run
     * @throws InitializationError indicates invalid test class
     */
    @Test(expected = InitializationError.class)
    public void testConstructorThrowsBadClass() throws InitializationError {
        RandomIndividualClassRunner r = new RandomIndividualClassRunner(TestBayesIndividualClassRunner.class);
    }

    /**
     * verify a proper constructor call to individual class runner
     * @throws InitializationError indicates invalid test class
     */
    @Test
    public void testConstructorValid() throws InitializationError {
        RandomIndividualClassRunner r = new RandomIndividualClassRunner(Test1.class);
    }

    /**
     * verify that the shuffle method can find a new ordering
     */
    @Test(timeout = 5000)
    public void testShuffle() {
        Method[] ms = this.getClass().getDeclaredMethods();
        while (true) {
            ArrayList<Method> shuffleMs = RandomIndividualClassRunner.shuffle(ms);
            for (int i = 0; i < ms.length; i++) {
                if (!ms[i].equals(shuffleMs.get(i))) {
                    return;  // Found good shuffle
                }
            }
        }
    }
}