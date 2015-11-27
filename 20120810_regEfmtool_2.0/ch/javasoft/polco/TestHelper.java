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
package ch.javasoft.polco;

import ch.javasoft.metabolic.efm.config.Arithmetic;

/**
 * The <code>TestHelper</code> contains static helper methods used by the
 * junit test in this package.
 */
public class TestHelper {
	
	/**
	 * Returns the polco arithmetic, given the efm arithmetic. Currently, it
	 * always returns {@link ch.javasoft.polco.config.Arithmetic#FRACTIONAL FRACTIONAL},
	 * since the gauss algorithm for double does not work correctly yet.
	 */
    public static final ch.javasoft.polco.config.Arithmetic<?, ?> getPolcoArithmetic(Arithmetic arithmetic) {
//    	return ch.javasoft.polco.config.Arithmetic.FRACTIONAL;
//    	if (Arithmetic.bigint.equals(arithmetic)) {
//    		return ch.javasoft.polco.config.Arithmetic.FRACTIONAL;
//    	}
    	for (final ch.javasoft.polco.config.Arithmetic ari : ch.javasoft.polco.config.Arithmetic.VALUES) {
    		if (arithmetic.getNiceName().equals(ari.name())) return ari;
    	}
    	throw new RuntimeException("ERROR, unknown arithmetic: " + arithmetic);
    }

}
