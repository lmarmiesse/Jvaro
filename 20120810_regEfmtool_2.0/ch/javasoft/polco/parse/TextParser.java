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
import ch.javasoft.math.array.parse.MatrixParser;
import ch.javasoft.polco.PolyhedralCone;
import ch.javasoft.polco.config.Arithmetic;
import ch.javasoft.polco.impl.DefaultInequalityCone;
import ch.javasoft.polco.impl.DefaultPolyhedralCone;
import ch.javasoft.polco.parse.util.ArithmeticConverter;
import ch.javasoft.polco.parse.util.SortUtil;
import ch.javasoft.util.numeric.Zero;

/**
 * The <code>TextParser</code> parses text files containing matrix data. Each
 * line corresponds to a matrix row, column values are separated by tab or
 * whitespace.
 * <p>
 * A sample file looks like this:
 * <pre>
2 -2 1 1 1 -2
1 0 0 0 0 -1
1 -1 -1 2 2 -1
2 1 -2 1 1 -2
1 -1 2 -1 2 -1
2 1 1 -2 1 -2
0 0 0 0 1 0
1 2 -1 -1 2 -1
0 0 1 0 0 0
1 2 2 -1 -1 -1
0 1 0 0 0 0
-1 1 1 1 1 1
1 2 -1 2 -1 -1
0 0 0 1 0 0
2 1 1 1 -2 -2
1 -1 2 2 -1 -1
1 2 -1 -1 -1 2
0 0 0 0 0 1
2 1 1 -2 -2 1
1 -1 2 -1 -1 2
2 1 -2 1 -2 1
1 -1 -1 2 -1 2
1 0 0 0 -1 0
2 -2 1 1 -2 1
2 1 -2 -2 1 1
1 -1 -1 -1 2 2
1 0 0 -1 0 0
2 -2 1 -2 1 1
1 0 -1 0 0 0
2 -2 -2 1 1 1
4 -1 -1 -1 -1 -1
1 -1 0 0 0 0
 * </pre>
 */
public class TextParser {
	
	public <N extends Number, A> PolyhedralCone<N, A> parse(Element config, Arithmetic<N, A> arithmetic, Zero zero, InputStream eq, InputStream ine) throws IOException {
		final MatrixParser ineParser = new MatrixParser(ine);
		LogPkg.LOGGER.info("parsed inequality matrix " + ineParser);
		final A[] ineMatrix = convertMatrix(arithmetic, zero, ineParser.getData());
		SortUtil.sortMatrix(config, arithmetic, zero, ineMatrix);
		if (eq == null) {
			return new DefaultInequalityCone<N, A>(arithmetic.getLinAlgOperations(zero), ineMatrix);
		}
		else {
			final MatrixParser eqParser = new MatrixParser(eq);
			LogPkg.LOGGER.info("parsed equality matrix " + eqParser);
			final A[] eqMatrix = convertMatrix(arithmetic, zero, eqParser.getData());
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
