package edu.uw.cse.testbayes.fileio;

import edu.uw.cse.testbayes.utils.FileNameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TestLogReader {

    // TODO: Make this a parameter of some sort
    private static final int RUNNING_AVERAGE = 15;

    /**
     * Returns a map of the last @RUNNING_AVERAGE test runs and the stats of the same
     *
     * @return  a map with the details of the last @RUNNING_AVERAGE test runs and the stats of the same
     * @throws FileNotFoundException
     */
    public static Map<String, Map<String, Double>> read() throws FileNotFoundException {
        Map<String, File> fileMap = getFileMap();
        Map<String, Map<String, Double>> allData = new HashMap<String, Map<String, Double>>();
        int counter = 0;
        for(String fileName : fileMap.keySet()) {
            if(counter < RUNNING_AVERAGE) {
                Map<String, Double> data = readFile(fileMap.get(fileName));
                allData.put(fileName, data);
                counter++;
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
        Map<String, File> fileMap = new TreeMap<String, File>(Collections.reverseOrder());
        for(File file : fileArray) {
            if (file.isFile()) {
                fileMap.put(file.toString(), file);
            }
        }
        return fileMap;
    }

    /**
     * Returns a map depicting the data of the file provided
     *
     * @param file  A file with test data
     * @return  A map with data of the test run associated with the file provided
     * @throws FileNotFoundException
     */
    public static Map<String, Double> readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        String fileData = scanner.nextLine();
        return readString(fileData);
    }

    /**
     * Returns a map depicting the data of the file provided
     *
     * @param fileData  A file with test data
     * @return  A map with data of the test run associated with the file provided
     */
    public static Map<String, Double> readString(String fileData) {
        Map<String, Double> data = new HashMap<String, Double>();
        if(fileData.length() == 0) {
            return data;
        }
        String[] tuples = fileData.split(" ");
        for(String tuple : tuples) {
            String[] bits = tuple.split(",");
            data.put(bits[0].replaceAll("%", " "), Double.parseDouble(bits[1]));
        }
        return data;
    }
}
