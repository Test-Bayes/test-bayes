package edu.uw.cse.testbayes.model;

import edu.uw.cse.testbayes.model.Probability;

import java.lang.reflect.Method;
import java.util.*;

public class Bayes {
  // Map to store total probabilty
  private Map<String, Probability> tots;

  // Map to store Conditional probabilty of passing based on the fact that previous test passed
  private Map<String, Map<String, Probability>> passconds;

  // Map to store Conditional probabilty of passing based on the fact that previous test failed
  private Map<String, Map<String, Probability>> failconds;

  // Set to store already ran tests
  private Set<String> alreadyRan;

  //Numerator Parameter
  public static final int NUMERATOR = 1;

  //denominator Parameter
  public static final int DENOMINATOR = 2;

  /*
   *@param testExecs : A map with all the execution strings mapping to a map that has test information like failing and passing times.
   *
   * Builds the Bayes used to compute the next test to be run.
   *
   */
  public Bayes(Map<String, Map<String, Double>> testExecs, List<Method> ms) {
    this.tots = buildTot(testExecs, ms);
    this.passconds = buildPasscond(testExecs);
    this.failconds = buildFailcond(testExecs);
    this.alreadyRan = new HashSet<String>();
  }

  /*
   * @return: A map with the total probability of each test failing.
   *
   * returns the map with total probability of failing.
   */
  public Map<String, Probability> getProb() {
    return new HashMap<String, Probability>(tots);
  }

  /*
   * @param s : The name of the test.
   *
   * @returns: total probabilty of test passing.
   */
  public Probability getTestProb(String s) {
    return new Probability(tots.get(s));
  }

  /*
   * @param s1 : The name of the test that already passed.
   * @param s2 : The name of the test whose probability of passing you need to know based on previous tests.
   * @return : probability s2 passes given s1 passed.
   */
  public Probability getPassCondProb(String s1, String s2) {
    return new Probability(passconds.get(s1).get(s2));
  }

  /*
   * @param s1 : The name of the test that already failed.
   * @param s2 : The name of the test whose probability of passing you need to know based on previous tests.
   * @return : probability s2 passes given s1 failed.
   */
  public Probability getFailCondProb(String s1, String s2) {
    return new Probability(failconds.get(s1).get(s2));
  }

  // Uses the execution map provided to produce total probability that a test will pass.
  private Map<String, Probability> buildTot(Map<String, Map<String, Double>> testExecs, List<Method> ms) {
    Map<String, Probability> tot = new HashMap<String, Probability> ();
    Set<String> executions = testExecs.keySet();
    for (String execution : executions) {
      Map<String, Double> tests = testExecs.get(execution);
      Set<String> thisexec = tests.keySet();
      for (String test : thisexec) {
        Double time = tests.get(test);
        if (!tot.containsKey(test)) {
          tot.put(test, new Probability(NUMERATOR, DENOMINATOR));
        }
        Probability prob = tot.get(test);
        if (time > 0) {
          prob.addNumerator(1);
        }
        prob.addDenominator(1);
        tot.put(test, prob);
      }
    }
//    for (Method m : ms) {
//      if (!tot.containsKey(m.toString())) {
//        tot.put(m.toString(), new Probability(1, 2));
//      }
//    }
    return tot;
  }

  // Uses the execution map provided to produce Conditional probability that a test will pass based on other tests passing.
  private Map<String, Map<String, Probability>> buildPasscond(Map<String, Map<String, Double>> testExecs) {
    Map<String, Map<String, Probability>> passcond = new HashMap<String, Map<String, Probability>>();
    Set<String> executions = testExecs.keySet();
    for (String execution : executions) {
      Map<String, Double> tests = testExecs.get(execution);
      Set<String> thisexec = tests.keySet();
      for (String test1 : thisexec) {
        for (String test2 : thisexec) {
          Double time1 = tests.get(test1);
          Double time2 = tests.get(test2);
          if (!passcond.containsKey(test1)) {
            passcond.put(test1, new HashMap<String, Probability>());
          }
          Map<String, Probability> conds1 = passcond.get(test1);
          if (!conds1.containsKey(test2)) {
            conds1.put(test2, new Probability(NUMERATOR, DENOMINATOR));
          }
          Probability prob = conds1.get(test2);
          if (time1 > 0) {
            if (time2 > 0) {
              prob.addNumerator(1);
            }
            prob.addDenominator(1);
            conds1.put(test2, prob);
            passcond.put(test1, conds1);
          }
        }
      }
    }
    return passcond;
  }

  // Uses the execution map provided to produce Conditional probability that a test will pass based on other tests failing.
  private Map<String, Map<String, Probability>> buildFailcond(Map<String, Map<String, Double>> testExecs) {
    Map<String, Map<String, Probability>> failcond = new HashMap<String, Map<String, Probability>>();
    Set<String> executions = testExecs.keySet();
    for (String execution : executions) {
      Map<String, Double> tests = testExecs.get(execution);
      Set<String> thisexec = tests.keySet();
      for (String test1 : thisexec) {
        for (String test2 : thisexec) {
          Double time1 = tests.get(test1);
          Double time2 = tests.get(test2);
          if (!failcond.containsKey(test1)) {
            failcond.put(test1, new HashMap<String, Probability>());
          }
          Map<String, Probability> conds1 = failcond.get(test1);
          if (!conds1.containsKey(test2)) {
            conds1.put(test2, new Probability(NUMERATOR, DENOMINATOR));
          }
          Probability prob = conds1.get(test2);
          if (time1 < 0) {
            if (time2 > 0) {
              prob.addNumerator(1);
            }
            prob.addDenominator(1);
            conds1.put(test2, prob);
            failcond.put(test1, conds1);
          }
        }
      }
    }
    return failcond;
  }

  /*
   * @param s : Name of last test executed.
   * @param pass : tells us whether the last test passed or failed.
   * @return : Name of the next test to be executed.
   */
  public String nextTest(String s, boolean pass, Set<String> ignores) {
    alreadyRan.add(s);
    if (pass) {
      Map<String, Probability> cond = passconds.get(s);
      Set<String> tests = cond.keySet();
      Probability min = new Probability(1, 1);
      String minTest = "";
      for (String test : tests) {
//          System.out.println("Test " + test + ": " + alreadyRan.contains(test));
	      if(!alreadyRan.contains(test) && !ignores.contains(test)) {
          if (cond.get(test).compareTo(min) < 0) {
            min = cond.get(test);
            minTest = test;
          }
        }
      }
      if (minTest.equals("")) {
        System.out.println("Bad test");
      }
      return minTest;
    } else {
      Map<String, Probability> cond = failconds.get(s);
      Set<String> tests = cond.keySet();
      Probability min = new Probability(1, 1);
      String minTest = "";
      for (String test : tests) {
        if(!alreadyRan.contains(test) && !ignores.contains(test)) {
          if (cond.get(test).compareTo(min) < 0) {
            min = cond.get(test);
            minTest = test;
          }
        }
      }
      return minTest;
    }
  }
}