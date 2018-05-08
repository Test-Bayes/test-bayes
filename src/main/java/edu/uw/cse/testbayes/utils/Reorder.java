package edu.uw.cse.testbayes.utils;

import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import java.lang.annotation.*;
import java.util.List;

public class Reorder extends Suite {
    public Reorder(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, builder);
    }

    public Reorder(RunnerBuilder builder, Class<?>[] classes) throws InitializationError {
        super(builder, classes);
    }

    protected Reorder(Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
        super(klass, suiteClasses);
    }

    protected Reorder(RunnerBuilder builder, Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
        super(builder, klass, suiteClasses);
    }

    protected Reorder(Class<?> klass, List<Runner> runners) throws InitializationError {
        super(klass, runners);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @Inherited
    public @interface SmartOrder {
        Class<?>[] value();
    }
}
