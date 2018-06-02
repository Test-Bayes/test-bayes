# Test Bayes

-[![Build Status](https://travis-ci.org/Test-Bayes/test-bayes.svg?branch=master)](https://travis-ci.org/Test-Bayes/test-bayes/)
-
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

1. Copy the file `test-bayes.jar` from the root of Test Bayes to the root of your project

2. Add `test-bayes.jar` to your project classpath

3. Create a Suite class as follows:

    ```
    @RunWith(IndividualClassRunner.class)
    @Reorder.SmartOrder({
        Test1.class,
        Test2.class,
        Test3.class,
        Test4.class
    })
    public class TestRunner {
    
    }
    ```
    Add all the classes you want to reorder to the list in the `@Reorder.SmartOrder({...})` annotation replacing `Test1.class, Test2.class, Test3.class, Test4.class`

4. Run your tests as you would normally

## Side Effects
Using Test Bayes will result in the following:
 - A directory `log-data/` will be created in your repository with data from the test runs. This data is used in future runs to reorder your tests
 - Git commits will include files in `log-data/` to allow tests to be reordered using the data of all contributers

## Contribute to project
 If you would like to contribute to this project, contact testbayes@googlegroups.com for permissions.
 
 
