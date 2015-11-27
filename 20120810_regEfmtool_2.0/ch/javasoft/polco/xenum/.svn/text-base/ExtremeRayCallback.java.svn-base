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

import java.io.IOException;

import org.dom4j.Element;

import ch.javasoft.math.linalg.LinAlgOperations;
import ch.javasoft.polco.Numeric;
import ch.javasoft.polco.PolyhedralCone;

/**
 * The <code>ExtremeRayCallback</code> interface is called to output extreme
 * rays resulting from 
 * {@link ExtremeRayEnumerator#enumerateExtremeRays(PolyhedralCone, ExtremeRayCallback, LinAlgOperations) extreme ray enumeration}.
 * <p>
 * Implementations are expected to have a constructor with a single
 * {@link Element} argument, containing the xml configuration for this 
 * enumerator.
 * 
 * @type Num	the number type of a single number
 * @type Arr	the number type of an array of numbers
 */
public interface ExtremeRayCallback<Num extends Number, Arr> extends Numeric<Num, Arr> {
	/**
	 * Initializes the callback process and returns {@code true} if 
	 * {@link #outputExtremeRay(ExtremeRayEvent, long, Object) outputExtremeRay(..)}
	 * shall be called with each extreme ray. If {@code false} is returned, the
	 * extreme rays are not output, and {@link #terminate(ExtremeRayEvent)} is
	 * called subsequently to complete the callback process.
	 * 
	 * @param event	the event
	 * @return	{@code true} if extreme rays shall be output individually
	 */
	boolean initialize(ExtremeRayEvent<Num, Arr> event) throws IOException;
	/**
	 * Output callback, invoked with each individual extreme ray, but only if
	 * {@link #initialize(ExtremeRayEvent)} returned {@code true}. 
	 * 
	 * @param event	the event
	 * @param index the zero-based index for the rays, with no particular 
	 * 				ordering
	 * @param extremeRay	a numeric array representing the ray values
	 */
	void outputExtremeRay(ExtremeRayEvent<Num, Arr> event, long index, Arr extremeRay) throws IOException;
	/**
	 * Called to terminate the callback process, for instance to close file
	 * handles. 
	 * 
	 * @param event	the event
	 */
	void terminate(ExtremeRayEvent<Num, Arr> event) throws IOException;
}
