package edu.uw.cse.testbayes.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Probability implements Comparable<Probability> {

    private int numerator, denominator;

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
        denominator *= denominator;
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

    // TODO: Consider overflow
    public int compareTo(Probability o) {
        return (numerator * o.denominator - denominator * o.numerator);
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
