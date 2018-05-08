package fileio;

import edu.uw.cse.testbayes.fileio.TestLogReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestLogReaderTest {

    private static String filename1;
    private static String filename2;
    private static Map<String, Double> map1;
    private static Map<String, Double> map2;
    public static Set<File> files;

    @BeforeClass
    public static void createFile() throws IOException {

        String data1 = "method1,100.23 method2,-100.23 ";
        String data2 = "public%void%package.Class.method1(String%s)%throws%IOException,100.23 " +
                "public%void%package.Class.method2(String%s)%throws%IOException,-100.23 ";

        files = new HashSet<File>();

        filename1 = "temp-file-for-test1";
        File file1 = new File(filename1);
        files.add(file1);
        file1.createNewFile();
        PrintStream printStream1 = new PrintStream(file1);
        printStream1.println(data1);
        printStream1.close();

        map1 = new HashMap<String, Double>();
        map1.put("method1", 100.23);
        map1.put("method2", -100.23);

        filename2 = "temp-file-for-test2";
        File file2 = new File(filename2);
        files.add(file2);
        file2.createNewFile();
        PrintStream printStream2 = new PrintStream(file2);
        printStream2.println(data2);
        printStream2.close();

        map2 = new HashMap<String, Double>();
        map2.put("public void package.Class.method1(String s) throws IOException", 100.23);
        map2.put("public void package.Class.method2(String s) throws IOException", -100.23);

    }

    @Test
    public void readFileWithoutSpaces() throws FileNotFoundException {
        Map<String, Double> map = TestLogReader.readFile(new File(filename1));
        assertEquals(map1, map);
    }

    @Test
    public void readFileWithSpaces() throws FileNotFoundException {
        Map<String, Double> map = TestLogReader.readFile(new File(filename2));
        assertEquals(map2, map);
    }

    @Test
    public void readEmptyString() throws FileNotFoundException {
        assertEquals(TestLogReader.readString(""), new HashMap<String, Double>());
    }

    @Test(expected=NullPointerException.class)
    public void readNullString() {
        System.out.println(TestLogReader.readString(null));
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
