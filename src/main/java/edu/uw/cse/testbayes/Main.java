package edu.uw.cse.testbayes;

import edu.uw.cse.testbayes.fileio.LogReader;
import edu.uw.cse.testbayes.fileio.LogWriter;
import edu.uw.cse.testbayes.model.Bayes;
import edu.uw.cse.testbayes.runner.MethodInvocation;
import org.junit.*;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static java.lang.System.exit;

public class Main {
    private static int testsRun = 0;
    private static boolean firstFailFound = false;
    private static Instant startTime;

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IOException {
        Class d = Class.forName("runner.utilTestClasses.Test1");
        startTime = Instant.now();
        String[] testDirs = {"src/test/"};
        for (String a : args) {
            if (a.startsWith("--dirs=")) {
                testDirs = a.replace("--dirs=", "").split(",");
            }
        }

        // Get the past map
        Map<String, Map<String, Double>> oldRuns = null;
        try {
            oldRuns = LogReader.read();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            exit(1);
        }

        // Get all methods, and categorize each
        ArrayList<Method> befores = new ArrayList<Method>();
        ArrayList<Method> afters = new ArrayList<Method>();
        ArrayList<Method> beforeClasses = new ArrayList<Method>();
        ArrayList<Method> afterClasses = new ArrayList<Method>();
        Set<String> ignores = new HashSet<String>();
        Map<String, Method> nameToMethod = new HashMap<String, Method>();
        Map<Method, Class> methodToClass = new HashMap<Method, Class>();
        Map<Class, Object> classToObject = new HashMap<Class, Object>();
        ArrayList<Method> methods = null;
        try {
            methodToClass = getAllMethods(testDirs);
            methods = new ArrayList<Method>(methodToClass.keySet());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Please make sure the classes used in test directories " +
                    "are fully built, and the passed directory args are valid");
        }
        for (Class c : methodToClass.values()) {
            try {
                classToObject.put(c, c.newInstance());
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                exit(1);
            }
        }

        sortMethods(methods, befores, afters, beforeClasses, afterClasses, ignores, nameToMethod);

        // Create the bayes module
        Bayes bayes = new Bayes(oldRuns, methods);

        // Separate new methods from old ones
        Set<String> temp = new HashSet<>(nameToMethod.keySet());
        temp.removeAll(bayes.getProb().keySet());
        List<String> newMs = new ArrayList<>(temp);
        temp = new HashSet<>(nameToMethod.keySet());
        temp.removeAll(newMs);
        List<String> oldMs = new ArrayList<>(temp);
        oldMs.removeAll(ignores);
        newMs.removeAll(ignores);

        // Start of method runs
        Instant startTime = Instant.now();

        // Run the beforeClasses
        runSetups(beforeClasses, null, methodToClass, classToObject);

        // Run new methods
        for (int i = 0; i < newMs.size(); i++) {
            runMethod(nameToMethod.get(newMs.get(i)),
                      befores, afters, methodToClass, classToObject);
        }

        // Run already seen methods
        boolean passed = false;
        Method method = null;
        for (int i = newMs.size(); i < methods.size(); i++) {
            if (i == newMs.size()) {
                method = getFirstMethod(oldMs, bayes, nameToMethod);
            } else {
                String newS = bayes.nextTest(method.toString(), passed, new HashSet<>(ignores));
                method = nameToMethod.get(newS);
            }
            passed = runMethod(method, befores, afters,
                    methodToClass, classToObject);
        }

        // Run the afters
        runSetups(afterClasses, null, methodToClass, classToObject);

        // Mark log as complete
        try {
            markComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<Method, Class> getAllMethods(String[] dirs) throws IOException, ClassNotFoundException {
        Map<Method, Class> ret = new HashMap<Method, Class>();
        for (String s : dirs) {
            ret.putAll(getAllMethods(s));
        }
        return ret;
    }

    private static Map<Method, Class> getAllMethods(String dir) throws ClassNotFoundException, IOException {
        Map<Method, Class> methods = new HashMap<Method, Class>();
        File f = new File(dir);
        if (!f.exists()) {
            throw new FileNotFoundException(dir);
        }
        for (File curr : f.listFiles()) {
            if (curr.isDirectory()) {
                methods.putAll(getAllMethods(curr.getPath()));
            } else {
                String currName = curr.getPath();
                Class c = null;
                while (true) {
                    try {
                        c = Class.forName(currName.replace("/", ".").replace("\\", "."));//.replace(".class", ""));
                        break;  // Found valid class
                    } catch (ClassNotFoundException e) {
                        System.out.println(currName);
                        int nextIdx = Math.max(currName.indexOf('\\'), currName.indexOf('/'));
                        if (nextIdx == -1) {
                            break;
                        }
                        currName = currName.substring(nextIdx + 1);
                    }
                }
                if (c == null) {
                    // Class never found
                    throw new ClassNotFoundException(curr.getPath());
                } else {
                    for (Method m : c.getDeclaredMethods()) {
                        methods.put(m, c);
                    }
                }
            }
        }
        return methods;
    }

    /**
     * Returns the first method to be run out of ms according the the Bayes model
     *
     * @param ms The list of potential methods to be run, as strings
     * @param b The bayes probability model
     * @param nameMap A map from method names as Strings, to Methods
     * @return The best method to be run out of ms, according to the model, b
     */
    private static Method getFirstMethod(List<String> ms, Bayes b, Map<String, Method> nameMap) {
        double best = 0;
        Method bestM = null;
        for (String m : ms) {
            Method currM = nameMap.get(m);
            if (bestM == null || b.getTestProb(currM.toString()).doubleValue() < best) {
                bestM = currM;
                best = b.getTestProb(currM.toString()).doubleValue();
            }
        }
        return bestM;
    }

    /**
     * Sorts the methods in the methods list based on annotations
     * @param methods List of methods to be sorted
     * @param befores List for methods with annotation @Before
     * @param afters List for methods with annotation @After
     * @param beforeClasses List for methods with annotation @BeforeClass
     * @param afterClasses List for methods with annotation @AfterClass
     * @param ignores List for methods with annotation @Ignore
     * @param nameToMethod Map from method name to method
     */
    public static void sortMethods(ArrayList<Method> methods, ArrayList<Method> befores, ArrayList<Method> afters,
                            ArrayList<Method> beforeClasses, ArrayList<Method> afterClasses, Set<String> ignores,
                            Map<String, Method> nameToMethod) {
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
            } else if (methods.get(i).isAnnotationPresent(Ignore.class)) {
                nameToMethod.put(methods.get(i).toString(), methods.get(i));
                ignores.add(methods.get(i).toString());
                methods.remove(i);
                i--;
            } else {
                nameToMethod.put(methods.get(i).toString(), methods.get(i));
            }
        }
    }

    private static boolean runMethod(Method method, List<Method> befores, List<Method> afters,
                              Map<Method, Class> methodToClass,
                              Map<Class, Object> classToObject) {
        System.out.println("Running method " + method.getName() + "...");
        Class testClass = methodToClass.get(method);
        Object testObject = classToObject.get(testClass);
        testsRun++;
        runSetups(befores, testClass, methodToClass, classToObject);
        Instant end = null;
        Instant start = null;
        boolean passed = true;
        try {
            start = Instant.now();
            method.invoke(testObject);
            end = Instant.now();
        } catch (InvocationTargetException e) {
            Class f = method.getAnnotation(Test.class).expected();
            if (!e.getTargetException().getClass().toString().equals(f.toString())) {
                // Expected wasn't thrown
                passed = false;
            } else {
                passed = true;
            }
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
                LogWriter.write(method.toString(), (double)(passed ? Math.max(time, 0.1) : Math.min(-time, -0.1)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        runSetups(afters, testClass, methodToClass, classToObject);
        System.out.println(passed ? "SUCCESS" : "FAILURE");
        return passed;
    }

    /**
     * Runs the before class methods
     *
     * @param ms A list of before class Methods
     */
    public static void runSetups(List<Method> ms, Class c, Map<Method, Class> methodToClass,
                          Map<Class, Object> classToObject) {
        for (Method m : ms) {
            if (c == null || methodToClass.get(m).equals(c)) {
                try {
                    m.invoke(classToObject.get(methodToClass.get(m)));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    exit(1);
                }
            }
        }
    }

    /**
     * Marks the run as complete
     * @throws IOException in case of any IO issues
     */
    private static void markComplete() throws IOException {
        LogWriter.completeRun();
    }
}
