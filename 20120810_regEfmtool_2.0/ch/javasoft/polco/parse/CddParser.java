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
import ch.javasoft.util.numeric.Zero;

/**
 * The <code>CddParser</code> parses cdd .ine or .ext files.
 * <p>
 * A definition of the file formats can be found here:<br/>
 * <a href="ftp://ftp.ifor.math.ethz.ch/pub/fukuda/cdd/cddman/node3.html">ftp://ftp.ifor.math.ethz.ch/pub/fukuda/cdd/cddman/node3.html</a>
 */
public class CddParser implements InputStreamParser {
	
	public <N extends Number, A> PolyhedralCone<N, A> parse(Element config, Arithmetic<N, A> arithmetic, Zero zero, InputStream in) throws IOException {
		final ch.javasoft.cdd.parser.CddParser parser = new ch.javasoft.cdd.parser.CddParser(in);
		LogPkg.LOGGER.info("parsed " + parser);
		final A[] matrix = getMatrix(arithmetic, zero, parser);
		SortUtil.sortMatrix(config, arithmetic, zero, matrix);
		return new DefaultInequalityCone<N, A>(arithmetic.getLinAlgOperations(zero), matrix);
	}
	
	/**
	 * Converts the given matrix data to a matrix of the target number type
	 */
	private static <N extends Number, A> A[] getMatrix(final Arithmetic<N, A> arithmetic, Zero zero, ch.javasoft.cdd.parser.CddParser parser) {
		final NumberArrayOperations<N, A> naops = arithmetic.getLinAlgOperations(zero).getNumberArrayOperations();
		try {
			return parser.getMatrixCast(naops.getNumberOperators().arrayClass());
		}
		catch (ClassCastException e) {
			return parser.getMatrixConverted(new ArithmeticConverter<N, A>(arithmetic, zero));
		}
	}
}
