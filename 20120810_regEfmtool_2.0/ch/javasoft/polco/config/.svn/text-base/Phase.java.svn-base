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

import org.dom4j.DocumentException;
import org.dom4j.Element;

import ch.javasoft.factory.ConfigException;
import ch.javasoft.factory.FactoryNotFoundException;
import ch.javasoft.factory.IllegalFactoryException;
import ch.javasoft.xml.config.XmlConfigException;

/**
 * The <code>Phase</code> denotes a phase of the algorithm. Note that different
 * {@link Arithmetic} instances may be used at different algorithm phases.
 */
public enum Phase {
	/**
	 * The <i>Pre</i> phase denotes pre-processing, such as file parsing, 
	 * cone transformations.
	 */
	Pre {
		@Override
		protected XmlElement getNumericXmlElement() {
			return XmlElement.numeric_pre;
		}
	},
	/**
	 * The <i>Core</i> phase denotes main computation task of the algorithm.
	 * The extreme rays are computed in this phase.
	 */
	Core {
		@Override
		protected XmlElement getNumericXmlElement() {
			return XmlElement.numeric;
		}		
	},
	/**
	 * The <i>Post</i> phase denotes post-processing, such as writing output to
	 * an output file and cone back-transformation.
	 */
	Post {
		@Override
		protected XmlElement getNumericXmlElement() {
			return XmlElement.numeric_post;
		}		
	};
	
	/**
	 * Returns the {@link Arithmetic} instance associated with the {@code this}
	 * phase, taken from the specified config.
	 * 
	 * @param config	the configuration to derive the arithmetic to use
	 * 
	 * @return	the arithmetic for {@code this} algorith phase
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws DocumentException 
	 * @throws XmlConfigException 
	 * @throws IllegalFactoryException 
	 * @throws FactoryNotFoundException 
	 * @throws ConfigException 
	 */
	public Arithmetic getArithmetic(Element config) throws ConfigException, FactoryNotFoundException, IllegalFactoryException, XmlConfigException, DocumentException, InstantiationException, IllegalAccessException {
		return PolcoConfig.getArithmetic(config, this);
	}
	
	/**
	 * Returns the numeric {@link XmlElement} constant associated with the 
	 * {@code this} phase.
	 */
	abstract protected XmlElement getNumericXmlElement();
}
