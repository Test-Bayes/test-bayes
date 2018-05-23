package model;

import edu.uw.cse.testbayes.model.Probability;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ProbabilityTest {

    /**
     * Tests equality when adding 0 to the numerator
     */
    @Test
    public void addZeroNumerator() {
        Probability oneTwo1 = new Probability(1, 2);
        Probability oneTwo2 = new Probability(1, 2);
        oneTwo1.addNumerator(0);
        assertEquals(oneTwo1, oneTwo2);
        assertEquals(oneTwo1.hashCode(), oneTwo2.hashCode());
    }

    /**
     * Tests equality when adding 0 to the denominator
     */
    @Test
    public void addZeroDenominator() {
        Probability oneTwo1 = new Probability(1, 2);
        Probability oneTwo2 = new Probability(1, 2);
        oneTwo1.addDenominator(0);
        assertEquals(oneTwo1, oneTwo2);
        assertEquals(oneTwo1.hashCode(), oneTwo2.hashCode());
    }

    /**
     * Tests addition to numerator
     */
    @Test
    public void addOneNumerator() {
        Probability oneTwo = new Probability(1, 2);
        Probability twoTwo = new Probability(2, 2);
        oneTwo.addNumerator(1);
        assertEquals(oneTwo, twoTwo);
        assertEquals(oneTwo.hashCode(), twoTwo.hashCode());
    }

    /**
     * Tests addition to the denominator
     */
    @Test
    public void addOneDenominator() {
        Probability oneTwo = new Probability(1, 2);
        Probability oneThree = new Probability(1, 3);
        oneTwo.addDenominator(1);
        assertEquals(oneTwo, oneThree);
        assertEquals(oneTwo.hashCode(), oneThree.hashCode());
    }

    /**
     * Tests multiplication by integer value 0
     */
    @Test
    public void multiplyZeroInts() {
        Probability oneTwo = new Probability(1, 2);
        Probability zeroSix = new Probability(0, 6);
        oneTwo.multiply(0, 3);
        assertEquals(oneTwo, zeroSix);
        assertEquals(oneTwo.hashCode(), zeroSix.hashCode());
    }

    /**
     * Tests multiplication by integers
     */
    @Test
    public void multiplyInts() {
        Probability oneTwo = new Probability(1, 2);
        Probability fourSix = new Probability(4, 6);
        oneTwo.multiply(4, 3);
        assertEquals(oneTwo, fourSix);
        assertEquals(oneTwo.hashCode(), fourSix.hashCode());
    }

    /**
     * Tests multiplication by Probability value 0
     */
    @Test
    public void multiplyZeroProbability() {
        Probability oneTwo = new Probability(1, 2);
        Probability zeroSix = new Probability(0, 6);
        Probability zeroThree = new Probability(0, 3);
        oneTwo.multiply(zeroThree);
        assertEquals(oneTwo, zeroSix);
        assertEquals(oneTwo.hashCode(), zeroSix.hashCode());
    }

    /**
     * Tests multiplication by Probability
     */
    @Test
    public void multiplyProbability() {
        Probability oneTwo = new Probability(1, 2);
        Probability fourSix = new Probability(4, 6);
        Probability fourThree = new Probability(4, 3);
        oneTwo.multiply(fourThree);
        assertEquals(oneTwo, fourSix);
        assertEquals(oneTwo.hashCode(), fourSix.hashCode());
    }

    /**
     * Tests equality to 0
     */
    @Test
    public void testEqualityZero() {
        Probability zeroOne = new Probability(0, 1);
        Probability zeroTwo = new Probability(0, 2);
        assertEquals(zeroOne, zeroTwo);
        assertEquals(zeroOne.hashCode(), zeroTwo.hashCode());
    }

    /**
     * Tests in-equality of varying value
     */
    @Test
    public void testInEqualityOneHalf() {
        Probability zero = new Probability(0, 2);
        Probability half = new Probability(1, 2);
        assertNotEquals(zero, half);
    }

    /**
     * Tests equality of varying states having the same value
     */
    @Test
    public void testEqualityHalf() {
        Probability oneTwo1 = new Probability(1, 2);
        Probability oneTwo2 = new Probability(1, 2);
        assertEquals(oneTwo1, oneTwo2);
        assertEquals(oneTwo1.hashCode(), oneTwo2.hashCode());
    }

    /**
     * Tests the double value of Probability value 0
     */
    @Test
    public void doubleValueZero() {
        Probability zero = new Probability(0, 2);
        assertEquals(0, zero.doubleValue(), 0.001);
    }

    /**
     * Tests the double value of Probability value 1/2
     */
    @Test
    public void doubleValueHalf() {
        Probability half = new Probability(1, 2);
        assertEquals(0.5, half.doubleValue(), 0.001);
    }

    /**
     * Tests equality of two Probability value 0 in different states
     */
    @Test
    public void compareZeroes() {
        Probability zeroOne = new Probability(0, 1);
        Probability zeroTwo = new Probability(0, 2);
        assertEquals(0, zeroOne.compareTo(zeroTwo));
    }

    /**
     * Tests equality of two Probability value half in different states
     */
    @Test
    public void compareHalves() {
        Probability oneTwo = new Probability(1, 2);
        Probability twoFour = new Probability(2, 4);
        assertEquals(oneTwo.compareTo(twoFour), 0);
    }

    /**
     * Tests inequality of two Probability with different values
     */
    @Test
    public void compareHalfZero() {
        Probability half = new Probability(1, 2);
        Probability zero = new Probability(0, 1);
        assertEquals(half.compareTo(zero), 1);
        assertEquals(zero.compareTo(half), -1);
    }
}
