package edu.uw.cse.testbayes.runner;

import edu.uw.cse.testbayes.fileio.LogWriter;
import edu.uw.cse.testbayes.utils.LoggerUtils;
import org.junit.*;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * This class is an abstract class for a Runner for an Individual Class that extends the BlockJUnit4ClassRunner
 */
abstract public class IndividualClassRunner extends BlockJUnit4ClassRunner {

    protected Class<?> testClass;
    protected boolean ignore;
    protected Object testObject;
    protected int testsRun;
    protected Instant startTime;
    protected boolean firstFailFound;

    /**
     * Constructs an individual class runner over the methods in klass
     *
     * @param klass The junit class to be tested in a custom order
     * @throws InitializationError Indicates invalid junit test class
     */
    public IndividualClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    /**
     * Runs the before class methods
     *
     * @param ms A list of before class Methods
     */
    public void runSetups(List<Method> ms) {
        for (Method m : ms) {
            try {
                m.invoke(testObject);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * Sorts the methods in the methods list based on annotations
     * @param methods List of methods to be sorted
     * @param befores List for methods with annotation @Before
     * @param afters List for methods with annotation @After
     * @param beforeClasses List for methods with annotation @BeforeClass
     * @param afterClasses List for methods with annotation @AfterClass
     * @param ignores List for methods with annotation @Ignore
     * @param nameToMethod Map from method name to method
     */
    public void sortMethods(ArrayList<Method> methods, ArrayList<Method> befores, ArrayList<Method> afters,
                                ArrayList<Method> beforeClasses, ArrayList<Method> afterClasses, Set<String> ignores,
                                Map<String, Method> nameToMethod) {
        for (int i = 0; i < methods.size(); i++) {
            if (!methods.get(i).isAnnotationPresent(Test.class)) {
                if (methods.get(i).isAnnotationPresent(Before.class)) {
                    befores.add(methods.get(i));
                }
                if (methods.get(i).isAnnotationPresent(After.class)) {
                    afters.add(methods.get(i));
                }
                if (methods.get(i).isAnnotationPresent(BeforeClass.class)) {
                    beforeClasses.add(methods.get(i));
                }
                if (methods.get(i).isAnnotationPresent(AfterClass.class)) {
                    afterClasses.add(methods.get(i));
                }
                methods.remove(i);
                i--;
            } else if (ignore && methods.get(i).isAnnotationPresent(Ignore.class)) {
                nameToMethod.put(methods.get(i).toString(), methods.get(i));
                ignores.add(methods.get(i).toString());
                methods.remove(i);
                i--;
            } else {
                nameToMethod.put(methods.get(i).toString(), methods.get(i));
            }
        }
    }

    /**
     *
     * @param notifier Used to notify JUnit of progress running tests
     * @param method The Method to be run
     * @param befores Before class methods that need to be invoked before method
     * @param afters After class methods that need to be invoked after method
     * @return A boolean indicating if the test passed
     */
    public boolean runMethod(RunNotifier notifier, Method method,
                             List<Method> befores, List<Method> afters) {
        testsRun++;
        runSetups(befores);
        Instant end = null;
        Instant start = null;
        boolean passed = true;
        try {
            notifier.fireTestStarted(Description
                    .createTestDescription(testClass, method.getName()));
            start = Instant.now();
            method.invoke(testObject);
            end = Instant.now();
            notifier.fireTestFinished(Description
                    .createTestDescription(testClass, method.getName()));
        } catch (InvocationTargetException e) {
            Class f = method.getAnnotation(Test.class).expected();
            if (!e.getTargetException().getClass().toString().equals(f.toString())) {
                // Expected wasn't thrown
                notifier.fireTestFailure(
                        new Failure(
                                Description.createTestDescription(testClass, method.getName()),
                                e));
                passed = false;
            } else {
                passed = true;
            }
            notifier.fireTestFinished(Description
                    .createTestDescription(testClass, method.getName()));
            end = Instant.now();
        } catch (IllegalAccessException e) {
            end = Instant.now();
            passed = false;
            LoggerUtils.error("Bad test");
            e.printStackTrace();
        } finally {
            long time = Duration.between(start, end).toMillis();
            if (!passed && !firstFailFound) {
                firstFailFound = true;
                LoggerUtils.info("Test taken until first failure: " + testsRun);
                LoggerUtils.info("Time taken until first failure: " + Duration.between(Instant.now(), startTime));
            }
            try {
                LogWriter.write(method.toString(), (double)(passed ? Math.max(time, 0.1) : Math.min(-time, -0.1)));
            } catch (IOException e) {
                LoggerUtils.error(e);
                e.printStackTrace();
            }
        }
        runSetups(afters);
        return passed;
    }

    /**
     * Shuffles the Method array, ms
     *
     * @param ms The Method array to be shuffled
     * @return A shuffled list of Methods
     */
    public static ArrayList<Method> shuffle(Method[] ms) {
        ArrayList<Method> methods = new ArrayList<>(Arrays.asList(ms));
        Collections.shuffle(methods);
        return methods;
    }

    /**
     * Runs the tests contained in the test class in a specific order based on the use case of the class
     *
     * @param notifier Used to notify JUnit of progress running tests
     */
    @Override
    abstract public void run(RunNotifier notifier);
}