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
 
## Evaluation
| Repository name | Number of log files used | Time taken to first failure Junit(ms) | Random(ms) | TestBayes(ms) | Number of tests to first failure JUnit | Random | TestBayes |
|-----------------|--------------------------|---------------------------------------|------------|---------------|----------------------------------------|--------|-----------|
| Test Bayes      | 0                        | 38                                    | 7          | 30            | 4                                      | 1      | 2         |
| Test Bayes      | 3                        | 65                                    | 38         | 17            | 12                                     | 8      | 9         |
| Test Bayes      | 5                        | 43                                    | 32         | 21            | 8                                      | 2      | 3         |
| Test Bayes      | 10                       | N/A All tests pass                    | N/A        | N/A           | N/A                                    | N/A    | N/A       |
| Atomix          | 0                        | 130                                   | 549        | 576           | 2                                      | 54     | 64        |
| Atomix          | 3                        | 130                                   | 419        | 100           | 2                                      | 43     | 1         |
| Atomix          | 5                        | 110                                   | 630        | 100           | 2                                      | 59     | 1         |
| Atomix          | 10                       | 128                                   | 403        | 100           | 2                                      | 40     | 1         |

Link to our evaluation repository to recreate results of our evaluation- https://github.com/Test-Bayes/evals


## Contribute to project
 If you would like to contribute to this project, contact testbayes@googlegroups.com for permissions.
 
 
