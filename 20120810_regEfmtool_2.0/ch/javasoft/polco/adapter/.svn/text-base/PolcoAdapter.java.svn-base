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
package ch.javasoft.polco.adapter;

import java.math.BigInteger;

import ch.javasoft.math.BigFraction;
import ch.javasoft.math.linalg.LinAlgOperations;
import ch.javasoft.math.operator.impl.BigFractionOperators;
import ch.javasoft.math.operator.impl.BigIntegerOperators;
import ch.javasoft.math.operator.impl.DoubleOperators;
import ch.javasoft.polco.PolyhedralCone;
import ch.javasoft.polco.config.PolcoConfig;
import ch.javasoft.polco.impl.DefaultInequalityCone;
import ch.javasoft.polco.impl.DefaultPolyhedralCone;
import ch.javasoft.polco.main.Polco;
import ch.javasoft.util.ExceptionUtil;
import ch.javasoft.xml.config.MissingReferableException;
import ch.javasoft.xml.config.XmlArgException;
import ch.javasoft.xml.config.XmlConfig;
import ch.javasoft.xml.config.XmlConfigException;

/**
 * The <code>PolcoAdapter</code> is an interface to the polco program if it is
 * used as a library from another java program. Polco computes extreme rays of
 * a polyhedral cone.
 */
public class PolcoAdapter {
	
	private final Polco polco;

	/**
	 * Creates a new polco adapter instance using default options. The XML 
	 * configuration file is read and all configuration settings are 
	 * initialized.
	 * 
	 * @exception	XmlConfigException if the configuration is erroneous		
	 */
	public PolcoAdapter() throws XmlConfigException {
		this(new Options());
	}
	/**
	 * Creates a new polco adapter instance using the specified options. The XML 
	 * configuration file is read and all configuration settings are 
	 * initialized.
	 * 
	 * @exception	XmlConfigException if the configuration is erroneous		
	 */
	public PolcoAdapter(Options options) throws XmlConfigException {
		try {
			try {
				final XmlConfig xmlConfig = PolcoConfig.resolveXmlConfig(options.toArgs());
				this.polco = new Polco(xmlConfig);
			}
			catch (XmlArgException ex) {
				//already traced
				throw ex;
			}
			catch (MissingReferableException ex) {
				//already traced
				throw ex;
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			PolcoConfig.traceArgs(System.err, PolcoAdapter.class.getName() + " with following arguments: ", options.toArgs());
			throw ExceptionUtil.toRuntimeExceptionOr(XmlConfigException.class, ex);
		}
	}
	
	/**
	 * Computes and returns the extreme rays of a polyhedral cone
	 * <pre>
	 * P = { x | eq*x = 0 , iq*x &ge; 0 } = { x | x = R*c , c &ge; 0 }.
	 * </pre>
	 * The equality matrix <tt>eq</tt> may be {@code null}. The returned matrix
	 * <tt>R<sup>T</sup></tt> is the transformed matrix <tt>R</tt>, that is, 
	 * every row of the returned matrix corresponds to an extreme ray. Hence, 
	 * the second dimension <tt>d</tt> of {@code eq}, {@code iq} and {@code R} 
	 * must be equal (the number of columns).
	 * 
	 * @param eq	the equality matrix, or {@code null} if no equalities are 
	 * 				present 
	 * @param iq	the inequality matrix, {@code null} is not permitted
	 * @return	the transpose <tt>R<sup>T</sup></tt> of the extreme ray matrix
	 * 			<tt>R</tt> containing one extreme ray per matrix row
	 */
	public double[][] getDoubleRays(double[][] eq, double[][] iq) {
		final LinAlgOperations<Double, double[]> dblOps = DoubleOperators.DEFAULT.getLinAlgOperations();
		final PolyhedralCone<Double, double[]> cone;
		if (eq == null || eq.length == 0) {
			cone = new DefaultInequalityCone<Double, double[]>(dblOps, iq);
		}
		else {
			cone = new DefaultPolyhedralCone<Double, double[]>(dblOps, eq, iq);
		}
		final AdapterCallback<Double, double[]> callback = new AdapterCallback<Double, double[]>(dblOps.getArrayOperations());
		polco.call(cone, callback, dblOps);
		return callback.yield();
	}
	/**
	 * Computes and returns the extreme rays of a polyhedral cone
	 * <pre>
	 * P = { x | eq*x = 0 , iq*x &ge; 0 } = { x | x = R*c , c &ge; 0 }.
	 * </pre>
	 * The equality matrix <tt>eq</tt> may be {@code null}. The returned matrix
	 * <tt>R<sup>T</sup></tt> is the transformed matrix <tt>R</tt>, that is, 
	 * every row of the returned matrix corresponds to an extreme ray. Hence, 
	 * the second dimension <tt>d</tt> of {@code eq}, {@code iq} and {@code R} 
	 * must be equal (the number of columns).
	 * <p>
	 * It is recommended (but not required) to set integer arithmetic options:
	 * <pre>
	 *   Options opts = new Options();
	 *   opts.setArithmetic(Arithmetic.bigint, Arithmetic.bigint, Arithmetic.bigint);
	 *   PolcoAdapter polco = new PolcoAdapter(opts).
	 *   polco.getDoubleRays(...)
	 * </pre>
	 * 
	 * @param eq	the equality matrix, or {@code null} if no equalities are 
	 * 				present 
	 * @param iq	the inequality matrix, {@code null} is not permitted
	 * @return	the transpose <tt>R<sup>T</sup></tt> of the extreme ray matrix
	 * 			<tt>R</tt> containing one extreme ray per matrix row
	 */
	public double[][] getDoubleRays(int[][] eq, int[][] iq) {
		final BigInteger[][] beq = eq == null ? null : toBigInteger(eq);
		final BigInteger[][] biq = toBigInteger(iq);
		return getDoubleRays(beq, biq);
	}
	/**
	 * Computes and returns the extreme rays of a polyhedral cone
	 * <pre>
	 * P = { x | eq*x = 0 , iq*x &ge; 0 } = { x | x = R*c , c &ge; 0 }.
	 * </pre>
	 * The equality matrix <tt>eq</tt> may be {@code null}. The returned matrix
	 * <tt>R<sup>T</sup></tt> is the transformed matrix <tt>R</tt>, that is, 
	 * every row of the returned matrix corresponds to an extreme ray. Hence, 
	 * the second dimension <tt>d</tt> of {@code eq}, {@code iq} and {@code R} 
	 * must be equal (the number of columns).
	 * <p>
	 * It is recommended (but not required) to set integer arithmetic options:
	 * <pre>
	 *   Options opts = new Options();
	 *   opts.setArithmetic(Arithmetic.bigint, Arithmetic.bigint, Arithmetic.bigint);
	 *   PolcoAdapter polco = new PolcoAdapter(opts).
	 *   polco.getDoubleRays(...)
	 * </pre>
	 * 
	 * @param eq	the equality matrix, or {@code null} if no equalities are 
	 * 				present 
	 * @param iq	the inequality matrix, {@code null} is not permitted
	 * @return	the transpose <tt>R<sup>T</sup></tt> of the extreme ray matrix
	 * 			<tt>R</tt> containing one extreme ray per matrix row
	 */
	public double[][] getDoubleRays(long[][] eq, long[][] iq) {
		final BigInteger[][] beq = eq == null ? null : toBigInteger(eq);
		final BigInteger[][] biq = toBigInteger(iq);
		return getDoubleRays(beq, biq);
	}
	
	/**
	 * Computes and returns the extreme rays of a polyhedral cone
	 * <pre>
	 * P = { x | eq*x = 0 , iq*x &ge; 0 } = { x | x = R*c , c &ge; 0 }.
	 * </pre>
	 * The equality matrix <tt>eq</tt> may be {@code null}. The returned matrix
	 * <tt>R<sup>T</sup></tt> is the transformed matrix <tt>R</tt>, that is, 
	 * every row of the returned matrix corresponds to an extreme ray. Hence, 
	 * the second dimension <tt>d</tt> of {@code eq}, {@code iq} and {@code R} 
	 * must be equal (the number of columns).
	 * <p>
	 * It is recommended (but not required) to set integer arithmetic options:
	 * <pre>
	 *   Options opts = new Options();
	 *   opts.setArithmetic(Arithmetic.bigint, Arithmetic.bigint, Arithmetic.bigint);
	 *   PolcoAdapter polco = new PolcoAdapter(opts).
	 *   polco.getDoubleRays(...)
	 * </pre>
	 * 
	 * @param eq	the equality matrix, or {@code null} if no equalities are 
	 * 				present 
	 * @param iq	the inequality matrix, {@code null} is not permitted
	 * @return	the transpose <tt>R<sup>T</sup></tt> of the extreme ray matrix
	 * 			<tt>R</tt> containing one extreme ray per matrix row
	 */
	public double[][] getDoubleRays(BigInteger[][] eq, BigInteger[][] iq) {
		final LinAlgOperations<BigInteger, BigInteger[]> intOps = BigIntegerOperators.EXACT_DIVISION_INSTANCE.getLinAlgOperations();
		final LinAlgOperations<Double, double[]> dblOps = DoubleOperators.DEFAULT.getLinAlgOperations();
		final PolyhedralCone<BigInteger, BigInteger[]> cone;
		if (eq == null || eq.length == 0) {
			cone = new DefaultInequalityCone<BigInteger, BigInteger[]>(intOps, iq);
		}
		else {
			cone = new DefaultPolyhedralCone<BigInteger, BigInteger[]>(intOps, eq, iq);
		}
		final AdapterCallback<Double, double[]> callback = new AdapterCallback<Double, double[]>(dblOps.getArrayOperations());
		polco.call(cone, callback, dblOps);
		return callback.yield();
	}

	/**
	 * Computes and returns the extreme rays of a polyhedral cone
	 * <pre>
	 * P = { x | eq*x = 0 , iq*x &ge; 0 } = { x | x = R*c , c &ge; 0 }.
	 * </pre>
	 * The equality matrix <tt>eq</tt> may be {@code null}. The returned matrix
	 * <tt>R<sup>T</sup></tt> is the transformed matrix <tt>R</tt>, that is, 
	 * every row of the returned matrix corresponds to an extreme ray. Hence, 
	 * the second dimension <tt>d</tt> of {@code eq}, {@code iq} and {@code R} 
	 * must be equal (the number of columns).
	 * 
	 * @param eq	the equality matrix, or {@code null} if no equalities are 
	 * 				present 
	 * @param iq	the inequality matrix, {@code null} is not permitted
	 * @return	the transpose <tt>R<sup>T</sup></tt> of the extreme ray matrix
	 * 			<tt>R</tt> containing one extreme ray per matrix row
	 */
	public double[][] getDoubleRays(BigFraction[][] eq, BigFraction[][] iq) {
		final LinAlgOperations<BigFraction, BigFraction[]> fraOps = BigFractionOperators.INSTANCE.getLinAlgOperations();
		final LinAlgOperations<Double, double[]> dblOps = DoubleOperators.DEFAULT.getLinAlgOperations();
		final PolyhedralCone<BigFraction, BigFraction[]> cone;
		if (eq == null || eq.length == 0) {
			cone = new DefaultInequalityCone<BigFraction, BigFraction[]>(fraOps, iq);
		}
		else {
			cone = new DefaultPolyhedralCone<BigFraction, BigFraction[]>(fraOps, eq, iq);
		}
		final AdapterCallback<Double, double[]> callback = new AdapterCallback<Double, double[]>(dblOps.getArrayOperations());
		polco.call(cone, callback, dblOps);
		return callback.yield();
	}

	/**
	 * Computes and returns the extreme rays of a polyhedral cone
	 * <pre>
	 * P = { x | eq*x = 0 , iq*x &ge; 0 } = { x | x = R*c , c &ge; 0 }.
	 * </pre>
	 * The equality matrix <tt>eq</tt> may be {@code null}. The returned matrix
	 * <tt>R<sup>T</sup></tt> is the transformed matrix <tt>R</tt>, that is, 
	 * every row of the returned matrix corresponds to an extreme ray. Hence, 
	 * the second dimension <tt>d</tt> of {@code eq}, {@code iq} and {@code R} 
	 * must be equal (the number of columns).
	 * <p>
	 * It is recommended (but not required) to set integer core and 
	 * post-processing arithmetic options:
	 * <pre>
	 *   Options opts = new Options();
	 *   opts.setArithmetic(Arithmetic.bigint, Arithmetic.bigint);
	 *   PolcoAdapter polco = new PolcoAdapter(opts).
	 *   polco.getBigIntegerRays(...)
	 * </pre>
	 * 
	 * @param eq	the equality matrix, or {@code null} if no equalities are 
	 * 				present 
	 * @param iq	the inequality matrix, {@code null} is not permitted
	 * @return	the transpose <tt>R<sup>T</sup></tt> of the extreme ray matrix
	 * 			<tt>R</tt> containing one extreme ray per matrix row
	 */
	public BigInteger[][] getBigIntegerRays(double[][] eq, double[][] iq) {
		final LinAlgOperations<Double, double[]> dblOps = DoubleOperators.DEFAULT.getLinAlgOperations();
		final LinAlgOperations<BigInteger, BigInteger[]> intOps = BigIntegerOperators.EXACT_DIVISION_INSTANCE.getLinAlgOperations();
		final PolyhedralCone<Double, double[]> cone;
		if (eq == null || eq.length == 0) {
			cone = new DefaultInequalityCone<Double, double[]>(dblOps, iq);
		}
		else {
			cone = new DefaultPolyhedralCone<Double, double[]>(dblOps, eq, iq);
		}
		final AdapterCallback<BigInteger, BigInteger[]> callback = new AdapterCallback<BigInteger, BigInteger[]>(intOps.getArrayOperations());
		polco.call(cone, callback, intOps);
		return callback.yield();
	}

	/**
	 * Computes and returns the extreme rays of a polyhedral cone
	 * <pre>
	 * P = { x | eq*x = 0 , iq*x &ge; 0 } = { x | x = R*c , c &ge; 0 }.
	 * </pre>
	 * The equality matrix <tt>eq</tt> may be {@code null}. The returned matrix
	 * <tt>R<sup>T</sup></tt> is the transformed matrix <tt>R</tt>, that is, 
	 * every row of the returned matrix corresponds to an extreme ray. Hence, 
	 * the second dimension <tt>d</tt> of {@code eq}, {@code iq} and {@code R} 
	 * must be equal (the number of columns).
	 * <p>
	 * It is recommended (but not required) to set integer arithmetic options:
	 * <pre>
	 *   Options opts = new Options();
	 *   opts.setArithmetic(Arithmetic.bigint, Arithmetic.bigint, Arithmetic.bigint);
	 *   PolcoAdapter polco = new PolcoAdapter(opts).
	 *   polco.getBigIntegerRays(...)
	 * </pre>
	 * 
	 * @param eq	the equality matrix, or {@code null} if no equalities are 
	 * 				present 
	 * @param iq	the inequality matrix, {@code null} is not permitted
	 * @return	the transpose <tt>R<sup>T</sup></tt> of the extreme ray matrix
	 * 			<tt>R</tt> containing one extreme ray per matrix row
	 */
	public BigInteger[][] getBigIntegerRays(int[][] eq, int[][] iq) {
		final BigInteger[][] beq = eq == null ? null : toBigInteger(eq);
		final BigInteger[][] biq = toBigInteger(iq);
		return getBigIntegerRays(beq, biq);
	}

	/**
	 * Computes and returns the extreme rays of a polyhedral cone
	 * <pre>
	 * P = { x | eq*x = 0 , iq*x &ge; 0 } = { x | x = R*c , c &ge; 0 }.
	 * </pre>
	 * The equality matrix <tt>eq</tt> may be {@code null}. The returned matrix
	 * <tt>R<sup>T</sup></tt> is the transformed matrix <tt>R</tt>, that is, 
	 * every row of the returned matrix corresponds to an extreme ray. Hence, 
	 * the second dimension <tt>d</tt> of {@code eq}, {@code iq} and {@code R} 
	 * must be equal (the number of columns).
	 * <p>
	 * It is recommended (but not required) to set integer arithmetic options:
	 * <pre>
	 *   Options opts = new Options();
	 *   opts.setArithmetic(Arithmetic.bigint, Arithmetic.bigint, Arithmetic.bigint);
	 *   PolcoAdapter polco = new PolcoAdapter(opts).
	 *   polco.getBigIntegerRays(...)
	 * </pre>
	 * 
	 * @param eq	the equality matrix, or {@code null} if no equalities are 
	 * 				present 
	 * @param iq	the inequality matrix, {@code null} is not permitted
	 * @return	the transpose <tt>R<sup>T</sup></tt> of the extreme ray matrix
	 * 			<tt>R</tt> containing one extreme ray per matrix row
	 */
	public BigInteger[][] getBigIntegerRays(long[][] eq, long[][] iq) {
		final BigInteger[][] beq = eq == null ? null : toBigInteger(eq);
		final BigInteger[][] biq = toBigInteger(iq);
		return getBigIntegerRays(beq, biq);
	}

	/**
	 * Computes and returns the extreme rays of a polyhedral cone
	 * <pre>
	 * P = { x | eq*x = 0 , iq*x &ge; 0 } = { x | x = R*c , c &ge; 0 }.
	 * </pre>
	 * The equality matrix <tt>eq</tt> may be {@code null}. The returned matrix
	 * <tt>R<sup>T</sup></tt> is the transformed matrix <tt>R</tt>, that is, 
	 * every row of the returned matrix corresponds to an extreme ray. Hence, 
	 * the second dimension <tt>d</tt> of {@code eq}, {@code iq} and {@code R} 
	 * must be equal (the number of columns).
	 * <p>
	 * It is recommended (but not required) to set integer arithmetic options:
	 * <pre>
	 *   Options opts = new Options();
	 *   opts.setArithmetic(Arithmetic.bigint, Arithmetic.bigint, Arithmetic.bigint);
	 *   PolcoAdapter polco = new PolcoAdapter(opts).
	 *   polco.getBigIntegerRays(...)
	 * </pre>
	 * 
	 * @param eq	the equality matrix, or {@code null} if no equalities are 
	 * 				present 
	 * @param iq	the inequality matrix, {@code null} is not permitted
	 * @return	the transpose <tt>R<sup>T</sup></tt> of the extreme ray matrix
	 * 			<tt>R</tt> containing one extreme ray per matrix row
	 */
	public BigInteger[][] getBigIntegerRays(BigInteger[][] eq, BigInteger[][] iq) {
		final LinAlgOperations<BigInteger, BigInteger[]> intOps = BigIntegerOperators.EXACT_DIVISION_INSTANCE.getLinAlgOperations();
		final PolyhedralCone<BigInteger, BigInteger[]> cone;
		if (eq == null || eq.length == 0) {
			cone = new DefaultInequalityCone<BigInteger, BigInteger[]>(intOps, iq);
		}
		else {
			cone = new DefaultPolyhedralCone<BigInteger, BigInteger[]>(intOps, eq, iq);
		}
		final AdapterCallback<BigInteger, BigInteger[]> callback = new AdapterCallback<BigInteger, BigInteger[]>(intOps.getArrayOperations());
		polco.call(cone, callback, intOps);
		return callback.yield();		
	}

	/**
	 * Computes and returns the extreme rays of a polyhedral cone
	 * <pre>
	 * P = { x | eq*x = 0 , iq*x &ge; 0 } = { x | x = R*c , c &ge; 0 }.
	 * </pre>
	 * The equality matrix <tt>eq</tt> may be {@code null}. The returned matrix
	 * <tt>R<sup>T</sup></tt> is the transformed matrix <tt>R</tt>, that is, 
	 * every row of the returned matrix corresponds to an extreme ray. Hence, 
	 * the second dimension <tt>d</tt> of {@code eq}, {@code iq} and {@code R} 
	 * must be equal (the number of columns).
	 * <p>
	 * It is recommended (but not required) to set integer core and 
	 * post-processing arithmetic options:
	 * <pre>
	 *   Options opts = new Options();
	 *   opts.setArithmetic(Arithmetic.bigint, Arithmetic.bigint);
	 *   PolcoAdapter polco = new PolcoAdapter(opts).
	 *   polco.getBigIntegerRays(...)
	 * </pre>
	 * 
	 * @param eq	the equality matrix, or {@code null} if no equalities are 
	 * 				present 
	 * @param iq	the inequality matrix, {@code null} is not permitted
	 * @return	the transpose <tt>R<sup>T</sup></tt> of the extreme ray matrix
	 * 			<tt>R</tt> containing one extreme ray per matrix row
	 */
	public BigInteger[][] getBigIntegerRays(BigFraction[][] eq, BigFraction[][] iq) {
		final LinAlgOperations<BigFraction, BigFraction[]> fraOps = BigFractionOperators.INSTANCE.getLinAlgOperations();
		final LinAlgOperations<BigInteger, BigInteger[]> intOps = BigIntegerOperators.EXACT_DIVISION_INSTANCE.getLinAlgOperations();
		final PolyhedralCone<BigFraction, BigFraction[]> cone;
		if (eq == null || eq.length == 0) {
			cone = new DefaultInequalityCone<BigFraction, BigFraction[]>(fraOps, iq);
		}
		else {
			cone = new DefaultPolyhedralCone<BigFraction, BigFraction[]>(fraOps, eq, iq);
		}
		final AdapterCallback<BigInteger, BigInteger[]> callback = new AdapterCallback<BigInteger, BigInteger[]>(intOps.getArrayOperations());
		polco.call(cone, callback, intOps);
		return callback.yield();		
	}

	/**
	 * Computes and returns the extreme rays of a polyhedral cone
	 * <pre>
	 * P = { x | eq*x = 0 , iq*x &ge; 0 } = { x | x = R*c , c &ge; 0 }.
	 * </pre>
	 * The equality matrix <tt>eq</tt> may be {@code null}. The returned matrix
	 * <tt>R<sup>T</sup></tt> is the transformed matrix <tt>R</tt>, that is, 
	 * every row of the returned matrix corresponds to an extreme ray. Hence, 
	 * the second dimension <tt>d</tt> of {@code eq}, {@code iq} and {@code R} 
	 * must be equal (the number of columns).
	 * 
	 * @param eq	the equality matrix, or {@code null} if no equalities are 
	 * 				present 
	 * @param iq	the inequality matrix, {@code null} is not permitted
	 * @return	the transpose <tt>R<sup>T</sup></tt> of the extreme ray matrix
	 * 			<tt>R</tt> containing one extreme ray per matrix row
	 */
	public BigFraction[][] getBigFractionRays(double[][] eq, double[][] iq) {
		final LinAlgOperations<Double, double[]> dblOps = DoubleOperators.DEFAULT.getLinAlgOperations();
		final LinAlgOperations<BigFraction, BigFraction[]> fraOps = BigFractionOperators.INSTANCE.getLinAlgOperations();
		final PolyhedralCone<Double, double[]> cone;
		if (eq == null || eq.length == 0) {
			cone = new DefaultInequalityCone<Double, double[]>(dblOps, iq);
		}
		else {
			cone = new DefaultPolyhedralCone<Double, double[]>(dblOps, eq, iq);
		}
		final AdapterCallback<BigFraction, BigFraction[]> callback = new AdapterCallback<BigFraction, BigFraction[]>(fraOps.getArrayOperations());
		polco.call(cone, callback, fraOps);
		return callback.yield();
	}
	
	/**
	 * Computes and returns the extreme rays of a polyhedral cone
	 * <pre>
	 * P = { x | eq*x = 0 , iq*x &ge; 0 } = { x | x = R*c , c &ge; 0 }.
	 * </pre>
	 * The equality matrix <tt>eq</tt> may be {@code null}. The returned matrix
	 * <tt>R<sup>T</sup></tt> is the transformed matrix <tt>R</tt>, that is, 
	 * every row of the returned matrix corresponds to an extreme ray. Hence, 
	 * the second dimension <tt>d</tt> of {@code eq}, {@code iq} and {@code R} 
	 * must be equal (the number of columns).
	 * <p>
	 * It is recommended (but not required) to set integer arithmetic options:
	 * <pre>
	 *   Options opts = new Options();
	 *   opts.setArithmetic(Arithmetic.bigint, Arithmetic.bigint, Arithmetic.bigint);
	 *   PolcoAdapter polco = new PolcoAdapter(opts).
	 *   polco.getBigFractionRays(...)
	 * </pre>
	 * 
	 * @param eq	the equality matrix, or {@code null} if no equalities are 
	 * 				present 
	 * @param iq	the inequality matrix, {@code null} is not permitted
	 * @return	the transpose <tt>R<sup>T</sup></tt> of the extreme ray matrix
	 * 			<tt>R</tt> containing one extreme ray per matrix row
	 */
	public BigFraction[][] getBigFractionRays(int[][] eq, int[][] iq) {
		final BigInteger[][] beq = eq == null ? null : toBigInteger(eq);
		final BigInteger[][] biq = toBigInteger(iq);
		return getBigFractionRays(beq, biq);
	}
	/**
	 * Computes and returns the extreme rays of a polyhedral cone
	 * <pre>
	 * P = { x | eq*x = 0 , iq*x &ge; 0 } = { x | x = R*c , c &ge; 0 }.
	 * </pre>
	 * The equality matrix <tt>eq</tt> may be {@code null}. The returned matrix
	 * <tt>R<sup>T</sup></tt> is the transformed matrix <tt>R</tt>, that is, 
	 * every row of the returned matrix corresponds to an extreme ray. Hence, 
	 * the second dimension <tt>d</tt> of {@code eq}, {@code iq} and {@code R} 
	 * must be equal (the number of columns).
	 * <p>
	 * It is recommended (but not required) to set integer arithmetic options:
	 * <pre>
	 *   Options opts = new Options();
	 *   opts.setArithmetic(Arithmetic.bigint, Arithmetic.bigint, Arithmetic.bigint);
	 *   PolcoAdapter polco = new PolcoAdapter(opts).
	 *   polco.getBigFractionRays(...)
	 * </pre>
	 * 
	 * @param eq	the equality matrix, or {@code null} if no equalities are 
	 * 				present 
	 * @param iq	the inequality matrix, {@code null} is not permitted
	 * @return	the transpose <tt>R<sup>T</sup></tt> of the extreme ray matrix
	 * 			<tt>R</tt> containing one extreme ray per matrix row
	 */
	public BigFraction[][] getBigFractionRays(long[][] eq, long[][] iq) {
		final BigInteger[][] beq = eq == null ? null : toBigInteger(eq);
		final BigInteger[][] biq = toBigInteger(iq);
		return getBigFractionRays(beq, biq);
	}

	/**
	 * Computes and returns the extreme rays of a polyhedral cone
	 * <pre>
	 * P = { x | eq*x = 0 , iq*x &ge; 0 } = { x | x = R*c , c &ge; 0 }.
	 * </pre>
	 * The equality matrix <tt>eq</tt> may be {@code null}. The returned matrix
	 * <tt>R<sup>T</sup></tt> is the transformed matrix <tt>R</tt>, that is, 
	 * every row of the returned matrix corresponds to an extreme ray. Hence, 
	 * the second dimension <tt>d</tt> of {@code eq}, {@code iq} and {@code R} 
	 * must be equal (the number of columns).
	 * <p>
	 * It is recommended (but not required) to set integer arithmetic options:
	 * <pre>
	 *   Options opts = new Options();
	 *   opts.setArithmetic(Arithmetic.bigint, Arithmetic.bigint, Arithmetic.bigint);
	 *   PolcoAdapter polco = new PolcoAdapter(opts).
	 *   polco.getBigFractionRays(...)
	 * </pre>
	 * 
	 * @param eq	the equality matrix, or {@code null} if no equalities are 
	 * 				present 
	 * @param iq	the inequality matrix, {@code null} is not permitted
	 * @return	the transpose <tt>R<sup>T</sup></tt> of the extreme ray matrix
	 * 			<tt>R</tt> containing one extreme ray per matrix row
	 */
	public BigFraction[][] getBigFractionRays(BigInteger[][] eq, BigInteger[][] iq) {
		final LinAlgOperations<BigInteger, BigInteger[]> intOps = BigIntegerOperators.EXACT_DIVISION_INSTANCE.getLinAlgOperations();
		final LinAlgOperations<BigFraction, BigFraction[]> fraOps = BigFractionOperators.INSTANCE.getLinAlgOperations();
		final PolyhedralCone<BigInteger, BigInteger[]> cone;
		if (eq == null || eq.length == 0) {
			cone = new DefaultInequalityCone<BigInteger, BigInteger[]>(intOps, iq);
		}
		else {
			cone = new DefaultPolyhedralCone<BigInteger, BigInteger[]>(intOps, eq, iq);
		}
		final AdapterCallback<BigFraction, BigFraction[]> callback = new AdapterCallback<BigFraction, BigFraction[]>(fraOps.getArrayOperations());
		polco.call(cone, callback, fraOps);
		return callback.yield();		
	}
	
	/**
	 * Computes and returns the extreme rays of a polyhedral cone
	 * <pre>
	 * P = { x | eq*x = 0 , iq*x &ge; 0 } = { x | x = R*c , c &ge; 0 }.
	 * </pre>
	 * The equality matrix <tt>eq</tt> may be {@code null}. The returned matrix
	 * <tt>R<sup>T</sup></tt> is the transformed matrix <tt>R</tt>, that is, 
	 * every row of the returned matrix corresponds to an extreme ray. Hence, 
	 * the second dimension <tt>d</tt> of {@code eq}, {@code iq} and {@code R} 
	 * must be equal (the number of columns).
	 * 
	 * @param eq	the equality matrix, or {@code null} if no equalities are 
	 * 				present 
	 * @param iq	the inequality matrix, {@code null} is not permitted
	 * @return	the transpose <tt>R<sup>T</sup></tt> of the extreme ray matrix
	 * 			<tt>R</tt> containing one extreme ray per matrix row
	 */
	public BigFraction[][] getBigFractionRays(BigFraction[][] eq, BigFraction[][] iq) {
		final LinAlgOperations<BigFraction, BigFraction[]> fraOps = BigFractionOperators.INSTANCE.getLinAlgOperations();
		final PolyhedralCone<BigFraction, BigFraction[]> cone;
		if (eq == null || eq.length == 0) {
			cone = new DefaultInequalityCone<BigFraction, BigFraction[]>(fraOps, iq);
		}
		else {
			cone = new DefaultPolyhedralCone<BigFraction, BigFraction[]>(fraOps, eq, iq);
		}
		final AdapterCallback<BigFraction, BigFraction[]> callback = new AdapterCallback<BigFraction, BigFraction[]>(fraOps.getArrayOperations());
		polco.call(cone, callback, fraOps);
		return callback.yield();		
	}

	// static helper methods
	
	/**
	 * Converts an int matrix into a BigInteger matrix
	 */
	private static final BigInteger[][] toBigInteger(int[][] matrix) {
		final BigInteger[][] result = new BigInteger[matrix.length][];
		for (int i = 0; i < matrix.length; i++) {
			result[i] = new BigInteger[matrix[i].length];
			for (int j = 0; j < matrix[i].length; j++) {
				result[i][j] = BigInteger.valueOf(matrix[i][j]);
			}
		}
		return result;
	}
	/**
	 * Converts a long matrix into a BigInteger matrix
	 */
	private static final BigInteger[][] toBigInteger(long[][] matrix) {
		final BigInteger[][] result = new BigInteger[matrix.length][];
		for (int i = 0; i < matrix.length; i++) {
			result[i] = new BigInteger[matrix[i].length];
			for (int j = 0; j < matrix[i].length; j++) {
				result[i][j] = BigInteger.valueOf(matrix[i][j]);
			}
		}
		return result;
	}
	
}
