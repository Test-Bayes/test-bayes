package edu.uw.cse.testbayes.evaluation;

import edu.uw.cse.testbayes.fileio.TestLogWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DummyLogCreator {

    private static final int LOG_COUNT = 100;

    public static Set<Integer> fails;

    public static void main(String[] args) throws IOException, InterruptedException {
        fails = new HashSet<Integer>();
        fails.add(53);
        fails.add(23);
        fails.add(84);
        for(int i = 0; i < LOG_COUNT; i++) {
            makeFile(i > 90);
        }
    }

    private static String makeFile(boolean flag) throws IOException, InterruptedException {
        TestLogWriter.forceNewFile();
        Map<String, Double> map = new HashMap<String, Double>();
        for(int i = 0; i < 100; i++) {
            if(fails.contains(i) && flag) {
                map.put("test" + i, i * -1.0);
            } else {
                map.put("test" + i, i * 1.0);
            }
        }
        TimeUnit.SECONDS.sleep(1);
        return TestLogWriter.write(map);
    }

}
