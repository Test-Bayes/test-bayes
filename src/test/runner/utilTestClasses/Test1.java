package runner.utilTestClasses;

import edu.uw.cse.testbayes.Runner.IndividualClassRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(IndividualClassRunner.class)
public class Test1 {
    @Ignore
    @Test
    public void a1() throws InterruptedException {
        wasteTime();
        assert(false);
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
