package edu.uw.cse.testbayes.Runner;

import junit.framework.TestSuite;
import org.junit.runners.AllTests;
import org.junit.runners.Suite;

public class Main {
    public static void main(String[] args) {
        Suite.SuiteClasses suiteClassesAnnotation = TestRunner.class.getAnnotation(Suite.SuiteClasses.class);
        if (suiteClassesAnnotation == null)
            throw new NullPointerException("This class isn't annotated with @SuiteClasses");
        Class<?>[] classesInSuite = suiteClassesAnnotation.value();
        for (int i = 0; i < classesInSuite.length; i++) {
            System.out.println(classesInSuite[i].toString());
        }
    }
}
