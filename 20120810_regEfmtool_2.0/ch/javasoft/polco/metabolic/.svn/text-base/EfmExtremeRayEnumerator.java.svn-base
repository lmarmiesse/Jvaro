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

import java.util.logging.Level;
import java.util.logging.Logger;

import ch.javasoft.math.BigFraction;
import ch.javasoft.math.linalg.LinAlgOperations;
import ch.javasoft.metabolic.MetabolicNetwork;
import ch.javasoft.metabolic.efm.ElementaryFluxModes;
import ch.javasoft.metabolic.efm.output.CallbackGranularity;
import ch.javasoft.metabolic.impl.FractionNumberStoichMetabolicNetwork;
import ch.javasoft.polco.EqualityPolyhedralCone;
import ch.javasoft.polco.PolyhedralCone;
import ch.javasoft.polco.config.TypeConverter;
import ch.javasoft.polco.transform.TransformedEqualityCone;
import ch.javasoft.polco.xenum.ExtremeRayCallback;
import ch.javasoft.polco.xenum.ExtremeRayEnumerator;
import ch.javasoft.smx.iface.BigIntegerRationalMatrix;
import ch.javasoft.smx.impl.DefaultBigIntegerRationalMatrix;
import ch.javasoft.util.logging.LogWriter;
import ch.javasoft.util.logging.Loggers;
import ch.javasoft.util.numeric.Zero;

/**
 * The <code>EfmExtremeRayEnumerator</code> uses the EFM algorithm to compute
 * extreme rays.
 */
public class EfmExtremeRayEnumerator implements ExtremeRayEnumerator {
	
	private static final Logger LOG = LogPkg.LOGGER;
	
	private final Zero 		coreZero;
	private final boolean	allowMatrixRowScaling;
	
	public EfmExtremeRayEnumerator(Zero coreZero, boolean allowMatrixRowScaling) {
		this.coreZero 				= coreZero;
		this.allowMatrixRowScaling	= allowMatrixRowScaling;
	}
	
	public <IN extends Number, IA, RN extends Number, RA> long enumerateExtremeRays(PolyhedralCone<IN, IA> polycone, ExtremeRayCallback<RN, RA> callback, LinAlgOperations<RN, RA> callbackOps) {
		LOG.info("input cone: " + polycone);
		final EqualityPolyhedralCone<IN, IA> eqCone;
		if (polycone instanceof EqualityPolyhedralCone) {
			eqCone = (EqualityPolyhedralCone<IN, IA>)polycone;
		}
		else {
			if (Loggers.isLoggable(LOG, Level.FINER)) {
				polycone.writeToMultiline(new LogWriter(LOG, Level.FINER));
			}
			eqCone = new TransformedEqualityCone<IN, IA>(polycone);
			LOG.info("transformed cone: " + eqCone);
		}
		if (Loggers.isLoggable(LOG, Level.FINER)) {
			eqCone.writeToMultiline(new LogWriter(LOG, Level.FINER));
		}
		
		final TypeConverter<IN, IA, RN, RA> converter = new TypeConverter<IN, IA, RN, RA>(polycone.getLinAlgOperations(), callbackOps, allowMatrixRowScaling);
		final PolyhedralCone<RN, RA> outCone = converter.convertPolyhedralCone(eqCone);
		final EfmOutputCallbackToExtremeRayCallback<RN, RA> efmCb = new EfmOutputCallbackToExtremeRayCallback<RN, RA>(outCone, callback, CallbackGranularity.DoubleUncompressed, false, coreZero);
		final MetabolicNetwork metaNet = createMetabolicNetwork(eqCone);
		ElementaryFluxModes.calculateCallback(metaNet, efmCb);
		final long rays = efmCb.getIndex();
		LOG.info(rays + " extreme rays");
		return rays;
	}
	
	private <Num extends Number, Arr> MetabolicNetwork createMetabolicNetwork(EqualityPolyhedralCone<Num, Arr> eqCone) {
		final double tol = coreZero.mZeroPos;
		final int rows = eqCone.getRowCountA();
		final int cols = eqCone.getDimensions();
		final BigIntegerRationalMatrix matrix = new DefaultBigIntegerRationalMatrix(rows, cols);
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				final BigFraction val;
				final Num nval = eqCone.getA(row, col);
				if (nval instanceof Double || nval instanceof Float) {
//					val = BigFraction.valueOfAdjusted(nval.doubleValue(), 1e-10);
					val = BigFraction.valueOfAdjusted(nval.doubleValue(), tol);
					
				}
				else {
					val = BigFraction.valueOf(nval);
				}
				matrix.setValueAt(row, col, val);
			}
		}
		return new FractionNumberStoichMetabolicNetwork(matrix, new boolean[cols]);
	}

}
