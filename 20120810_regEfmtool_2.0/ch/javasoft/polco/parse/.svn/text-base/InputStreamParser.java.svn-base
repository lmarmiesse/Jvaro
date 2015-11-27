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

import ch.javasoft.polco.PolyhedralCone;
import ch.javasoft.polco.config.Arithmetic;
import ch.javasoft.util.numeric.Zero;

/**
 * The <code>InputStreamParser</code> parses a polyhedral cone from an 
 * {@link InputStream}.
 */
public interface InputStreamParser {
	
	/**
	 * Parses and returns a polyhedral cone. The cone data is read from the
	 * given input stream. The expected data format depends on the implementing
	 * parser class. 
	 *  
	 * @type N	the number type of a single number
	 * @type A	the number type of an array of numbers
	 * 
	 * @param config		the config to read extra settings, if needed (might
	 * 						be null for local JUnitTest use)
	 * @param arithmetic	the arithmetic used to define numeric types
	 * @param zero			a zero object to treat almost zero values
	 * @param in			the input stream with the cone definition
	 * @return the parsed polyhedral cone
	 * 
	 * @throws IOException 	if an I/O exception occured while reading from the 
	 * 						stream
	 */
	<N extends Number, A> PolyhedralCone<N, A> parse(Element config, Arithmetic<N, A> arithmetic, Zero zero, InputStream in) throws IOException;
	
}
