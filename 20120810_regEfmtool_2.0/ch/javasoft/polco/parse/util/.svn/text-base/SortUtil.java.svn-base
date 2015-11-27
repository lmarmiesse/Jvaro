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
package ch.javasoft.polco.parse.util;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Comparator;

import org.dom4j.Element;

import ch.javasoft.math.array.NumberOperators;
import ch.javasoft.math.array.sort.LexMinArrayComparator;
import ch.javasoft.math.array.sort.MatrixSortUtil;
import ch.javasoft.polco.config.Arithmetic;
import ch.javasoft.polco.config.PolcoConfig;
import ch.javasoft.polco.config.XmlAttribute;
import ch.javasoft.polco.config.XmlElement;
import ch.javasoft.util.ExceptionUtil;
import ch.javasoft.util.numeric.Zero;
import ch.javasoft.xml.config.XmlConfigException;
import ch.javasoft.xml.config.XmlUtil;

/**
 * The <code>SortUtil</code> contains a static method for matrix row sorting,
 * according to the input sorting option in the configuration.
 */
public class SortUtil {
	
	/**
	 * If an input sorting option is specified in {@code config}, it is used to
	 * sort the rows of the givem {@code matrix}. 
	 * 
	 * @type N	the number type of a single number
	 * @type A	the number type of an array of numbers
	 * 
	 * @param config		the configuration with the input sorting option
	 * @param arithmetic	arithmetic for numeric operations, such as 
	 * 						comparison of numbers
	 * @param zero			a zero object to treat almost zero values
	 * @param matrix		the matrix to sort
	 */
	@SuppressWarnings("unchecked")
	public static <N extends Number, A> void sortMatrix(Element config, Arithmetic<N, A> arithmetic, Zero zero, A[] matrix) throws IOException {
		if (config == null) return;
		try {
			final Element elPolcoConfig = PolcoConfig.getPolcoConfig(config.getDocument()); 
			final Element elInput = XmlUtil.getRequiredSingleChildElement(elPolcoConfig, XmlElement.input);
			final String sort = XmlUtil.getRequiredAttributeValue(elInput, XmlAttribute.sort);
			
			if ("default".equals(sort)) return;
	
			final String className = getComparatorClassName(sort);
			final Comparator<A> comparator;
			try {
				final Class<?> clazz = Class.forName(className);
				final Constructor<?> cons = clazz.getConstructor(NumberOperators.class);
				comparator = (Comparator<A>)cons.newInstance(arithmetic.getNumberOperators(zero));
			}
			catch (Exception e) {
				throw new XmlConfigException("could not instantiate sorter class " + className + ", e=" + e, XmlUtil.getElementPath(elInput, true), e); 
			}
			MatrixSortUtil.sortMatrixRows(matrix, comparator);
		}
		catch (Exception e) {
			throw ExceptionUtil.toRuntimeExceptionOr(IOException.class, e);
		}
	}
	private static String getComparatorClassName(String sort) {
		final String clsNameRef = LexMinArrayComparator.class.getName();
		final String clsNameSimpleRef = LexMinArrayComparator.class.getSimpleName();
		final String clsNameSimpleThis = sort + clsNameSimpleRef.substring("LexMin".length());
		return clsNameRef.replace(clsNameSimpleRef, clsNameSimpleThis);
	}
}
