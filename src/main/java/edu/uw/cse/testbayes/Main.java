package edu.uw.cse.testbayes;

import edu.uw.cse.testbayes.runner.MethodInvocation;
import edu.uw.cse.testbayes.utils.LoggerUtils;
import org.junit.runners.Suite;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException,
                                                  ClassNotFoundException, NoSuchMethodException,
                                                  InstantiationException {
        // TODO: Make TestRunner not be hardcoded in,
        // and instead be generic for any test suite set
        if (args.length < 2) {
            return;
        }

        Class<?> testRunner = Class.forName(args[1]);

        // save our test methods
        Map<String, MethodInvocation> testMethods= new HashMap<>();

        // get the suite class containing all test classes
        Suite.SuiteClasses suiteClassesAnnotation =
                testRunner.getAnnotation(Suite.SuiteClasses.class);

        // ensure the class is annotated
        if (suiteClassesAnnotation == null) {
            throw new NullPointerException("This class isn't annotated with @SuiteClasses");
        }
        Class<?>[] classesInSuite = suiteClassesAnnotation.value();

        // save each method
        for (int i = 0; i < classesInSuite.length; i++) {
            Method[] methods = classesInSuite[i].getDeclaredMethods();
            // get the name of the ith class
            String[] temp = classesInSuite[i].toString().split(" ");
            String className = temp[temp.length - 1];
            for (int j = 0; j < methods.length; j++) {
                Class c = Class.forName(className);
                Object o = c.getDeclaredConstructor().newInstance();

                testMethods.put(methods[j].getName(), new MethodInvocation(methods[j], o));
            }
        }

        // run our test methods
        for (String s: testMethods.keySet()) {
            try {
                testMethods.get(s).invoke();
            } catch (InvocationTargetException e) {
                LoggerUtils.error("\tTest " + testMethods.get(s).method.toString() + " failed");
            } finally {
                LoggerUtils.info("\tTest " + testMethods.get(s).method.toString() + " succeeded");
            }
        }

    }
}
