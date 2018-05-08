package fileio;

import edu.uw.cse.testbayes.fileio.TestLogReader;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class TestLogReaderTest {

    @Test
    public void readEmptyString() throws FileNotFoundException {
        assertEquals(TestLogReader.readString(""), new HashMap<String, Double>());
    }

    @Test(expected=NullPointerException.class)
    public void readNullString() {
        System.out.println(TestLogReader.readString(null));
    }
}
