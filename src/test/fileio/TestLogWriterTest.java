package fileio;

import edu.uw.cse.testbayes.fileio.TestLogWriter;
import org.junit.After;
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
    private static Map<String, Double> multipleTestDataSpaced;
    private static Set<File> files;
    private static String testname;
    private static double duration;

    @BeforeClass
    public static void initialize() {
        emptyTestData = new HashMap<String, Double>();
        individualTestData = new HashMap<String, Double>();
        multipleTestData = new HashMap<String, Double>();
        multipleTestDataSpaced = new HashMap<String, Double>();

        testname = "method";
        duration = 100.23;

        individualTestData.put("method1", -100.23);
        multipleTestData.put("method1", -100.23);
        multipleTestData.put("method2", 10.0);
        multipleTestData.put("method3", 0.23);

        multipleTestDataSpaced.put("public void package.Class.method1(String s) throws IOException", -100.23);
        multipleTestDataSpaced.put("public void package.Class.method2(String s) throws IOException", 10.0);

        files = new HashSet<File>();
    }

    @Test
    public void testSpaces() throws IOException {
        String filename = TestLogWriter.write(multipleTestDataSpaced);
        File file = new File(filename);
        files.add(file);
        Scanner scanner = new Scanner(file);
        String nextLine = scanner.nextLine();
        scanner.close();
        assertEquals("public%void%package.Class.method2(String%s)%throws%IOException,10.0 " +
                "public%void%package.Class.method1(String%s)%throws%IOException,-100.23 ", nextLine);
    }

    @Test
    public void testIndividualTestWriter() throws IOException {
        String filename = TestLogWriter.write(testname, duration);
        File file = new File(filename);
        files.add(file);
        Scanner scanner = new Scanner(file);
        String nextLine = scanner.nextLine();
        scanner.close();
        assertEquals("method,100.23 ", nextLine);
    }

    @Test
    public void testIndividualTestWriterMultiple() throws IOException {
        String filename = TestLogWriter.write(testname, duration);
        filename = TestLogWriter.write(testname, duration);
        File file = new File(filename);
        files.add(file);
        Scanner scanner = new Scanner(file);
        String nextLine = scanner.nextLine();
        scanner.close();
        assertEquals("method,100.23 method,100.23 ", nextLine);
    }

    @Test
    public void testIndividualTestWrite() throws IOException {
        String filename = TestLogWriter.write(individualTestData);
        File file = new File(filename);
        files.add(file);
        Scanner scanner = new Scanner(file);
        String nextLine = scanner.nextLine();
        scanner.close();
        assertEquals("method1,-100.23 ", nextLine);
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
        assertEquals("method1,-100.23 method2,10.0 method3,0.23 ", nextLine);
    }

    @After
    public void cleanUp() {
        for(File file: files) {
            if (!file.delete()) {
//                System.err.println("File " + file.getAbsolutePath() + " not deleted");
            }
        }
    }

}
