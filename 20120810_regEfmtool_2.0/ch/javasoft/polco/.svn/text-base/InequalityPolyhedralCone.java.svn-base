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
 * The <code>InequalityPolyhedralCone</code> represents a polyhedral cone
 *  <i>P</i> in halfspace representation, that is, 
 * <pre>
 * P = { B x &ge; 0 }
 * </pre>
 * Note that this interface extends {@link PolyhedralCone}, but does not add any
 * additional methods, and hence is only a marker interface. Some methods below
 * are restated unchanged, but with a specialized description. 
 * 
 * @type Num	the number type of a single number
 * @type Arr	the number type of an array of numbers
 */
public interface InequalityPolyhedralCone<Num extends Number, Arr> extends PolyhedralCone<Num, Arr> {
	/**
	 * Returns an empty matrix, corresponding to the equality matrix in the 
	 * definition of the polyhedral cone. For this inequality cone, only
	 * inequalities are used to define the cone.
	 * 
	 * @return	an empty array, since this cone is defined without equalities 
	 */
	Arr[] getA();
	/**
	 * Throws always an {@link IndexOutOfBoundsException}, since matrix A 
	 * contains equalities and this cone is defined solely by inequalities
	 * 
	 * @throws IndexOutOfBoundsException always
	 */
	Num getA(int row, int col);
	
	/**
	 * Returns always 0 since matrix A contains equalities and this cone is 
	 * defined solely by inequalities
	 * 
	 * @return always zero
	 */
	int getRowCountA();
	
	/**
	 * Returns a verbose string representation of this polyhedral cone, 
	 * including the matrix values and spanning multiple lines.
	 * <p>
	 * If both matrices are non-empty, the returned string looks for instance as 
	 * follows:
	 * <pre>
	 * P = { B x >= 0 }
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
	 * P = { B x >= 0 }
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
	 * The returned string looks for instance as follows:
	 * <pre>
	 * P = { B x >= 0 , B:5x2 }
	 * </pre>
	 */
	String toString();
}
