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
import java.io.Writer;

import ch.javasoft.math.linalg.LinAlgOperations;
import ch.javasoft.polco.EqualityPolyhedralCone;
import ch.javasoft.polco.PolyhedralCone;

/**
 * The <code>AbstractEqualityCone</code> mainly implements the methods of the
 * {@link PolyhedralCone}.
 */
abstract public class AbstractEqualityCone<Num extends Number, Arr> extends AbstractPolyhedralCone<Num, Arr> implements EqualityPolyhedralCone<Num, Arr> {
	
	public AbstractEqualityCone(LinAlgOperations<Num, Arr> ops) {
		super(ops);
	}

	public int getRowCountB() {
		return getDimensions();
	}
	/**
	 * Returns an identity matrix with {@link #getDimensions() dim} rows and 
	 * columns
	 */
	public Arr[] getB() {
		return numberArrayOps.newIdentityMatrix(getDimensions());
	}
	/**
	 * Returns one if {@code row == col}, and zero otherwise.
	 */
	public Num getB(int row, int col) {
		return row == col ? numberOps.one() : numberOps.zero();
	}
	@Override
	public void writeToMultiline(Writer writer) {
		final PrintWriter pw = writer instanceof PrintWriter ? (PrintWriter)writer : new PrintWriter(writer);
		pw.println("P = { A x = 0 , x >= 0 }");
		appendMatrixString(true, pw);
		pw.flush();
	}
	@Override
	public String toString() {
		return 
			"P = { A x = 0 , x >= 0 , " +
			getMatrixDimensionString(true) + " }";
	}

}
