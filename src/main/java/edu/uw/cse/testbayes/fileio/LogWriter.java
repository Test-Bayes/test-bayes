package edu.uw.cse.testbayes.fileio;

import edu.uw.cse.testbayes.utils.FileNameUtils;

import java.io.File;
import java.io.FileNotFoundException;
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
    private String filename;

    public LogWriter(String className) {
        this.filename = FileNameUtils.createFileName(className);
    }

    /**
     * Returns the Absolute Path of the file where the given data from the test run is stored
     *
     * @param methodName name of the method that was tested
     * @param num duration of the test run
     * @return Absolute Path of file with log or null if @testData is empty
     * @throws IOException if an I/O error occurs when creating the file
     */
    public String write(String methodName, double num) throws IOException {
        append(methodName.replaceAll(" ", "%") + "," + num + " ");
        return getFile().getAbsolutePath();
    }

    /**
     * Gets the File in which the logs are to be written
     *
     * @return File Object representing File in which the logs are to be written
     * @throws IOException if an I/O error occurs when creating the file
     */
    private File getFile() throws IOException {
        String directoryName = FileNameUtils.getDirectoryName();
        File directory = new File(directoryName);
        if(!directory.exists()) {
            directory.mkdir();
        }
        File file = new File(filename);
        file.createNewFile();
        return file;
    }

    /**
     * Marks a log as a completed run
     *
     * @throws IOException if an I/O error occurs when creating the file
     */
    public void completeRun() throws IOException {
        prefix(TEST_COMPLETE_MESSAGE + " ");
    }

    /**
     * Adds a prefix to the log file
     *
     * @param prefix String to add as a prefix
     * @throws IOException if an I/O error occurs when creating the file
     */
    private void prefix(String prefix) throws IOException {
        File file = getFile();
        String s = readFile(file);
        write(prefix + s);
    }

    /**
     * Adds a String to the end of the log file
     *
     * @param data String to add to the log file
     * @throws IOException if an I/O error occurs when creating the file
     */
    private void append(String data) throws IOException {
        File file = getFile();
        String s = readFile(file);
        write(s + data);
    }

    /**
     * Writes the given string to the log file, replacing all other data
     *
     * @param data String to be written to the file
     * @throws IOException if an I/O error occurs when creating the file
     */
    private void write(String data) throws IOException {
        PrintStream printStream = new PrintStream(getFile());
        printStream.print(data);
        printStream.close();
    }

    private String readFile(File file) throws FileNotFoundException {
        return LogReader.readRawLogFile(file);
    }

}
