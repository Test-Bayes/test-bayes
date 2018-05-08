package runner.utilTestClasses;

import edu.uw.cse.testbayes.utils.Reorder;

//@Ignore
//@RunWith(JUnit4.class)
@Reorder.SmartOrder({
    Test1.class,
    Test2.class,
    Test3.class,
    Test4.class
})
public class TestRunner {

}

