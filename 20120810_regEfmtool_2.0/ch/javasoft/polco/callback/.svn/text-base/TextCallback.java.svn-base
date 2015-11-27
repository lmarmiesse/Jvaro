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
package ch.javasoft.polco.callback;

import java.io.OutputStream;
import java.io.PrintStream;

import ch.javasoft.math.array.NumberArrayOperations;
import ch.javasoft.polco.xenum.ExtremeRayCallback;
import ch.javasoft.polco.xenum.ExtremeRayEvent;

/**
 * The <code>TextCallback</code> writes the output to a text file. Each ray is
 * written to a line, values are separated by tab.
 * 
 * @type Num	the number type of a single number
 * @type Arr	the number type of an array of numbers
 */
public class TextCallback<Num extends Number, Arr> implements ExtremeRayCallback<Num, Arr> {

	private final PrintStream print;
	
	/**
	 * Constructor with output stream to write to
	 */
	public TextCallback(OutputStream out) {
		print = out instanceof PrintStream ? (PrintStream)out : new PrintStream(out);
	}
	
	public boolean initialize(ExtremeRayEvent<Num, Arr> event) {
		return true;//ray output desired
	}

	public void outputExtremeRay(ExtremeRayEvent<Num, Arr> event, long index, Arr extremeRay) {
		final NumberArrayOperations<Num, Arr> naops = event.getPolyhedralCone().getLinAlgOperations().getNumberArrayOperations();
		final int cnt = naops.getArrayOperations().getLength(extremeRay);
		for (int i = 0; i < cnt; i++) {
			if (i > 0) print.print('\t');
			print.print(naops.get(extremeRay, i));
		}
		print.println();
	}

	public void terminate(ExtremeRayEvent<Num, Arr> event) {
		print.flush();
	}

}
