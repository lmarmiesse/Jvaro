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
package ch.javasoft.metabolic.efm.impl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.javasoft.metabolic.efm.adj.AdjEnum;
import ch.javasoft.metabolic.efm.column.Column;
import ch.javasoft.metabolic.efm.column.ColumnHome;
import ch.javasoft.metabolic.efm.config.Config;
import ch.javasoft.metabolic.efm.memory.AppendableMemory;
import ch.javasoft.metabolic.efm.memory.IterableMemory;
import ch.javasoft.metabolic.efm.memory.MemoryFactory;
import ch.javasoft.metabolic.efm.memory.PartId;
import ch.javasoft.metabolic.efm.model.AdjEnumModel;
import ch.javasoft.metabolic.efm.model.DefaultIterationStepModel;
import ch.javasoft.metabolic.efm.model.EfmModelFactory;
import ch.javasoft.metabolic.efm.model.IterationStepModel;
import ch.javasoft.metabolic.efm.model.NetworkEfmModel;
import ch.javasoft.metabolic.efm.util.ColumnUtil;

/**
 * The <code>SequentialDoubleDescriptionImpl</code> implementation is the 
 * usually known implementation of the double description method. The iteration 
 * phase (see {@link AbstractDoubleDescriptionImpl superclass} for the phases) 
 * processes each missing constraint sequentially, i.e. step by step.
 */
public class SequentialDoubleDescriptionImpl extends AbstractDoubleDescriptionImpl {
	
    private static final Logger LOG = LogPkg.LOGGER;

    /**
	 * Constructor with config access and the two factories for model and 
	 * memory. Note that most factories have default constructors without 
	 * arguments, and can thus be defined in the configuration file.
	 */
	public SequentialDoubleDescriptionImpl(Config config, EfmModelFactory modelFactory, MemoryFactory memoryFactory) {
		super(config, modelFactory, memoryFactory);
	}

	@Override
	protected <N extends Number, Col extends Column> IterableMemory<Col> iterate(ColumnHome<N,Col> columnHome, NetworkEfmModel efmModel, AppendableMemory<Col> memory) throws IOException {
        AppendableMemory<Col> pos = getMemoryFactory().createConcurrentAppendableMemory(columnHome, efmModel, 1, PartId.POS);
        AppendableMemory<Col> zer = getMemoryFactory().createConcurrentAppendableMemory(columnHome, efmModel, 1, PartId.ZER);
        AppendableMemory<Col> neg = getMemoryFactory().createConcurrentAppendableMemory(columnHome, efmModel, 1, PartId.NEG);
        
        final IterationStepModel initialItModel = new DefaultIterationStepModel(efmModel, 0);
        ColumnUtil.partition(columnHome, efmModel, memory, pos, zer, neg, initialItModel, false /*keep*/);

        final AdjEnum adjEnum = getConfig().getAdjMethodFactory().createAdjEnumFromConfig();
        adjEnum.initialize(columnHome, getConfig(), efmModel);
        
    	final int itCount  = efmModel.getIterationCount();

    	int cntPos = pos.getColumnCount();
    	int cntZer = zer.getColumnCount();
    	int cntNeg = neg.getColumnCount();
    	
    	int iteration 	= 0;
        long timeStart	= System.currentTimeMillis();        
        long timeEnd    = timeStart;
    	int colCount 	= cntPos + cntZer + cntNeg;
        while (colCount > 0 && iteration < itCount) {
        	
            LOG.info(
                "iteration " + iteration + "/" + itCount + ": " + colCount + " modes, dt=" + (timeEnd - timeStart) + "ms." +
                "\t{ next " + (iteration+1) + "/" + itCount + ": " + ((long)cntPos) * ((long)cntNeg) + " adj candidates, " +
                		"[+/0/-] = [" + cntPos + "/" + cntZer + "/" + cntNeg + "] }"
            );
            if (LOG.isLoggable(Level.ALL)) {
            	traceCols("col:+", 0, pos);
            	traceCols("col:0", cntPos, zer);
            	traceCols("col:-", cntPos + cntZer, neg);
            }
            
            iteration++;
            timeStart = System.currentTimeMillis();

            memory = getMemoryFactory().createConcurrentAppendableMemory(columnHome, efmModel, iteration + 1, null);

            //generate new rays from adjacent ray pairs
            final AdjEnumModel<Col> adjModel = new AdjEnumModel<Col>(efmModel, iteration, pos.toSortableMemory(), zer.toSortableMemory(), neg.toSortableMemory(), memory);
            if (cntPos > 0 && cntNeg > 0) {
            	adjEnum.adjacentPairs(columnHome, adjModel);
            	pos.flush();
            	zer.flush();
            	neg.flush();
            }
            
            if (iteration < itCount) {
                final AppendableMemory<Col> npos = getMemoryFactory().createConcurrentAppendableMemory(columnHome, efmModel, iteration + 1, PartId.POS);
                final AppendableMemory<Col> nzer = getMemoryFactory().createConcurrentAppendableMemory(columnHome, efmModel, iteration + 1, PartId.ZER);
                final AppendableMemory<Col> nneg = getMemoryFactory().createConcurrentAppendableMemory(columnHome, efmModel, iteration + 1, PartId.NEG);
                
                ColumnUtil.partitionOrClose(columnHome, efmModel, NetworkEfmModel.Partition.Positive, pos, npos, nzer, nneg, adjModel, true /*keep*/);
                ColumnUtil.partitionOrClose(columnHome, efmModel, NetworkEfmModel.Partition.Negative, neg, npos, nzer, nneg, adjModel, true /*keep*/);
                ColumnUtil.partitionOrClose(columnHome, efmModel, NetworkEfmModel.Partition.Zero, zer, npos, nzer, nneg, adjModel, true /*keep*/);
                ColumnUtil.partitionOrClose(columnHome, efmModel, NetworkEfmModel.Partition.Zero, memory, npos, nzer, nneg, adjModel, false /*keep*/);//the new columns
                
	            pos = npos;
	            zer = nzer;
	            neg = nneg;
	        	cntPos = pos.getColumnCount();
	        	cntZer = zer.getColumnCount();
	        	cntNeg = neg.getColumnCount();
            }
            else {
            	ColumnUtil.moveToOrClose(columnHome, efmModel, NetworkEfmModel.Partition.Positive, pos, memory, adjModel, true /*keep*/);
            	ColumnUtil.moveToOrClose(columnHome, efmModel, NetworkEfmModel.Partition.Negative, neg, memory, adjModel, true /*keep*/);
            	ColumnUtil.moveToOrClose(columnHome, efmModel, NetworkEfmModel.Partition.Zero, zer, memory, adjModel, true /*keep*/);
            }            
        	colCount = cntPos + cntZer + cntNeg;
        	
            timeEnd = System.currentTimeMillis();
        }
        if (iteration < itCount) {
            LOG.info("iteration " + iteration + "/" + itCount + ": discontinued since no modes left.");            
        }
        else {
            LOG.info("iteration " + iteration + "/" + itCount + ": " + memory.getColumnCount() + " modes, dt=" + (timeEnd - timeStart) + "ms.");            
        	if (LOG.isLoggable(Level.ALL)) {
            	traceCols("cols:", 0, memory);
            }
        }

		return memory;
    }
	
	private static void traceCols(String prefix, int indexOffset, Iterable<?> cols) {
		int index = indexOffset;
		for (final Object c : cols) {
			LOG.finest(prefix + "[" + index + "]: " + c);
			index++;
		}		
	}
}
