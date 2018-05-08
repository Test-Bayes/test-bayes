package model;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.uw.cse.testbayes.model.Bayes;
import edu.uw.cse.testbayes.model.Probability;
import org.junit.Before;
import org.junit.Test;

public class BayesTests {
    public final int TIMEOUT = 300;

    private Bayes n;
    @Before
    public void setup() throws Exception {
      Map<String, Map<String, Double>> allExecs = new HashMap<String, Map<String, Double>>();
      Map<String, Double> exec1 = new HashMap<String, Double>();
      exec1.put("test1", 2.5);
      exec1.put("test2", -3.1);
      exec1.put("test3", 4.0);
      exec1.put("test4", -2.1);
      Map<String, Double> exec2 = new HashMap<String, Double>();
      exec2.put("test1", -2.2);
      exec2.put("test2", 3.3);
      exec2.put("test3", -2.0);
      exec2.put("test4", 2.8);
      Map<String, Double> exec3 = new HashMap<String, Double>();
      exec3.put("test1", 2.7);
      exec3.put("test2", 3.3);
      exec3.put("test3", 2.9);
      exec3.put("test4", 2.8);
      Map<String, Double> exec4 = new HashMap<String, Double>();
      exec4.put("test1", -1.7);
      exec4.put("test2", 0.3);
      exec4.put("test3", -2.2);
      exec4.put("test4", 2.0);
      allExecs.put("exec1", exec1);
      allExecs.put("exec2", exec2);
      allExecs.put("exec3", exec3);
      allExecs.put("exec4", exec4);
      n = new Bayes(allExecs);
    }

    @Test(timeout = TIMEOUT)
    public void Test1get(){
      assertEquals(new Probability(1, 2), n.getTestProb("test1"));
    }

    @Test(timeout = TIMEOUT)
    public void Test2get(){
      assertEquals(new Probability(4, 6), n.getTestProb("test2"));
    }

    @Test(timeout = TIMEOUT)
    public void Test3get(){
      assertEquals(new Probability(1, 2), n.getTestProb("test3"));
    }

    @Test(timeout = TIMEOUT)
    public void Test4get(){
      assertEquals(new Probability(4, 6), n.getTestProb("test4"));
    }

    @Test
    public void Testprobget(){
      Map<String, Probability> total = new HashMap<String, Probability>();
      total.put("test1", new Probability(1,2));
      total.put("test2", new Probability(4,6));
      total.put("test3", new Probability(1,2));
      total.put("test4", new Probability(4,6));
      assertEquals(total, n.getProb());
    }

    @Test(timeout = TIMEOUT)
    public void Test1Test2Cond(){
      assertEquals(new Probability(1,2), n.getPassCondProb("test1", "test2"));
    }

    @Test(timeout = TIMEOUT)
    public void Test1Test3Cond(){
      assertEquals(new Probability(3,4), n.getPassCondProb("test1", "test3"));
    }

    @Test(timeout = TIMEOUT)
    public void Test1Test4Cond(){
      assertEquals(new Probability(1,2), n.getPassCondProb("test1", "test4"));
    }

    @Test(timeout = TIMEOUT)
    public void Test2Test1Cond(){
      assertEquals(new Probability(2,5), n.getPassCondProb("test2", "test1"));
    }

    @Test(timeout = TIMEOUT)
    public void Test2Test3Cond(){
      assertEquals(new Probability(2,5), n.getPassCondProb("test2", "test3"));
    }

    @Test(timeout = TIMEOUT)
    public void Test2Test4Cond(){
      assertEquals(new Probability(4,5), n.getPassCondProb("test2", "test4"));
    }
}
