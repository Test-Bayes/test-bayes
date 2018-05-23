package edu.uw.cse.testbayes.runner;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.*;

import static java.lang.System.exit;

/**
 * This class runs an individual test class in an order determined by the Bayes model by extending
 * the IndividualClassRunner
 */
public class RandomIndividualClassRunner extends IndividualClassRunner {

    public RandomIndividualClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.testClass = klass;
        this.ignore = true;
        this.testObject = null;
        this.testsRun = 0;
        this.startTime = null;
        this.firstFailFound = true;
        try {
            this.testObject = testClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            exit(1);
        } catch (InstantiationException e) {
            e.printStackTrace();
            exit(1);
        }
    }

    /**
     * Runs the tests contained in the test class in a random order
     *
     * @param notifier Used to notify JUnit of progress running tests
     */
    @Override
    public void run(RunNotifier notifier) {
        System.out.println("running the tests from MyRunner: " + testClass);

        ArrayList<Method> methods = shuffle(testClass.getMethods());
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