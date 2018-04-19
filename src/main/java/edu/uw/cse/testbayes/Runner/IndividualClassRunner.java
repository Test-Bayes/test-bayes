package edu.uw.cse.testbayes.Runner;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class IndividualClassRunner extends BlockJUnit4ClassRunner {
    public IndividualClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        System.out.println("invoking: " + method.getName());
        Statement result = super.methodInvoker(method, test);
        System.out.println(result.toString());
        try {
            result.evaluate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }
}