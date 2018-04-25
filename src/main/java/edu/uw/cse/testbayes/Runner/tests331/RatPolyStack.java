package edu.uw.cse.testbayes.Runner.tests331;

import java.util.Iterator;
import java.util.Stack;

/**
 * <b>edu.uw.cse.testbayes.Runner.tests331.RatPolyStack</B> is a mutable finite sequence of edu.uw.cse.testbayes.Runner.tests331.RatPoly objects.
 * <p>
 * Each edu.uw.cse.testbayes.Runner.tests331.RatPolyStack can be described by [p1, p2, ... ], where [] is an empty
 * stack, [p1] is a one element stack containing the Poly 'p1', and so on.
 * RatPolyStacks can also be described constructively, with the append
 * operation, ':'. such that [p1]:S is the result of putting p1 at the front of
 * the edu.uw.cse.testbayes.Runner.tests331.RatPolyStack S.
 * <p>
 * A finite sequence has an associated size, corresponding to the number of
 * elements in the sequence. Thus the size of [] is 0, the size of [p1] is 1,
 * the size of [p1, p1] is 2, and so on.
 * <p>
 */
public final class RatPolyStack implements Iterable<RatPoly> {

  private final Stack<RatPoly> polys;

  // Abstraction Function:
  // Each element of a edu.uw.cse.testbayes.Runner.tests331.RatPolyStack, s, is mapped to the
  // corresponding element of polys.
  //
  // RepInvariant:
  // polys != null &&
  // forall i such that (0 <= i < polys.size(), polys.get(i) != null

  /**
   * @effects Constructs a new edu.uw.cse.testbayes.Runner.tests331.RatPolyStack, [].
   */
  public RatPolyStack() {
    polys = new Stack<RatPoly>();
    checkRep();
  }

  /**
   * Returns the number of RayPolys in this edu.uw.cse.testbayes.Runner.tests331.RatPolyStack.
   *
   * @return the size of this sequence.
   */
  public int size() {
    return polys.size();
  }

  /**
   * Pushes a edu.uw.cse.testbayes.Runner.tests331.RatPoly onto the top of this.
   *
   * @param p The edu.uw.cse.testbayes.Runner.tests331.RatPoly to push onto this stack.
   * @requires p != null
   * @modifies this
   * @effects this_post = [p]:this
   */
  public void push(RatPoly p) {
    polys.push(p);
  }

  /**
   * Removes and returns the top edu.uw.cse.testbayes.Runner.tests331.RatPoly.
   *
   * @requires this.size() > 0
   * @modifies this
   * @effects If this = [p]:S then this_post = S
   * @return p where this = [p]:S
   */
  public RatPoly pop() {
    return polys.pop();
  }

  /**
   * Duplicates the top edu.uw.cse.testbayes.Runner.tests331.RatPoly on this.
   *
   * @requires this.size() > 0
   * @modifies this
   * @effects If this = [p]:S then this_post = [p, p]:S
   */
  public void dup() {
    // Gets the top poly, and adds it twice back
	RatPoly top = polys.pop();
	polys.push(top);
	polys.push(top);
  }

  /**
   * Swaps the top two elements of this.
   *
   * @requires this.size() >= 2
   * @modifies this
   * @effects If this = [p1, p2]:S then this_post = [p2, p1]:S
   */
  public void swap() {
    // Gets top two polys, and adds them in opposite order
    RatPoly newBot = polys.pop();
    RatPoly newTop = polys.pop();
    polys.push(newBot);
    polys.push(newTop);
  }

  /**
   * Clears the stack.
   *
   * @modifies this
   * @effects this_post = []
   */
  public void clear() {
    polys.clear();
  }

  /**
   * Returns the edu.uw.cse.testbayes.Runner.tests331.RatPoly that is 'index' elements from the top of the stack.
   *
   * @param index The index of the edu.uw.cse.testbayes.Runner.tests331.RatPoly to be retrieved.
   * @requires index >= 0 && index < this.size()
   * @return If this = S:[p]:T where S.size() = index, then returns p.
   */
  public RatPoly getNthFromTop(int index) {
    // Stack for holding the 'index' number of elements to be popped off
	Stack<RatPoly> tempStack = new Stack<RatPoly>();
	for (int p = 0; p < index; p++) {
	  tempStack.push(polys.pop());
	}
	
	// Get the edu.uw.cse.testbayes.Runner.tests331.RatPoly to be returned, and put it back
	RatPoly ret = polys.pop();
	polys.push(ret);
	
	// Pushes the RatPolys that were held in our temp variable
	// back onto the original stack
	for (int p = 0; p < index; p++) {
	  polys.push(tempStack.pop());
	}
	
	return ret;
  }

  /**
   * Pops two elements off of the stack, adds them, and places the result on
   * top of the stack.
   *
   * @requires this.size() >= 2
   * @modifies this
   * @effects If this = [p1, p2]:S then this_post = [p3]:S where p3 = p1 + p2
   */
  public void add() {
    // Gets the two elements from the top of the stack
	RatPoly firTop = polys.pop();
	RatPoly secTop = polys.pop();
	polys.push(firTop.add(secTop));
  }

  /**
   * Subtracts the top poly from the next from top poly, pops both off the
   * stack, and places the result on top of the stack.
   *
   * @requires this.size() >= 2
   * @modifies this
   * @effects If this = [p1, p2]:S then this_post = [p3]:S where p3 = p2 - p1
   */
  public void sub() {
    // Gets the two elements from the top of the stack
	RatPoly firTop = polys.pop();
	RatPoly secTop = polys.pop();
	polys.push(secTop.sub(firTop));
  }

  /**
   * Pops two elements off of the stack, multiplies them, and places the
   * result on top of the stack.
   *
   * @requires this.size() >= 2
   * @modifies this
   * @effects If this = [p1, p2]:S then this_post = [p3]:S where p3 = p1 * p2
   */
  public void mul() {
    // Gets the two elements from the top of the stack
	RatPoly firTop = polys.pop();
	RatPoly secTop = polys.pop();
	polys.push(firTop.mul(secTop));
  }

  /**
   * Divides the next from top poly by the top poly, pops both off the stack,
   * and places the result on top of the stack.
   *
   * @requires this.size() >= 2
   * @modifies this
   * @effects If this = [p1, p2]:S then this_post = [p3]:S where p3 = p2 / p1
   */
  public void div() {
    // Gets the two elements from the top of the stack
	RatPoly firTop = polys.pop();
	RatPoly secTop = polys.pop();
	polys.push(secTop.div(firTop));
  }

  /**
   * Pops the top element off of the stack, differentiates it, and places the
   * result on top of the stack.
   *
   * @requires this.size() >= 1
   * @modifies this
   * @effects If this = [p1]:S then this_post = [p2]:S where p2 = derivative
   *          of p1
   */
  public void differentiate() {
	// Gets the top element to be differentiated
	RatPoly top = polys.pop();
	polys.push(top.differentiate());
  }

  /**
   * Pops the top element off of the stack, integrates it, and places the
   * result on top of the stack.
   *
   * @requires this.size() >= 1
   * @modifies this
   * @effects If this = [p1]:S then this_post = [p2]:S where p2 = indefinite
   *          integral of p1 with integration constant 0
   */
  public void integrate() {
	// Gets the top element to be differentiated
	RatPoly top = polys.pop();
	polys.push(top.antiDifferentiate(new RatNum(0)));
  }

  /**
   * Returns an iterator of the elements contained in the stack.
   *
   * @return an iterator of the elements contained in the stack in order from
   *         the bottom of the stack to the top of the stack.
   */
  public Iterator<RatPoly> iterator() {
    return polys.iterator();
  }

  /**
   * Checks that the representation invariant holds (if any).
   */
  private void checkRep() {
    assert (polys != null) : "polys should never be null.";
    
    for (RatPoly p : polys) {
        assert (p != null) : "polys should never contain a null element.";
    }
  }
}
