package edu.uw.cse.testbayes.evaluation;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class TestCreator {

    public static void main(String[] args) throws IOException {
        File file = new File("tests-file.txt");
        file.createNewFile();
        PrintStream printStream = new PrintStream(file);
        for(int i = 1; i <= 100; i++) {
            makeTest(printStream, i);
        }
    }

    public static void makeTest(PrintStream printStream, int i) {
        printStream.println("\t@Test");
        printStream.println("\tpublic void test" + i + " () {");
//        printStream.println("\t\tSystem.out.println(\"test# " + i + "\");");
        printStream.println("\t\tassert(true);");
        printStream.println("\t}");
        printStream.println();
    }

}
