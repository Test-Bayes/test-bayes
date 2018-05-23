package edu.uw.cse.testbayes.fileio;

import edu.uw.cse.testbayes.utils.FileNameUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

/**
 * Writes Logs from the Test Run
 */
public class LogWriter extends TestLogIO {

    /**
     * Filename in which the data from the current run are saved
     */
    private static String filename;

    /**
     * Returns the Absolute Path of the file where the given data from the test run is stored
     *
     * @param methodName name of the method that was tested
     * @param num duration of the test run
     * @return Absolute Path of file with log or null if @testData is empty
     * @throws IOException if an I/O error occurs when creating the file
     */
    public static String write(String methodName, double num) throws IOException {
        append(methodName.replaceAll(" ", "%") + "," + num + " ");
        return getFile().getAbsolutePath();
    }

    /**
     * Gets the File in which the logs are to be written
     *
     * @return File Object representing File in which the logs are to be written
     * @throws IOException if an I/O error occurs when creating the file
     */
    private static File getFile() throws IOException {
        String directoryName = FileNameUtils.getDirectoryName();
        File directory = new File(directoryName);
        if(!directory.exists()) {
            directory.mkdir();
        }
        if(filename == null) {
            filename = FileNameUtils.createFileName();
        }
        File file = new File(filename);
        file.createNewFile();
        return file;
    }

    /**
     * DO NOT USE. ONLY FOR TESTING
     * Forces a new file to be created for the next set of data points to be written to the file system
     *
     * @throws InterruptedException if interrupted when sleeping
     */
    public static void forceNewFile() throws InterruptedException {
        filename = null;
        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * Marks a log as a completed run
     *
     * @throws IOException if an I/O error occurs when creating the file
     */
    public static void completeRun() throws IOException {
        prefix(TEST_COMPLETE_MESSAGE + " ");
    }

    /**
     * Adds a prefix to the log file
     *
     * @param prefix String to add as a prefix
     * @throws IOException if an I/O error occurs when creating the file
     */
    private static void prefix(String prefix) throws IOException {
        File file = getFile();
        String s = LogReader.readRawLogFile(file);
        write(prefix + s);
    }

    /**
     * Adds a String to the end of the log file
     *
     * @param data String to add to the log file
     * @throws IOException if an I/O error occurs when creating the file
     */
    private static void append(String data) throws IOException {
        File file = getFile();
        String s = LogReader.readRawLogFile(file);
        write(s + data);
    }

    /**
     * Writes the given string to the log file, replacing all other data
     *
     * @param data String to be written to the file
     * @throws IOException if an I/O error occurs when creating the file
     */
    private static void write(String data) throws IOException {
        PrintStream printStream = new PrintStream(getFile());
        printStream.print(data);
        printStream.close();
    }

}
