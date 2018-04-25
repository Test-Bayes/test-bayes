package edu.uw.cse.testbayes.Runner;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;

import static java.lang.System.exit;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class IndividualClassRunner extends BlockJUnit4ClassRunner {
    private Class<?> testClass;

    public IndividualClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.testClass = klass;
    }
//
//    @Override
//    protected Statement methodInvoker(FrameworkMethod method, Object test) {
//        System.out.println("invoking: " + method.getName());
//        Statement result = super.methodInvoker(method, test);
//        System.out.println(result.toString());
//        try {
//            result.evaluate();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
////        System.out.println(result.);
//        return result;
//    }

    @Override
    public void run(RunNotifier notifier) {
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
        ArrayList<Method> methods = new ArrayList<Method>(Arrays.asList(testClass.getMethods()));
        Collections.shuffle(methods);
        System.out.println(methods.toString());
        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                Instant end = null;
                Instant start = null;
                boolean passed = true;
                try {
                    System.out.println("Running test " + method.toString());
                    notifier.fireTestStarted(Description
                            .createTestDescription(testClass, method.getName()));
                    start = Instant.now();
                    method.invoke(testObject);
                    end = Instant.now();
                    notifier.fireTestFinished(Description
                            .createTestDescription(testClass, method.getName()));
                } catch (InvocationTargetException e) {
                    end = Instant.now();
                    passed = false;
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    end = Instant.now();
                    passed = false;
                    System.out.println("Illegal test");
                    e.printStackTrace();
                } finally {
                    JSONArray result = new JSONArray();
                    long time = Duration.between(start, end).toMillis();
                    result.put(method.getName());
                    result.put(passed ? time : (0 - time));
                    System.out.println(result.toString());
                }
            }
        }
    }
}