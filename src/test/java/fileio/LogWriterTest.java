package fileio;

import edu.uw.cse.testbayes.fileio.LogWriter;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LogWriterTest {

    private static Set<File> files;
    private static String testname;
    private static double duration;

    @BeforeClass
    public static void initialize() {
        testname = "method";
        duration = 100.23;
        files = new HashSet<>();
    }

    /**
     * Tests that a single test is written correctly
     * @throws IOException in case of any IO issues
     */
    @Test
    public void testIndividualTestWriter() throws IOException, InterruptedException {
        LogWriter.forceNewFile();
        String filename = LogWriter.write(testname, duration);
        File file = new File(filename);
        files.add(file);
        Scanner scanner = new Scanner(file);
        String nextLine = scanner.nextLine();
        scanner.close();
        assertEquals("method,100.23 ", nextLine);
    }

    /**
     * Tests that tests are written correctly when multiple tests are written in sequence
     * @throws IOException in case of any IO issues
     */
    @Test
    public void testIndividualTestWriterMultiple() throws IOException, InterruptedException {
        LogWriter.forceNewFile();
        String filename = LogWriter.write(testname, duration);
        filename = LogWriter.write(testname, duration);
        File file = new File(filename);
        files.add(file);
        Scanner scanner = new Scanner(file);
        String nextLine = scanner.nextLine();
        scanner.close();
        assertEquals("method,100.23 method,100.23 ", nextLine);
    }


    @After
    public void cleanUp() {
        for(File file: files) {
            if (!file.delete()) {
                System.err.println("File " + file.getAbsolutePath() + " not deleted");
            }
        }
    }

}
