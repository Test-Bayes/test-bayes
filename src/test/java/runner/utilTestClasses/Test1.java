package runner.utilTestClasses;

import edu.uw.cse.testbayes.runner.TestBayesIndividualClassRunner;
import edu.uw.cse.testbayes.utils.LoggerUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TestBayesIndividualClassRunner.class)
public class Test1 {
    private boolean a1Fail =
            System.getProperty("A1_FAIL_FOR_TEST") != null ?
                    Boolean.valueOf(System.getProperty("A1_FAIL_FOR_TEST")) :
                    false;

    @Ignore
    @Test
    public void a1() throws InterruptedException {
        wasteTime();
        LoggerUtils.info(a1Fail);
        if (a1Fail) {
            System.getProperties().remove("A1_FAIL_FOR_TEST");
            assert (false);
        }
        return;
    }

    @Test
    public void b1() throws InterruptedException {
        wasteTime();
        return;
    }

    @Test
    public void c1() throws InterruptedException {
        wasteTime();
        return;
    }


    @Test
    public void d1() throws InterruptedException {
        wasteTime();
        return;
    }

    private double wasteTime() throws InterruptedException {
        double i = 1;
        Thread.sleep(1000);
        for (int j = 0; j < Integer.MAX_VALUE; j++) {
            i = Math.sqrt(Math.sqrt(j));
        }
        return i;
    }
}
