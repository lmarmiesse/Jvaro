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
import ch.javasoft.polco.EqualityPolyhedralCone;

/**
 * Default implementation of {@link EqualityPolyhedralCone}, a polyhedral cone
 * <i>P</i> in hyperplane representation with non-zero variables, that is, 
 * <pre>
 * P = { A x = 0 , x &ge; 0 }
 * </pre>
 * An instance is {@link #DefaultEqualityCone(LinAlgOperations, Object[]) constructed}
 * from the dense matrix A.
 * 
 * @type Num	the number type of a single number
 * @type Arr	the number type of an array of numbers
 */
public class DefaultEqualityCone<Num extends Number, Arr> extends AbstractEqualityCone<Num, Arr> {

	private final Arr[] mxA;
	
	/**
	 * Constructor with equality matrix A. The matrix is not cloned.
	 * 
	 * @param linAlgOps	linalg operations
	 * @param mxA		the equality matrix A
	 */
	public DefaultEqualityCone(LinAlgOperations<Num, Arr> linAlgOps, Arr[] mxA) {
		super(linAlgOps);
		this.mxA = normalize(mxA);
//		final int fullA = Math.min(getDimensions(), getRowCountA());
//		final int rankA = linAlgOps.rank(this.mxA);
//		if (rankA < fullA) {
//			throw new RuntimeException("matrix A has not full rank, " + rankA + 
//					" < " + fullA);
//		}
	}
	
	public int getDimensions() {
		return getLinAlgOperations().getArrayOperations().getColumnCount(mxA);
	}

	public int getRowCountA() {
		return getLinAlgOperations().getArrayOperations().getRowCount(mxA);
	}

	public Arr[] getA() {
		return mxA;
	}

	public Num getA(int row, int col) {
		return getLinAlgOperations().getNumberArrayOperations().get(mxA, row, col);
	}

}
