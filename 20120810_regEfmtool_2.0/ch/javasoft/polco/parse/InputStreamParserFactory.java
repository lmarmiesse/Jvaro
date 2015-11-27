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
package ch.javasoft.polco.parse;

import java.io.InputStream;

import org.dom4j.Element;

import ch.javasoft.factory.ConfigException;
import ch.javasoft.polco.Numeric;
import ch.javasoft.polco.PolyhedralCone;
import ch.javasoft.polco.config.Arithmetic;
import ch.javasoft.polco.config.NumericFactory;
import ch.javasoft.polco.config.Phase;
import ch.javasoft.polco.config.PolcoConfig;
import ch.javasoft.util.numeric.Zero;
import ch.javasoft.xml.config.StreamConfigParser;
import ch.javasoft.xml.config.XmlNode;
import ch.javasoft.xml.config.XmlUtil;
import ch.javasoft.xml.factory.XmlConfiguredFactory;

/**
 * The <code>InputStreamParserFactory</code> parses data from a single input
 * stream.
 */
public class InputStreamParserFactory implements NumericFactory {
	
	public static enum XmlElement implements XmlNode {
		input;
		public String getXmlName() {
			return name().replaceAll("_", "-");
		}
	}
	
	private final InputStreamParser parser;
	
	public InputStreamParserFactory(InputStreamParser parser) {
		this.parser = parser;
	}
	
	/**
	 * Returns a typed xml factory, which expects that the config looks as 
	 * follows:
	 * <pre>
		<parse factory="ch.javasoft.polco.parse.CddParserFactory">
			<input type="file">
				<file name="${-in[1]}"/>
			</input>
		</parse>
	 * </pre>
	 */
	public <Num extends Number, Arr> XmlConfiguredFactory<? extends Numeric<Num, Arr>> createTypedFactory(final Arithmetic<Num, Arr> arithmetic) {
		return new XmlConfiguredFactory<PolyhedralCone<Num, Arr>>() {
			public PolyhedralCone<Num, Arr> create(Element config) throws ConfigException {
				try {
					final Element elInput = XmlUtil.getRequiredSingleChildElement(config, XmlElement.input);
					final Zero zero = PolcoConfig.getZero(config, Phase.Pre);
					final InputStream in = StreamConfigParser.parseInputStream(elInput);
					return parser.parse(config, arithmetic, zero, in);
				}
				catch (Exception e) {
					throw new ConfigException(e, XmlUtil.getElementPath(config, true));
				}
			}
		};
	}
	
}
