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
package ch.javasoft.metabolic.efm.memory.outcore;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ch.javasoft.metabolic.efm.column.Column;
import ch.javasoft.metabolic.efm.column.ColumnHome;
import ch.javasoft.metabolic.efm.config.Config;
import ch.javasoft.metabolic.efm.memory.MemoryFactory;
import ch.javasoft.metabolic.efm.memory.MemoryPart;
import ch.javasoft.metabolic.efm.memory.ReadWriteMemory;
import ch.javasoft.metabolic.efm.model.NetworkEfmModel;

/**
 * The <code>OutOfCoreMemoryFactory</code> creates an 
 * {@link OutOfCoreMemory out-of-core memory}, meaning that intermediary modes 
 * are stored in files.
 */
public class OutOfCoreMemoryFactory implements MemoryFactory {

	public static final String CONFIG_FILE_NAME	= "config.xml";
	
	private final boolean sortInCore;
	
	public boolean isInitialized = false;
	
	public OutOfCoreMemoryFactory() {
		this(false);
	}
	public OutOfCoreMemoryFactory(boolean sortInCore) {
		this.sortInCore = sortInCore;
	}
	
	private synchronized <N extends Number, Col extends Column> void ensureInitialized(ColumnHome<N, Col> columnHome, NetworkEfmModel efmModel) throws IOException {
		if (!isInitialized) {
			final Config config = efmModel.getConfig();
			config.getTempDir().mkdirPersonalized();
			
	        final FileWriter configWriter = new FileWriter(new File(config.getTempDir().getPersonalizedDir(), CONFIG_FILE_NAME));
	        config.writeTo(configWriter);
	        configWriter.close();

	        isInitialized = true;
		}
	}
    
	public <N extends Number, Col extends Column> OutOfCoreMemory<Col> createConcurrentAppendableMemory(ColumnHome<N, Col> columnHome, NetworkEfmModel efmModel, int iteration, MemoryPart part) throws IOException {
		ensureInitialized(columnHome, efmModel);
		final Config config = efmModel.getConfig();
		
		if (part == null) {
	        return new OutOfCoreMemory<Col>(
	        	config.getTempDir().getPersonalizedDir(),
	        	iteration, 
	        	efmModel.getBooleanSize(iteration), 
	        	efmModel.getNumericSize(iteration),
	        	sortInCore,
	        	columnHome
	        );
		}
        return new OutOfCoreMemory<Col>(
        	config.getTempDir().getPersonalizedDir(),
        	part, iteration,  
        	efmModel.getBooleanSize(iteration), 
        	efmModel.getNumericSize(iteration),
        	sortInCore,
        	columnHome
        );
	}
	
	public <N extends Number, Col extends Column> ReadWriteMemory<Col> createReadWriteMemory(ColumnHome<N, Col> columnHome, NetworkEfmModel efmModel, int iteration, MemoryPart part) throws IOException {
		return createConcurrentAppendableMemory(columnHome, efmModel, iteration, part);
	}
	
	
}
