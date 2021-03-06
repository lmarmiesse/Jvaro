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
package ch.javasoft.metabolic.efm.model.nullspace;

import ch.javasoft.bitset.IBitSet;
import ch.javasoft.metabolic.efm.column.ColumnHome;
import ch.javasoft.metabolic.efm.model.AbstractColumnInspectorModifier;
import ch.javasoft.metabolic.efm.model.EfmModel;
import ch.javasoft.metabolic.efm.model.IterationStepModel;

/**
 * Abstract superclass for column inspector/modifier for nullspace model
 */
abstract public class AbstractNullspaceColumnInspectorModifier<N extends Number, A> extends AbstractColumnInspectorModifier<N, A> {

	protected final boolean convertNumericToBinaryOnMerge;
	
	protected AbstractNullspaceColumnInspectorModifier(boolean convertNumericToBinaryOnMerge) {
		this.convertNumericToBinaryOnMerge = convertNumericToBinaryOnMerge;
	}
	/**
	 * Performs an and operation with both binary parts, sets the bit of the
	 * currently processed reaction, and returns the result
	 */
	public IBitSet mergeBinary(ColumnHome<N, ?> columnHome, EfmModel model, IBitSet binaryValsCol1, int binarySizeCol1, A numericValsCol1, IBitSet binaryValsCol2, int binarySizeCol2, A numericValsCol2, IterationStepModel iteration) {
		final IBitSet and = binaryValsCol1.getAnd(binaryValsCol2);
		and.set(iteration.getCurrentState().getBooleanSize());
		return and;
	}

}
