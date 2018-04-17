package edu.uw.cse.testbayes.Runner;

import org.junit.runners.Suite;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException,
                                                  ClassNotFoundException, NoSuchMethodException,
                                                  InstantiationException {
        // TODO: Make TestRunner not be hardcoded in,
        // and instead be generic for any test suite set
        Suite.SuiteClasses suiteClassesAnnotation =
                TestRunner.class.getAnnotation(Suite.SuiteClasses.class);
        if (suiteClassesAnnotation == null)
            throw new NullPointerException("This class isn't annotated with @SuiteClasses");
        Class<?>[] classesInSuite = suiteClassesAnnotation.value();
        for (int i = 0; i < classesInSuite.length; i++) {
            System.out.println(classesInSuite[i].toString());
            Method[] methods = classesInSuite[i].getDeclaredMethods();
            for (int j = 0; j < methods.length; j++) {
                String[] temp = classesInSuite[i].toString().split(" ");
                String className = temp[temp.length - 1];
                Class c = Class.forName(className);
                Object o = c.getDeclaredConstructor().newInstance();
                // TODO: Make JUnit do this, instead of doing it manually
                try {
                    methods[j].invoke(o);
                } catch (InvocationTargetException e) {
                    System.out.println("\tTest " + methods[j].toString() + " failed");
                } finally {
                    System.out.println("\tTest " + methods[j].toString() + " succeeded");
                }
            }
        }
    }
}
