package edu.uw.cse.testbayes.runner;

import edu.uw.cse.testbayes.fileio.TestLogReader;
import edu.uw.cse.testbayes.model.Bayes;
import edu.uw.cse.testbayes.model.Probability;
import org.junit.*;
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

    private int testsRun;
    private Instant startTime;
    private boolean firstFailFound;


    public IndividualClassRunner(Class<?> klass) throws InitializationError {
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
            if (bestM == null || b.getTestProb(currM.toString()).doubleValue() < best) {
                bestM = currM;
                best = b.getTestProb(currM.toString()).doubleValue();
            }
//            System.out.println(currM.getName() + ": " + b.getTestProb(currM.toString()).doubleValue());
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
        ArrayList<Method> befores = new ArrayList<Method>();
        ArrayList<Method> afters = new ArrayList<Method>();
        ArrayList<Method> beforeClasses = new ArrayList<Method>();
        ArrayList<Method> afterClasses = new ArrayList<Method>();
        Set<String> ignores = new HashSet<String>();
        Map<String, Method> nameToMethod = new HashMap<String, Method>();
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

        // Create the bayes module
        Bayes bay = new Bayes(oldRuns, methods);

        // Separate new methods from old ones
        Set<String> temp = new HashSet<String>(nameToMethod.keySet());
        temp.removeAll(bay.getProb().keySet());
        List<String> newMs = new ArrayList<String>(temp);
        temp = new HashSet<String>(nameToMethod.keySet());
        temp.removeAll(newMs);
        List<String> oldMs = new ArrayList<String>(temp);
        oldMs.removeAll(ignores);
        newMs.removeAll(ignores);

//        System.out.println("Olds: " + oldMs.toString());
//        System.out.println("News: " + newMs.toString());
//
//        System.out.println(beforeClasses.toString());
//        System.out.println(befores.toString());
//        System.out.println(afters.toString());
//        System.out.println(afterClasses.toString());

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
                method = getFirstMethod(oldMs, bay, nameToMethod);
            } else {
                String newS = bay.nextTest(method.toString(), passed, new HashSet<String>(ignores));
                method = nameToMethod.get(newS);
            }
            passed = runMethod(notifier, method,
                               befores, afters);
        }

        // Run the afters
        runSetups(afterClasses);
    }

    public void runSetups(List<Method> ms) {
        for (Method m : ms) {
            try {
                m.invoke(testObject);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                exit(1);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                exit(1);
            }
        }
    }

    public boolean runMethod(RunNotifier notifier, Method method,
                             List<Method> befores, List<Method> afters) {
        testsRun++;
        runSetups(befores);
        Instant end = null;
        Instant start = null;
        boolean passed = true;
//        System.out.println("Running test " + method.toString());
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
            System.out.println("Illegal test");
            e.printStackTrace();
        } finally {
            long time = Duration.between(start, end).toMillis();
            if (!passed && !firstFailFound) {
                firstFailFound = true;
                System.out.println("Test taken until first failure: " + testsRun);
                System.out.println("Time taken until first failure: " + Duration.between(Instant.now(), startTime));
            }
            try {
                TestLogWriter.write(method.toString(), (double)(passed ? Math.max(time, 0.1) : Math.min(-time, -0.1)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        runSetups(afters);
        return passed;
    }
}