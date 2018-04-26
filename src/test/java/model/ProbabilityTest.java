package model;

import edu.uw.cse.testbayes.fileio.TestLogWriter;
import edu.uw.cse.testbayes.model.Probability;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class ProbabilityTest {

    @Test
    public void addZeroNumerator() {
        Probability oneTwo1 = new Probability(1, 2);
        Probability oneTwo2 = new Probability(1, 2);
        oneTwo1.addNumerator(0);
        assertEquals(oneTwo1, oneTwo2);
    }

    @Test
    public void addZeroDenominator() {
        Probability oneTwo1 = new Probability(1, 2);
        Probability oneTwo2 = new Probability(1, 2);
        oneTwo1.addDenominator(0);
        assertEquals(oneTwo1, oneTwo2);
    }

    @Test
    public void addOneNumerator() {
        Probability oneTwo = new Probability(1, 2);
        Probability twoTwo = new Probability(2, 2);
        oneTwo.addNumerator(1);
        assertEquals(oneTwo, twoTwo);
    }

    @Test
    public void addOneDenominator() {
        Probability oneTwo = new Probability(1, 2);
        Probability oneThree = new Probability(1, 3);
        oneTwo.addDenominator(1);
        assertEquals(oneTwo, oneThree);
    }

    @Test
    public void multiplyZeroInts() {
        Probability oneTwo = new Probability(1, 2);
        Probability zeroSix = new Probability(0, 6);
        oneTwo.multiply(0, 3);
        assertEquals(oneTwo, zeroSix);
    }

    @Test
    public void multiplyInts() {
        Probability oneTwo = new Probability(1, 2);
        Probability fourSix = new Probability(4, 6);
        oneTwo.multiply(4, 3);
        assertEquals(oneTwo, fourSix);
    }

    @Test
    public void multiplyZeroProbability() {
        Probability oneTwo = new Probability(1, 2);
        Probability zeroSix = new Probability(0, 6);
        Probability zeroThree = new Probability(0, 3);
        oneTwo.multiply(zeroThree);
        assertEquals(oneTwo, zeroSix);
    }

    @Test
    public void multiplyProbability() {
        Probability oneTwo = new Probability(1, 2);
        Probability fourSix = new Probability(4, 6);
        Probability fourThree = new Probability(4, 3);
        oneTwo.multiply(fourThree);
        assertEquals(oneTwo, fourSix);
    }

    @Test
    public void testEqualityZero() {
        Probability zeroOne = new Probability(0, 1);
        Probability zeroTwo = new Probability(0, 2);
        assertEquals(zeroOne, zeroTwo);
    }

    @Test
    public void testInEqualityOneHalf() {
        Probability zero = new Probability(0, 2);
        Probability half = new Probability(1, 2);
        assertNotEquals(zero, half);
    }

    @Test
    public void testEqualityHalf() {
        Probability oneTwo1 = new Probability(1, 2);
        Probability oneTwo2 = new Probability(1, 2);
        assertEquals(oneTwo1, oneTwo2);
    }

    @Test
    public void doubleValueZero() {
        Probability zero = new Probability(0, 2);
        assertEquals(0, zero.doubleValue(), 0.001);
    }

    @Test
    public void doubleValueHalf() {
        Probability half = new Probability(1, 2);
        assertEquals(0.5, half.doubleValue(), 0.001);
    }

    @Test
    public void compareZeroes() {
        Probability zeroOne = new Probability(0, 1);
        Probability zeroTwo = new Probability(0, 2);
        assertEquals(0, zeroOne.compareTo(zeroTwo));
    }

    @Test
    public void compareHalves() {
        Probability oneTwo = new Probability(1, 2);
        Probability twoFour = new Probability(2, 4);
        assertEquals(oneTwo.compareTo(twoFour), 0);
    }

    public void compareHalfZero() {
        Probability half = new Probability(1, 2);
        Probability zero = new Probability(0, 1);
        assertEquals(half.compareTo(zero), 1);
        assertEquals(zero.compareTo(half), -1);
    }
}
