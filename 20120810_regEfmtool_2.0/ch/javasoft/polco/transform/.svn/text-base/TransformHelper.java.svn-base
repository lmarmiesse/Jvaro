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

import java.util.concurrent.atomic.AtomicReference;

import ch.javasoft.math.array.ExpressionComposer;
import ch.javasoft.math.array.NumberArrayOperations;
import ch.javasoft.math.linalg.BasicLinAlgOperations;
import ch.javasoft.math.operator.BooleanUnaryOperator;
import ch.javasoft.math.operator.UnaryOperator;
import ch.javasoft.polco.PolyhedralCone;

/**
 * The <code>TransformHelper</code> contains static helper methods commonly used 
 * by most {@link TransformedPolyhedralCone} implementations.
 */
abstract public class TransformHelper {

	/**
	 * Returns the original cone, that is, the root in the nesting hierarchy of
	 * the {@code polycone} if it is a {@link TransformedPolyhedralCone}.
	 * See also {@link TransformedPolyhedralCone#getOriginalCone()}.
	 * 
	 * @param polycone	the polyhedral cone, possibly a transformed cone
	 * @return the {@code polycone}'s original cone
	 */
	public static <N extends Number, A> PolyhedralCone<N, A> getOriginalCone(PolyhedralCone<N, A> polycone) {
		while (polycone instanceof TransformedPolyhedralCone) {
			polycone = ((TransformedPolyhedralCone<N, A>)polycone).getParentCone();
		}
		return polycone;
	}
	
	/**
	 * Returns the transformation matrix used to transform a ray <code>x2</code>
	 * back to an original ray <code>x</code> of the {@code transformedCone}'s 
	 * {@link TransformedPolyhedralCone#getOriginalCone() original cone}.
	 * The transformation with the returned matrix {@code T} is 
	 * {@code x = T x2}.
	 * <p>
	 * The matrix to tranform back to the original cone vector is the product of 
	 * all parent transformation matrices.
	 * 
	 * @return	the matrix {@code T} used for the back-transformation of rays
	 * 			into rays of the {@code transformedCone}'s 
	 * 			{@link TransformedPolyhedralCone#getOriginalCone() original cone}
	 */
	public static <N extends Number, A> A[] getTransformationMatrixToOriginal(TransformedPolyhedralCone<N, A> transformedCone) {
		final BasicLinAlgOperations<N, A> lops = transformedCone.getLinAlgOperations();
		PolyhedralCone<N, A> parent = transformedCone.getParentCone();
		A[] mxT = transformedCone.getTransformationMatrixToParent();
		while (parent instanceof TransformedPolyhedralCone) {
			transformedCone = ((TransformedPolyhedralCone<N, A>)parent);
			mxT = lops.multiply(transformedCone.getTransformationMatrixToParent(), mxT);
			parent = transformedCone.getParentCone();
		}
		final UnaryOperator<N, A> norm = lops.getNumberOperators().unary(UnaryOperator.Id.normalize);
		lops.getNumberArrayOperations().applyToEachElement(mxT, mxT, norm);
		return mxT;
	}

	/**
	 * Returns the back-transformed ray <code>x</code>, that is, 
	 * <code>x2</code>, a ray of this transformed cone, is transformed back to a 
	 * ray of the {@code transformedCone}'s 
	 * {@link TransformedPolyhedralCone#getOriginalCone() original cone}.
	 * <p>
	 * If {@code matrixT} contains no transformation matrix, it is constructed
	 * and set. For later transformation invocations, it already exists and is 
	 * thus taken from the atomic reference.
	 * 
	 * @param transformedCone	the transformed cone
	 * @param matrixT			the atomic reference caching the transformation
	 * 							matrix T
	 * @param x2 				a ray of the transformed cone
	 * @return the ray of the {@code transformedCone}'s original cone
	 */
	public static <N extends Number, A> A transformToOriginal(TransformedPolyhedralCone<N, A> transformedCone, AtomicReference<A[]> matrixT, A x2) {
		A[] mxT = matrixT.get();
		if (mxT == null) {
			matrixT.compareAndSet(null, getTransformationMatrixToOriginal(transformedCone));
			mxT = matrixT.get();
		}
		final BasicLinAlgOperations<N, A> lops = transformedCone.getLinAlgOperations();
		final UnaryOperator<N, A> norm = lops.getNumberOperators().unary(UnaryOperator.Id.normalize);
		final A x = lops.multiply(mxT, x2);
		lops.getNumberArrayOperations().applyToEachElement(x, x, norm);
		return x;
	}
	
	/**
	 * If the specified cone is a transformed cone, the given vector is 
	 * back-transformed into a vector of the original cone. If transformed, the
	 * vector is also normalized according to its number type (e.g. length 1 for
	 * float types).
	 * <p>
	 * A vector of an untransformed cone is returned unchanged.
	 */
	public static <N extends Number, A> A transformToOriginal(PolyhedralCone<N, A> polycone, A transformed) {
		//transform to original
		if (polycone instanceof TransformedPolyhedralCone) {
			final NumberArrayOperations<N, A> naops = polycone.getLinAlgOperations().getNumberArrayOperations();
			final ExpressionComposer<N, A> composer = naops.getExpressionComposer();

			//transform
			final A original = ((TransformedPolyhedralCone<N, A>)polycone).transformToOriginal(transformed);
			
			//normalize			
			final int dims = getOriginalCone(polycone).getDimensions();
			final N div = composer.vectorNormDivisor().operate(original, 0, dims);
			return divAndNormalize(naops, div, original);
		}
		return transformed;
	}
	
	/**
	 * Divides each vector element by {@code div}, but only if 
	 * {@code (div != 0) and (div != 1)}. The values are also normalized.
	 */
	private static <N extends Number, A> A divAndNormalize(NumberArrayOperations<N, A> naops, N div, A vector) {
		final ExpressionComposer<N, A> composer = naops.getExpressionComposer();
		final BooleanUnaryOperator<N, A> isZero	= composer.isZero();
		final BooleanUnaryOperator<N, A> isOne 	= composer.isOne();
		
		final UnaryOperator<N, A> norm; 
		if (!isZero.booleanOperate(div) && !isOne.booleanOperate(div)) {
			//divide if div != 0 and div != 1
			norm = composer.normalize(composer.divFreeBy(composer.constant(div)));
		}
		else {
			//normalize only
			norm = composer.normalize();			
		}
		
		//normalize now
		naops.applyToEachElement(vector, vector, norm);
		return vector;
	}

	//no instances
	private TransformHelper() {
		super();
	}
}
