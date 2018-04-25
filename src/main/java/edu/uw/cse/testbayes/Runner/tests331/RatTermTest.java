package edu.uw.cse.testbayes.Runner.tests331;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * This class contains a set of test cases that can be used to test the
 * implementation of the edu.uw.cse.testbayes.Runner.tests331.RatPoly class.
 */
public final class RatTermTest {

  /**
   * checks that Java asserts are enabled, and exits if not
   */
  @Before
  public void testAssertsEnabled() {
	  SpecificationTests.checkAssertsEnabled();
  }

  // Get a edu.uw.cse.testbayes.Runner.tests331.RatNum for in an integer
  private static RatNum num(int i) {
    return new RatNum(i);
  }

  private static RatNum num(int i, int j) {
    return new RatNum(i, j);
  }

  private static final RatNum nanNum = (num(1)).div(num(0));
  private static final RatTerm nanTerm = new RatTerm(nanNum, 3);
  private static final RatTerm zeroTerm = RatTerm.ZERO;
  private static final RatTerm one = new RatTerm(new RatNum(1),0);

  // Convenient way to make a edu.uw.cse.testbayes.Runner.tests331.RatTerm equals to coeff*x^expt.
  private static RatTerm term(int coeff, int expt) {
    return new RatTerm(num(coeff), expt);
  }

  // Convenient way to make a edu.uw.cse.testbayes.Runner.tests331.RatTerm equals to num/denom*x^expt.
  private static RatTerm term(int numer, int denom, int expt) {
    return new RatTerm(num(numer, denom), expt);
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  ////	Constructor
  ///////////////////////////////////////////////////////////////////////////////////////

  // Is it good style to include these constructor tests? Explicit constructor
  // tests are often not needed. Generally we advise you to have at
  // least one assertion in every test unless testing that an exception IS
  // thrown (via the "expected" test parameter). The constructor tests below
  // violate this rule, but we chose to include them in this assignment in
  // case they help you isolate constructor errors as you write your code.
  @Test
  public void testCtor() {
    Assert.assertEquals(RatTerm.valueOf("1"), term(1, 0));
    Assert.assertEquals(RatTerm.valueOf("2*x^3"), term(2, 3));
    Assert.assertEquals(RatTerm.valueOf("4/3*x^6"), term(4, 3, 6));
    Assert.assertEquals(RatTerm.valueOf("-2/7*x^3"), term(-2, 7, 3));
  }

  @Test
  public void testCtorZeroCoeff() {
    Assert.assertEquals(RatTerm.ZERO, term(0, 0));
    Assert.assertEquals(RatTerm.ZERO, term(0, 1));
  }

  @Test
  public void testCtorNaN() {
    Assert.assertEquals(RatTerm.NaN, term(3, 0, 0));
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  ////	Get Coefficient
  ///////////////////////////////////////////////////////////////////////////////////////

  @Test
  public void testGetCoeffRegular() {
    // Simple cases
    Assert.assertEquals(num(3), term(3, 1).getCoeff());
    Assert.assertEquals(num(2, 5), term(2, 5, 2).getCoeff());
  }

  @Test
  public void testGetCoeffZero() {
    // Check zero
    Assert.assertEquals(num(0), term(0, 0).getCoeff());
  }

  @Test
  public void testGetCoeffNegative() {
    // Check negative coeff
    Assert.assertEquals(num(-2, 3), term(-2, 3, 2).getCoeff());
  }

  @Test
  public void testGetCoeffNaN() {
    // Check NaN
    Assert.assertEquals(nanNum, term(3, 0, 4).getCoeff());
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  ////	Get Coefficient
  ///////////////////////////////////////////////////////////////////////////////////////

  @Test
  public void testGetExptRegular() {
    // Simple
    Assert.assertEquals(4, term(2, 4).getExpt());
  }

  @Test
  public void testGetExptZero() {
    // Zero always have zero expt
    Assert.assertEquals(0, term(0, 0).getExpt());
    Assert.assertEquals(0, term(0, 1).getExpt());
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  ////	isNaN Test
  ///////////////////////////////////////////////////////////////////////////////////////

  @Test
  public void testIsNaNZeroDenomRegNumer() {
    // Check that 5/0*x^0 isNaN instead of zero
    assertTrue(term(5, 0, 0).isNaN());
  }

  @Test
  public void testIsNaNZeroDenomZeroNumer() {
    // Check that 0/0*x^4 isNaN instead of zero
    assertTrue(term(0, 0, 4).isNaN());
  }

  @Test
  public void testIsNaNFalse() {
    // Check for false positives
    assertFalse(term(2, 3, 2).isNaN());
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  ////	isZero Test
  ///////////////////////////////////////////////////////////////////////////////////////

  @Test
  public void testIsZeroNumAndDen() {
    assertTrue(term(0, 0).isZero());
  }

  @Test
  public void testIsZeroNum() {
    assertTrue(term(0, 1).isZero());
    assertTrue(term(0, 4, 3).isZero());
    assertTrue(term(0, -2, 2).isZero());
  }

  @Test
  public void testIsZeroFalsoPos() {
    // Check for false positives
    assertFalse(term(1, 3, 0).isZero());
  }

  @Test
  public void testIsNaNNotTerm() {
    // Check that 0/0*x^4 is not zero
    assertFalse(term(0, 0, 4).isZero());
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  ////	eval Test
  ///////////////////////////////////////////////////////////////////////////////////////

  @Test
  public void testEvalZero() {
    Assert.assertEquals(0.0, term(0, 0).eval(5.0), 0.0000001);
    Assert.assertEquals(0.0, term(0, 5).eval(1.2), 0.0000001);
  }

  @Test
  public void testEvalSmallFrac() {
    Assert.assertEquals(.125, term(1, 2, 2).eval(.5), 0.0000001);
  }

  @Test
  public void testEvalSmallWhole() {
    Assert.assertEquals(2.0, term(2, 0).eval(3.1), 0.0000001);
    Assert.assertEquals(1.0, term(1, 0).eval(100.0), 0.0000001);
    Assert.assertEquals(1.0, term(-1, 1).eval(-1.0), 0.0000001);
    Assert.assertEquals(2.0, term(1, 2, 2).eval(2.0), 0.0000001);
  }

  @Test
  public void testEvalLarge() {
    Assert.assertEquals(35.0, term(5, 1).eval(7.0), 0.0000001);
    Assert.assertEquals(12.0, term(3, 2).eval(2.0), 0.0000001);
  }

  @Test
  public void testEvalNegative() {
    Assert.assertEquals(-16.0, term(-2, 3).eval(2.0), 0.0000001);
    Assert.assertEquals(-3.0, term(3, 3).eval(-1.0), 0.0000001);
  }

  @Test
  public void testEvalNaN() {
    // To understand the use of "new Double(Double.NaN)" instead of
    // "Double.NaN", see the Javadoc for Double.equals().
    assertEquals(new Double(Double.NaN),
        new Double(term(3, 0, 2).eval(1.0)));
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  ////	Equals Test
  ///////////////////////////////////////////////////////////////////////////////////////

  @Test
  public void testEquals() {
    Assert.assertEquals(term(3, 5), term(3, 5));
    Assert.assertEquals(term(1, 2, 4), term(1, 2, 4));
    Assert.assertEquals(term(-2, 4, 2), term(1, -2, 2));
  }
  @Test
  public void testNotEqualsReg() {
    assertThat(term(4, 6), not(term(7, 8)));
  }

  @Test
  public void testEqualsZeroCoeff() {
    Assert.assertEquals(term(0, 0), term(0, 0));
    Assert.assertEquals(term(0, 1), term(0, 0));
  }

  @Test
  public void testEqualsNotZeroCoeff() {
    assertThat(term(0, 0), not(term(3, 5)));
  }

  @Test
  public void testEqualsNaNCoeff() {
    Assert.assertEquals(nanTerm, term(19, 0, 0));
    Assert.assertEquals(nanTerm, term(0, 0, 0));
  }

  @Test
  public void testEqualsNotNaNCoeff() {
    assertThat(nanTerm, not(term(3, 5)));
    assertThat(term(0, 3), not(nanTerm));
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  ////	ValueOf Test
  ///////////////////////////////////////////////////////////////////////////////////////

  // All tests below depend on constructor and equals.

  private void testValueOf(String actual, RatTerm target) {
    Assert.assertEquals(target, RatTerm.valueOf(actual));
  }

  @Test
  public void testValueOfSimple() {
    testValueOf("x", term(1, 1));
    testValueOf("-x", term(-1, 1));
  }

  @Test
  public void testValueOfConst() {
    testValueOf("2", term(2, 0));
    testValueOf("3/4", term(3, 4, 0));
    testValueOf("-4", term(-4, 0));
    testValueOf("-7/5", term(-7, 5, 0));
  }

  @Test
  public void testValueOfLeadingCoeff() {
    testValueOf("2*x", term(2, 1));
    testValueOf("3/7*x", term(3, 7, 1));
    testValueOf("-4/3*x", term(-4, 3, 1));
  }

  @Test
  public void testValueOfPow() {
    testValueOf("x^3", term(1, 3));
    testValueOf("-x^4", term(-1, 4));
  }

  @Test
  public void testValueOfFull() {
    testValueOf("4*x^2", term(4, 2));
    testValueOf("2/5*x^6", term(2, 5, 6));
    testValueOf("-3/2*x^2", term(-3, 2, 2));
  }

  @Test
  public void testValueOfNaN() {
    testValueOf("NaN", term(1, 0, 0));
  }

  @Test
  public void testValueOfZero() {
    testValueOf("0", term(0, 0));
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  ////	toString Test
  ///////////////////////////////////////////////////////////////////////////////////////

  private void testToString(String target, RatTerm actual) {
    Assert.assertEquals(target, actual.toString());
  }

  @Test
  public void testToStringSimple() {
    testToString("x", term(1, 1));
    testToString("-x", term(-1, 1));
  }

  @Test
  public void testToStringConst() {
    testToString("2", term(2, 0));
    testToString("3/4", term(3, 4, 0));
    testToString("-4", term(-4, 0));
    testToString("-7/5", term(-7, 5, 0));
  }

  @Test
  public void testToStringLeadingCoeff() {
    testToString("2*x", term(2, 1));
    testToString("3/7*x", term(3, 7, 1));
    testToString("-4/3*x", term(-4, 3, 1));
  }

  @Test
  public void testToStringPow() {
    testToString("x^3", term(1, 3));
    testToString("-x^4", term(-1, 4));
  }

  @Test
  public void testToStringFull() {
    testToString("4*x^2", term(4, 2));
    testToString("2/5*x^6", term(2, 5, 6));
    testToString("-3/2*x^2", term(-3, 2, 2));
  }

  @Test
  public void testToStringNaN() {
    testToString("NaN", term(1, 0, 0));
  }

  @Test
  public void testToStringZero() {
    testToString("0", term(0, 0));
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  ////	Add Test
  ///////////////////////////////////////////////////////////////////////////////////////
  //Relies on edu.uw.cse.testbayes.Runner.tests331.RatTerm equals method and edu.uw.cse.testbayes.Runner.tests331.RatNum add method is correct

  @Test // Addition with Whole Number Coefficient
  public void testAddWholeNumCoeff() {
    Assert.assertEquals(term(3, 0), term(1, 0).add(term(2, 0))); //Zero Exponent Value
    Assert.assertEquals(term(4, 2), term(3, 2).add(term(1, 2))); //Non Zero Exponent
  }

  @Test // Addition with Fractional Number Coefficient
  public void testAddFracNumCoeff() {
    Assert.assertEquals(term(1, 2, 3), term(1, 6, 3).add(term(1, 3, 3)));
    Assert.assertEquals(term(1, 8, 1), term(1, 4, 1).add(term(-1, 8, 1)));
    Assert.assertEquals(term(-1, 8, 1), term(-1, 4, 1).add(term(1, 8, 1)));
  }

  @Test // Addition Associativity
  public void testAddAssociativity() {
    //Whole Number Coefficient
    Assert.assertEquals(term(6, 0), term(1, 0).add(term(2, 0)).add(term(3,0)));
    Assert.assertEquals(term(6, 0), term(3, 0).add(term(2, 0)).add(term(1,0)));

    //Fractional Number Coefficient
    Assert.assertEquals(term(7, 8, 3), term(1, 8, 3).add(term(1, 4, 3)).add(term(1, 2, 3)));
    Assert.assertEquals(term(7, 8, 3), term(1, 2, 3).add(term(1, 4, 3)).add(term(1, 8, 3)));
  }

  @Test // Addition Commutative
  public void testAddCommutativity() {
    Assert.assertEquals(term(1, 2, 3), term(1, 3, 3).add(term(1, 6, 3)));
    Assert.assertEquals(term(3, 0), term(2, 0).add(term(1, 0)));
  }

  @Test // Zero Term + Zero Term == Zero Term
  public void testAddZeroToZero() {
    Assert.assertEquals(zeroTerm, zeroTerm.add(zeroTerm));
  }

  @Test // t + Zero Term == t && Zero Term + t == t
  public void testAddZeroToNonZero() {
    RatTerm t = term(-2, 3);
    Assert.assertEquals(t, zeroTerm.add(t));
    Assert.assertEquals(t, t.add(zeroTerm));
  }

  @Test // NaN + NaN == NaN
  public void testAddNaNtoNaN() {
    Assert.assertEquals(nanTerm, nanTerm.add(nanTerm));
  }

  @Test // t + NaN == NaN
  public void testAddNaNtoNonNaN() {
    Assert.assertEquals(nanTerm, nanTerm.add(term(3, 4)));
    Assert.assertEquals(nanTerm, term(3, 4).add(nanTerm));
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  ////	Subtract Test
  ///////////////////////////////////////////////////////////////////////////////////////

  //Also Tests Addition inverse property

  @Test // Subtraction with Whole Number Coefficient
  public void testSubtractWholeNumCoeff() {
    Assert.assertEquals(term(1, 0), term(2, 0).sub(term(1, 0)));
    Assert.assertEquals(term(-1, 0), term(1, 0).sub(term(2, 0)));
    Assert.assertEquals(term(2, 2), term(3, 2).sub(term(1, 2)));
  }

  @Test // Subtraction with Fractional Number Coefficient
  public void testSubtractFractionalNumCoeff() {
    Assert.assertEquals(term(-1, 6, 3), term(1, 6, 3).sub(term(1, 3, 3)));
    Assert.assertEquals(term(3, 8, 1), term(1, 4, 1).sub(term(-1, 8, 1)));
    Assert.assertEquals(term(-3, 8, 1), term(-1, 4, 1).sub(term(1, 8, 1)));
  }

  @Test // Zero Term - Zero Term == Zero Term
  public void testSubtractZeroFromZero() {
    Assert.assertEquals(zeroTerm, zeroTerm.sub(zeroTerm));
  }

  //Following test method depends on correctness of negate
  @Test // t - Zero Term == t && Zero Term - t == -t
  public void testSubtractZeroAndNonZero() {
    RatTerm t = term(-2, 3);
    Assert.assertEquals(t.negate(), zeroTerm.sub(t));
    Assert.assertEquals(t, t.sub(zeroTerm));
  }

  @Test // NaN - NaN == NaN
  public void testSubtractNaNtoNaN() {
    Assert.assertEquals(nanTerm, nanTerm.sub(nanTerm));
  }

  @Test // t - NaN == NaN && NaN - t == NaN
  public void testSubtractNaNtoNonNaN() {
    Assert.assertEquals(nanTerm, nanTerm.sub(term(3, 4)));
    Assert.assertEquals(nanTerm, term(3, 4).sub(nanTerm));
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  ////	Multiplication Test
  ///////////////////////////////////////////////////////////////////////////////////////

  @Test // Multiplication with Whole Number Coefficient
  public void testMultiplicationWholeNumCoeff() {
    Assert.assertEquals(term(2, 0), term(1, 0).mul(term(2, 0))); //Zero Exponent Value
    Assert.assertEquals(term(30, 4), term(3, 2).mul(term(10, 2))); //Non Zero Exponent
  }

  @Test // Multiplication with Fractional Number Coefficient
  public void testMultiplicationFracNumCoeff() {
    Assert.assertEquals(term(1, 18, 6), term(1, 6, 3).mul(term(1, 3, 3)));
    Assert.assertEquals(term(-1, 32, 2), term(1, 4, 1).mul(term(-1, 8, 1)));
  }

  @Test // Multiplication with different Exponent Values
  public void testMultiplicationDifferentExpVal() {
    Assert.assertEquals(term(3*2, 6*3, 9+3), term(3, 6, 9).mul(term(2, 3, 3)));
    Assert.assertEquals(term(1*-1, 8*4, -1+1), term(1, 4, -1).mul(term(-1, 8, 1)));
    Assert.assertEquals(term(-1*1, 4*8, 100+10), term(-1, 4, 100).mul(term(1, 8, 10)));
  }

  @Test // Multiplication Associativity
  public void testMultiplicationAssociativity() {
    //Whole Number Coefficient
    Assert.assertEquals(term(12,5).mul(term(11, 4)).mul(term(10, 3)),
        term(10, 3).mul(term(11, 4)).mul(term(12,5)));

    //Fractional Number Coefficient
    Assert.assertEquals(term(4, 9, 3).mul(term(2, 9, 2)).mul(term(1, 3, 1)),
        term(1, 3, 1).mul(term(2, 9, 2)).mul(term(4, 9, 3)));
  }

  @Test // Multiplication Commutative
  public void testMultiplicationCommutativity() {
    //Whole Number Coefficient
    Assert.assertEquals(term(7, 1).mul(term(3, 9)), term(3, 9).mul(term(7, 1)));

    //Fractional Number Coefficient
    Assert.assertEquals(term(1, 6, 3).mul(term(1, 3, 3)), term(1, 3, 3).mul(term(1, 6, 3)));
  }

  @Test // Zero Term * Zero Term == Zero Term
  public void testMultiplicationZeroToZero() {
    Assert.assertEquals(zeroTerm, zeroTerm.mul(zeroTerm));
  }

  @Test // t * Zero Term == Zero Term && Zero Term * t == Zero Term
  public void testMultiplicationZeroToNonZero() {
    RatTerm t = term(-2, 3);
    Assert.assertEquals(zeroTerm, zeroTerm.mul(t));
    Assert.assertEquals(zeroTerm, t.mul(zeroTerm));
  }

  @Test // NaN * NaN == NaN
  public void testMultiplicationNaNtoNaN() {
    Assert.assertEquals(nanTerm, nanTerm.mul(nanTerm));
  }

  @Test // t * NaN == NaN
  public void testMultiplicationNaNtoNonNaN() {
    Assert.assertEquals(nanTerm, nanTerm.mul(term(3, 4)));
    Assert.assertEquals(nanTerm, term(3, 4).mul(nanTerm));
  }

  @Test // a * 1 == a
  public void testMultiplicationIdentity() {
    Assert.assertEquals(term(4, 3), term(4, 3).mul(one));
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  ////	Division Test
  ///////////////////////////////////////////////////////////////////////////////////////

  @Test // Division with Whole Number Coefficient
  public void testDivisionWholeNumCoeff() {
    Assert.assertEquals(term(3, 0), term(3, 2).div(term(1, 2)));
    Assert.assertEquals(term(2, 1), term(2, 1).div(term(1, 0)));
    Assert.assertEquals(term(8, 3), term(-16, 5).div(term(-2, 2)));
  }

  @Test // Division with Fractional Number Coefficient
  public void testDivisionFractionalNumCoeff() {
    Assert.assertEquals(term(1, 2, 0), term(1, 0).div(term(2, 0)));
    Assert.assertEquals(term(1, 2, 0), term(1, 6, 3).div(term(1, 3, 3)));
    Assert.assertEquals(term(-2, 0), term(1, 4, 1).div(term(-1, 8, 1)));
  }

  @Test // Zero Term / Zero Term == NaN
  public void testDivisionZeroFromZero() {
    Assert.assertEquals(nanTerm, zeroTerm.div(zeroTerm));
  }

  //Following test method depends on correctness of negate
  @Test // t / Zero Term == NaN && Zero Term / t == 0
  public void testDivisionZeroAndNonZero() {
    RatTerm t = term(-2, 3);
    Assert.assertEquals(zeroTerm, zeroTerm.div(t));
    Assert.assertEquals(nanTerm, t.div(zeroTerm));
  }

  @Test // NaN / NaN == NaN
  public void testDivisionNaNtoNaN() {
    Assert.assertEquals(nanTerm, nanTerm.div(nanTerm));
  }

  @Test // t / NaN == NaN && NaN / t == NaN
  public void testDivisionNaNtoNonNaN() {
    Assert.assertEquals(nanTerm, nanTerm.div(term(3, 4)));
    Assert.assertEquals(nanTerm, term(3, 4).div(nanTerm));
  }

  @Test // a / 1 == a
  public void testDivisionByOne() {
    Assert.assertEquals(term(4, 3), term(4, 3).div(one));
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  ////	Differentiation Test
  ///////////////////////////////////////////////////////////////////////////////////////
  //As stated in specification for any term b is assumed >= 0
  //Note : Derivative of f = f'

  // (NaN)' = NaN
  @Test
  public void testDifferentiateNaN(){
    Assert.assertEquals(nanTerm, nanTerm.differentiate());
    Assert.assertEquals(nanTerm, (new RatTerm(RatNum.NaN, 0)).differentiate());
  }

  // (edu.uw.cse.testbayes.Runner.tests331.RatTerm.ZERO)' = edu.uw.cse.testbayes.Runner.tests331.RatTerm.ZERO
  @Test
  public void testDifferentiateZero(){
    Assert.assertEquals(term(0, 0), term(0, 0).differentiate());
  }

  // constant a => (a)' = 0
  @Test
  public void testDifferentiateConstantNonZero(){
    Assert.assertEquals(term(0, 0), term(99, 0).differentiate());
    Assert.assertEquals(term(0, 0), term(1, 0).differentiate());
  }

  // Constant Multiple Rule (af)' = af'
  @Test
  public void testDifferentiateMultiplicationRule(){
    // Whole Number Coefficient
    Assert.assertEquals(term(5, 0).mul(term(1,3).differentiate()),
        (term(5, 0).mul(term(1,3))).differentiate());

    // Fractional Number Coefficient
    Assert.assertEquals(term(2,3,0).mul(term(1,4,3).differentiate()),
        (term(2,3,0).mul(term(1,4,3))).differentiate());
  }

  // Polynomial Power Rule (ax^b) = (a*b)*x^(b-1)
  @Test
  public void testDifferentiatePowerRule(){
    Assert.assertEquals(term(1, 0), term(1, 1).differentiate());
    Assert.assertEquals(term(5, 0), term(5, 1).differentiate());
    Assert.assertEquals(term(14, 1), term(7, 2).differentiate());
    Assert.assertEquals(term(-2, 3), term(-1, 2, 4).differentiate());
  }

  // Sum rule (f + g)' = f' + g'
  @Test
  public void testDifferentiateSumRule(){
    // Whole Number Coefficient
    Assert.assertEquals(((term(5, 2)).add(term(1,2))).differentiate(),
        (term(5, 2).differentiate()).add(term(1,2).differentiate()));

    // Fractional Number Coefficient
    Assert.assertEquals(((term(5, 2, 7)).add(term(1,3, 7))).differentiate(),
        (term(5, 2, 7).differentiate()).add(term(1,3, 7).differentiate()));
  }

  // Subtraction rule (f - g)' = f' - g'
  @Test
  public void testDifferentiateSubtractionRule(){
    // Whole Number Coefficient
    Assert.assertEquals(((term(5, 2)).sub(term(1,2))).differentiate(),
        (term(5, 2).differentiate()).sub(term(1,2).differentiate()));

    // Fractional Number Coefficient
    Assert.assertEquals(((term(5, 2, 7)).sub(term(1,3, 7))).differentiate(),
        (term(5, 2, 7).differentiate()).sub(term(1,3, 7).differentiate()));
  }

  // Product Rule h(x) = f(x)*g(x) => h'(x) = f'(x)g(x) + f(x)g'(x)
  @Test
  public void testDifferentiateProductRule(){
    // Whole Number Coefficient
    RatTerm init_product = term(12, 4).mul(term(5,4));
    RatTerm deriv_pt1 = (term(12, 4).differentiate()).mul(term(5,4));
    RatTerm deriv_pt2 = term(12, 4).mul(term(5,4).differentiate());

    Assert.assertEquals(init_product.differentiate() , deriv_pt1.add(deriv_pt2));

    // Fractional Number Coefficient
    init_product = term(1,2, 4).mul(term(2,3,4));
    deriv_pt1 = (term(1,2, 4).differentiate()).mul(term(2,3,4));
    deriv_pt2 = term(1,2, 4).mul(term(2,3,4).differentiate());

    Assert.assertEquals(init_product.differentiate() , deriv_pt1.add(deriv_pt2));
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  ////	AntiDifferentiation Test
  ///////////////////////////////////////////////////////////////////////////////////////
  //As stated in specification for any term b is assumed >= 0 and Integration Constant is Zero
  //Note : AntiDerivative of f(x) = F(x) , f = F
  //Note : c = Arbitrary Constant

  // Constant Rule f(x) = c => F = c*x
  @Test
  public void testAntiDifferentiateConstantRule() {
    // Zero
    Assert.assertEquals(zeroTerm, zeroTerm.antiDifferentiate());

    // Non Zero constant
    Assert.assertEquals(term(3,1), term(3,0).antiDifferentiate());
  }

  // Constant Multiple Rule f(x) = c*g(x) => F = c*G(x)
  @Test
  public void testAntiDifferentiateConstantMultipleRule() {
    // Whole Number Coefficient
    Assert.assertEquals(((term(5, 0)).mul(term(1,2))).antiDifferentiate(),
        term(5, 0).mul(term(1,2).antiDifferentiate()));

    // Fraction Coefficient
    Assert.assertEquals(((term(1,500, 0)).mul(term(1,2,3))).antiDifferentiate(),
        term(1,500, 0).mul(term(1,2,3).antiDifferentiate()));
  }

  // Power Rule f(x) = x^a => F = (x^(a+1))/(a+1)
  @Test
  public void testAntiDifferentiatePowerRule() {
    Assert.assertEquals(term(1, 2), term(2, 1).antiDifferentiate());
    Assert.assertEquals(term(4, 3, 3), term(4, 2).antiDifferentiate());
  }

  // Sum Rule if h(x) = f(x) + g(x) => H(x) = F(x) + G(x)
  @Test
  public void testAntiDifferentiateSumRule() {
    // Whole Number Coefficient
    Assert.assertEquals((term(1, 2).add(term(3,2))).antiDifferentiate(),
        term(1, 2).antiDifferentiate().add(term(3,2).antiDifferentiate()));

    // Fraction Coefficient
    Assert.assertEquals((term(3,4,5).add(term(1,2,5))).antiDifferentiate(),
        term(3,4,5).antiDifferentiate().add(term(1,2,5).antiDifferentiate()));
  }

  // Difference Rule if h(x) = f(x) - g(x) => H(x) = F(x) - G(x)
  @Test
  public void testAntiDifferentiateDifferenceRule() {
    // Whole Number Coefficient
    Assert.assertEquals((term(1, 2).sub(term(3,2))).antiDifferentiate(),
        term(1, 2).antiDifferentiate().sub(term(3,2).antiDifferentiate()));

    // Fraction Coefficient
    Assert.assertEquals((term(3,4,5).sub(term(1,2,5))).antiDifferentiate(),
        term(3,4,5).antiDifferentiate().sub(term(1,2,5).antiDifferentiate()));
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  ////	HashCode Test
  ///////////////////////////////////////////////////////////////////////////////////////

  // Same Object same hashcode after multiple queries
  @Test
  public void testHashCodeSameObject() {
    RatTerm t = term(23,45);
    for (int i = 0; i < 25 ; i++){ //Verify Non random Hascode
      Assert.assertEquals(t.hashCode(), t.hashCode());
    }
  }

  // Equals Objects same hashcode
  @Test
  public void testHashCodeEqualDiffObjects() {
    RatTerm t = term(23,45);
    RatTerm t2 = term(23,45);
    Assert.assertEquals(t.hashCode(), t2.hashCode());
  }

  // Non Equal Object different hashcode
  @Test
  public void testHashCodeNonEqualObjects() {
    RatTerm t = term(1,2);
    RatTerm t2 = term(3,2);
    RatTerm t3 = term(1,3);
    assertFalse(t.hashCode() == t2.hashCode());
    assertFalse(t.hashCode() == t3.hashCode());
  }
}
