package edu.uw.cse.testbayes.utils;

import java.io.File;
import java.sql.Timestamp;

public class FileNameUtils {

    public static String createFileName() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String filename = "log-data" + File.separator + timestamp.getTime() + "-" + System.getProperty("user.name") + ".txt";
        return filename;
    }

    public static String getDirectoryName() {
        return "log-data" + File.separator;
    }
}
