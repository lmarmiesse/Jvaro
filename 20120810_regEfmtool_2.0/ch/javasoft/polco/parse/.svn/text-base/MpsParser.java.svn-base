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
package ch.javasoft.polco.parse;

import java.io.IOException;
import java.io.InputStream;

import org.dom4j.Element;

import ch.javasoft.math.array.NumberArrayOperations;
import ch.javasoft.math.array.parse.MatrixData;
import ch.javasoft.polco.PolyhedralCone;
import ch.javasoft.polco.config.Arithmetic;
import ch.javasoft.polco.impl.DefaultInequalityCone;
import ch.javasoft.polco.impl.DefaultPolyhedralCone;
import ch.javasoft.polco.parse.util.ArithmeticConverter;
import ch.javasoft.polco.parse.util.SortUtil;
import ch.javasoft.util.numeric.Zero;

/**
 * The <code>MpsParser</code> parses MPS .mps files.
 * <p>
 * See
 * <a href="http://lpsolve.sourceforge.net/5.5/mps-format.htm">MPS at sourceforge</a>
 * or
 * <a href="http://en.wikipedia.org/wiki/MPS_(format)">wikipedia MPS</a>
 * <p>
 * A sample problem:
 * <pre>
Optimize
 COST:    XONE + 4*YTWO + 9*ZTHREE
Subject To
 LIM1:    XONE + YTWO          <= 5
 LIM2:    XONE        + ZTHREE >= 10
 MYEQN:        - YTWO + ZTHREE  = 7
Bounds
       XONE <= 4
 -1 <= YTWO <= 1
End
 * </pre>
 * <p>
 * The same sample in MPS format looks like this:
 * <pre>
NAME          TESTPROB
ROWS
 N  COST
 L  LIM1
 G  LIM2
 E  MYEQN
COLUMNS
    XONE      COST                 1   LIM1                 1
    XONE      LIM2                 1
    YTWO      COST                 4   LIM1                 1
    YTWO      MYEQN               -1
    ZTHREE    COST                 9   LIM2                 1
    ZTHREE    MYEQN                1
RHS
    RHS1      LIM1                 5   LIM2                10
    RHS1      MYEQN                7
BOUNDS
 UP BND1      XONE                 4
 LO BND1      YTWO                -1
 UP BND1      YTWO                 1
ENDATA
 * </pre>
 */
public class MpsParser implements InputStreamParser {
	
	public <N extends Number, A> PolyhedralCone<N, A> parse(Element config, Arithmetic<N, A> arithmetic, Zero zero, InputStream in) throws IOException {
		final ch.javasoft.mps.parse.MpsParser parser = new ch.javasoft.mps.parse.MpsParser(in);
		LogPkg.LOGGER.info("parsed " + parser);
		final MatrixData ineData = parser.getInequalityMatrix();
		final MatrixData eqData = parser.getInequalityMatrix();
		final A[] ineMatrix = convertMatrix(arithmetic, zero, ineData);
		SortUtil.sortMatrix(config, arithmetic, zero, ineMatrix);
		if (eqData.getRowCount() == 0) {
			return new DefaultInequalityCone<N, A>(arithmetic.getLinAlgOperations(zero), ineMatrix);
		}
		else {
			final A[] eqMatrix = convertMatrix(arithmetic, zero, eqData);
			SortUtil.sortMatrix(config, arithmetic, zero, eqMatrix);
			return new DefaultPolyhedralCone<N, A>(arithmetic.getLinAlgOperations(zero), eqMatrix, ineMatrix);
		}
	}
	
	/**
	 * Converts the given matrix data to a matrix of the target number type
	 */
	private static <N extends Number, A> A[] convertMatrix(final Arithmetic<N, A> arithmetic, Zero zero, MatrixData matrixData) {
		final NumberArrayOperations<N, A> naops = arithmetic.getLinAlgOperations(zero).getNumberArrayOperations();
		try {
			return matrixData.getMatrixCast(naops.getNumberOperators().arrayClass());
		}
		catch (ClassCastException e) {
			return matrixData.getMatrixConverted(new ArithmeticConverter<N, A>(arithmetic, zero));
		}
	}
}
