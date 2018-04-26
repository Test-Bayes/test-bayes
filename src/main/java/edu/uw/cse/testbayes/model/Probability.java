package edu.uw.cse.testbayes.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Probability implements Comparable<Probability> {

    private int numerator, denominator;

    public Probability(Probability probability) {
        this.numerator = probability.numerator;
        this.denominator = probability.denominator;
    }

    public Probability(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public void addNumerator(int num) {
        numerator += num;
    }

    public void addDenominator(int num) {
        denominator += num;
    }

    public void multiply(int num, int den) {
        numerator *= num;
        denominator *= den;
    }

    public void multiply(Probability other) {
        multiply(other.numerator, other.denominator);
    }

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

    @Override
    public int hashCode() {
        int gcd = gcd();
        return Objects.hashCode(numerator / gcd, denominator / gcd);
    }

    public double doubleValue() {
        return numerator * 1.0 / denominator;
    }

    private int gcd() {
        return gcd(numerator, denominator);
    }

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

    public int compareTo(Probability o) {
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

    @Override
    public String toString() {
        int gcd = gcd();
        return MoreObjects.toStringHelper(this)
                .add("numerator", numerator / gcd)
                .add("denominator", denominator / gcd)
                .toString();
    }
}
