package edu.uw.cse.testbayes.utils;

import org.apache.maven.plugin.logging.Log;

public class LoggerUtils {

    public static Log LOGGER;

    public static void initializaLogger(Log logger) {
        LOGGER = logger;
    }

    public static void info(Object o) {
        log(o.toString(), Level.INFO);
    }

    public static void info(String s) {
        log(s, Level.INFO);
    }

    public static void error(String s) {
        log(s, Level.ERROR);
    }

    public static void error(Exception e) {
        log(e.toString(), Level.ERROR);

    }

    public static void warn(String s) {
        log(s, Level.WARN);
    }

    private static void log(String s, Level level) {
        if(LOGGER == null) {
            if(level == Level.ERROR) {
                System.err.println(s);
            } else {
                System.out.println(s);
            }
        } else {
            if(level == Level.ERROR) {
                LOGGER.error(s);
            } else if(level == Level.WARN){
                LOGGER.warn(s);
            } else {
                LOGGER.info(s);
            }
        }
    }

    private enum Level {
        ERROR, WARN, INFO
    }
}
