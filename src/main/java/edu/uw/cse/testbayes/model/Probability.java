package edu.uw.cse.testbayes.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Creates an object to store and represent a probability
 */
public class Probability implements Comparable<Probability> {

    /**
     * int value stores the numerator of the Probability
     */
    private int numerator;

    /**
     * int value stores the denominator of the Probability
     */
    private int denominator;

    /**
     * Creates a copy of the given Probability Object
     *
     * @param probability Probability Object to copy
     */
    public Probability(Probability probability) {
        this.numerator = probability.numerator;
        this.denominator = probability.denominator;
    }

    /**
     * Creates a new Probability Object with the given numerator and denominator
     *
     * @param numerator Numberator of the Probability
     * @param denominator Denominator of the Probability
     */
    public Probability(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * Increments the numerator by the given number
     *
     * @param num Amount to increment the numereator
     */
    public void addNumerator(int num) {
        numerator += num;
    }

    /**
     * Increments the denominator by the given number
     *
     * @param num Amount to increment the denominator
     */
    public void addDenominator(int num) {
        denominator += num;
    }

    /**
     * Multiplies the numerator and denominator by the given numbers
     *
     * @param num Amount to multiply the numerator by
     * @param den Amount to multiple the denominator by
     */
    public void multiply(int num, int den) {
        numerator *= num;
        denominator *= den;
    }

    /**
     * Multiples the probability by another probability
     *
     * @param other Probability to multiply with
     */
    public void multiply(Probability other) {
        multiply(other.numerator, other.denominator);
    }

    /**
     * Verifies if the Probability is equal to the given/other Object
     *
     * @param other Other Object against which equality is verified
     * @return
     *          true if the given Object represents a Probability equivalent to this Probability;
     *          false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Probability)) {
            return false;
        }
        Probability o = (Probability) other;
        int thisGcd = this.gcd();
        int thisNum = this.numerator / thisGcd;
        int thisDen = this.denominator / thisGcd;
        int otherGcd = o.gcd();
        int otherNum = o.numerator / otherGcd;
        int otherDen = o.denominator / otherGcd;
        return thisNum == otherNum && thisDen == otherDen;
    }

    /**
     * Calculates the Hash Code for the given Probability
     *
     * @return Returns the calculated Hash Code
     */
    @Override
    public int hashCode() {
        int gcd = gcd();
        return Objects.hashCode(numerator / gcd, denominator / gcd);
    }

    /**
     * Returns the approximate double value of the Probability
     *
     * @return Approximate value of the Probability as a double
     */
    public double doubleValue() {
        return numerator * 1.0 / denominator;
    }

    /**
     * Returns the GCD of the numerator and the denominator
     *
     * @return GCD of the numerator and denominator
     */
    private int gcd() {
        return gcd(numerator, denominator);
    }

    /**
     * Returns the GCD of the given parameters
     *
     * @param n1 First parameter to calculate GCD
     * @param n2 Second parameter to calculate GCD
     * @return GCD of n1 and n2
     */
    private int gcd(int n1, int n2) {
        if(n2 == 0) {
            return 0;
        }
        while(n2 != 0) {
            int temp = n2;
            n2 = n1 % n2;
            n1 = temp;
        }
        return n1;
    }

    /**
     * Compares the Probability to the other/given Probability
     *
     * @param o other Probability Object
     * @return
     *          0 if both Probabilities are equal;
     *          -1 if this Probability is less than the other Probability;
 *              1 if this Probabiliy is greater than the other Probability
     */
    @Override
    public int compareTo(Probability o) {
        //TODO: Remove the doubleValue() calls. Make it more certain
        double thisValue = doubleValue();
        double otherValue = o.doubleValue();
        if (thisValue < otherValue) {
            return -1;
        } else if (thisValue > otherValue) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Returns the String representation of the Probability
     * @return String representation of the Probability
     */
    @Override
    public String toString() {
        int gcd = gcd();
        return MoreObjects.toStringHelper(this)
                .add("numerator", numerator / gcd)
                .add("denominator", denominator / gcd)
                .toString();
    }
}
