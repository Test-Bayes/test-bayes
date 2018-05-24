package fileio;

import edu.uw.cse.testbayes.fileio.LogData;
import edu.uw.cse.testbayes.fileio.LogReader;
import edu.uw.cse.testbayes.runner.TestBayesIndividualClassRunner;
import edu.uw.cse.testbayes.utils.LoggerUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LogReaderTest {

    private static String filename1;
    private static String filename2;
    private static Map<String, Double> map1;
    private static Map<String, Double> map2;
    private static LogData logData1;
    private static LogData logData2;
    public static Set<File> files;

    @BeforeClass
    public static void createFile() throws IOException {

        String data1 = "method1,100.23 method2,-100.23 ";
        String data2 = "public%void%package.Class.method1(String%s)%throws%IOException,100.23 " +
                "public%void%package.Class.method2(String%s)%throws%IOException,-100.23 ";

        files = new HashSet<>();

        filename1 = "temp-file-for-test1";
        File file1 = new File(filename1);
        files.add(file1);
        file1.createNewFile();
        PrintStream printStream1 = new PrintStream(file1);
        printStream1.println(data1);
        printStream1.close();

        map1 = new HashMap<>();
        map1.put("method1", 100.23);
        map1.put("method2", -100.23);

        logData1 = new LogData(map1, false);

        filename2 = "temp-file-for-test2";
        File file2 = new File(filename2);
        files.add(file2);
        file2.createNewFile();
        PrintStream printStream2 = new PrintStream(file2);
        printStream2.println(data2);
        printStream2.close();

        map2 = new HashMap<>();
        map2.put("public void package.Class.method1(String s) throws IOException", 100.23);
        map2.put("public void package.Class.method2(String s) throws IOException", -100.23);

        logData2 = new LogData(map2, false);
    }

    /**
     * Tests that a file where the method names do not have any spaces is read correctly
     * @throws FileNotFoundException
     */
    @Test
    public void readFileWithoutSpaces() throws FileNotFoundException {
        LogData result = LogReader.readFile(new File(filename1));
        assertEquals(logData1, result);
        assertEquals(logData1.hashCode(), result.hashCode());
        assertEquals(map1, result.getData());
        assertFalse(result.isComplete());
    }

    /**
     * Tests that a file where the method name has spaces is read correctly
     * @throws FileNotFoundException if the File is not found
     */
    @Test
    public void readFileWithSpaces() throws FileNotFoundException {
        LogData result = LogReader.readFile(new File(filename2));
        assertEquals(logData2, result);
        assertEquals(logData2.hashCode(), result.hashCode());
        assertFalse(result.isComplete());
        assertEquals(map2, result.getData());
    }

    /**
     * Tests that an empty String is read correctly
     * @throws FileNotFoundException if the File is not found
     */
    @Test
    public void readEmptyString() {
        LogData result = LogReader.readString("");
        LogData expected = new LogData();
        assertEquals(expected, result);
        assertEquals(expected.hashCode(), result.hashCode());
    }

    /**
     * Tests that null value throws a NullPointerException
     */
    @Test(expected=NullPointerException.class)
    public void readNullString() {
        LogReader.readString(null);
    }


    @AfterClass
    public static void cleanUp() {
        for(File file: files) {
            if (!file.delete()) {
                LoggerUtils.error("File " + file.getAbsolutePath() + " not deleted");
            }
        }
    }

}
