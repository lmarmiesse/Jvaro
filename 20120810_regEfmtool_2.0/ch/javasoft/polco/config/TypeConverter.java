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
package ch.javasoft.polco.config;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import ch.javasoft.math.array.Converter;
import ch.javasoft.math.linalg.LinAlgOperations;
import ch.javasoft.polco.EqualityPolyhedralCone;
import ch.javasoft.polco.InequalityPolyhedralCone;
import ch.javasoft.polco.PolyhedralCone;
import ch.javasoft.polco.impl.DefaultEqualityCone;
import ch.javasoft.polco.impl.DefaultInequalityCone;
import ch.javasoft.polco.impl.DefaultPolyhedralCone;
import ch.javasoft.polco.transform.TransformHelper;
import ch.javasoft.polco.transform.TransformedPolyhedralCone;
import ch.javasoft.polco.xenum.ExtremeRayCallback;
import ch.javasoft.polco.xenum.ExtremeRayEvent;
import ch.javasoft.util.numeric.Zero;

/**
 * The <code>TypeConverter</code> contains methods to convert different type 
 * dependent objects from one number type to another.
 */
public class TypeConverter<IN extends Number, IA, RN extends Number, RA> {
	private final boolean						allowMatrixRowScaling;
	private final LinAlgOperations<IN, IA> 		originalOps;
	private final LinAlgOperations<RN, RA> 		targetOps;
	private final Converter<IN, IA, RN, RA>		converter;
	public TypeConverter(LinAlgOperations<IN, IA> originalOps, LinAlgOperations<RN, RA> targetOps, boolean allowMatrixRowScaling) {
		this.allowMatrixRowScaling	= allowMatrixRowScaling;
		this.originalOps			= originalOps;
		this.targetOps				= targetOps;
		this.converter				= new Converter<IN, IA, RN, RA>(originalOps.getNumberArrayOperations(), targetOps.getNumberArrayOperations());
	}
	public TypeConverter(Arithmetic<IN, IA> originalType, Zero originalZero, Arithmetic<RN, RA> targetType, Zero targetZero, boolean allowMatrixRowScaling) {
		this(originalType.getLinAlgOperations(originalZero), targetType.getLinAlgOperations(targetZero), allowMatrixRowScaling);
	}
	public Converter<IN, IA, RN, RA> getConverter() {
		return converter;
	}
	public boolean isIdentityConverter() {
		return converter.isIdentityConverter();
	}
	@SuppressWarnings("unchecked")
	public ExtremeRayCallback<RN, RA> convertCallback(final ExtremeRayCallback<IN, IA> original) {
		if (isIdentityConverter()) {
			return (ExtremeRayCallback<RN, RA>)original;
		}
		return convertCallbackInternal(original);
	}
	private ExtremeRayCallback<RN, RA> convertCallbackInternal(final ExtremeRayCallback<IN, IA> original) {
		final TypeConverter<RN, RA, IN, IA> back = new TypeConverter<RN, RA, IN, IA>(targetOps, originalOps, allowMatrixRowScaling);
		return new ExtremeRayCallback<RN, RA>() {
			final ThreadLocal<ExtremeRayEvent<IN, IA>> convertedEvent	= new ThreadLocal<ExtremeRayEvent<IN,IA>>();
			final ThreadLocal<ExtremeRayEvent<RN, RA>> originalEvent	= new ThreadLocal<ExtremeRayEvent<RN,RA>>();
			public boolean initialize(ExtremeRayEvent<RN, RA> event) throws IOException {
				final ExtremeRayEvent<IN, IA> cEvent = back.convertEvent(event);
				originalEvent.set(event);
				convertedEvent.set(cEvent);
				return original.initialize(cEvent);
			}
			public void outputExtremeRay(ExtremeRayEvent<RN,RA> event, long index, RA extremeRay) throws IOException {
				final ExtremeRayEvent<IN, IA> cEvent;
				if (event.equals(originalEvent.get())) {
					cEvent = convertedEvent.get();
				}
				else {
					originalEvent.set(event);
					convertedEvent.set(cEvent = back.convertEvent(event));
				}
				final IA cExtremeRay = back.converter.convertVector(extremeRay, true /*normalize*/, true /*allowScaling*/);
				original.outputExtremeRay(cEvent, index, cExtremeRay);
			}
			public void terminate(ExtremeRayEvent<RN, RA> event) throws IOException {
				final ExtremeRayEvent<IN, IA> cEvent;
				if (event.equals(originalEvent.get())) {
					cEvent = convertedEvent.get();
				}
				else {
					originalEvent.set(event);
					convertedEvent.set(cEvent = back.convertEvent(event));
				}
				original.terminate(cEvent);
				originalEvent.set(null);
				convertedEvent.set(null);
			}
		};
	}
	@SuppressWarnings("unchecked")
	public ExtremeRayEvent<RN, RA> convertEvent(final ExtremeRayEvent<IN, IA> original) {
		if (isIdentityConverter()) {
			return (ExtremeRayEvent<RN, RA>)original;
		}
		return convertEventInternal(original);		
	}
	private ExtremeRayEvent<RN, RA> convertEventInternal(final ExtremeRayEvent<IN, IA> original) {
		final PolyhedralCone<RN, RA> converted = convertPolyhedralCone(original.getPolyhedralCone());
		return new ExtremeRayEvent<RN, RA>(converted, original.getRayCount());		
	}
	@SuppressWarnings("unchecked")
	public PolyhedralCone<RN, RA> convertPolyhedralCone(final PolyhedralCone<IN, IA> original) {
		if (isIdentityConverter()) {
			return (PolyhedralCone<RN, RA>)original;
		}
		return convertPolyhedralConeInternal(original);		
		
	}
	private PolyhedralCone<RN, RA> convertPolyhedralConeInternal(final PolyhedralCone<IN, IA> original) {
		if (original instanceof EqualityPolyhedralCone) {
			final EqualityPolyhedralCone<IN, IA> cone = (EqualityPolyhedralCone<IN, IA>)original;
			final RA[] convA;
			final IA[] origA = cone.getA();
			synchronized (origA) {
				convA = convertMatrix(origA, true);
			}
			if (original instanceof TransformedPolyhedralCone) {
				return createTransformedEqualityCone((TransformedPolyhedralCone<IN, IA>)original, convA);
			}
			else {
				return new DefaultEqualityCone<RN, RA>(targetOps, convA) {
					@Override
					public int getDimensions() {
						return original.getDimensions();
					}					
				};
			}
		}
		else if (original instanceof InequalityPolyhedralCone) {
			final InequalityPolyhedralCone<IN, IA> cone = (InequalityPolyhedralCone<IN, IA>)original;
			final RA[] convB;
			final IA[] origB = cone.getB();
			synchronized (origB) {
				convB = convertMatrix(origB, true);
			}
			if (original instanceof TransformedPolyhedralCone) {
				return createTransformedInequalityCone((TransformedPolyhedralCone<IN, IA>)original, convB);
			}
			return new DefaultInequalityCone<RN, RA>(targetOps, convB) {
				@Override
				public int getDimensions() {
					return original.getDimensions();
				}				
			};
		}
		else {
			final RA[] convA;
			final RA[] convB;
			final IA[] origA = original.getA();
			final IA[] origB = original.getB();
			synchronized (origA) {
				convA = convertMatrix(origA, true);
			}
			synchronized (origB) {
				convB = convertMatrix(origB, true);
			}
			if (original instanceof TransformedPolyhedralCone) {
				return createTransformedPolyhedralCone((TransformedPolyhedralCone<IN, IA>)original, convA, convB);
			}
			return new DefaultPolyhedralCone<RN, RA>(targetOps, convA, convB) {
				@Override
				public int getDimensions() {
					return original.getDimensions();
				}				
			};
		}
	}
	
	private PolyhedralCone<RN, RA> createTransformedEqualityCone(final TransformedPolyhedralCone<IN, IA> original, RA[] mxA) {
		final class TransformedEqualityCone extends DefaultEqualityCone<RN, RA> implements TransformedPolyhedralCone<RN, RA> {
			private volatile PolyhedralCone<RN, RA> parent;
			private volatile AtomicReference<RA[]>	mxTtoOriginal = new AtomicReference<RA[]>();
			public TransformedEqualityCone(LinAlgOperations<RN, RA> linAlgOps, RA[] mxA) {
				super(linAlgOps, mxA);
			}
			public PolyhedralCone<RN, RA> getParentCone() {
				if (parent == null) {
					if (parent == null) {
						parent = convertPolyhedralCone(original.getParentCone());
					}
				}
				return parent;
			}
			public PolyhedralCone<RN, RA> getOriginalCone() {
				return TransformHelper.getOriginalCone(this);
			}
			@Override
			public int getDimensions() {
				return original.getDimensions();
			}
			public RA transformToOriginal(RA x2) {
				if (mxTtoOriginal.get() == null) {
					final IA[] mxTtoOriginalI = TransformHelper.getTransformationMatrixToOriginal(original);
					synchronized (mxTtoOriginalI) {
						if (mxTtoOriginal.get() == null) {
							mxTtoOriginal.compareAndSet(null, convertMatrix(mxTtoOriginalI, false));
						}
					}
				}
				return TransformHelper.transformToOriginal(this, mxTtoOriginal, x2);
			}
			public RA[] getTransformationMatrixToParent() {
				throw new RuntimeException("unsupported, use transformToOriginal() instead");
			}
		}
		return new TransformedEqualityCone(targetOps, mxA);
	}
	private PolyhedralCone<RN, RA> createTransformedInequalityCone(final TransformedPolyhedralCone<IN, IA> original, RA[] mxB) {
		final class TransformedEqualityCone extends DefaultInequalityCone<RN, RA> implements TransformedPolyhedralCone<RN, RA> {
			private volatile PolyhedralCone<RN, RA> parent;
			private volatile AtomicReference<RA[]>	mxTtoOriginal = new AtomicReference<RA[]>();
			public TransformedEqualityCone(LinAlgOperations<RN, RA> linAlgOps, RA[] mxB) {
				super(linAlgOps, mxB);
			}
			public PolyhedralCone<RN, RA> getParentCone() {
				if (parent == null) {
					if (parent == null) {
						parent = convertPolyhedralCone(original.getParentCone());
					}
				}
				return parent;
			}
			public PolyhedralCone<RN, RA> getOriginalCone() {
				return TransformHelper.getOriginalCone(this);
			}
			@Override
			public int getDimensions() {
				return original.getDimensions();
			}
			public RA transformToOriginal(RA x2) {
				if (mxTtoOriginal.get() == null) {
					final IA[] mxTtoOriginalI = TransformHelper.getTransformationMatrixToOriginal(original);
					synchronized (mxTtoOriginalI) {
						if (mxTtoOriginal.get() == null) {
							mxTtoOriginal.compareAndSet(null, convertMatrix(mxTtoOriginalI, false));
						}
					}
				}
				return TransformHelper.transformToOriginal(this, mxTtoOriginal, x2);
			}
			public RA[] getTransformationMatrixToParent() {
				throw new RuntimeException("unsupported, use transformToOriginal() instead");
			}
		}
		return new TransformedEqualityCone(targetOps, mxB);
	}
	private PolyhedralCone<RN, RA> createTransformedPolyhedralCone(final TransformedPolyhedralCone<IN, IA> original, RA[] mxA, RA[] mxB) {
		final class TransformedCone extends DefaultPolyhedralCone<RN, RA> implements TransformedPolyhedralCone<RN, RA> {
			private volatile PolyhedralCone<RN, RA> parent;
			private volatile AtomicReference<RA[]>	mxTtoOriginal = new AtomicReference<RA[]>();
			public TransformedCone(LinAlgOperations<RN, RA> linAlgOps, RA[] mxA, RA[] mxB) {
				super(linAlgOps, mxA, mxB);
			}
			public PolyhedralCone<RN, RA> getParentCone() {
				if (parent == null) {
					if (parent == null) {
						parent = convertPolyhedralCone(original.getParentCone());
					}
				}
				return parent;
			}
			public PolyhedralCone<RN, RA> getOriginalCone() {
				return TransformHelper.getOriginalCone(this);
			}
			public RA transformToOriginal(RA x2) {
				if (mxTtoOriginal.get() == null) {
					final IA[] mxTtoOriginalI = TransformHelper.getTransformationMatrixToOriginal(original);
					synchronized (mxTtoOriginalI) {
						if (mxTtoOriginal.get() == null) {
							mxTtoOriginal.compareAndSet(null, convertMatrix(mxTtoOriginalI, false));
						}
					}
				}
				return TransformHelper.transformToOriginal(this, mxTtoOriginal, x2);
			}
			public RA[] getTransformationMatrixToParent() {
				throw new RuntimeException("unsupported, use transformToOriginal() instead");
			}
		}
		return new TransformedCone(targetOps, mxA, mxB);
	}
	
	private RA[] convertMatrix(IA[] original, boolean allowRowScaling) {
		try {
			return converter.convertMatrix(original);
		}
		catch (ArithmeticException e) {
			return converter.convertMatrix(original, true, allowRowScaling && allowMatrixRowScaling, false, true);			
		}
	}
	
}
