package math;

import edu.uw.cse.testbayes.math.Bayes;
import edu.uw.cse.testbayes.model.Probability;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BayesTests {

    private final int TIMEOUT = 300;

    private Bayes n;

    @Before
    public void setup() {
        Map<String, Map<String, Double>> allExecs = new HashMap<>();
        Map<String, Double> exec1 = new HashMap<>();
        exec1.put("test1", 2.5);
        exec1.put("test2", -3.1);
        exec1.put("test3", 4.0);
        exec1.put("test4", -2.1);
        Map<String, Double> exec2 = new HashMap<>();
        exec2.put("test1", -2.2);
        exec2.put("test2", 3.3);
        exec2.put("test3", -2.0);
        exec2.put("test4", 2.8);
        Map<String, Double> exec3 = new HashMap<>();
        exec3.put("test1", 2.7);
        exec3.put("test2", 3.3);
        exec3.put("test3", 2.9);
        exec3.put("test4", 2.8);
        Map<String, Double> exec4 = new HashMap<>();
        exec4.put("test1", -1.7);
        exec4.put("test2", 0.3);
        exec4.put("test3", -2.2);
        exec4.put("test4", 2.0);
        allExecs.put("exec1", exec1);
        allExecs.put("exec2", exec2);
        allExecs.put("exec3", exec3);
        allExecs.put("exec4", exec4);
        n = new Bayes(allExecs, new ArrayList<>());
    }

    /**
     * Tests the getTestProb method
     */
    @Test
    public void testGet(){
        assertEquals(new Probability(1, 2), n.getTestProb("test1"));
        assertEquals(new Probability(4, 6), n.getTestProb("test2"));
        assertEquals(new Probability(1, 2), n.getTestProb("test3"));
        assertEquals(new Probability(4, 6), n.getTestProb("test4"));
    }

    /**
     * Tests the getProb method
     */
    @Test
    public void testGetProb(){
        Map<String, Probability> total = new HashMap<>();
        total.put("test1", new Probability(1,2));
        total.put("test2", new Probability(4,6));
        total.put("test3", new Probability(1,2));
        total.put("test4", new Probability(4,6));
        assertEquals(total, n.getProb());
    }

    /**
     * Tests the getPassCondProb method
     */
    @Test(timeout = TIMEOUT)
    public void testGetPassCondProb(){
        assertEquals(new Probability(1,2), n.getPassCondProb("test1", "test2"));
        assertEquals(new Probability(3,4), n.getPassCondProb("test1", "test3"));
        assertEquals(new Probability(1,2), n.getPassCondProb("test1", "test4"));
        assertEquals(new Probability(2,5), n.getPassCondProb("test2", "test1"));
        assertEquals(new Probability(2,5), n.getPassCondProb("test2", "test3"));
        assertEquals(new Probability(4,5), n.getPassCondProb("test2", "test4"));
    }

    /**
     * Tests the getFailCondProb method
     */
    @Test(timeout = TIMEOUT)
    public void testGetFailCondProb(){
        assertEquals(new Probability(3,4), n.getFailCondProb("test1", "test2"));
        assertEquals(new Probability(1,4), n.getFailCondProb("test1", "test3"));
        assertEquals(new Probability(3,4), n.getFailCondProb("test1", "test4"));
        assertEquals(new Probability(2,3), n.getFailCondProb("test2", "test1"));
        assertEquals(new Probability(2,3), n.getFailCondProb("test2", "test3"));
        assertEquals(new Probability(1,3), n.getFailCondProb("test2", "test4"));
    }
}
