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

import ch.javasoft.math.linalg.LinAlgOperations;
import ch.javasoft.polco.PolyhedralCone;

/**
 * Default implementation of {@link PolyhedralCone}, a general polyhedral cone
 * <i>P</i> in hyperplane and halfspace representation, that is, 
 * <pre>
 * P = { A x = 0 , B x &ge; 0 }
 * </pre>
 * An instance is {@link #DefaultPolyhedralCone(LinAlgOperations, Object[], Object[]) constructed}
 * from the two dense matrices A and B.
 * 
 * @type Num	the number type of a single number
 * @type Arr	the number type of an array of numbers
 */
public class DefaultPolyhedralCone<Num extends Number, Arr> extends AbstractPolyhedralCone<Num, Arr> implements PolyhedralCone<Num, Arr> {

	private final Arr[] mxA;
	private final Arr[] mxB;
	
	/**
	 * Constructor with equality matrix A and inequality matrix B. The matrices 
	 * are not cloned. If the number of columns in A and B are not equal, an 
	 * exception is thrown.
	 * 
	 * @param linAlgOps	linalg operations
	 * @param mxA		the equality matrix A
	 * @param mxB		the inequality matrix B
	 * @throws IllegalArgumentException if A and B have not the same number of 
	 * 									columns
	 */
	public DefaultPolyhedralCone(LinAlgOperations<Num, Arr> linAlgOps, Arr[] mxA, Arr[] mxB) {
		super(linAlgOps);
		if (arrayOps.getColumnCount(mxA) != arrayOps.getColumnCount(mxB)) {
			throw new IllegalArgumentException("column size of A and B must be equal: " + 
					arrayOps.getColumnCount(mxA) + "!=" + arrayOps.getColumnCount(mxB)	
			);
		}
		this.mxA = normalize(mxA);
		this.mxB = normalize(mxB);
//		final int fullA = Math.min(getDimensions(), getRowCountA());
//		final int fullB = Math.min(getDimensions(), getRowCountB());
//		final int rankA = linAlgOps.rank(this.mxA);
//		final int rankB = linAlgOps.rank(this.mxB);
//		if (rankA < fullA) {
//			throw new RuntimeException("matrix A has not full rank, " + rankA + 
//					" < " + fullA);
//		}
//		if (rankB < fullB) {
//			throw new RuntimeException("matrix B has not full rank, " + rankB + 
//					" < " + fullB);
//		}
	}
	
	public int getDimensions() {
		return linAlgOps.getArrayOperations().getColumnCount(mxA);
	}

	public int getRowCountA() {
		return linAlgOps.getArrayOperations().getRowCount(mxA);
	}

	public Arr[] getA() {
		return mxA;
	}

	public Num getA(int row, int col) {
		return linAlgOps.getNumberArrayOperations().get(mxA, row, col);
	}

	public int getRowCountB() {
		return linAlgOps.getArrayOperations().getRowCount(mxB);
	}

	public Arr[] getB() {
		return mxB;
	}

	public Num getB(int row, int col) {
		return linAlgOps.getNumberArrayOperations().get(mxB, row, col);
	}

}
