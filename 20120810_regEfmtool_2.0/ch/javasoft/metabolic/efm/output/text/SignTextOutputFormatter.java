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
package ch.javasoft.metabolic.efm.output.text;

import java.io.PrintWriter;

import ch.javasoft.metabolic.efm.output.EfmOutputCallback;
import ch.javasoft.metabolic.efm.output.EfmOutputEvent;
import ch.javasoft.util.numeric.DoubleUtil;

/**
 * The <tt>SignTextOutputFormatter</tt> optionally writes a header line with 
 * output options and number of modes. Then, the flux directions (flux signs) of 
 * the efm's are written. Each mode is printed on a new line, values are 
 * separated by tab. If desired, the 0-based mode index is prefixed on each 
 * line.
 * <p>
 * Sample output of this formatter, with header line and mode number prefix:
 * <pre>
	efm-output[8 modes, SignTextOutputFormatter, Mon Sep 08 11:15:52 CEST 2008]
	0	1	-1	0	0	0	1	0	-1	0	0
	1	1	0	1	1	1	0	1	1	0	1
	2	1	-1	0	0	1	0	0	0	0	0
	3	1	0	1	0	0	1	0	-1	1	0
	4	1	0	1	0	1	0	0	0	1	0
	5	1	1	1	1	0	0	1	1	0	1
	6	0	1	1	0	0	0	0	0	1	0
	7	1	0	1	1	0	1	1	0	0	1
 * </pre>
 */
public class SignTextOutputFormatter extends AbstractTextOutputFormatter {
	
	/**
	 * Constructor with options
	 * 
	 * @param headerLine	if true, a header line will be added to the output
	 * @param modeIndex		if true, each efm line is prefixed with the mode
	 * 						index (0 based)
	 */
	public SignTextOutputFormatter(boolean headerLine, boolean modeIndex) {
		super(headerLine, modeIndex);
	}
	
	@Override
	public void formatEfmValue(EfmOutputCallback cb, PrintWriter writer, EfmOutputEvent evt, long efmIndex, int valueIndex, Number value) {
		if (valueIndex > 0) {
			writer.print('\t');
		}
		writer.print(DoubleUtil.signum(value.doubleValue()));
	}

}
