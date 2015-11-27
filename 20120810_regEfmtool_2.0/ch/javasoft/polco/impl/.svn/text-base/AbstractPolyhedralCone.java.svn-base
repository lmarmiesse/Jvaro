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
package ch.javasoft.polco.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import ch.javasoft.math.array.ArrayOperations;
import ch.javasoft.math.array.NumberArrayOperations;
import ch.javasoft.math.array.NumberOperators;
import ch.javasoft.math.linalg.LinAlgOperations;
import ch.javasoft.math.operator.UnaryOperator;
import ch.javasoft.polco.PolyhedralCone;

/**
 * Implements methods common to most implementations of {@link PolyhedralCone}, 
 * such as {@link #toString()}.
 * 
 * @type Num	the number type of a single number
 * @type Arr	the number type of an array of numbers
 */
abstract public class AbstractPolyhedralCone<Num extends Number, Arr> implements PolyhedralCone<Num, Arr> {

	protected final LinAlgOperations<Num, Arr>		linAlgOps;
	protected final ArrayOperations<Arr> 			arrayOps;
	protected final NumberArrayOperations<Num, Arr>	numberArrayOps;
	protected final NumberOperators<Num, Arr>		numberOps;
	
	/**
	 * Constructor with linalg operations
	 * 
	 * @param linAlgOps	linalg operations
	 */
	public AbstractPolyhedralCone(LinAlgOperations<Num, Arr> linAlgOps) {
		this.linAlgOps		= linAlgOps;
		this.arrayOps		= linAlgOps.getArrayOperations();
		this.numberArrayOps	= linAlgOps.getNumberArrayOperations();
		this.numberOps		= linAlgOps.getNumberOperators();
	}
	
	public LinAlgOperations<Num, Arr> getLinAlgOperations() {
		return linAlgOps;
	}
	
	@Override
	public int hashCode() {
		int code = 0;
		final int dims	= getDimensions();
		final int rowsA = getRowCountA();
		final int rowsB = getRowCountB();
		for (int c = 0; c < dims; c++) {
			for (int r = 0; r < rowsA; r++) {
				code ^= getA(r, c).hashCode();
			}
			for (int r = 0; r < rowsB; r++) {
				code ^= getB(r, c).hashCode();
			}
		}
		return code;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj instanceof PolyhedralCone) {
			final PolyhedralCone other = (PolyhedralCone)obj;
			final int dims	= getDimensions();
			final int rowsA = getRowCountA();
			final int rowsB = getRowCountB();
			if (dims != other.getDimensions()) return false;
			if (rowsA != other.getRowCountA()) return false;
			if (rowsB != other.getRowCountB()) return false;
			for (int c = 0; c < dims; c++) {
				for (int r = 0; r < rowsA; r++) {
					if (!getA(r, c).equals(other.getA(r, c))) {
						return false;
					}
				}
				for (int r = 0; r < rowsB; r++) {
					if (!getB(r, c).equals(other.getB(r, c))) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * The given vector is normalized and returned. Normalizing means for 
	 * instance reducing fraction numbers or rounding. The vector is not cloned,
	 * that is, the returned value is the same instance as {@code vec}.
	 * 
	 * @param vec	the vector to normalize
	 * @return	the normalized vector, the same instance as {@code vec}
	 */
	protected Arr normalize(Arr vec) {
		final LinAlgOperations<Num, Arr> lops = getLinAlgOperations();
		final ArrayOperations<Arr> aops = lops.getArrayOperations();
		final NumberOperators<Num, Arr> nops = lops.getNumberOperators();
		final UnaryOperator<Num, Arr> norm = nops.unary(UnaryOperator.Id.normalize);
		final int size = aops.getLength(vec);
		for (int i = 0; i < size; i++) {
			norm.operate(vec, i, vec, i);
		}
		return vec;
	}
	/**
	 * The given matrix is normalized and returned. Normalizing means for 
	 * instance reducing fraction numbers or rounding. The matrix is not cloned,
	 * that is, the returned value is the same instance as {@code mx}.
	 * 
	 * @param mx	the matrix to normalize
	 * @return	the normalized matrix, the same instance as {@code mx}
	 */
	protected Arr[] normalize(Arr[] mx) {
		for (int r = 0; r < mx.length; r++) {
			normalize(mx[r]);
		}
		return mx;
	}

	/**
	 * Returns a matrix dimension string. If {@code matrixA} is {@code true},
	 * the string is returned for matrix {@code A}, otherwise for matrix 
	 * {@code B}. The returned string looks for instance as follows: 
	 * {@code "A:7x2"}
	 */
	protected String getMatrixDimensionString(boolean matrixA) {
		final int dims = getDimensions();
		return matrixA ?
			"A:" + getRowCountA() + "x" + dims :
			"B:" + getRowCountB() + "x" + dims;
	}
	/**
	 * Returns a matrix string. If {@code matrixA} is {@code true}, the string 
	 * is returned for matrix {@code A}, otherwise for matrix {@code B}. The 
	 * returned string spans multiple lines and looks for instance as follows:
	 * <pre> 
	 * B = 5x2 {
	 *   [ 0 , -3 ]
	 *   [ 1 , 1 ]
	 *   [ -1 , 1 ]
	 *   [ 1/2 , -1/2 ]
	 *   [ 1 , 1 ]
	 * }
	 * </pre> 
	 */
	protected void appendMatrixString(boolean matrixA, PrintWriter pw) {
		final int dims = getDimensions();
		if (matrixA) {
			final int rows = getRowCountA();
			pw.println("A = " + rows + "x" + dims + " {");
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < dims; c++) {
					pw.print(c == 0 ? "  [ " : " , ");
					pw.print(getA(r, c));
				}
				pw.println(" ]");
			}
		}
		else {
			final int rows = getRowCountB();
			pw.println("B = " + rows + "x" + dims + " {");
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < dims; c++) {
					pw.print(c == 0 ? "  [ " : " , ");
					pw.print(getB(r, c));
				}
				pw.println(" ]");
			}			
		}
		pw.println("}");
	}
	public String toMultilineString() {
		final StringWriter sw = new StringWriter();
		writeToMultiline(sw);
		return sw.toString();
	}
	public void writeToMultiline(Writer writer) {
		final PrintWriter pw = writer instanceof PrintWriter ? (PrintWriter)writer : new PrintWriter(writer);
		pw.println("P = { A x = 0 , B x >= 0 }");
		appendMatrixString(true, pw);
		appendMatrixString(false, pw);
		pw.flush();
	}
	@Override
	public String toString() {
		return 
			"P = { A x = 0 , B x >= 0 , " +
			getMatrixDimensionString(true) + " , " +
			getMatrixDimensionString(false) + " }";
	}
	

}
