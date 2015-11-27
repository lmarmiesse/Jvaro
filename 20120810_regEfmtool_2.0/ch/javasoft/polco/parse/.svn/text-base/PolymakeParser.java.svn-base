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
import ch.javasoft.polco.PolyhedralCone;
import ch.javasoft.polco.config.Arithmetic;
import ch.javasoft.polco.impl.DefaultInequalityCone;
import ch.javasoft.polco.parse.util.ArithmeticConverter;
import ch.javasoft.polco.parse.util.SortUtil;
import ch.javasoft.polymake.parse.PolymakeMatrixData;
import ch.javasoft.util.numeric.Zero;

/**
 * The <code>PolymakeParser</code> parses polymake .poly files.
 * <p>
 * See
 * <a href="http://www.math.tu-berlin.de/polymake">http://www.math.tu-berlin.de/polymake</a>
 * <p>
 * A sample file looks like this:
 * <pre>
DESCRIPTION

A 5-dimensional cross polytope.

DIM
5

VERTICES
1 1 0 0 0 0
1 0 1 0 0 0
1 0 0 1 0 0
1 0 0 0 1 0
1 0 0 0 0 1 
1 0 1 1 1 1
1 1 0 1 1 1
1 1 1 0 1 1
1 1 1 1 0 1 
1 1 1 1 1 0

FACETS
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

AFFINE_HULL

N_FACETS
32

AMBIENT_DIM
5


_version 1.5.1
_application polytope
 * </pre>
 */
public class PolymakeParser implements InputStreamParser {
	
	public <N extends Number, A> PolyhedralCone<N, A> parse(Element config, Arithmetic<N, A> arithmetic, Zero zero, InputStream in) throws IOException {
		final ch.javasoft.polymake.parse.PolymakeParser parser = new ch.javasoft.polymake.parse.PolymakeParser(in);
		LogPkg.LOGGER.info("parsed " + parser);
		LogPkg.LOGGER.info("using " + parser.getData(0).getDefinitionType() + " as algorithm input");
		final A[] matrix = getMatrix(arithmetic, zero, parser.getData(0));
		SortUtil.sortMatrix(config, arithmetic, zero, matrix);
		return new DefaultInequalityCone<N, A>(arithmetic.getLinAlgOperations(zero), matrix);
	}
	
	/**
	 * Converts the given matrix data to a matrix of the target number type
	 */
	private static <N extends Number, A> A[] getMatrix(final Arithmetic<N, A> arithmetic, Zero zero, PolymakeMatrixData polymakeMatrixData) {
		final NumberArrayOperations<N, A> naops = arithmetic.getLinAlgOperations(zero).getNumberArrayOperations();
		try {
			return polymakeMatrixData.getMatrixCast(naops.getNumberOperators().arrayClass());
		}
		catch (ClassCastException e) {
			return polymakeMatrixData.getMatrixConverted(new ArithmeticConverter<N, A>(arithmetic, zero));
		}
	}
}
