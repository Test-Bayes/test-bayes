package runner.utilTestClasses;

import edu.uw.cse.testbayes.runner.IndividualClassRunner;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.*;

@Ignore
@RunWith(IndividualClassRunner.class)
@Suite.SuiteClasses({
    Test1.class,
    Test2.class,
    Test3.class,
    Test4.class
})
public class TestRunner {

}

