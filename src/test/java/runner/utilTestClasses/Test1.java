package runner.utilTestClasses;

import edu.uw.cse.testbayes.runner.TestBayesIndividualClassRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Simple test class that runs tests that take time to complete,
 * along with one that can parametrically fail.
 */
@RunWith(TestBayesIndividualClassRunner.class)
public class Test1 {
    /**
     * Determines if test1 should fail (for debugging)
     */
    private boolean a1Fail =
            System.getProperty("A1_FAIL_FOR_TEST") != null ?
                    Boolean.valueOf(System.getProperty("A1_FAIL_FOR_TEST")) :
                    false;

    /**
     * Simple test that asserts false if specified to by a1Fail
     */
    @Ignore
    @Test
    public void a1() throws InterruptedException {
        wasteTime();
        System.out.println(a1Fail);
        if (a1Fail) {
            System.getProperties().remove("A1_FAIL_FOR_TEST");
            assert (false);
        }
        return;
    }

    /**
     * Simple test that wastes time and passes
     */
    @Test
    public void b1() throws InterruptedException {
        wasteTime();
        return;
    }

    /**
     * Simple test that wastes time and passes
     */
    @Test
    public void c1() throws InterruptedException {
        wasteTime();
        return;
    }

    /**
     * Simple test that wastes time and passes
     */
    @Test
    public void d1() throws InterruptedException {
        wasteTime();
        return;
    }

    /**
     * Helper method that wastes 1 second of time, plus random pointless calculations
     */
    private double wasteTime() throws InterruptedException {
        double i = 1;
        Thread.sleep(1000);
        for (int j = 0; j < Integer.MAX_VALUE; j++) {
            i = Math.sqrt(Math.sqrt(j));
        }
        return i;
    }
}
