package edu.uw.cse.testbayes.utils;

import java.io.File;

/**
 * Provides static methods for file and directory names
 */
public class FileNameUtils {

    /**
     * Generates the name of the log file
     * @return String with the name of the log file
     */
    public static String createFileName() {
        long timestamp = System.currentTimeMillis();
        String filename = "log-data" + File.separator + timestamp + "-" + System.getProperty("user.name") + ".txt";
        return filename;
    }

    /**
     * Gets the name of the directory in which log files are stored
     * @return String with the name of the directory
     */
    public static String getDirectoryName() {
        return "log-data" + File.separator;
    }
}
