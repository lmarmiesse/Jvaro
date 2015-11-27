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

/**
 * The <code>EqualityPolyhedralCone</code> represents a polyhedral cone <i>P</i>
 * in hyperplane representation with non-zero variables, that is, 
 * <pre>
 * P = { A x = 0 , x &ge; 0 }
 * </pre>
 * Note that this interface extends {@link PolyhedralCone}, but does not add any
 * additional methods, and hence is only a marker interface. Some methods below
 * are restated unchanged, but with a specialized description. 
 * 
 * @type Num	the number type of a single number
 * @type Arr	the number type of an array of numbers
 */
public interface EqualityPolyhedralCone<Num extends Number, Arr> extends PolyhedralCone<Num, Arr> {
	/**
	 * Returns an identity matrix, corresponding to the inequality matrix B
	 * in the definition of the polyhedral cone. For this equality cone, the
	 * inequalities are simply non-negativity constraints, hence, an identity
	 * matrix is returned.
	 *  
	 * @return	a two dimensional array representing the {@code d x d} identity
	 * 			matrix, corresponding to the inequality matrix B. The first 
	 * 			dimension enumerates rows, the second columns
	 */
	Arr[] getB();
	/**
	 * Returns a single number of the inequality matrix B. Here, B is an 
	 * identity matrix, hence one is returned if {@code row==col}, and zero
	 * otherwise.
	 */
	Num getB(int row, int col);
	/**
	 * Returns the number inequalities, that is, the number of rows in B. Here,
	 * this equals the number of {@link #getDimensions() dimensions}, since B
	 * is an identity matrix.
	 */
	int getRowCountB();
	/**
	 * Returns a verbose string representation of this polyhedral cone, 
	 * including the matrix values and spanning multiple lines.
	 * <p>
	 * If both matrices are non-empty, the returned string looks for instance as 
	 * follows:
	 * <pre>
	 * P = { A x = 0 , x >= 0 }
	 * A = 7x2 {
	 *   [ 1 , 1 ]
	 *   [ -1 , 1 ]
	 *   [ 0 , -3 ]
	 *   [ 1 , 1 ]
	 *   [ 0 , -3 ]
	 *   [ -1 , -1/2 ]
	 *   [ 1/2 , -1/2 ]
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
	 * P = { A x = 0 , x >= 0 }
	 * A = 7x2 {
	 *   [ 1 , 1 ]
	 *   [ -1 , 1 ]
	 *   [ 0 , -3 ]
	 *   [ 1 , 1 ]
	 *   [ 0 , -3 ]
	 *   [ -1 , -1/2 ]
	 *   [ 1/2 , -1/2 ]
	 * }
	 * </pre>
	 */
	String toMultilineString();
	/**
	 * Returns a short string representation of this polyhedral cone, without
	 * matrix values, but including matrix dimensions.
	 * <p>
	 * The returned string looks for instance as follows:
	 * <pre>
	 * P = { A x = 0 , x >= 0 , A:7x2 }
	 * </pre>
	 */
	String toString();
}
