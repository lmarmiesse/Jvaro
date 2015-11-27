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
package ch.javasoft.polco.metabolic;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import ch.javasoft.math.BigFraction;
import ch.javasoft.math.array.ExpressionComposer;
import ch.javasoft.math.array.NumberArrayOperations;
import ch.javasoft.math.operator.BooleanUnaryOperator;
import ch.javasoft.math.operator.UnaryOperator;
import ch.javasoft.math.operator.impl.BigFractionOperators;
import ch.javasoft.math.operator.impl.DoubleOperators;
import ch.javasoft.metabolic.FluxDistribution;
import ch.javasoft.metabolic.efm.output.AbstractOutputCallback;
import ch.javasoft.metabolic.efm.output.CallbackGranularity;
import ch.javasoft.metabolic.efm.output.EfmOutputCallback;
import ch.javasoft.metabolic.efm.output.EfmOutputEvent;
import ch.javasoft.polco.PolyhedralCone;
import ch.javasoft.polco.transform.TransformHelper;
import ch.javasoft.polco.xenum.ExtremeRayCallback;
import ch.javasoft.polco.xenum.ExtremeRayEvent;
import ch.javasoft.util.numeric.Zero;

/**
 * The <code>EfmOutputCallbackToExtremeRayCallback</code> is an adapter from
 * {@link EfmOutputCallback} to {@link ExtremeRayCallback}.
 */
public class EfmOutputCallbackToExtremeRayCallback<Num extends Number, Arr> extends AbstractOutputCallback {
	
	//real constants
	private final PolyhedralCone<Num, Arr>			polycone; 
	private final ExtremeRayCallback<Num, Arr> 		callback;
	private final CallbackGranularity 				granularity;
	private final boolean 							allowLoggingDuringOutput;
	private final Zero 								coreZero;
	
	//modified during output
	private final AtomicBoolean 							 iterateRays	= new AtomicBoolean();
	private final AtomicReference<ExtremeRayEvent<Num, Arr>> event			= new AtomicReference<ExtremeRayEvent<Num, Arr>>();
	private final AtomicLong 								 index			= new AtomicLong();
	
	public EfmOutputCallbackToExtremeRayCallback(PolyhedralCone<Num, Arr> polycone, ExtremeRayCallback<Num, Arr> callback, CallbackGranularity granularity, boolean allowLoggingDuringOutput, Zero coreZero) {
		this.polycone 					= polycone;
		this.callback 					= callback;
		this.granularity				= granularity;
		this.allowLoggingDuringOutput	= allowLoggingDuringOutput;
		this.coreZero					= coreZero;
	}
	public boolean allowLoggingDuringOutput() {
		return allowLoggingDuringOutput;
	}
	public CallbackGranularity getGranularity() {
		return iterateRays.get() ? granularity : CallbackGranularity.Null;
	}
	
	/**
	 * If the underlying cone is a transformed cone, the original cone is 
	 * returned.
	 */
	private PolyhedralCone<Num, Arr> getOriginalCone() {
		return TransformHelper.getOriginalCone(polycone);
	}
	/**
	 * If the underlying cone is a transformed cone, the given vector is 
	 * back-transformed into a vector of the original cone. If transformed, the
	 * vector is also normalized according to its number type (e.g. length 1 for
	 * float types).
	 * <p>
	 * A vector of an untransformed cone is returned unchanged.
	 */
	private Arr transformToOriginal(Arr transformed) {
		return TransformHelper.transformToOriginal(polycone, transformed);
	}
	@Override
	protected void callbackPre(EfmOutputEvent evt) throws IOException {
		final ExtremeRayEvent<Num, Arr> xevt = new ExtremeRayEvent<Num, Arr>(getOriginalCone(), evt.getEfmCount());
		iterateRays.set(callback.initialize(xevt));
		event.set(xevt);
		index.set(0);
	}
	@Override
	protected void callbackEfmOut(EfmOutputEvent evt) throws IOException {
		final FluxDistribution dist = evt.getEfm();		
		final int dims = polycone.getDimensions();
		//panic check
		if (dist.getSize() != dims) {
			throw new IOException("internal error: expected " + dims + " dimensions, but found " + dist.getSize() + " flux values");
		}

		//convert flux dist to ray vector
		final Arr xray = convertDistToRay(polycone.getLinAlgOperations().getNumberArrayOperations(), coreZero, dist);
		
		//transform to original
		final long currentIndex = index.getAndIncrement();
		final Arr oray = transformToOriginal(xray);

		//invoke xray callback method
		final ExtremeRayEvent<Num, Arr> xevt = event.get();
		synchronized (callback) {
			callback.outputExtremeRay(xevt, currentIndex, oray);
		}
	}
	/**
	 * Converts the given flux distribution into a ray. The conversion depends 
	 * on original and target number type, meaning that certain cases need 
	 * special treatment.
	 */
	private static <N extends Number, A> A convertDistToRay(NumberArrayOperations<N, A> naops, Zero coreZero, FluxDistribution dist) {
		final int len = dist.getSize();
		
		if (dist.getPreferredNumberClass().equals(naops.numberClass())) {
			//same type, just copy and normalize
			final A xray;
			if (naops.numberClass().equals(Double.class)) {
				xray = naops.arrayClass().cast(dist.getDoubleRates());
			}
			else {
				xray = naops.newZeroVector(len);
				for (int i = 0; i < len; i++) {
					if (dist.getRateSignum(i) != 0) {
						final Number val = dist.getNumberRate(i);
						naops.set(xray, i, naops.numberClass().cast(val));
					}
				}
			}
			return normalize(naops, xray);
		}
		if (dist.getPreferredNumberClass().equals(Double.class)) {
			//double to something, use zero for rounding
			BigFraction[] tmp = new BigFraction[len];
			for (int i = 0; i < len; i++) {
				final double val = dist.getDoubleRates()[i];
				if (coreZero.isZero(val)) {
					tmp[i] = BigFraction.ZERO;
				}
				else if (coreZero.isOne(val)) {
					tmp[i] = BigFraction.ONE;
				}
				else {
					tmp[i] = BigFraction.valueOfAdjusted(val, coreZero.mZeroPos);
				}
			}
			final NumberArrayOperations<BigFraction, BigFraction[]> fraAops = BigFractionOperators.INSTANCE.getNumberArrayOperations();
			if (naops.getNumberOperators().getDivisionSupport().mightCauseException()) {
				//to make number convertible into an integer, we use the
				//squeezer divisor
				tmp = squeezeVector(fraAops, tmp);
				return fraAops.getConverterTo(naops).convertVector(tmp);
			}
			final A xray = fraAops.getConverterTo(naops).convertVector(tmp);
			return normalize(naops, xray);
		}
		if (naops.getNumberOperators().getDivisionSupport().mightCauseException()) {
			//to make number convertible into an integer, we use the
			//squeezer divisor
			if (dist.getPreferredNumberClass().equals(Double.class)) {
				final NumberArrayOperations<Double, double[]> dblAops = new DoubleOperators(coreZero).getNumberArrayOperations();
				final double[] tmp = squeezeFluxDist(dblAops, coreZero, dist);
				return dblAops.getConverterTo(naops).convertVector(tmp);
			}
			else {
				final NumberArrayOperations<BigFraction, BigFraction[]> fraAops = BigFractionOperators.INSTANCE.getNumberArrayOperations();
				final BigFraction[] tmp = squeezeFluxDist(fraAops, coreZero, dist);
				return fraAops.getConverterTo(naops).convertVector(tmp);
			}
		}
		//default: use standard conversion
		final A xray = naops.newZeroVector(len);
		for (int i = 0; i < len; i++) {
			if (dist.getRateSignum(i) != 0) {
				final Number val = dist.getNumberRate(i);
				naops.set(xray, i, naops.convertNumber(val));
			}
		}
		return normalize(naops, xray);
	}
	/**
	 * Divides each vector element by the norm divisor of the vector. 
	 */
	private static <N extends Number, A> A normalize(NumberArrayOperations<N, A> naops, A vector) {
		final ExpressionComposer<N, A> composer = naops.getExpressionComposer();

		final int len = naops.getArrayOperations().getLength(vector);
		final N div = composer.vectorNormDivisor().operate(vector, 0, len);
		return divAndNormalize(naops, div, vector);
	}
	/**
	 * Converts the flux distribution into a vector and divides each vector 
	 * element by the squeeze divisor of the vector. The values are also 
	 * normalized.
	 */
	private static <N extends Number, A> A squeezeFluxDist(NumberArrayOperations<N, A> naops, Zero coreZero, FluxDistribution dist) {
		final A tmp = convertDistToRay(naops, coreZero, dist);
		return squeezeVector(naops, tmp);
	}
	/**
	 * Divides each vector element by the squeeze divisor of the vector. The 
	 * values are also normalized.
	 */
	private static <N extends Number, A> A squeezeVector(NumberArrayOperations<N, A> naops, A vector) {
		final ExpressionComposer<N, A> composer = naops.getExpressionComposer();
		
		final int len = naops.getArrayOperations().getLength(vector);
		final N div = composer.vectorSqueezeDivisor().operate(vector, 0, len);
		return divAndNormalize(naops, div, vector);
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
	@Override
	protected void callbackPost(EfmOutputEvent evt) throws IOException {
		final ExtremeRayEvent<Num, Arr> xevt = event.getAndSet(null);
		callback.terminate(xevt);
		if (!iterateRays.get()) {
			index.set(xevt.getRayCount());
		}
	}
	long getIndex() {
		return index.get();
	}
	/**
	 * Returns {@code true}
	 * @see EfmOutputCallback#isThreadSafe()
	 */
	public boolean isThreadSafe() {
		return true;
	}

}
