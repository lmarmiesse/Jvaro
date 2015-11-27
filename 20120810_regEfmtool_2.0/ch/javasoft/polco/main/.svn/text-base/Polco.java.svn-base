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
package ch.javasoft.polco.main;

import java.io.IOException;
import java.util.logging.Level;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import ch.javasoft.factory.ConfigException;
import ch.javasoft.factory.FactoryNotFoundException;
import ch.javasoft.factory.IllegalFactoryException;
import ch.javasoft.math.linalg.LinAlgOperations;
import ch.javasoft.polco.PolyhedralCone;
import ch.javasoft.polco.config.Arithmetic;
import ch.javasoft.polco.config.ArithmeticCallback;
import ch.javasoft.polco.config.Phase;
import ch.javasoft.polco.config.PolcoConfig;
import ch.javasoft.polco.config.TypeConverter;
import ch.javasoft.polco.config.XmlElement;
import ch.javasoft.polco.xenum.ExtremeRayCallback;
import ch.javasoft.polco.xenum.ExtremeRayEnumerator;
import ch.javasoft.util.logging.LogPrintStream;
import ch.javasoft.util.logging.Loggers;
import ch.javasoft.util.numeric.Zero;
import ch.javasoft.xml.config.ConstConfigParser;
import ch.javasoft.xml.config.MissingReferableException;
import ch.javasoft.xml.config.XmlArgException;
import ch.javasoft.xml.config.XmlConfig;
import ch.javasoft.xml.config.XmlConfigException;
import ch.javasoft.xml.config.XmlUtil;

/**
 * Polco is a program to compute extreme rays of polyhedral cones. It contains
 * the main method and supports different input options. Input options are 
 * described in the configuration file {@code config/polco.xml}.
 */
public class Polco {
	
	public static enum ExitVal {
		/** Exit value for normal termination */
		Ok, 
		/** Exit value for invocation without arguments*/
		NoArgs,
		/** Exit value for invocation with help or version option and invalid extra options */
		ImpureHelpOrVersionInvocation,
		/** Exit value for missing or invalid input argument or option */
		InvalidConfigArg,
		/** Exit value for missing referable in config, e.g. no kind was specified */
		InvalidConfigRef,
		/** Exit value for occurrence of unexpected exception */
		UnexpectedException,
		/** Exit value with unknown reason, should not happen */
		Unknown;
		
		/**
		 * Returns the exit value, equal to the negation of 
		 * {@link #ordinal() ordinal}. Consequently, zero is returned for 
		 * {@link #Ok}, and a  small negative integer otherwise.
		 */
		public int getExitValue() {
			return -ordinal();
		}
		
		/**
		 * Calls {@link System#exit(int)} with the 
		 * {@link #getExitValue() exit value} associated with this constant.
		 */
		public void exit() {
			System.exit(getExitValue());			
		}
	}

	private final XmlConfig config;
	
	/**
	 * Creates a new polco instance using the given XML configuration. The XML
	 * configuration must be 
	 * {@link PolcoConfig#resolveXmlConfig(String[]) resolved}.
	 * 
	 * @exception	XmlConfigException if the configuration is erroneous		
     * @exception	SecurityException  if a security manager exists and if
     *				the caller does not have LoggingPermission("control").
     * @exception	IOException if there are problems reading the properties
	 */
	public Polco(XmlConfig config) throws SecurityException, IOException, XmlConfigException {
		this.config = config;
		Loggers.initLogManagerConfiguration(config.getLoggingProperties());
		final LogPrintStream logStream = new LogPrintStream(LogPkg.LOGGER, Level.INFO);
		PolcoConfig.printVersion(logStream, config);
		logStream.flush();
	}
	
	/**
	 * Calls the extreme ray enumeration. System.exit is called after 
	 * completion, returning the number of extreme rays. For abnormal
	 * termination, a negative exit value is returned.
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 0) {
				final XmlConfig xmlConfig = PolcoConfig.parseXmlConfig(args); 
				PolcoConfig.printNoArgs(System.err, xmlConfig);
				ExitVal.NoArgs.exit();
			}
			else if (args.length >= 1 && ("--help".equals(args[0]) || "-h".equals(args[0]) || "-?".equals(args[0]) || "?".equals(args[0]) || "--version".equals(args[0]) || "-v".equals(args[0]))) {
				final XmlConfig xmlConfig = PolcoConfig.parseXmlConfig(args); 
				boolean err = args.length > 1;
				if ("--help".equals(args[0]) || "-h".equals(args[0]) || "-?".equals(args[0]) || "?".equals(args[0])) {
					PolcoConfig.printHelp(err ? System.err : System.out, xmlConfig);
					if (err) ExitVal.ImpureHelpOrVersionInvocation.exit();
					else ExitVal.Ok.exit();
				}
				if ("--version".equals(args[0]) || "-v".equals(args[0])) {
					PolcoConfig.printVersion(err ? System.err : System.out, xmlConfig);
					if (err) ExitVal.ImpureHelpOrVersionInvocation.exit();
					else ExitVal.Ok.exit();
				}
			}
			try {
				final XmlConfig xmlConfig = PolcoConfig.resolveXmlConfig(args);
				final Polco polco = new Polco(xmlConfig);
				polco.call();
			}
			catch (XmlArgException ex) {
				//already traced
				ExitVal.InvalidConfigArg.exit();
			}
			catch (MissingReferableException ex) {
				//already traced
				ExitVal.InvalidConfigRef.exit();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			PolcoConfig.traceArgs(System.err, Polco.class.getName() + " with following arguments: ", args);
			ExitVal.UnexpectedException.exit();
		}
		ExitVal.Ok.exit();
	}
	/**
	 * Calls the extreme ray enumeration using the types, input parser and 
	 * output callback from the configuration.
	 * 
	 * @return the number of extreme rays
	 */
	private long call() throws Exception {
		PolcoConfig.tracePolcoConfig(LogPkg.LOGGER, Level.INFO, config);
		final Arithmetic<?,?> arithmeticPre  = PolcoConfig.getArithmetic(config, Phase.Pre);
		final Arithmetic<?,?> arithmeticPost = PolcoConfig.getArithmetic(config, Phase.Post);
		final ArithmeticCallback<Long> postCb = new ArithmeticCallback<Long>() {
			public <NPost extends Number, APost> Long callback(final Arithmetic<NPost, APost> arithmeticPost) throws Exception  {
				final ArithmeticCallback<Long> preCb = new ArithmeticCallback<Long>() {
					public <NPre extends Number, APre> Long callback(Arithmetic<NPre, APre> arithmeticPre) throws FactoryNotFoundException, IllegalFactoryException, ConfigException, XmlConfigException, DocumentException  {
						final long res = callInternal(arithmeticPre, arithmeticPost);
						return Long.valueOf(res);
					}
				};
				return arithmeticPre.callback(preCb);
			}
		};
		return arithmeticPost.callback(postCb).longValue();
	}
	
	/**
	 * Calls the typed extreme ray enumeration using the types specified by the
	 * arithmetics. Input parser and output callback are taken from the 
	 * configuration.
	 * 
	 * @param arithmeticPre 	arithmetic for pre-processing
	 * @param arithmeticPost	arithmetic for post-processing
	 * @return the number of extreme rays
	 */
	private <NPost extends Number, APost, NPre extends Number, APre> long callInternal(Arithmetic<NPre, APre> arithmeticPre, Arithmetic<NPost, APost> arithmeticPost) throws FactoryNotFoundException, IllegalFactoryException, ConfigException, XmlConfigException, DocumentException  {
		final Element elParse		= PolcoConfig.getPolcoParse(config);
		final Element elCallback	= PolcoConfig.getPolcoCallback(config);

		final PolyhedralCone<NPre, APre> polycone	= arithmeticPre.createNumeric(PolyhedralCone.class, elParse);
		final ExtremeRayCallback<NPost, APost> cb 	= arithmeticPost.createNumeric(ExtremeRayCallback.class, elCallback);
		
		return callInternal(polycone, cb, arithmeticPost);
	}

	/**
	 * Calls the typed extreme ray enumeration for the given input cone. To 
	 * return the extreme rays, the given {@code callback} is used. Types and
	 * options are taken from the configuration. 
	 * 
	 * @param polycone	the input cone
	 * @param callback	callback to receive result rays
	 * @return the number of extreme rays
	 * 
	 * @throws IllegalStateException	if any exception occurs usually 
	 * 									associated with erroneous XML 
	 * 									configuration (original exception is 
	 * 									nested as cause exception) 		
	 */
	public <NPost extends Number, APost, NPre extends Number, APre> long call(final PolyhedralCone<NPre, APre> polycone, final ExtremeRayCallback<NPost, APost> callback, final LinAlgOperations<NPost, APost> linalgOpsPost) throws IllegalStateException {
		try {
			PolcoConfig.tracePolcoConfig(LogPkg.LOGGER, Level.INFO, config);
			final Arithmetic<?,?> arithmeticPre = PolcoConfig.getArithmetic(config, Phase.Pre);
			final Arithmetic<?,?> arithmeticPost = PolcoConfig.getArithmetic(config, Phase.Post);
			final Zero zeroPre	= PolcoConfig.getZero(config, Phase.Pre);
			final Zero zeroPost = PolcoConfig.getZero(config, Phase.Post);
			
			final Element elConfig	= PolcoConfig.getPolcoConfig(config);
			final Element elConvert	= XmlUtil.getRequiredSingleChildElement(elConfig, XmlElement.convert);
			final Element elMatrix	= XmlUtil.getRequiredSingleChildElement(elConvert, XmlElement.matrix_scaling);
			final boolean allowMatrixRowScaling = ConstConfigParser.parseBooleanConstant(elMatrix);
	
			final ArithmeticCallback<Long> postCb = new ArithmeticCallback<Long>() {
				public <NPo extends Number, APo> Long callback(final Arithmetic<NPo, APo> arithmeticPo) throws Exception {
					final TypeConverter<NPost, APost, NPo, APo> postConverter = new TypeConverter<NPost, APost, NPo, APo>(linalgOpsPost, arithmeticPo.getLinAlgOperations(zeroPost), allowMatrixRowScaling);
					final ExtremeRayCallback<NPo, APo> cb = postConverter.convertCallback(callback);
					final ArithmeticCallback<Long> preCb = new ArithmeticCallback<Long>() {
						public <NPr extends Number, APr> Long callback(Arithmetic<NPr, APr> arithmeticPr) throws Exception {
							final TypeConverter<NPre, APre, NPr, APr> preConverter = new TypeConverter<NPre, APre, NPr, APr>(polycone.getLinAlgOperations(), arithmeticPr.getLinAlgOperations(zeroPre), allowMatrixRowScaling);
							final PolyhedralCone<NPr, APr> cone = preConverter.convertPolyhedralCone(polycone);
							final long res = callInternal(cone, cb, arithmeticPo);
							return Long.valueOf(res);
						}
					};
					return arithmeticPre.callback(preCb);
				}
			};
			return arithmeticPost.callback(postCb).longValue();
		}
		catch (Error ex) {
			throw ex;
		}
		catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}
	/**
	 * Calls the typed extreme ray enumeration using the types specified by the
	 * arithmetics. The given polyhedral cone is used as input, and the 
	 * specified callback is use to return the results.
	 * 
	 * @param polycone			the input cone
	 * @param callback			callback to receive result
	 * @param arithmeticPost	arithmetic for post-processing
	 * @return the number of extreme rays
	 */
	private <NPost extends Number, APost, NPre extends Number, APre> long callInternal(PolyhedralCone<NPre, APre> polycone, ExtremeRayCallback<NPost, APost> callback, Arithmetic<NPost, APost> arithmeticPost) throws FactoryNotFoundException, IllegalFactoryException, ConfigException, XmlConfigException, DocumentException  {
		final Element elXray	= PolcoConfig.getPolcoExtremeRayEnum(config);
		final Zero zeroPost		= PolcoConfig.getZero(config, Phase.Post);

		final ExtremeRayEnumerator alg = PolcoConfig.createFromFactory(ExtremeRayEnumerator.class, elXray);
		final LinAlgOperations<NPost, APost> cbOps	= arithmeticPost.getLinAlgOperations(zeroPost);
		
		return alg.enumerateExtremeRays(polycone, callback, cbOps);
	}

}
