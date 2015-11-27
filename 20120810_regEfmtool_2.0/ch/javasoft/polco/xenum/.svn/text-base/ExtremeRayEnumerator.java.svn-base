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
package ch.javasoft.polco.xenum;

import ch.javasoft.math.linalg.LinAlgOperations;
import ch.javasoft.polco.PolyhedralCone;

/**
 * <code>ExtremeRayEnumerator</code> implementations enumerate all extreme rays 
 * and of a {@link PolyhedralCone polyhedral cone}. The result is output via a 
 * callback interface to keep memory brackets low during the output process.
 */
public interface ExtremeRayEnumerator {
	/**
	 * Enumerates all extreme rays of the specified polyhedral cone, generating
	 * ouptut using the specified callback interface. 
	 * <p>
	 * Since input cone and output callback have different number type 
	 * parameters, the method must also convert between the number types if 
	 * necessary. For such conversations, a linear algebra operations object of
	 * the output type is also submitted.  
	 *  
	 * @type IN				input type of a single number
	 * @type IA				input type of an array of numbers
	 * @type RN				output type of a single number
	 * @type RA				output type of an array of numbers
	 * @param polycone		polyhedral cone, for which to compute extreme rays
	 * @param callback		callback interface to handle generated output
	 * @param callbackOps	linear algebra operations for algebraic operations
	 * 						with output type, for instance to convert numbers
	 * 						to output type
	 * 
	 * @return the number of extreme rays found
	 */
	<IN extends Number, IA, RN extends Number, RA> long enumerateExtremeRays(PolyhedralCone<IN, IA> polycone, ExtremeRayCallback<RN, RA> callback, LinAlgOperations<RN, RA> callbackOps);
}
