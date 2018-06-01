package edu.uw.cse.testbayes.runner;

import edu.uw.cse.testbayes.fileio.LogReader;
import edu.uw.cse.testbayes.fileio.LogWriter;
import edu.uw.cse.testbayes.model.Bayes;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.*;

import static java.lang.System.exit;

/**
 * This class runs an individual test class in an order determined by the Bayes model by extending
 * the IndividualClassRunner
 */
public class TestBayesIndividualClassRunner extends IndividualClassRunner {

    final static Logger LOGGER = Logger.getLogger(TestBayesIndividualClassRunner.class);

    /**
     * Constructs an individual class runner over the methods in klass
     *
     * @param klass The junit class to be tested in a custom order
     * @throws InitializationError Indicates invalid junit test class
     */
    public TestBayesIndividualClassRunner(Class<?> klass) throws InitializationError {
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
            exit(1);
        } catch (InstantiationException e) {
            e.printStackTrace();
            exit(1);
        }
    }

    /**
     * Returns the first method to be run out of ms according the the Bayes model
     *
     * @param ms The list of potential methods to be run, as strings
     * @param b The bayes probability model
     * @param nameMap A map from method names as Strings, to Methods
     * @return The best method to be run out of ms, according to the model, b
     */
    private Method getFirstMethod(List<String> ms, Bayes b, Map<String, Method> nameMap) {
        double best = 0;
        Method bestM = null;
        for (String m : ms) {
            Method currM = nameMap.get(m);
            if (bestM == null || b.getTestProb(currM.toString()).doubleValue() < best) {
                bestM = currM;
                best = b.getTestProb(currM.toString()).doubleValue();
            }
        }
        return bestM;
    }

    /**
     * Runs the tests contained in the test class in an order determined by the bayes model
     *
     * @param notifier Used to notify JUnit of progress running tests
     */
    @Override
    public void run(RunNotifier notifier) {
        LOGGER.info("running the tests from MyRunner: " + testClass);
        // Get the past map
        Map<String, Map<String, Double>> oldRuns = null;
        try {
            oldRuns = LogReader.read();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            exit(1);
        }

        ArrayList<Method> methods = shuffle(testClass.getMethods());
        ArrayList<Method> befores = new ArrayList<>();
        ArrayList<Method> afters = new ArrayList<>();
        ArrayList<Method> beforeClasses = new ArrayList<>();
        ArrayList<Method> afterClasses = new ArrayList<>();
        Set<String> ignores = new HashSet<>();
        Map<String, Method> nameToMethod = new HashMap<>();
        sortMethods(methods, befores, afters, beforeClasses, afterClasses, ignores, nameToMethod);

        // Create the bayes module
        Bayes bayes = new Bayes(oldRuns, methods);

        // Separate new methods from old ones
        Set<String> temp = new HashSet<>(nameToMethod.keySet());
        temp.removeAll(bayes.getProb().keySet());
        List<String> newMs = new ArrayList<>(temp);
        temp = new HashSet<>(nameToMethod.keySet());
        temp.removeAll(newMs);
        List<String> oldMs = new ArrayList<>(temp);
        oldMs.removeAll(ignores);
        newMs.removeAll(ignores);

        // Start of method runs
        startTime = Instant.now();

        // Run the beforeClasses
        runSetups(beforeClasses);

        // Notify ignored tests
        for (String i : ignores) {
            notifier.fireTestIgnored(Description.createTestDescription(testClass,
                    nameToMethod.get(i).getName()));
        }

        // Run new methods
        for (int i = 0; i < newMs.size(); i++) {
            runMethod(notifier, nameToMethod.get(newMs.get(i)),
                    befores, afters);
        }

        // Run already seen methods
        boolean passed = false;
        Method method = null;
        for (int i = newMs.size(); i < methods.size(); i++) {
            if (i == newMs.size()) {
                method = getFirstMethod(oldMs, bayes, nameToMethod);
            } else {
                String newS = bayes.nextTest(method.toString(), passed, new HashSet<>(ignores));
                method = nameToMethod.get(newS);
            }
            passed = runMethod(notifier, method,
                    befores, afters);
        }

        // Run the afters
        runSetups(afterClasses);

        // Mark log as complete
        try {
            markComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Marks the run as complete
     * @throws IOException in case of any IO issues
     */
    private void markComplete() throws IOException {
        LogWriter.completeRun();
    }
}