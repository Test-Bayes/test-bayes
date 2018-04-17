package edu.uw.cse.testbayes.fileio;

import edu.uw.cse.testbayes.utils.FileNameUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

public class TestLogWriter {

    /**
     * Returns the Absolute Path of the file where the given data from the test run is stored
     *
     * @param testData  a map with test names mapping to the time taken to run the test. Time taken to be negative
     *                  if test fails
     * @return  Absolute Path of file with log or null if @testData is empty
     * @throws IOException
     */
    public static String write(Map<String, Double> testData) throws IOException {
        if(testData.size() == 0) {
            return null;
        }
        String directoryName = FileNameUtils.getDirectoryName();
        File directory = new File(directoryName);
        if(!directory.exists()) {
            directory.mkdir();
        }
        String filename = FileNameUtils.createFileName();
        File file = new File(filename);
        file.createNewFile();
        PrintStream printStream = new PrintStream(file);
        StringBuilder stringBuilder = new StringBuilder();
        for(String methodName : testData.keySet()) {
            stringBuilder.append(methodName + "," + testData.get(methodName) + " ");
        }
        printStream.append(stringBuilder.toString().trim());
        printStream.close();
        return file.getAbsolutePath();
    }

}
