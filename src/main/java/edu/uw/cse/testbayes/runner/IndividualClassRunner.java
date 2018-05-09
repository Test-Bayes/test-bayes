package edu.uw.cse.testbayes.runner;

import edu.uw.cse.testbayes.fileio.TestLogReader;
import edu.uw.cse.testbayes.model.Bayes;
import edu.uw.cse.testbayes.model.Probability;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import edu.uw.cse.testbayes.fileio.TestLogWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class IndividualClassRunner extends BlockJUnit4ClassRunner {
    private Class<?> testClass;
    private boolean ignore;
    private Object testObject;

    public IndividualClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.testClass = klass;
        this.ignore = true;
        this.testObject = null;
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
//
//    @Override
//    protected Statement methodInvoker(FrameworkMethod method, Object test) {
//        System.out.println("invoking: " + method.toString());
//        Statement result = super.methodInvoker(method, test);
//        System.out.println(result.toString());
//        try {
//            result.evaluate();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
////        System.out.println(result.);
//        return result;*
//    }

    public static ArrayList<Method> shuffle(Method[] ms) {
        ArrayList<Method> methods = new ArrayList<Method>(Arrays.asList(ms));
        Collections.shuffle(methods);
        return methods;
    }

    private Method getFirstMethod(List<String> ms, Bayes b, Map<String, Method> nameMap) {
        double best = 0;
        Method bestM = null;
        for (String m : ms) {
            Method currM = nameMap.get(m);
            if (bestM == null || b.getTestProb(currM.toString()).doubleValue() > best) {
                bestM = currM;
                best = b.getTestProb(currM.toString()).doubleValue();
            }
        }
        return bestM;
    }

    @Override
    public void run(RunNotifier notifier) {
        System.out.println("running the tests from MyRunner: " + testClass);

        // Get the past map
        Map<String, Map<String, Double>> oldRuns = null;
        try {
            oldRuns = TestLogReader.read();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            exit(1);
        }


        ArrayList<Method> methods = shuffle(testClass.getMethods());
        Set<String> ignores = new HashSet<>();
        Map<String, Method> nameToMethod = new HashMap<String, Method>();
        for (int i = 0; i < methods.size(); i++) {
            if (!methods.get(i).isAnnotationPresent(Test.class)) {
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

        // Create the bayes module
        Bayes bay = new Bayes(oldRuns, methods);

        // Separate new methods from old ones
        Set<String> temp = new HashSet<>(nameToMethod.keySet());
        temp.removeAll(bay.getProb().keySet());
        List<String> newMs = new ArrayList<>(temp);
        temp = new HashSet<>(nameToMethod.keySet());
        temp.removeAll(newMs);
        List<String> oldMs = new ArrayList<>(temp);
        oldMs.removeAll(ignores);
        newMs.removeAll(ignores);

        System.out.println("Olds: " + oldMs.toString());
        System.out.println("News: " + newMs.toString());


        // Notify ignored tests
        for (String i : ignores) {
            notifier.fireTestIgnored(Description.createTestDescription(testClass,
                    nameToMethod.get(i).getName()));
        }

        // Run new methods
        for (int i = 0; i < newMs.size(); i++) {
            runMethod(notifier, nameToMethod.get(newMs.get(i)));
        }

        // Run already seen methods
        boolean passed = false;
        Method method = null;
        for (int i = newMs.size(); i < methods.size(); i++) {
            if (i == newMs.size()) {
                method = getFirstMethod(oldMs, bay, nameToMethod);
            } else {
                String newS = bay.nextTest(method.toString(), passed, new HashSet<>(ignores));
                method = nameToMethod.get(newS);
            }
            passed = runMethod(notifier, method);
        }

        // TODO: Call this in the loop, write one by one
//        TestLogWriter outputWriter = new TestLogWriter();
//        try {
//            outputWriter.write(fileOutput);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }

    public boolean runMethod(RunNotifier notifier, Method method) {
        Instant end = null;
        Instant start = null;
        boolean passed = true;
        System.out.println("Running test " + method.toString());
        try {
            notifier.fireTestStarted(Description
                    .createTestDescription(testClass, method.getName()));
            start = Instant.now();
            method.invoke(testObject);
            end = Instant.now();
            notifier.fireTestFinished(Description
                    .createTestDescription(testClass, method.getName()));
        } catch (InvocationTargetException e) {
            notifier.fireTestFailure(
                    new Failure(
                            Description.createTestDescription(testClass, method.getName()),
                            e));
            notifier.fireTestFinished(Description
                    .createTestDescription(testClass, method.getName()));
            end = Instant.now();
            passed = false;
        } catch (IllegalAccessException e) {
            end = Instant.now();
            passed = false;
            System.out.println("Illegal test");
            e.printStackTrace();
        } finally {
            long time = Duration.between(start, end).toMillis();
            try {
                TestLogWriter.write(method.toString(), (double)(passed ? time : (0.0 - time)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return passed;
    }
}