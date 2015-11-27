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

import ch.javasoft.polco.xenum.ExtremeRayCallback;
import ch.javasoft.polco.xenum.ExtremeRayEvent;

/**
 * The <code>NullCallback</code> simply ignores all callback invocations, that
 * is, the output virtually goes to {@code /dev/null}.
 */
public class NullCallback implements ExtremeRayCallback {

	/**
	 * The single instance, untyped. It is recommended to use 
	 * {@link #instance()} instead.
	 */
	public static final NullCallback INSTANCE = new NullCallback();
	
	/**
	 * Returns the single instance, casted to the desired number types.
	 */
	@SuppressWarnings("unchecked")
	public static final <Num extends Number, Arr> ExtremeRayCallback<Num, Arr> instance() {
		return INSTANCE;
	}
	/**
	 * Private constructor for single instance
	 */
	private NullCallback() {
		super();
	}
	
	public boolean initialize(ExtremeRayEvent event) {
		return false;//no ray output desired
	}

	public void outputExtremeRay(ExtremeRayEvent event, long index, Object extremeRay) {
		//no op
	}

	public void terminate(ExtremeRayEvent event) {
		//no op
	}

}
