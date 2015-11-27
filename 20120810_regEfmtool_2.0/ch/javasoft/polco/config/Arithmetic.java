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

import java.math.BigInteger;

import org.dom4j.Element;

import ch.javasoft.factory.ConfigException;
import ch.javasoft.factory.FactoryNotFoundException;
import ch.javasoft.factory.IllegalFactoryException;
import ch.javasoft.math.BigFraction;
import ch.javasoft.math.array.ArrayOperations;
import ch.javasoft.math.array.NumberArrayOperations;
import ch.javasoft.math.array.NumberOperators;
import ch.javasoft.math.array.impl.DefaultArrayOperations;
import ch.javasoft.math.array.impl.DefaultNumberArrayOperations;
import ch.javasoft.math.array.impl.DoubleArrayOperations;
import ch.javasoft.math.linalg.DefaultLinAlgOperations;
import ch.javasoft.math.linalg.GaussPivotingFactory;
import ch.javasoft.math.linalg.LinAlgOperations;
import ch.javasoft.math.linalg.impl.BigFractionGaussPivoting;
import ch.javasoft.math.linalg.impl.BigIntegerGaussPivoting;
import ch.javasoft.math.linalg.impl.DoubleGaussPivoting;
import ch.javasoft.math.operator.impl.BigFractionOperators;
import ch.javasoft.math.operator.impl.BigIntegerOperators;
import ch.javasoft.math.operator.impl.DoubleOperators;
import ch.javasoft.math.varint.VarInt;
import ch.javasoft.math.varint.VarIntNumber;
import ch.javasoft.math.varint.array.VarIntGaussPivoting;
import ch.javasoft.math.varint.array.VarIntOperators;
import ch.javasoft.polco.Numeric;
import ch.javasoft.util.numeric.Zero;
import ch.javasoft.xml.config.XmlUtil;
import ch.javasoft.xml.factory.XmlConfiguredFactory;

/**
 * The <code>Arithmetic</code> class is actually an enum defining constants for
 * arithmetic operations. It has not been implemented as enum to make use of 
 * generic types. 
 * <p>
 * The constants are available as static member types, and all constant values 
 * are collected in the {@link #VALUES} array.
 * 
 * @type N	the number type of a single number
 * @type A	the number type of an array of numbers
 */
abstract public class Arithmetic<N extends Number, A> {
	
	/**
	 * Constant for use of double precision arithmetic with 64bit double values.
	 */
	public static final Arithmetic<Double, double[]> DOUBLE = new Arithmetic<Double, double[]>("double") {
		@Override
		public Class<Double> getNumberClass() {
			return Double.class;
		}
		@Override
		public Class<double[]> getNumberArrayClass() {
			return double[].class;
		}
		@Override
		public LinAlgOperations<Double, double[]> getLinAlgOperations(Zero zero) {
			final GaussPivotingFactory<Double, double[]> pivoting = DoubleGaussPivoting.ABS_G;
			return new DefaultLinAlgOperations<Double, double[]>(getNumberArrayOperations(zero), pivoting);
		}
		@Override
		public ArrayOperations<double[]> getArrayOperations() {
			return DoubleArrayOperations.INSTANCE;
		}
		@Override
		public NumberOperators<Double, double[]> getNumberOperators(Zero zero) {			
			return zero.mZeroPos == 0 ? DoubleOperators.DEFAULT : new DoubleOperators(zero);
		}
		@Override
		public Zero getDefaultZero() {
			return new Zero();
		}
	};
	/**
	 * Constant for use of fractional arithmetic using two big integers of 
	 * unbounded size (see {@link BigFraction}). 
	 */
	public static final Arithmetic<BigFraction, BigFraction[]> FRACTIONAL = new Arithmetic<BigFraction, BigFraction[]>("fractional") {
		@Override
		public Class<BigFraction[]> getNumberArrayClass() {
			return BigFraction[].class;
		}
		@Override
		public Class<BigFraction> getNumberClass() {
			return BigFraction.class;
		}
		@Override
		public LinAlgOperations<BigFraction, BigFraction[]> getLinAlgOperations(Zero zero) {
			if (zero.mZeroPos != 0) throw new IllegalArgumentException("zero must be true zero: " + zero);
			final GaussPivotingFactory<BigFraction, BigFraction[]> pivoting = BigFractionGaussPivoting.LEN_PRODUCT_L;
			return new DefaultLinAlgOperations<BigFraction, BigFraction[]>(getNumberArrayOperations(zero), pivoting);
		}
		@Override
		public ArrayOperations<BigFraction[]> getArrayOperations() {
			return new DefaultArrayOperations<BigFraction>(BigFraction[].class); 
		}
		@Override
		public NumberOperators<BigFraction, BigFraction[]> getNumberOperators(Zero zero) {			
			if (zero.mZeroPos != 0) throw new IllegalArgumentException("zero must be true zero: " + zero);
			return BigFractionOperators.INSTANCE;
		}
		@Override
		public Zero getDefaultZero() {
			return new Zero(0d);
		}
	};
	/**
	 * Constant for use of big integer arithmetic of unbounded size 
	 * (see {@link BigInteger}). Division operations are not supported.
	 */
	public static final Arithmetic<BigInteger, BigInteger[]> BIGINT = new Arithmetic<BigInteger, BigInteger[]>("bigint") {
		@Override
		public Class<BigInteger[]> getNumberArrayClass() {
			return BigInteger[].class;
		}
		@Override
		public Class<BigInteger> getNumberClass() {
			return BigInteger.class;
		}
		@Override
		public LinAlgOperations<BigInteger, BigInteger[]> getLinAlgOperations(Zero zero) {
			if (zero.mZeroPos != 0) throw new IllegalArgumentException("zero must be true zero: " + zero);
			final GaussPivotingFactory<BigInteger, BigInteger[]> pivoting = BigIntegerGaussPivoting.LEN_L;
			return new DefaultLinAlgOperations<BigInteger, BigInteger[]>(getNumberArrayOperations(zero), pivoting);
		}
		@Override
		public ArrayOperations<BigInteger[]> getArrayOperations() {
			return new DefaultArrayOperations<BigInteger>(BigInteger[].class); 
		}
		@Override
		public NumberOperators<BigInteger, BigInteger[]> getNumberOperators(Zero zero) {			
			if (zero.mZeroPos != 0) throw new IllegalArgumentException("zero must be true zero: " + zero);
			return BigIntegerOperators.EXACT_DIVISION_INSTANCE;
		}
		@Override
		public Zero getDefaultZero() {
			return new Zero(0d);
		}
	};
	/**
	 * Constant for use of raw big integer arithmetic of unbounded size 
	 * (see {@link BigInteger}). The big integer values are encoded as raw byte
	 * values for memory optimization. Division operations are not supported.
	 */
	public static final Arithmetic<BigInteger, BigInteger[]> RAWINT = new Arithmetic<BigInteger, BigInteger[]>("rawint") {
		@Override
		public Class<BigInteger[]> getNumberArrayClass() {
			return BigInteger[].class;
		}
		@Override
		public Class<BigInteger> getNumberClass() {
			return BigInteger.class;
		}
		@Override
		public LinAlgOperations<BigInteger, BigInteger[]> getLinAlgOperations(Zero zero) {
			if (zero.mZeroPos != 0) throw new IllegalArgumentException("zero must be true zero: " + zero);
			final GaussPivotingFactory<BigInteger, BigInteger[]> pivoting = BigIntegerGaussPivoting.LEN_L;
			return new DefaultLinAlgOperations<BigInteger, BigInteger[]>(getNumberArrayOperations(zero), pivoting);
		}
		@Override
		public ArrayOperations<BigInteger[]> getArrayOperations() {
			return new DefaultArrayOperations<BigInteger>(BigInteger[].class); 
		}
		@Override
		public NumberOperators<BigInteger, BigInteger[]> getNumberOperators(Zero zero) {			
			if (zero.mZeroPos != 0) throw new IllegalArgumentException("zero must be true zero: " + zero);
			return BigIntegerOperators.EXACT_DIVISION_INSTANCE;
		}
		@Override
		public Zero getDefaultZero() {
			return new Zero(0d);
		}
	};
	/**
	 * Constant for use of variable size big integer arithmetic of unbounded 
	 * maximum size (see {@link VarInt}). The variable integer values are stored
	 * as raw byte values for memory optimization. Division operations are not 
	 * supported.
	 */
	public static final Arithmetic<VarIntNumber, VarIntNumber[]> VARINT = new Arithmetic<VarIntNumber, VarIntNumber[]>("varint") {
		@Override
		public Class<VarIntNumber[]> getNumberArrayClass() {
			return VarIntNumber[].class;
		}
		@Override
		public Class<VarIntNumber> getNumberClass() {
			return VarIntNumber.class;
		}
		@Override
		public LinAlgOperations<VarIntNumber, VarIntNumber[]> getLinAlgOperations(Zero zero) {
			if (zero.mZeroPos != 0) throw new IllegalArgumentException("zero must be true zero: " + zero);
			final GaussPivotingFactory<VarIntNumber, VarIntNumber[]> pivoting = VarIntGaussPivoting.LEN_L;
			return new DefaultLinAlgOperations<VarIntNumber, VarIntNumber[]>(getNumberArrayOperations(zero), pivoting);
		}
		@Override
		public ArrayOperations<VarIntNumber[]> getArrayOperations() {
			return new DefaultArrayOperations<VarIntNumber>(VarIntNumber[].class); 
		}
		@Override
		public NumberOperators<VarIntNumber, VarIntNumber[]> getNumberOperators(Zero zero) {			
			if (zero.mZeroPos != 0) throw new IllegalArgumentException("zero must be true zero: " + zero);
			return VarIntOperators.EXACT_DIVISION_INSTANCE;
		}
		@Override
		public Zero getDefaultZero() {
			return new Zero(0d);
		}
	};
	/**
	 * Constant for use of variable size big integer arithmetic of unbounded 
	 * maximum size (see {@link VarInt}). The variable integer values are stored
	 * as gzipped byte values for memory optimization. Division operations are 
	 * not supported.
	 */
	public static final Arithmetic<VarIntNumber, VarIntNumber[]> ZIPINT = new Arithmetic<VarIntNumber, VarIntNumber[]>("zipint") {
		@Override
		public Class<VarIntNumber[]> getNumberArrayClass() {
			return VarIntNumber[].class;
		}
		@Override
		public Class<VarIntNumber> getNumberClass() {
			return VarIntNumber.class;
		}
		@Override
		public LinAlgOperations<VarIntNumber, VarIntNumber[]> getLinAlgOperations(Zero zero) {
			if (zero.mZeroPos != 0) throw new IllegalArgumentException("zero must be true zero: " + zero);
			final GaussPivotingFactory<VarIntNumber, VarIntNumber[]> pivoting = VarIntGaussPivoting.LEN_L;
			return new DefaultLinAlgOperations<VarIntNumber, VarIntNumber[]>(getNumberArrayOperations(zero), pivoting);
		}
		@Override
		public ArrayOperations<VarIntNumber[]> getArrayOperations() {
			return new DefaultArrayOperations<VarIntNumber>(VarIntNumber[].class); 
		}
		@Override
		public NumberOperators<VarIntNumber, VarIntNumber[]> getNumberOperators(Zero zero) {			
			if (zero.mZeroPos != 0) throw new IllegalArgumentException("zero must be true zero: " + zero);
			return VarIntOperators.EXACT_DIVISION_INSTANCE;
		}
		@Override
		public Zero getDefaultZero() {
			return new Zero(0d);
		}
	};

	/**
	 * Array containing all constants. Corresponds to {@code MyEnum.values()} 
	 */
	public static final Arithmetic[] VALUES = new Arithmetic[] {
		DOUBLE, FRACTIONAL, BIGINT, RAWINT, VARINT, ZIPINT
	};
	
	private final String name;
	
	private Arithmetic(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name as used in configuration files or as command line 
	 * argument. This is usually the lower case constant name, for instance 
	 * {@code "double"} for the {@link #DOUBLE} constant.
	 */
	public String name() {
		return name;
	}
	/**
	 * Returns the {@link #name() name} of this constant.
	 */
	@Override
	public String toString() {
		return name();
	}
	
	/**
	 * Returns an implementation for linear algebra arithmetic using the number
	 * type specified by this arithmetic constant. The zero object is used for
	 * rounding, and may only be non-zero for floating point arithmetic.
	 * 
	 * @param zero	the almost-zero value for rounding
	 * @throws IllegalArgumentException if zero is not the true zero value and
	 * 									the number type not a floating point
	 * 									type
	 */
	abstract public LinAlgOperations<N, A> getLinAlgOperations(Zero zero);
	/**
	 * Returns an implementation for array operations on numeric arrays of the
	 * number type specified by this arithmetic constant
	 */
	abstract public ArrayOperations<A> getArrayOperations();
	
	/**
	 * Returns the default zero instance, usually the true zero value for exact
	 * types, and a positive value close to zero for floating point types.
	 */
	abstract public Zero getDefaultZero();
	
	/**
	 * Returns an implementation for array operations on numeric arrays using 
	 * the number type specified by this arithmetic constant. The zero object is 
	 * used for rounding, and may only be non-zero for floating point 
	 * arithmetic.
	 * 
	 * @param zero	the almost-zero value for rounding
	 * @throws IllegalArgumentException if zero is not the true zero value and
	 * 									the number type not a floating point
	 * 									type
	 */
	public NumberArrayOperations<N, A> getNumberArrayOperations(Zero zero) {
		return new DefaultNumberArrayOperations<N, A>(getNumberOperators(zero), getArrayOperations());		
	}
	
	/**
	 * Returns an implementation for number operators using the number type 
	 * specified by this arithmetic constant. The zero object is used for
	 * rounding, and may only be non-zero for floating point arithmetic.
	 * 
	 * @param zero	the almost-zero value for rounding
	 * @throws IllegalArgumentException if zero is not the true zero value and
	 * 									the number type not a floating point
	 * 									type
	 */
	abstract public NumberOperators<N, A> getNumberOperators(Zero zero);
	
	/**
	 * The class used to represent a single numeric value of this kind. For 
	 * numbers with primitive type support in Java, the wrapped (object) type
	 * is returned, for instance, <code>{@link Double}.class</code> for 
	 * {@link #DOUBLE}.
	 */
	abstract public Class<N> getNumberClass();
	/**
	 * The class used to represent an array of numeric values of this kind. For 
	 * numbers with primitive type support in Java, the primitive array class
	 * is returned, for instance, <code>double[].class</code> for 
	 * {@link #DOUBLE}.
	 */
	abstract public Class<A> getNumberArrayClass();
	
	/**
	 * Uses the specified argument for a generically typed callback using this
	 * arithmetic constant as argument to define the generic types.
	 * <p>
	 * Technically, 
	 * {@link ArithmeticCallback#callback(Arithmetic) callback(this)} 
	 * is invoked with this arithmetic instance.
	 */
	public <R> R callback(ArithmeticCallback<R> callback) throws Exception {
		return callback.callback(this);
	}
	
	////////////////////////////////////////////////////////////////////////////
	// FACTORY methods
	
	/**
	 * Creates a typed {@link Numeric} instance of the specified class, using 
	 * the generic types from this arithmetic instance. The given {@code config} 
	 * is used to determine the factory for the instance to create, as well as
	 * for the factory to initialize the created instance.
	 * 
	 * @param <U>		The untyped class of the expected instance 
	 * @param <T>		The typed class of the expected instance, should be
	 * 					same class object as {@code U}
	 * @param clazz		The class argument to specify the desired return type 
	 * @param config	The config used to specify the factory to use, specified 
	 * 					in a factory attribute. It is also passed to the factory 
	 * 					to initialize the returned instance
	 * @return	the new instance, generically typed by this arithmetic instance
	 * 
	 * @throws IllegalFactoryException	if the factory was not appropriate for
	 * 									the desired return type
	 * @throws ConfigException			if the factory did not like the 
	 * 									configuration contents
	 * @throws FactoryNotFoundException	if factory class was not found
	 */
	public <U extends Numeric, T extends U> T createNumeric(Class<U> clazz, Element config) throws IllegalFactoryException, ConfigException, FactoryNotFoundException {
		final String factoryClassName = config.attributeValue(XmlAttribute.factory.getXmlName());
		if (factoryClassName == null) {
			throw new ConfigException("factory class-name attribute not found: " + XmlAttribute.factory.getXmlName(), XmlUtil.getElementPath(config, true));
		}
		final Class<?> factoryClass;
		try {
			factoryClass = Class.forName(factoryClassName);
		}
		catch (ClassNotFoundException e) {
			throw new FactoryNotFoundException(factoryClassName, e);
		}
		final Class<? extends NumericFactory> numFacClass;
		try {
			numFacClass = factoryClass.asSubclass(NumericFactory.class);
		}
		catch (ClassCastException e) {
			throw new IllegalFactoryException("not an instance of " + NumericFactory.class.getName() + ": " + factoryClass.getName());
		}
		return createNumeric(clazz, numFacClass, config);
	}
	/**
	 * Creates a typed {@link Numeric} instance of the specified class, using 
	 * the generic types from this arithmetic instance. The given {@code config} 
	 * is used to initialize the instance to create.
	 * 
	 * @param <U>		The untyped class of the expected instance 
	 * @param <T>		The typed class of the expected instance, should be
	 * 					same class object as {@code U}
	 * @param clazz		The class argument to specify the desired return type 
	 * @param factoryClass	The class to use as factory
	 * @param config	The config passed to the factory to initialize the
	 * 					returned instance
	 * @return	the new instance, generically typed by this arithmetic instance
	 * 
	 * @throws IllegalFactoryException	if the factory was not appropriate for
	 * 									the desired return type
	 * @throws ConfigException			if the factory did not like the 
	 * 									configuration contents
	 */
	public <U extends Numeric, T extends U> T createNumeric(Class<U> clazz, Class<? extends NumericFactory> factoryClass, Element config) throws IllegalFactoryException, ConfigException {
		final NumericFactory factory;
		try {
			factory = factoryClass.newInstance();
		}
		catch (IllegalAccessException e) {
			throw new IllegalFactoryException("could not instantiated factory " + factoryClass.getName() + ", e=" + e, e);
		} 
		catch (InstantiationException e) {
			throw new IllegalFactoryException("could not instantiated factory " + factoryClass.getName() + ", e=" + e, e);
		}
		return createNumeric(clazz, factory, config);
	}
	/**
	 * Creates a typed {@link Numeric} instance of the specified class, using 
	 * the generic types from this arithmetic instance. The given {@code config} 
	 * is used to initialize the instance to create.
	 * 
	 * @param <U>		The untyped class of the expected instance 
	 * @param <T>		The typed class of the expected instance, should be
	 * 					same class object as {@code U}
	 * @param clazz		The class argument to specify the desired return type 
	 * @param factory	The factory to use
	 * @param config	The config passed to the factory to initialize the
	 * 					returned instance
	 * @return	the new instance, generically typed by this arithmetic instance
	 * 
	 * @throws IllegalFactoryException	if the factory was not appropriate for
	 * 									the desired return type
	 * @throws ConfigException			if the factory did not like the 
	 * 									configuration contents
	 */
	@SuppressWarnings("unchecked")
	public <U extends Numeric, T extends U> T createNumeric(Class<U> clazz, NumericFactory factory, Element config) throws ConfigException, IllegalFactoryException {
		final XmlConfiguredFactory<? extends Numeric<N, A>> typedFactory = factory.createTypedFactory(this);
		final Numeric<N, A> numeric = typedFactory.create(config);
		try {
			return (T)clazz.cast(numeric);
		}
		catch (ClassCastException e) {
			throw new IllegalFactoryException("factory " + factory + " created not an instance of " + clazz.getName() + ": " + numeric);
		}
	}

}
