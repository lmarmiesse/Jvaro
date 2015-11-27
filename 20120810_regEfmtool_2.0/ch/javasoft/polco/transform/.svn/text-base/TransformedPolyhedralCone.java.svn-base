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
package ch.javasoft.polco.transform;

import ch.javasoft.polco.PolyhedralCone;

/**
 * The <code>TransformedPolyhedralCone</code> is derived from another 
 * representation. Rays can be transformed back to the original cone.
 */
public interface TransformedPolyhedralCone<Num extends Number, Arr> extends PolyhedralCone<Num, Arr> {

	/**
	 * Returns the original cone, that is, the root in the nesting hierarchy
	 */
	PolyhedralCone<Num, Arr> getOriginalCone();
	
	/**
	 * Returns the parent cone
	 */
	PolyhedralCone<Num, Arr> getParentCone();

	/**
	 * Returns the back-transformed ray <code>x</code>, that is, 
	 * <code>x2</code>, a ray of this transformed cone, is transformed back to a 
	 * ray of the {@link #getOriginalCone() original cone}.
	 * 
	 * @param x2 a ray of this transformed cone
	 * @return the ray of the original cone
	 */
	Arr transformToOriginal(Arr x2);

	/**
	 * Returns the transformation matrix used to transform a ray <code>x2</code>
	 * back to a parent ray <code>x</code> of the 
	 * {@link #getParentCone() parent cone}.
	 * The transformation with the returned matrix {@code T} is 
	 * {@code x = T x2}.
	 * 
	 * @return	the matrix {@code T} used for the back-transformation of rays
	 * 			into rays of the {@link #getParentCone() parent cone}
	 */
	Arr[] getTransformationMatrixToParent();
}
