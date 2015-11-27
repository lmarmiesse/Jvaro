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
package ch.javasoft.metabolic.efm.adj.incore.tree.search;

import java.io.IOException;
import java.util.Queue;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.BitSet;

import ch.javasoft.metabolic.efm.adj.incore.AbstractAdjacencyEnumerator;
import ch.javasoft.metabolic.efm.adj.incore.tree.DefaultTreeFactory;
import ch.javasoft.metabolic.efm.adj.incore.tree.JobScheduleMultiThreadTreeFactory;
import ch.javasoft.metabolic.efm.adj.incore.tree.Root;
import ch.javasoft.metabolic.efm.adj.incore.tree.TreeFactory;
import ch.javasoft.metabolic.efm.column.Column;
import ch.javasoft.metabolic.efm.column.ColumnHome;
import ch.javasoft.metabolic.efm.column.ColumnPair;
import ch.javasoft.metabolic.efm.concurrent.SemaphoreConcurrentToken;
import ch.javasoft.metabolic.efm.config.Config;
import ch.javasoft.metabolic.efm.memory.SortableMemory;
import ch.javasoft.metabolic.efm.model.EfmModel;

import at.acib.generegulation.GeneticRuleSet;
import at.acib.generegulation.GeneBitSet;
import at.acib.generule_filestore.GeneRuleFilenameStore;

/**
 * The <code>PatternTreeMinZerosAdjacencyEnumerator</code> uses candidate
 * narrowing to enumerate adjacent pairs, and it uses three pattern trees to
 * perform superset search for the combinatorial adjacency test.
 */
public class PatternTreeMinZerosAdjacencyEnumerator extends AbstractAdjacencyEnumerator {

	public static final String NAME = "pattern-tree-minzero";

	private int mRequiredZeroCount;
	
	public PatternTreeMinZerosAdjacencyEnumerator() {
		super();
	}

	public String name() {
		return NAME;
	}

	@Override
	public <Col extends Column, N extends Number> void initialize(ColumnHome<N, Col> columnHome, Config config, EfmModel model) {
		super.initialize(columnHome, config, model);
		mRequiredZeroCount = model.getRequiredCardinality();
	}
	@Override
	public void adjacentPairs(Queue<ColumnPair> adjacentPairs, SortableMemory<Column> zerCols, SortableMemory<Column> posCols, SortableMemory<Column> negCols) throws IOException {
		if (mModel.getAdjEnumThreads() > 1) {
//	    	TreeFactory<SemaphoreConcurrentToken> fac = new SemIncMultiThreadTreeFactory(threads);
	    	TreeFactory<SemaphoreConcurrentToken> fac = new JobScheduleMultiThreadTreeFactory(mModel);//faster, mainly if cpu-cores > 2
	    	Root<SemaphoreConcurrentToken> root = new SearchRoot<SemaphoreConcurrentToken>(mConfig, mModel, fac, mRequiredZeroCount, posCols, zerCols, negCols);
//	    	TreeFactory<PoolToken> fac = new PoolTreeFactory(threads);
//	    	Root<PoolToken> root = new SearchRoot<PoolToken>(fac, mRequiredZeroCount, posCols, zerCols, negCols);
	    	fac.createTraverser().traverseTree(root, posCols, zerCols, negCols, adjacentPairs);
		}
		else {
    		// normal, 1 thread
    		TreeFactory<Void> fac = new DefaultTreeFactory(mModel);
	    	Root<Void> root = new SearchRoot<Void>(mConfig, mModel, fac, mRequiredZeroCount, posCols, zerCols, negCols);
	    	fac.createTraverser().traverseTree(root, posCols, zerCols, negCols, adjacentPairs);    			
		}

                ///////////////////////////////////////////////////////////////
                ///////////////////////////////////////////////////////////////
                GeneRuleFilenameStore fileNameStore = new GeneRuleFilenameStore();

                if( fileNameStore.isFilenameAvailable() )
                {
                   String filename = fileNameStore.getFilename();
                   GeneticRuleSet myRuleSet = new GeneticRuleSet(filename, mModel, mConfig);
                   GeneBitSet arrLstRules[] = myRuleSet.getAllBitSets();

		   int remove_pairs = 0;
                   int maxReacIdx;
                   for( int r = 0; r < arrLstRules.length; r++ )
                   {
                      maxReacIdx = arrLstRules[r].getMaxReacIdx();

                      Iterator<ColumnPair> myIterator = adjacentPairs.iterator();
                      while(myIterator.hasNext())
                      {
                         ColumnPair myAdjacentPairs = myIterator.next();

                         // System.out.println("maxReacIdx = " + maxReacIdx + "  booleanSize = " + myAdjacentPairs.getColumnA().booleanSize());
                         // System.out.println("Compare: ruleBitSet = " + arrLstRules[r] + " interset = " + myAdjacentPairs.intersectBitValues());

                         BitSet myBitSet = myAdjacentPairs.intersectBitValues().toBitSet();
                         // System.out.println("myBitSet = " + myBitSet);
                         myBitSet.flip( 0, maxReacIdx + 1);
                         // System.out.println("myBitSet = " + myBitSet);
                         myBitSet.and( arrLstRules[r] );
                         // System.out.println("myBitSet = " + myBitSet);
                         if( ((myAdjacentPairs.getColumnA().booleanSize() - 1) >= maxReacIdx ) &&
                             myBitSet.equals(arrLstRules[r]) )
                         {
                            myIterator.remove();
                            remove_pairs++;
		         }
                      }
		   }
                   System.out.println("Removed " + remove_pairs + " modes");
		}
                ///////////////////////////////////////////////////////////////
	}
}
