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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import ch.javasoft.factory.ConfigException;
import ch.javasoft.factory.FactoryNotFoundException;
import ch.javasoft.factory.FactoryUtil;
import ch.javasoft.factory.IllegalFactoryException;
import ch.javasoft.util.logging.Loggers;
import ch.javasoft.util.logging.SystemProperties;
import ch.javasoft.util.logging.matlab.LogConfiguration;
import ch.javasoft.util.logging.matlab.LogConfigurationReader;
import ch.javasoft.util.numeric.Zero;
import ch.javasoft.xml.config.MissingReferableException;
import ch.javasoft.xml.config.XmlArgException;
import ch.javasoft.xml.config.XmlConfig;
import ch.javasoft.xml.config.XmlConfigException;
import ch.javasoft.xml.config.XmlUtil;
import ch.javasoft.xml.factory.XmlFactoryUtil;

/**
 * The <code>PolcoConfig</code> ... TODO javadoc-PolcoConfig-type
 * 
 */
public class PolcoConfig {
	
	public static final String CONFIG_FILE	= "config/polco.xml";

	public static final String DEFAULT_CONFIG_NAME	= "default";
	public static final String DEFAULT_APP_NAME		= "polco";

	private static final String HELP_NAME			= "polco-help";
	private static final String NOARGS_NAME			= "polco-noargs";
	private static final String VERSION_NAME		= "polco-version";

	public static Element getPolco(XmlConfig xmlConfig) throws XmlConfigException, DocumentException {
		return getPolco(xmlConfig.getDefaultConfigElement());		
	}
	public static Element getPolco(Document xmlConfig) throws XmlConfigException, DocumentException {
		return getPolco(XmlConfig.getXmlConfig(xmlConfig));
	}
	public static Element getPolco(Element xmlConfig) throws XmlConfigException {
		return XmlUtil.getRequiredSingleChildElement(xmlConfig, XmlElement.polco);		
	}
	public static Element getPolcoExtremeRayEnum(XmlConfig xmlConfig) throws XmlConfigException, DocumentException {
		return XmlUtil.getRequiredSingleChildElement(getPolco(xmlConfig), XmlElement.extreme_ray_enum);
	}
	public static Element getPolcoParse(XmlConfig xmlConfig) throws XmlConfigException, DocumentException {
		return XmlUtil.getRequiredSingleChildElement(getPolco(xmlConfig), XmlElement.parse);
	}
	public static Element getPolcoCallback(XmlConfig xmlConfig) throws XmlConfigException, DocumentException {
		return XmlUtil.getRequiredSingleChildElement(getPolco(xmlConfig), XmlElement.callback);
	}
	public static Element getPolcoConfig(XmlConfig xmlConfig) throws XmlConfigException, DocumentException {
		return XmlUtil.getRequiredSingleChildElement(getPolco(xmlConfig), XmlElement.config);
	}
	public static Element getPolcoConfig(Document xmlConfig) throws XmlConfigException, DocumentException {
		return XmlUtil.getRequiredSingleChildElement(getPolco(xmlConfig), XmlElement.config);
	}
	public static Element getPolcoConfigNumeric(XmlConfig xmlConfig, Phase phase) throws XmlConfigException, DocumentException {
		return XmlUtil.getRequiredSingleChildElement(getPolcoConfig(xmlConfig), phase.getNumericXmlElement());
	}
	public static Element getPolcoConfigNumeric(Document xmlConfig, Phase phase) throws XmlConfigException, DocumentException {
		return XmlUtil.getRequiredSingleChildElement(getPolcoConfig(xmlConfig), phase.getNumericXmlElement());
	}
	public static Element getPolcoConfigNumericArithmetic(XmlConfig xmlConfig, Phase phase) throws XmlConfigException, DocumentException {
		return XmlUtil.getRequiredSingleChildElement(getPolcoConfigNumeric(xmlConfig, phase), XmlElement.arithmetic);
	}
	public static Element getPolcoConfigNumericArithmetic(Document xmlConfig, Phase phase) throws XmlConfigException, DocumentException {
		return XmlUtil.getRequiredSingleChildElement(getPolcoConfigNumeric(xmlConfig, phase), XmlElement.arithmetic);
	}
	
	public static Arithmetic getArithmetic(XmlConfig xmlConfig, Phase phase) throws XmlConfigException, DocumentException, InstantiationException, IllegalAccessException, ConfigException, FactoryNotFoundException, IllegalFactoryException {
		final Element elArithmetic = PolcoConfig.getPolcoConfigNumericArithmetic(xmlConfig, phase);
		return FactoryUtil.create(Arithmetic.class, ArithmeticFactory.class, elArithmetic);
	}
	public static Arithmetic getArithmetic(Element config, Phase phase) throws XmlConfigException, DocumentException, InstantiationException, IllegalAccessException, ConfigException, FactoryNotFoundException, IllegalFactoryException {
		if (XmlElement.arithmetic.getXmlName().equals(config.getName())) {
			return FactoryUtil.create(Arithmetic.class, ArithmeticFactory.class, config);
		}
		return getArithmetic(config.getDocument(), phase);
	}
	public static Arithmetic getArithmetic(Document xmlConfig, Phase phase) throws FactoryNotFoundException, IllegalFactoryException, ConfigException, XmlConfigException, DocumentException {
		final Element elArithmetic = PolcoConfig.getPolcoConfigNumericArithmetic(xmlConfig.getDocument(), phase);
		return FactoryUtil.create(Arithmetic.class, ArithmeticFactory.class, elArithmetic);
	}
	
	public static Zero getZero(XmlConfig xmlConfig, Phase phase) throws XmlConfigException, DocumentException, ConfigException {
		final Element elNumeric = PolcoConfig.getPolcoConfigNumeric(xmlConfig, phase);
		final Element elZero	= XmlUtil.getRequiredSingleChildElement(elNumeric, XmlElement.zero);
		return getZero(elZero, phase);
	}
	public static Zero getZero(Element config, Phase phase) throws XmlConfigException, DocumentException, ConfigException {
		if (XmlElement.zero.getXmlName().equals(config.getName())) {
			final String strZero = XmlUtil.getRequiredAttributeValue(config, XmlAttribute.value);
			try {
				final double tolerance = Double.parseDouble(strZero.trim());
				if (Double.isNaN(tolerance)) {
					return getArithmetic(config, phase).getDefaultZero();
				}
				return new Zero(tolerance);
			}
			catch (Exception e) {
				throw new ConfigException("cannot parse zero value \"" + strZero + "\", e=" + e, e, XmlUtil.getElementPath(config, true));
			}
		}
		return getZero(config.getDocument(), phase);
	}
	public static Zero getZero(Document xmlConfig, Phase phase) throws XmlConfigException, DocumentException, ConfigException {
		final Element elNumeric = PolcoConfig.getPolcoConfigNumeric(xmlConfig.getDocument(), phase);
		final Element elZero	= XmlUtil.getRequiredSingleChildElement(elNumeric, XmlElement.zero);
		return getZero(elZero, phase);
	}

	/**
	 * Same as {@link #parseXmlConfig(String[])}, but resolving is forced, and
	 * potential resolve errors are displayed on the system error stream.
	 */
	public static XmlConfig resolveXmlConfig(String[] args) throws FileNotFoundException, DocumentException, XmlConfigException {
		final XmlConfig xmlConfig = parseXmlConfig(args);
		try {
			xmlConfig.getDefaultConfigDocument();
		} 
		catch (XmlArgException ex) {
			if (ex.isOption()) {
				System.err.println("ERROR:   missing " + ex.getOptionWithIndex() + " option");
			}
			System.err.println("DETAILS: " + ex.getLocalizedMessage());
			ex.printStackTrace();//NOTE remove this?
			System.err.println("use --help option to display help message");
			throw ex;
		}
		catch (MissingReferableException ex) {
			if (("config[" + DEFAULT_CONFIG_NAME + "]/metabolic-parse/parse").equals(ex.getPath())) {
				System.err.println("ERROR:   invalid input kind option '" + ex.getReferable() + "'");
			}
			System.err.println("DETAILS: " + ex.getLocalizedMessage());
			System.err.println("use --help option to display help message");
			throw ex;
		}
		catch (XmlConfigException ex) {
			throw ex;
		}
		return xmlConfig;
	}
	/**
	 * Parses the config, but does not resolve it, that is, all options 
	 * specified by {@code args} are not replaced yet in the configuration file. 
	 * However, default config, application name and the specified arguments are 
	 * stored in the returned xml config instance. 
	 */
	public static XmlConfig parseXmlConfig(String[] args) throws FileNotFoundException, DocumentException {
		final InputStream in;
		String config = DEFAULT_CONFIG_NAME;
		if (args.length >= 1 && !args[0].startsWith("-")) {
//			System.out.println("using config file: " + args[0]);
			in = new FileInputStream(args[0]);
			if (args.length >= 2 && !args[1].startsWith("-")) {
				config = args[1];
//				System.out.println("using config entry: " + args[1]);
			}
		}
		else {
			final File file = new File(CONFIG_FILE);
			if (file.canRead()) {
//				System.out.println("using config file: " + file.getAbsolutePath());
				in = new FileInputStream(CONFIG_FILE);
			}
			else {
//				System.out.println("using package config file: " + "/" + CONFIG_FILE);
				in = PolcoConfig.class.getResourceAsStream("/" + CONFIG_FILE); 
			}
			if (in == null) throw new FileNotFoundException("/" + CONFIG_FILE);
		}
		final XmlConfig xmlConfig = XmlConfig.getXmlConfig(DEFAULT_APP_NAME, in, args);
		xmlConfig.setAppName(DEFAULT_APP_NAME);
		if (config != null) {
			xmlConfig.setDefaultConfigName(config);
		}
		return xmlConfig;
	}

	/**
	 * Initializes logging for matlab. Within matlab, logging cannot be 
	 * initialized using the log manager since the system class loader does not 
	 * know classes in the dynamic path.
	 */
	public static void initLoggingConfigurationForMatlab(XmlConfig config) throws XmlConfigException, IOException {
		final Properties logProps = config.getLoggingProperties();
		final LogConfiguration logConfig = logProps == null ? new LogConfiguration() : new LogConfiguration(logProps);
		System.setProperty(SystemProperties.LogManagerPropertiesClass.getPropertyName(), new LogConfigurationReader(logConfig).getClass().getName());
        final Logger logger = ch.javasoft.util.logging.Loggers.getRootLogger();
        logger.info("logger initialized");
	}

	/**
	 * The factory class name is taken from the xml configuration using the 
	 * {@link XmlAttribute#factory factory} attribute. The factory is 
	 * instantiated and an object created and returned, again using the xml 
	 * configuration.
	 *  
	 * @param <T>					type of objects produced by the factory 
	 * @param clazz					class of objects produced by the factory
	 * @param config				the element containing configuration 
	 * 								settings for the object to create
	 * @throws ConfigException 
	 * @throws IllegalFactoryException 
	 * @throws FactoryNotFoundException 
	 * 
	 * @throws ConfigException 			if the factory class attribute was not
	 * 									found in {@code config}, or if the 
	 * 									config content was not as expected
	 * @throws FactoryNotFoundException	if the factory class was not found
	 * @throws IllegalFactoryException	if the factory class was not a factory,
	 * 									if the factory could not be instantiated,
	 * 									if the config was not of the expected 
	 * 									type for this factory, or if the object 
	 * 									created by the factory was not 
	 * 									compatible with the type specified by 
	 * 									{@code clazz} 
	 * 
	 * @see XmlFactoryUtil#create(Class, Element, String)
	 */
	public static <T> T createFromFactory(Class<T> clazz, Element config) throws FactoryNotFoundException, IllegalFactoryException, ConfigException {
		return XmlFactoryUtil.create(clazz, config, XmlAttribute.factory.getXmlName());
	}
	/**
	 * Traces the polco configuration using the given logger and log level.
	 * 
	 * @param logger	the logger for tracing
	 * @param level		the log level
	 * @param xmlConfig	the config to trace
	 */
	public static void tracePolcoConfig(Logger logger, Level level, XmlConfig xmlConfig) throws XmlConfigException, DocumentException {
		final Element elPolcoConfig = getPolco(xmlConfig);
		logger.log(level, "polco config:");
		logger.log(level, "..args             : " + traceArgsString("", xmlConfig.getArgs()));
		logger.log(level, "..enum factory     : " + getForTracing(elPolcoConfig, XmlAttribute.factory, XmlElement.extreme_ray_enum));
		logger.log(level, "..parser factory   : " + getForTracing(elPolcoConfig, XmlAttribute.factory, XmlElement.parse));
		logger.log(level, "..output callback  : " + getForTracing(elPolcoConfig, XmlAttribute.factory, XmlElement.callback));
		logger.log(level, "..arithmetic");
		logger.log(level, "....-core          : " + getForTracing(elPolcoConfig, XmlAttribute.value, XmlElement.config, XmlElement.numeric, XmlElement.arithmetic));
		logger.log(level, "....-preprocessing : " + getForTracing(elPolcoConfig, XmlAttribute.value, XmlElement.config, XmlElement.numeric_pre, XmlElement.arithmetic));
		logger.log(level, "....-post/output   : " + getForTracing(elPolcoConfig, XmlAttribute.value, XmlElement.config, XmlElement.numeric_post, XmlElement.arithmetic));
		logger.log(level, "..zero");
		logger.log(level, "....-core          : " + getForTracing(elPolcoConfig, XmlAttribute.value, XmlElement.config, XmlElement.numeric, XmlElement.zero));
		logger.log(level, "....-preprocessing : " + getForTracing(elPolcoConfig, XmlAttribute.value, XmlElement.config, XmlElement.numeric_pre, XmlElement.zero));
		logger.log(level, "....-post/output   : " + getForTracing(elPolcoConfig, XmlAttribute.value, XmlElement.config, XmlElement.numeric_post, XmlElement.zero));
		logger.log(level, "..sort input       : " + getForTracing(elPolcoConfig, XmlAttribute.sort, XmlElement.config, XmlElement.input));
	}
	/**
	 * Traces the polco junit configuration on INFO level using the root logger
	 * 
	 * @param arithmetic	the arithmetic used
	 * @param zero			the zero value
	 * @param testName		the test name invoking this method
	 */
	public static void traceJunitConfig(Arithmetic<?, ?> arithmetic, Zero zero, String testName) throws XmlConfigException, DocumentException {
		final Logger logger = Loggers.getRootLogger();
		logger.info("polco junit config:");
		logger.info("..test name        : " + testName);
		logger.info("..arithmetic       : " + arithmetic);
		logger.info("..zero             : " + zero.mZeroPos);
	}
	private static String getForTracing(Element element, XmlAttribute attribute, XmlElement... elements) {
		try {
			for (int i = 0; i < elements.length; i++) {
				element = XmlUtil.getRequiredSingleChildElement(element, elements[i]);
			}
			final String value = XmlUtil.getRequiredAttributeValue(element, attribute);
			if (XmlAttribute.factory.equals(attribute) && value != null) {
				int index = value.lastIndexOf('.');
				if (index >= 0) {
					return value.substring(index + 1);
				}
			}
			return value;
		}
		catch (Exception e) {
			return "<ERROR>, e=" + e;
		}
	}
	/**
	 * Traces the given (command line) arguments using the specified stream
	 * 
	 * @param print		the stream for tracing
	 * @param prefix	the prefix to use for the log line
	 * @param args		the (command line) arguments to trace
	 */
	public static void traceArgs(PrintStream print, String prefix, String[] args) {
		print.println(traceArgsString(prefix, args));
		
	}
	/**
	 * Returns the given arguments as a command-line-like string
	 * 
	 * @param prefix	the prefix to use for the command line
	 * @param args		the (command line) arguments to trace
	 */
	public static String traceArgsString(String prefix, String[] args) {
		final StringBuilder sb = new StringBuilder(prefix);
		for (int i = 0; i < args.length; i++) {
			if (i > 0) sb.append(' ');
			sb.append(args[i]);
		}
		return sb.toString();
	}
	public static void printVersion(PrintStream stream, XmlConfig xmlConfig) throws XmlConfigException {
		xmlConfig.printUsage(stream, VERSION_NAME);
	}
	public static void printHelp(PrintStream stream, XmlConfig xmlConfig) throws XmlConfigException {
		xmlConfig.printUsage(stream, HELP_NAME);
	}
	public static void printNoArgs(PrintStream stream, XmlConfig xmlConfig) throws XmlConfigException {
		xmlConfig.printUsage(stream, NOARGS_NAME);
	}

}
