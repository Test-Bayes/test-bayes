public class draft{
  private Map<String, Fraction> tots = new HashMap<String, Fraction>();
  private Map<String, Map<String, Fraction>> passconds = new HashMap<String, HashMap<String, Fraction>>();
  private Map<String, Map<String, Fraction>> failconds = new HashMap<String, HashMap<String, Fraction>>();
  public Map<String, Fraction> getprob(){
    return tots;
  }
  public Fraction getTestProb(String s){
    return tots.get(s);
  }
  public Fraction getPassCondProb(String s){
    return passconds.get(s);
  }
  public Fraction getFailCondProb(String s){
    return failconds.get(s);
  }
  private void buildTot(Map<String, Map<String, Double>> testExecs){
    Set<String> executions = testExecs.keySet();
    for(String execution : executions){
      Map<String, Double> tests = testExecs.get(execution);
      Set<String> thisexec = tests.keySet();
      for(String test : thisexec){
        Double time = tests.get(test);
        if(!tots.containsKey(test)){
          tots.put(test, new Fraction(0.5));
        }
        Fraction prob = tots.get(test);
        if(time > 0){
          prob.addNumerator(1);
        }
        prob.addDenominator(1);
        tots.put(test, prob);
      }
    }
  }
  private void buildPasscond(Map<String, Map<String, Double>> testExecs){
    Set<String> executions = testExecs.keySet();
    for(String execution : executions){
      Map<String, Double> tests = testExecs.get(execution);
      Set<String> thisexec = tests.keySet();
      for(String test1 : thisexec){
        for(String test2: thisexec){
          Double time1 = tests.get(test1);
          Double time2 = tests.get(test2);
          if(!passconds.containsKey(test1)){
            passconds.put(test1, new HashMap<String, Fraction>());
          }
          Map<String, Fraction> conds1 = passconds.get(test1);
          if(!conds1.containsKey(test2)){
            conds1.put(test2, new Fraction(0.5));
          }
          Fraction prob = conds1.get(test2);
          if(time1 > 0){
            if(time2 > 0){
              prob.addNumerator(1);
            }
            prob.addDenominator(1);
            conds1.put(test2, prob1);
            passconds.put(test1, conds1);
          }
        }
      }
    }
  }
  private void buildFailcond(Map<String, Map<String, Double>> testExecs){
    Set<String> executions = testExecs.keySet();
    for(String execution : executions){
      Map<String, Double> tests = testExecs.get(execution);
      Set<String> thisexec = tests.keySet();
      for(String test1 : thisexec){
        for(String test2: thisexec){
          Double time1 = tests.get(test1);
          Double time2 = tests.get(test2);
          if(!failconds.containsKey(test1)){
            failconds.put(test1, new HashMap<String, Fraction>());
          }
          Map<String, Fraction> conds1 = failconds.get(test1);
          if(!conds1.containsKey(test2)){
            conds1.put(test2, new Fraction(0.5));
          }
          Fraction prob = conds1.get(test2);
          if(time1 < 0){
            if(time2 > 0){
              prob.addNumerator(1);
            }
            prob.addDenominator(1);
            conds1.put(test2, prob1);
            failconds.put(test1, conds1);
          }
        }
      }
    }
  }
}
