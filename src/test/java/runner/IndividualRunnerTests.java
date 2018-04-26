package runner;

import edu.uw.cse.testbayes.Runner.IndividualClassRunner;
import runner.utilTestClasses.Test1;
import org.junit.Test;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.Method;
import java.util.ArrayList;

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
}