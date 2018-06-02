# Test Bayes

[![Build Status](https://travis-ci.org/Test-Bayes/test-bayes.svg?branch=master)](https://travis-ci.org/Test-Bayes/test-bayes/)
[![Coverage Status](https://coveralls.io/repos/github/Test-Bayes/test-bayes/badge.svg?branch=master)](https://coveralls.io/github/Test-Bayes/test-bayes?branch=master)

## Project Info 
Test Bayes is an automatic test reordering tool developed in CSE 403 at the University of Washington. It is written in Java and is built using Maven. Currently under active development, it is maintained by:
 - Adavya Bhalla (@adavya)
 - Aditya Jhamb (@adityajhamb)
 - Avidant Bhagat (@avidant)
 - Ethan Mayer (@emayer2)
 - Steven Austin (@steveoaustin)

## Project Introduction
Test Bayes is a tool designed to speed up the testing process of development significantly. This is done by reordering tests such that tests most likely to fail are run first.

Test Bayes runs on top of JUnit and requires the addition of a simple annotation on top of the testclasses you want reordered.

Test Bayes uses a combination of Bayes' theorem, running probabilities, total probabilities, test duration, and conditional probability to reorder the tests.

## Pre-Requisites
The minimum requirements to develop Test Bayes is:
 - Maven
 - Java 8

## Usage
To reorder tests, do the following:

1. Add the following dependency to your `pom.xml`
    ```
    <dependency>
        <groupId>edu.uw.cse.testbayes</groupId>
        <artifactId>test-bayes</artifactId>
        <version>1.0</version>
    </dependency>
    ```

2. Add the following plugin to your `pom.xml`
    ```
    <plugin>
        <artifactId>maven-dependency-plugin</artifactId>
        <executions>
            <execution>
                <phase>compile</phase>
                <goals>
                    <goal>copy-dependencies</goal>
                </goals>
                <configuration>
                    <outputDirectory>${project.build.directory}/mylib</outputDirectory>
                </configuration>
            </execution>
        </executions>
    </plugin>
    ```

3. Add the following repository to your `pom.xml`
    ```
    <repository>
        <id>ProjectRepo</id>
        <name>ProjectRepo</name>
        <url>file://${project.basedir}/mylibs</url>
    </repository>
    ```

4. Run `mvn clean package` on Test Bayes

5. Make directory `mylibs/edu/uw/cse/testbayes` in the root of your project.

6. Copy the file `testbayes-1.0.jar` from the `target/` directory into the directory you crated in your project.

7. Add the following annotation to the Test Classes you would like reordered: `@RunWith(TestBayesIndividualClassRunner.class)`

8. Run your tests as you would normally

## Side Effects
Using Test Bayes will result in the following:
 - A directory `log-data/` will be created in your repository with data from the test runs. This data is used in future runs to reorder your tests
 - Git commits will include files in `log-data/` to allow tests to be reordered using the data of all contributers

## Contribute to project
 If you would like to contribute to this project, contact testbayes@googlegroups.com for permissions.
 
 