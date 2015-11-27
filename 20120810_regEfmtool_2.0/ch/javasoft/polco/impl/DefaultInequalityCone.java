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
import ch.javasoft.polco.InequalityPolyhedralCone;

/**
 * Default implementation of {@link InequalityPolyhedralCone}, a polyhedral cone
 * <i>P</i> in halfspace representation, that is, 
 * <pre>
 * P = { B x &ge; 0 }
 * </pre>
 * An instance is {@link #DefaultInequalityCone(LinAlgOperations, Object[]) constructed}
 * from the dense matrix B.
 * 
 * @type Num	the number type of a single number
 * @type Arr	the number type of an array of numbers
 */
public class DefaultInequalityCone<Num extends Number, Arr> extends AbstractInequalityCone<Num, Arr> {

	private final Arr[] mxB;
	
	/**
	 * Constructor with inequality matrix B. The matrix is not cloned.
	 * 
	 * @param linAlgOps	linalg operations
	 * @param mxB		the inequality matrix B
	 */
	public DefaultInequalityCone(LinAlgOperations<Num, Arr> linAlgOps, Arr[] mxB) {
		super(linAlgOps);
		this.mxB = normalize(mxB);
//		final int fullB = Math.min(getDimensions(), getRowCountB());
//		final int rankB = linAlgOps.rank(this.mxB);
//		if (rankB < fullB) {
//			throw new RuntimeException("matrix B has not full rank, " + rankB + 
//					" < " + fullB);
//		}
	}
	
	public int getDimensions() {
		return getLinAlgOperations().getArrayOperations().getColumnCount(mxB);
	}

	public int getRowCountB() {
		return getLinAlgOperations().getArrayOperations().getRowCount(mxB);
	}

	public Arr[] getB() {
		return mxB;
	}

	public Num getB(int row, int col) {
		return getLinAlgOperations().getNumberArrayOperations().get(mxB, row, col);
	}
}
