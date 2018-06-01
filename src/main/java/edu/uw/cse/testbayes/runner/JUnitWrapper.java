package edu.uw.cse.testbayes.runner;

import edu.uw.cse.testbayes.utils.LoggerUtils;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.*;


/**
 * This class runs an individual test class in an order determined randomly by extending
 * the IndividualClassRunner
 */
public class JUnitWrapper extends IndividualClassRunner {

    public JUnitWrapper(Class<?> klass) throws InitializationError {
        super(klass);
        this.testClass = klass;
        this.ignore = true;
        this.testObject = null;
        this.testsRun = 0;
        this.startTime = null;
        this.firstFailFound = false;
        try {
            this.testObject = testClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Runs the tests contained in the test class in a random order
     *
     * @param notifier Used to notify JUnit of progress running tests
     */
    @Override
    public void run(RunNotifier notifier) {
        LoggerUtils.info("running the tests from MyRunner: " + testClass);

        ArrayList<Method> methods = new ArrayList<>(Arrays.asList(testClass.getMethods()));
        ArrayList<Method> befores = new ArrayList<>();
        ArrayList<Method> afters = new ArrayList<>();
        ArrayList<Method> beforeClasses = new ArrayList<>();
        ArrayList<Method> afterClasses = new ArrayList<>();
        Set<String> ignores = new HashSet<>();
        Map<String, Method> nameToMethod = new HashMap<>();

        sortMethods(methods, befores, afters, beforeClasses, afterClasses, ignores, nameToMethod);

        // Start of method runs
        startTime = Instant.now();

        // Run the beforeClasses
        runSetups(beforeClasses);

        // Notify ignored tests
        for (String i : ignores) {
            notifier.fireTestIgnored(Description.createTestDescription(testClass,
                    nameToMethod.get(i).getName()));
        }

        // Run already seen methods
        for (int i = 0; i < methods.size(); i++) {
            runMethod(notifier, methods.get(i), befores, afters);
        }

        // Run the afters
        runSetups(afterClasses);
    }
}