package edu.uw.cse.testbayes.fileio;

import edu.uw.cse.testbayes.model.LogData;
import edu.uw.cse.testbayes.utils.FileNameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Reads Logs from Test Runs and parses through the data
 */
public class LogReader extends TestLogIO {

    /**
     * The number of files of logs to be read
     */
    private static final int RUNNING_AVERAGE = System.getProperty("avg") == null ?
                                               15 : Integer.valueOf(System.getProperty("avg"));

    /**
     * Returns a map of the last @RUNNING_AVERAGE test runs and the stats of the same
     *
     * @return  a map with the details of the last @RUNNING_AVERAGE test runs and the stats of the same
     * @throws FileNotFoundException if a file is not found
     */
    public static Map<String, Map<String, Double>> read() throws FileNotFoundException {
        Map<String, File> fileMap = getFileMap();
        Map<String, Map<String, Double>> allData = new HashMap<>();
        int counter = 0;
        for(String fileName : fileMap.keySet()) {
            if(counter < RUNNING_AVERAGE) {
                LogData logData = readFile(fileMap.get(fileName));
                allData.put(fileName, logData.getData());
                if(logData.isComplete()) {
                    counter++;
                }
            } else {
                break;
            }
        }
        return allData;
    }

    /**
     * Returns a map mapping the filename to the file with the test data of the last @RUNNING_AVERAGE test runs
     *
     * @return  a map of @RUNNING_AVERAGE files with test data
     */
    private static Map<String, File> getFileMap() {
        File directory = new File(FileNameUtils.getDirectoryName());
        File[] fileArray = directory.listFiles();

        Map<String, File> fileMap = new TreeMap<>(Collections.reverseOrder());
        if (fileArray != null) {
            for (File file : fileArray) {
                if (file.isFile()) {
                    fileMap.put(file.toString(), file);
                }
            }
        } else {
            if (!directory.mkdir()) {
                throw new IllegalStateException("Could not create log-data directory");
            }
        }
        return fileMap;
    }

    /**
     * Returns a map depicting the data of the file provided
     *
     * @param file  A file with test data
     * @return  A map with data of the test run associated with the file provided
     * @throws FileNotFoundException if file is not found
     */
    public static LogData readFile(File file) throws FileNotFoundException {
        String fileData = readRawLogFile(file);
        return readString(fileData);
    }

    /**
     * Returns a LogData object depicting the data of the String provided
     *
     * @param fileData  A file with test data
     * @return  A map with data of the test run associated with the file provided
     */
    public static LogData readString(String fileData) {
        LogData result = new LogData();
        Map<String, Double> data = new HashMap<String, Double>();
        if(fileData.length() == 0) {
            return result;
        }
        String[] tuples = fileData.split(" ");
        if(tuples[0].equals(TEST_COMPLETE_MESSAGE)) {
            result.setComplete(true);
        }
        for(String tuple : tuples) {
            if(tuple.equals(TEST_COMPLETE_MESSAGE)) {
                continue;
            }
            String[] bits = tuple.split(",");
            data.put(bits[0].replaceAll("%", " "), Double.parseDouble(bits[1]));
        }
        result.setData(data);
        return result;
    }

    /**
     * Reads the log file in the raw format
     *
     * @param filename String representing the filename
     * @return String with the raw logs
     * @throws FileNotFoundException if file is not found
     */
    public static String readRawLogFile(String filename) throws FileNotFoundException {
        return readRawLogFile(new File(filename));
    }

    /**
     * Reads the log file in the raw format
     *
     * @param file File object representing the file
     * @return String with the raw logs
     * @throws FileNotFoundException if file is not found
     */
    public static String readRawLogFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        String result = "";
        if(scanner.hasNextLine()) {
            result = scanner.nextLine();
        }
        scanner.close();
        return result;
    }
}
