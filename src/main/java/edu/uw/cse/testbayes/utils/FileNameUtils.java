package edu.uw.cse.testbayes.utils;

import java.io.File;
import java.sql.Timestamp;

public class FileNameUtils {

    /**
     * Generates the name of the log file
     * @return String with the name of the log file
     */
    public static String createFileName() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String filename = "log-data" + File.separator + timestamp.getTime() + "-" + System.getProperty("user.name") + ".txt";
        return filename;
    }

    /**
     * Gets the name of the directory in which log files are stored
     * @return Strign with the name of the directory
     */
    public static String getDirectoryName() {
        return "log-data" + File.separator;
    }
}
