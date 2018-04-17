package fileio;

import edu.uw.cse.testbayes.fileio.TestLogWriter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestLogWriterTest {

    private static Map<String, Double> individualTestData;
    private static Map<String, Double> emptyTestData;
    private static Map<String, Double> multipleTestData;
    private static Set<File> files;

    @BeforeClass
    public static void initialize() {
        emptyTestData = new HashMap<String, Double>();
        individualTestData = new HashMap<String, Double>();
        multipleTestData = new HashMap<String, Double>();

        individualTestData.put("method1", -100.23);
        multipleTestData.put("method1", -100.23);
        multipleTestData.put("method2", 10.0);
        multipleTestData.put("method3", 0.23);

        files = new HashSet<File>();
    }

    @Test
    public void testIndividualTestWrite() throws IOException {
        String filename = TestLogWriter.write(individualTestData);
        File file = new File(filename);
        files.add(file);
        Scanner scanner = new Scanner(file);
        String nextLine = scanner.nextLine();
        scanner.close();
        assertEquals(nextLine, "method1,-100.23");
    }

    @Test
    public void testEmptyTestWrite() throws IOException {
        String filename = TestLogWriter.write(emptyTestData);
        assertNull(filename);
    }

    @Test
    public void testMultipleTestWrite() throws IOException {
        String filename = TestLogWriter.write(multipleTestData);
        File file = new File(filename);
        files.add(file);
        Scanner scanner = new Scanner(file);
        String nextLine = scanner.nextLine();
        scanner.close();
        assertEquals(nextLine, "method1,-100.23 method2,10.0 method3,0.23");
    }

    @AfterClass
    public static void cleanUp() {
        for(File file: files) {
            if (!file.delete()) {
                System.err.println("File " + file.getAbsolutePath() + " not deleted");
            }
        }
    }

}
