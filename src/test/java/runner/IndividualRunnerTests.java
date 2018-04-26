package java.runner;

import edu.uw.cse.testbayes.Runner.IndividualClassRunner;
import edu.uw.cse.testbayes.Runner.Test1;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import sun.plugin.dom.exception.InvalidStateException;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class IndividualRunnerTests {

    @Test(expected = InitializationError.class)
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
    public void testShuffle() throws InitializationError {
        Method[] ms = this.getClass().getDeclaredMethods();
        ArrayList<Method> shuffleMs = IndividualClassRunner.shuffle(ms);
        ArrayList<>
    }
}