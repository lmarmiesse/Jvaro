/*
 * =============================================================================
 * Simplified BSD License, see http://www.opensource.org/licenses/
 * -----------------------------------------------------------------------------
 * Copyright (c) 2008-2009, Marco Terzer, Zurich, Switzerland
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice, 
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the 
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Swiss Federal Institute of Technology Zurich 
 *       nor the names of its contributors may be used to endorse or promote 
 *       products derived from this software without specific prior written 
 *       permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * =============================================================================
 */
package ch.javasoft.polco;

import java.io.Writer;

import ch.javasoft.math.linalg.LinAlgOperations;

/**
 * The <code>PolyhedralCone</code> represents a general polyhedral cone 
 * <i>P</i> in hyperplane and halfspace representation, that is, 
 * <pre>
 * P = { A x = 0 , B x &ge; 0 }
 * </pre>
 * 
 * @type Num	the number type of a single number
 * @type Arr	the number type of an array of numbers
 */
public interface PolyhedralCone<Num extends Number, Arr> extends Numeric<Num, Arr> {
	/**
	 * Returns the matrix A corresponding to the equalities in the definition of
	 * the polyhedral cone.
	 * 
	 * @return	a two dimensional array representing the equality matrix, 
	 * 			where the first dimension enumerates rows, the second columns
	 */
	Arr[] getA();
	/**
	 * Returns the matrix B corresponding to the inequalities in the definition 
	 * of the polyhedral cone.
	 * 
	 * @return	a two dimensional array representing the inequality matrix, 
	 * 			where the first dimension enumerates rows, the second columns
	 */
	Arr[] getB();
	/**
	 * Returns a single number of the equality matrix A
	 */
	Num getA(int row, int col);
	/**
	 * Returns a single number of the inequality matrix B
	 */
	Num getB(int row, int col);
	/**
	 * Returns the number of equalities, that is, the number of rows in A
	 */
	int getRowCountA();
	/**
	 * Returns the number inequalities, that is, the number of rows in B
	 */
	int getRowCountB();
	/**
	 * Returns the number dimensions, that is, the size of the vector x or
	 * equivalently, the number of columns in A and B
	 */
	int getDimensions();
	/**
	 * Returns the operations object used to perform basic mathematical and
	 * linear algebra computations on the number type used for this cone 
	 */
	LinAlgOperations<Num, Arr> getLinAlgOperations();
	
	/**
	 * Returns a hash code created by combining all values of the matrices 
	 * {@link #getA() A} and {@link #getB()} using the XOR function.
	 */
	int hashCode();

	/**
	 * Returns true if the other object {@code obj} is a {@link PolyhedralCone} 
	 * instance and if both matrices {@link #getA() A} and {@link #getB()} of 
	 * the two cones are equal.
	 */
	@Override
	boolean equals(Object obj);
	
	/**
	 * Returns a verbose string representation of this polyhedral cone, 
	 * including the matrix values and spanning multiple lines.
	 * <p>
	 * If both matrices are non-empty, the returned string looks for instance as 
	 * follows:
	 * <pre>
	 * P = { A x = 0 , B x >= 0 }
	 * A = 7x2 {
	 *   [ 1 , 1 ]
	 *   [ -1 , 1 ]
	 *   [ 0 , -3 ]
	 *   [ 1 , 1 ]
	 *   [ 0 , -3 ]
	 *   [ -1 , -1/2 ]
	 *   [ 1/2 , -1/2 ]
	 * }
	 * B = 5x2 {
	 *   [ 0 , -3 ]
	 *   [ 1 , 1 ]
	 *   [ -1 , 1 ]
	 *   [ 1/2 , -1/2 ]
	 *   [ 1 , 1 ]
	 * }
	 * </pre>
	 */
	void writeToMultiline(Writer writer);
	
	/**
	 * Writes a verbose string representation of this polyhedral cone using the
	 * specified {@code writer}. The output includes the matrix values and spans
	 * multiple lines.
	 * <p>
	 * If both matrices are non-empty, the written string looks for instance as 
	 * follows:
	 * <pre>
	 * P = { A x = 0 , B x >= 0 }
	 * A = 7x2 {
	 *   [ 1 , 1 ]
	 *   [ -1 , 1 ]
	 *   [ 0 , -3 ]
	 *   [ 1 , 1 ]
	 *   [ 0 , -3 ]
	 *   [ -1 , -1/2 ]
	 *   [ 1/2 , -1/2 ]
	 * }
	 * B = 5x2 {
	 *   [ 0 , -3 ]
	 *   [ 1 , 1 ]
	 *   [ -1 , 1 ]
	 *   [ 1/2 , -1/2 ]
	 *   [ 1 , 1 ]
	 * }
	 * </pre>
	 */
	String toMultilineString();
	
	/**
	 * Returns a short string representation of this polyhedral cone, without
	 * matrix values, but including matrix dimensions.
	 * <p>
	 * If both matrices are non-empty, the returned string looks for instance as 
	 * follows:
	 * <pre>
	 * P = { A x = 0 , B x >= 0 , A:7x2 , B:5x2 }
	 * </pre>
	 */
	String toString();
	
}
