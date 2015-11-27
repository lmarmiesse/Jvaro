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
package ch.javasoft.polco.adapter;

import java.io.IOException;

import ch.javasoft.math.array.ArrayOperations;
import ch.javasoft.polco.xenum.ExtremeRayCallback;
import ch.javasoft.polco.xenum.ExtremeRayEvent;

/**
 * The <code>AdapterCallback</code> stores the extreme rays in a matrix.
 * 
 * @type Num	the number type of a single number
 * @type Arr	the number type of an array of numbers
 */
/*package*/ class AdapterCallback<Num extends Number, Arr> implements ExtremeRayCallback<Num, Arr> {
	
	private final ArrayOperations<Arr> arrayOps;
	
	private Arr[] extremeRays;
	
	/**
	 * Constructor for <code>AdapterCallback</code> with array operations to
	 * create the output matrix
	 * 
	 * @param arrayOps	array operations to create output matrix
	 */
	public AdapterCallback(ArrayOperations<Arr> arrayOps) {
		this.arrayOps = arrayOps;
	}
	//inherit javadoc
	public boolean initialize(ExtremeRayEvent<Num, Arr> event) throws IOException {
		final long count = event.getRayCount();
		if (count > Integer.MAX_VALUE) {
			throw new IndexOutOfBoundsException("too many extreme rays (out of int range): " + count);
		}
		extremeRays = arrayOps.newMatrix((int)count, event.getPolyhedralCone().getDimensions());
		return true;//yes, we want the rays
	}
	//inherit javadoc
	public void outputExtremeRay(ExtremeRayEvent<Num,Arr> event, long index, Arr extremeRay) throws IOException {
		extremeRays[(int)index] = extremeRay;
	};
	//inherit javadoc
	public void terminate(ExtremeRayEvent<Num, Arr> event) throws IOException {
		//nothing to do
	}
	
	/**
	 * Returns the extreme ray matrix and sets and resets the internal matrix to
	 * {@code null}
	 * 
	 * @return the extreme ray matrix
	 */
	public Arr[] yield() {
		final Arr[] rays = extremeRays;
		extremeRays = null;
		return rays;
	}
}
