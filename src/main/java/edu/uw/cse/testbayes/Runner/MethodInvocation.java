package edu.uw.cse.testbayes.Runner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInvocation {
    public Object constructor;
    public Method method;

    public MethodInvocation(Method method, Object constructor) {
        this.method = method;
        this.constructor = constructor;
    }


    public void invoke() throws InvocationTargetException,
                                IllegalAccessException {
        this.method.invoke(this.constructor);
    }
}
