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
package ch.javasoft.metabolic.efm.concurrent;

import java.io.IOException;

import ch.javasoft.bitset.IBitSet;
import ch.javasoft.metabolic.efm.model.EfmModel;
import ch.javasoft.metabolic.efm.progress.ProgressAggregator;
import ch.javasoft.metabolic.efm.rankup.PreprocessableMatrix;

public class RankUpdateToken extends SemaphoreConcurrentToken {
	
	public final InheritableThreadLocal<PreprocessableMatrix> rankMatrices = new InheritableThreadLocal<PreprocessableMatrix>() {
		@Override
		protected PreprocessableMatrix childValue(PreprocessableMatrix parentValue) {
			return parentValue == null ? null : parentValue.clone();
		}
	};
	
	public RankUpdateToken(EfmModel efmModel) {
		super(efmModel);
	}
	public RankUpdateToken(EfmModel efmModel, ProgressAggregator progress) throws IOException {
		super(efmModel, progress);
	}
	
	public PreprocessableMatrix getRankMatrix() {
		return rankMatrices.get();
	}
	public void removeChildRankMatrix() {
		final PreprocessableMatrix mx = rankMatrices.get();
		rankMatrices.set(mx.parent);
	}
	
	public void addRootRankMatrix(IBitSet key) {
		final PreprocessableMatrix rootMx = PreprocessableMatrix.createRootMatrix(key);
		rankMatrices.set(rootMx);
	}
	public void addChildRankMatrix(IBitSet key) {
		final PreprocessableMatrix parent 	= rankMatrices.get();
		final PreprocessableMatrix child	= parent.createChild(key);
		rankMatrices.set(child);
	}

	public void removeRootRankMatrix() {
		rankMatrices.set(null);
	}
	
}
