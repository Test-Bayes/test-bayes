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

    public IndividualClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.testClass = klass;
        this.ignore = true;
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

    private Method getFirstMethod(List<Method> ms, Bayes b) {
        double best = 0;
        Method bestM = null;
        for (Method m : ms) {
            if (bestM == null || b.getTestProb(m.toString()).doubleValue() > best) {
                bestM = m;
                best = b.getTestProb(m.toString()).doubleValue();
            }
        }
        return bestM;
    }

    @Override
    public void run(RunNotifier notifier) {
        Map<String, Double> fileOutput = new HashMap<String, Double>();
        System.out.println("running the tests from MyRunner: " + testClass);
        Object testObject = null;
        try {
            testObject = testClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            exit(1);
        } catch (InstantiationException e) {
            e.printStackTrace();
            exit(1);
        }

        // Get the past map
        Map<String, Map<String, Double>> oldRuns = null;
        try {
            oldRuns = TestLogReader.read();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            exit(1);
        }

        ArrayList<Method> methods = shuffle(testClass.getMethods());
        Map<String, Method> nameToMethod = new HashMap<String, Method>();
        for (int i = 0; i < methods.size(); i++) {
            if (!methods.get(i).isAnnotationPresent(Test.class)) {
                methods.remove(i);
                i--;
            } else if (ignore && methods.get(i).isAnnotationPresent(Ignore.class)) {
                oldRuns.remove(methods.get(i).toString());
                methods.remove(i);
                i--;
            } else {
                nameToMethod.put(methods.get(i).toString(), methods.get(i));
            }
        }

        System.out.println(nameToMethod.toString());

        // Create the bayes module
        Bayes bay = new Bayes(oldRuns, methods);

        // Get new methods never seen
        Set<String> s = nameToMethod.keySet();
        s.removeAll(bay.getProb().keySet());
        List<String> newMs = new ArrayList<>(s);
        System.out.println(newMs.toString());
        System.out.println(bay.getProb().toString());

        Method method;
        if (newMs.size() == 0) {
            method = getFirstMethod(methods, bay);
        } else {
            method = nameToMethod.get(newMs.get(0));
        }

        for (int i = 0; i < methods.size(); i++) {
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
//                fileOutput.put(method.toString(), (double)(passed ? time : (0.0 - time)));
            }
            if (i == newMs.size() - 1) {
                System.out.println("Getting first method: " + bay.getProb().keySet().toString());
                System.out.println(nameToMethod.toString());
                List<Method> ms = new ArrayList<>();
                for (String str : bay.getProb().keySet()) {
                    ms.add(nameToMethod.get(str));
                }
                if (ms.contains(null)) {
                    break;
                }
                System.out.println(ms.toString());
                method = getFirstMethod(ms, bay);
            } else if (i > newMs.size() - 1) {
                System.out.println("Getting next method");
                String newS = bay.nextTest(method.toString(), passed, nameToMethod.keySet());
                method = nameToMethod.get(newS);
                System.out.println("New method: " + method);
            } else {
                method = nameToMethod.get(newMs.get(i + 1));
            }
        }

        // TODO: Call this in the loop, write one by one
//        TestLogWriter outputWriter = new TestLogWriter();
//        try {
//            outputWriter.write(fileOutput);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }
}