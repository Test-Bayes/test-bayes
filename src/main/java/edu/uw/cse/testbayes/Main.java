package edu.uw.cse.testbayes;

import edu.uw.cse.testbayes.utils.LoggerUtils;
import edu.uw.cse.testbayes.utils.VariableUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    private static String directoryName = "src/main";

    public static void main(String[] args) throws ClassNotFoundException {
//        initialize(args);
        File directory = getDirectory();
        List<File> files = getFiles();
        runTests(files);
    }

    private static void runTests(List<File> files) throws ClassNotFoundException {
        Set<Class<?>> testRunners = new HashSet<>();
        for(File file : files) {
            Class<?> testRunner = Class.forName(file.getName().split("[.]")[0] + ".class");
            testRunners.add(testRunner);
        }
    }

    /**
     * Finds all the files in the directory and subdirectories
     *
     * @return List of File objects for each file in the directory and subdirectories
     */
    private static List<File> getFiles() {
        List<File> files = new ArrayList<>();
        listFiles(directoryName, files);
        System.out.println(files);
        return files;
    }

    /**
     * Finds all the files in the directory and subdirectories. Provided List of File objects is edited to include files
     * fount
     *
     * @param directoryName Name of the directory
     * @param files List which will include all files in the directories and subdirectories
     */
    private static void listFiles(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile() && FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("java")) {
                files.add(file);
            } else if (file.isDirectory()) {
                listFiles(file.getAbsolutePath(), files);
            }
        }
    }

    /**
     * Gets the directory's File Object
     *
     * @return File object representing the directory
     */
    private static File getDirectory() {
        File file = new File(directoryName);
        if(file.isDirectory()) {
            return file;
        }
        throw new IllegalArgumentException("Provided filepath does not lead to a directory");
    }

    /**
     * Reads the provided arguments, sets the directory name, running average and epsilon value. Exists in case the
     * arguments are not provided as expected
     *
     * @param args Java arguments provided by the user. Expected to be of the format: [directory, running-average,
     *             epsilon]
     */
    public static void initialize(String[] args) {
        if(args.length < 3) {
            printUsage();
            System.exit(1);
        }
        directoryName = args[0];
        int runningAverage = 0;
        double epsilon = 0.;
        try {
            runningAverage = Integer.parseInt(args[1]);
        }
        catch(NumberFormatException e) {
            printError(args[1] + " cannot be read as an integer value");
        }
        try {
            epsilon = Double.parseDouble(args[2]);
        }
        catch(NumberFormatException e) {
            printError(args[2] + " cannot be read as an double value");
        }
        if(runningAverage > 0) {
            VariableUtils.RUNNING_AVERAGE = runningAverage;
        }
        if(Double.compare(epsilon, 0) > 0) {
            VariableUtils.EPSILON = epsilon;
        }
    }

    /**
     * Prints the usage for the given file onto the Log. Exits once the usage is printed
     */
    private static void printUsage() {
        LoggerUtils.info("usage: java -jar testbayes.jar test-directory running-average epsilon");
        LoggerUtils.info("\ttest-directory: path to directory with the tests");
        LoggerUtils.info("\trunning-average: running average to be used to reorder the tests");
        LoggerUtils.info("\tepsilon: epsilon value to be used when comparing probabilities");
        System.exit(1);
    }

    /**
     * Prints the provided error onto the Log. Exits once the error is printed.
     *
     * @param error Error text to be printed
     */
    private static void printError(String error) {
        LoggerUtils.error(error);
        System.exit(1);
    }


}
