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
package ch.javasoft.polco;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.logging.Level;

import junit.framework.TestCase;
import ch.javasoft.math.BigFraction;
import ch.javasoft.polco.adapter.Options;
import ch.javasoft.polco.adapter.PolcoAdapter;
import ch.javasoft.util.logging.Loggers;

/**
 * The <code>TextTest</code> ... TODO javadoc-TextTest-type
 * 
 */
public class AdapterTest extends TestCase {

    /**
     * Tests the polyhedron example of Marco Scarno. 
     * <p>
     * The extreme rays are:
     * <pre>
     * ray[0] = [   2     5     0 ]
     * ray[1] = [ 1/3   1/3  -1/2 ]
     * ray[2] = [ 1/3   1/3   1/2 ]
     */
    public void testMarcoScarnoDblDbl() throws Exception {
    	final double[][] iq = {
    		{ -1   , +1  , 0 },
    		{  2.5 , -1  , -1 },
    		{  2.5 , -1  , +1 },
    	};
    	internalTestDbl(null, null, iq);
    }
    /**
     * Tests the polyhedron example of Marco Scarno. 
     * <p>
     * The extreme rays are:
     * <pre>
     * ray[0] = [   2     5     0 ]
     * ray[1] = [ 1/3   1/3  -1/2 ]
     * ray[2] = [ 1/3   1/3   1/2 ]
     */
    public void testMarcoScarnoDblInt() throws Exception {
    	final double[][] iq = {
    		{ -1   , +1  , 0 },
    		{  2.5 , -1  , -1 },
    		{  2.5 , -1  , +1 },
    	};
    	internalTestInt(null, null, iq);
    }
    /**
     * Tests the polyhedron example of Marco Scarno. 
     * <p>
     * The extreme rays are:
     * <pre>
     * ray[0] = [   2     5     0 ]
     * ray[1] = [ 1/3   1/3  -1/2 ]
     * ray[2] = [ 1/3   1/3   1/2 ]
     */
    public void testMarcoScarnoDblFra() throws Exception {
    	final double[][] iq = {
    		{ -1   , +1  , 0 },
    		{  2.5 , -1  , -1 },
    		{  2.5 , -1  , +1 },
    	};
    	internalTestFra(null, null, iq);
    }

    /**
     * Tests the polyhedron example of Marco Scarno. 
     * <p>
     * The extreme rays are:
     * <pre>
     * ray[0] = [   2     5     0 ]
     * ray[1] = [ 1/3   1/3  -1/2 ]
     * ray[2] = [ 1/3   1/3   1/2 ]
     */
    public void testMarcoScarnoIntDbl() throws Exception {
    	final int[][] iq = {
    		{ -1 , +1  ,  0 },
    		{  5 , -2  , -2 },
    		{  5 , -2  , +2 },
    	};
    	internalTestDbl(null, null, iq);
    }
    /**
     * Tests the polyhedron example of Marco Scarno. 
     * <p>
     * The extreme rays are:
     * <pre>
     * ray[0] = [   2     5     0 ]
     * ray[1] = [ 1/3   1/3  -1/2 ]
     * ray[2] = [ 1/3   1/3   1/2 ]
     */
    public void testMarcoScarnoIntInt() throws Exception {
    	final int[][] iq = {
    		{ -1 , +1  ,  0 },
    		{  5 , -2  , -2 },
    		{  5 , -2  , +2 },
    	};
    	internalTestInt(null, null, iq);
    }
    /**
     * Tests the polyhedron example of Marco Scarno. 
     * <p>
     * The extreme rays are:
     * <pre>
     * ray[0] = [   2     5     0 ]
     * ray[1] = [ 1/3   1/3  -1/2 ]
     * ray[2] = [ 1/3   1/3   1/2 ]
     */
    public void testMarcoScarnoIntFra() throws Exception {
    	final int[][] iq = {
    		{ -1 , +1  ,  0 },
    		{  5 , -2  , -2 },
    		{  5 , -2  , +2 },
    	};
    	internalTestFra(null, null, iq);
    }
    
    /**
     * Tests the polyhedron example of Marco Scarno. 
     * <p>
     * The extreme rays are:
     * <pre>
     * ray[0] = [   2     5     0 ]
     * ray[1] = [ 1/3   1/3  -1/2 ]
     * ray[2] = [ 1/3   1/3   1/2 ]
     */
    public void testMarcoScarnoLongDbl() throws Exception {
    	final long[][] iq = {
    		{ -1 , +1  ,  0 },
    		{  5 , -2  , -2 },
    		{  5 , -2  , +2 },
    	};
    	internalTestDbl(null, null, iq);
    }
    /**
     * Tests the polyhedron example of Marco Scarno. 
     * <p>
     * The extreme rays are:
     * <pre>
     * ray[0] = [   2     5     0 ]
     * ray[1] = [ 1/3   1/3  -1/2 ]
     * ray[2] = [ 1/3   1/3   1/2 ]
     */
    public void testMarcoScarnoLongInt() throws Exception {
    	final long[][] iq = {
    		{ -1 , +1  ,  0 },
    		{  5 , -2  , -2 },
    		{  5 , -2  , +2 },
    	};
    	internalTestInt(null, null, iq);
    }
    /**
     * Tests the polyhedron example of Marco Scarno. 
     * <p>
     * The extreme rays are:
     * <pre>
     * ray[0] = [   2     5     0 ]
     * ray[1] = [ 1/3   1/3  -1/2 ]
     * ray[2] = [ 1/3   1/3   1/2 ]
     */
    public void testMarcoScarnoLongFra() throws Exception {
    	final long[][] iq = {
    		{ -1 , +1  ,  0 },
    		{  5 , -2  , -2 },
    		{  5 , -2  , +2 },
    	};
    	internalTestFra(null, null, iq);
    }
    
    /**
     * Tests the polyhedron example of Marco Scarno. 
     * <p>
     * The extreme rays are:
     * <pre>
     * ray[0] = [   2     5     0 ]
     * ray[1] = [ 1/3   1/3  -1/2 ]
     * ray[2] = [ 1/3   1/3   1/2 ]
     */
    public void testMarcoScarnoFraDbl() throws Exception {
    	final BigFraction[][] iq = {
    		{ BigFraction.valueOf(-1)   ,  BigFraction.ONE          ,  BigFraction.ZERO        },
    		{ BigFraction.valueOf(5, 2) ,  BigFraction.valueOf(-1)  ,  BigFraction.valueOf(-1) },
    		{ BigFraction.valueOf(5, 2) ,  BigFraction.valueOf(-1)  ,  BigFraction.valueOf(+1) },
    	};
    	internalTestDbl(null, null, iq);
    }
    /**
     * Tests the polyhedron example of Marco Scarno. 
     * <p>
     * The extreme rays are:
     * <pre>
     * ray[0] = [   2     5     0 ]
     * ray[1] = [ 1/3   1/3  -1/2 ]
     * ray[2] = [ 1/3   1/3   1/2 ]
     */
    public void testMarcoScarnoFraInt() throws Exception {
    	final BigFraction[][] iq = {
    		{ BigFraction.valueOf(-1)   ,  BigFraction.ONE          ,  BigFraction.ZERO        },
    		{ BigFraction.valueOf(5, 2) ,  BigFraction.valueOf(-1)  ,  BigFraction.valueOf(-1) },
    		{ BigFraction.valueOf(5, 2) ,  BigFraction.valueOf(-1)  ,  BigFraction.valueOf(+1) },
    	};
    	internalTestInt(null, null, iq);
    }
    /**
     * Tests the polyhedron example of Marco Scarno. 
     * <p>
     * The extreme rays are:
     * <pre>
     * ray[0] = [   2     5     0 ]
     * ray[1] = [ 1/3   1/3  -1/2 ]
     * ray[2] = [ 1/3   1/3   1/2 ]
     */
    public void testMarcoScarnoFraFra() throws Exception {
    	final BigFraction[][] iq = {
    		{ BigFraction.valueOf(-1)   ,  BigFraction.ONE          ,  BigFraction.ZERO        },
    		{ BigFraction.valueOf(5, 2) ,  BigFraction.valueOf(-1)  ,  BigFraction.valueOf(-1) },
    		{ BigFraction.valueOf(5, 2) ,  BigFraction.valueOf(-1)  ,  BigFraction.valueOf(+1) },
    	};
    	internalTestFra(null, null, iq);
    }    

    /**
     * Tests the tricky case with inequalities such that the transformed cone
     * contains no constraints
     */
    public void testZeroConstraintsDblDbl() throws Exception {
    	final double[][] iq = {
    		{1, 0, 1, 0, 0},
    		{0, 1, 0, 1, 0},
    		{0, 0, 1, 1, 0},
    		{0, 0, 1, 0, 1},
    		{0, 0, 0, 1, 1},
    	};
    	internalTestDbl(null, null, iq);
    }
    
	/**
	 * The sample of the "binary approach" paper, using an equality and an
	 * inequality matrix. Only a convex basis is computed.
     */
    public void testBinaryApproachDblDbl() throws Exception {
		final double[][] eq = new double[][] {
			{ 1, -1, -1,  0,  0,  0,  0},
			{ 0,  1,  0, -1, -1, -1,  0},
			{ 0,  0,  1,  0,  1, -1,  0},
			{ 0,  0,  0,  0,  0,  1, -1}
		};
		//reversibliliies:
		//false, false, false, false, true, false, false
		final double[][] iq = new double[][] {
			{ 1,  0,  0,  0,  0,  0,  0},
			{ 0,  1,  0,  0,  0,  0,  0},
			{ 0,  0,  1,  0,  0,  0,  0},
			{ 0,  0,  0,  1,  0,  0,  0},
			{ 0,  0,  0,  0,  0,  1,  0},
			{ 0,  0,  0,  0,  0,  0,  1},
		};
    	internalTestDbl(null, eq, iq);
    }
	/**
	 * The sample of the "binary approach" paper, using an equality and an
	 * inequality matrix. Only a convex basis is computed.
     */
    public void testBinaryApproachIntDbl() throws Exception {
		final int[][] eq = new int[][] {
			{ 1, -1, -1,  0,  0,  0,  0},
			{ 0,  1,  0, -1, -1, -1,  0},
			{ 0,  0,  1,  0,  1, -1,  0},
			{ 0,  0,  0,  0,  0,  1, -1}
		};
		//reversibliliies:
		//false, false, false, false, true, false, false
		final int[][] iq = new int[][] {
			{ 1,  0,  0,  0,  0,  0,  0},
			{ 0,  1,  0,  0,  0,  0,  0},
			{ 0,  0,  1,  0,  0,  0,  0},
			{ 0,  0,  0,  1,  0,  0,  0},
			{ 0,  0,  0,  0,  0,  1,  0},
			{ 0,  0,  0,  0,  0,  0,  1},
		};
    	internalTestDbl(null, eq, iq);
    }
	/**
	 * The sample of the "binary approach" paper, using an equality and an
	 * inequality matrix. Only a convex basis is computed.
     */
    public void testBinaryApproachLongDbl() throws Exception {
		final long[][] eq = new long[][] {
			{ 1, -1, -1,  0,  0,  0,  0},
			{ 0,  1,  0, -1, -1, -1,  0},
			{ 0,  0,  1,  0,  1, -1,  0},
			{ 0,  0,  0,  0,  0,  1, -1}
		};
		//reversibliliies:
		//false, false, false, false, true, false, false
		final long[][] iq = new long[][] {
			{ 1,  0,  0,  0,  0,  0,  0},
			{ 0,  1,  0,  0,  0,  0,  0},
			{ 0,  0,  1,  0,  0,  0,  0},
			{ 0,  0,  0,  1,  0,  0,  0},
			{ 0,  0,  0,  0,  0,  1,  0},
			{ 0,  0,  0,  0,  0,  0,  1},
		};
    	internalTestDbl(null, eq, iq);
    }
	/**
	 * The sample of the "binary approach" paper, using an equality and an
	 * inequality matrix. Only a convex basis is computed.
     */
    public void testBinaryApproachFraDbl() throws Exception {
		final BigFraction[][] eq = new BigFraction[][] {
			{ BigFraction.valueOf(1), BigFraction.valueOf(-1), BigFraction.valueOf(-1), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 1), BigFraction.valueOf( 0), BigFraction.valueOf(-1), BigFraction.valueOf(-1), BigFraction.valueOf(-1), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf(-1), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf(-1)}
		};
		//reversibliliies:
		//false), BigFraction.valueOf(false), BigFraction.valueOf(false), BigFraction.valueOf(false), BigFraction.valueOf(true), BigFraction.valueOf(false), BigFraction.valueOf(false
		final BigFraction[][] iq = new BigFraction[][] {
			{ BigFraction.valueOf(1), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 1), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 1)},
		};
    	internalTestDbl(null, eq, iq);
    }
    
	/**
	 * The sample of the "binary approach" paper, using an equality and an
	 * inequality matrix. Only a convex basis is computed.
     */
    public void testBinaryApproachDblInt() throws Exception {
		final double[][] eq = new double[][] {
			{ 1, -1, -1,  0,  0,  0,  0},
			{ 0,  1,  0, -1, -1, -1,  0},
			{ 0,  0,  1,  0,  1, -1,  0},
			{ 0,  0,  0,  0,  0,  1, -1}
		};
		//reversibliliies:
		//false, false, false, false, true, false, false
		final double[][] iq = new double[][] {
			{ 1,  0,  0,  0,  0,  0,  0},
			{ 0,  1,  0,  0,  0,  0,  0},
			{ 0,  0,  1,  0,  0,  0,  0},
			{ 0,  0,  0,  1,  0,  0,  0},
			{ 0,  0,  0,  0,  0,  1,  0},
			{ 0,  0,  0,  0,  0,  0,  1},
		};
    	internalTestInt(null, eq, iq);
    }
	/**
	 * The sample of the "binary approach" paper, using an equality and an
	 * inequality matrix. Only a convex basis is computed.
     */
    public void testBinaryApproachIntInt() throws Exception {
		final int[][] eq = new int[][] {
			{ 1, -1, -1,  0,  0,  0,  0},
			{ 0,  1,  0, -1, -1, -1,  0},
			{ 0,  0,  1,  0,  1, -1,  0},
			{ 0,  0,  0,  0,  0,  1, -1}
		};
		//reversibliliies:
		//false, false, false, false, true, false, false
		final int[][] iq = new int[][] {
			{ 1,  0,  0,  0,  0,  0,  0},
			{ 0,  1,  0,  0,  0,  0,  0},
			{ 0,  0,  1,  0,  0,  0,  0},
			{ 0,  0,  0,  1,  0,  0,  0},
			{ 0,  0,  0,  0,  0,  1,  0},
			{ 0,  0,  0,  0,  0,  0,  1},
		};
    	internalTestInt(null, eq, iq);
    }
	/**
	 * The sample of the "binary approach" paper, using an equality and an
	 * inequality matrix. Only a convex basis is computed.
     */
    public void testBinaryApproachLongInt() throws Exception {
		final long[][] eq = new long[][] {
			{ 1, -1, -1,  0,  0,  0,  0},
			{ 0,  1,  0, -1, -1, -1,  0},
			{ 0,  0,  1,  0,  1, -1,  0},
			{ 0,  0,  0,  0,  0,  1, -1}
		};
		//reversibliliies:
		//false, false, false, false, true, false, false
		final long[][] iq = new long[][] {
			{ 1,  0,  0,  0,  0,  0,  0},
			{ 0,  1,  0,  0,  0,  0,  0},
			{ 0,  0,  1,  0,  0,  0,  0},
			{ 0,  0,  0,  1,  0,  0,  0},
			{ 0,  0,  0,  0,  0,  1,  0},
			{ 0,  0,  0,  0,  0,  0,  1},
		};
    	internalTestInt(null, eq, iq);
    }
	/**
	 * The sample of the "binary approach" paper, using an equality and an
	 * inequality matrix. Only a convex basis is computed.
     */
    public void testBinaryApproachFraInt() throws Exception {
		final BigFraction[][] eq = new BigFraction[][] {
			{ BigFraction.valueOf(1), BigFraction.valueOf(-1), BigFraction.valueOf(-1), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 1), BigFraction.valueOf( 0), BigFraction.valueOf(-1), BigFraction.valueOf(-1), BigFraction.valueOf(-1), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf(-1), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf(-1)}
		};
		//reversibliliies:
		//false), BigFraction.valueOf(false), BigFraction.valueOf(false), BigFraction.valueOf(false), BigFraction.valueOf(true), BigFraction.valueOf(false), BigFraction.valueOf(false
		final BigFraction[][] iq = new BigFraction[][] {
			{ BigFraction.valueOf(1), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 1), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 1)},
		};
    	internalTestInt(null, eq, iq);
    }
    
	/**
	 * The sample of the "binary approach" paper, using an equality and an
	 * inequality matrix. Only a convex basis is computed.
     */
    public void testBinaryApproachDblFra() throws Exception {
		final double[][] eq = new double[][] {
			{ 1, -1, -1,  0,  0,  0,  0},
			{ 0,  1,  0, -1, -1, -1,  0},
			{ 0,  0,  1,  0,  1, -1,  0},
			{ 0,  0,  0,  0,  0,  1, -1}
		};
		//reversibliliies:
		//false, false, false, false, true, false, false
		final double[][] iq = new double[][] {
			{ 1,  0,  0,  0,  0,  0,  0},
			{ 0,  1,  0,  0,  0,  0,  0},
			{ 0,  0,  1,  0,  0,  0,  0},
			{ 0,  0,  0,  1,  0,  0,  0},
			{ 0,  0,  0,  0,  0,  1,  0},
			{ 0,  0,  0,  0,  0,  0,  1},
		};
    	internalTestFra(null, eq, iq);
    }
	/**
	 * The sample of the "binary approach" paper, using an equality and an
	 * inequality matrix. Only a convex basis is computed.
     */
    public void testBinaryApproachIntFra() throws Exception {
		final int[][] eq = new int[][] {
			{ 1, -1, -1,  0,  0,  0,  0},
			{ 0,  1,  0, -1, -1, -1,  0},
			{ 0,  0,  1,  0,  1, -1,  0},
			{ 0,  0,  0,  0,  0,  1, -1}
		};
		//reversibliliies:
		//false, false, false, false, true, false, false
		final int[][] iq = new int[][] {
			{ 1,  0,  0,  0,  0,  0,  0},
			{ 0,  1,  0,  0,  0,  0,  0},
			{ 0,  0,  1,  0,  0,  0,  0},
			{ 0,  0,  0,  1,  0,  0,  0},
			{ 0,  0,  0,  0,  0,  1,  0},
			{ 0,  0,  0,  0,  0,  0,  1},
		};
    	internalTestFra(null, eq, iq);
    }
	/**
	 * The sample of the "binary approach" paper, using an equality and an
	 * inequality matrix. Only a convex basis is computed.
     */
    public void testBinaryApproachLongFra() throws Exception {
		final long[][] eq = new long[][] {
			{ 1, -1, -1,  0,  0,  0,  0},
			{ 0,  1,  0, -1, -1, -1,  0},
			{ 0,  0,  1,  0,  1, -1,  0},
			{ 0,  0,  0,  0,  0,  1, -1}
		};
		//reversibliliies:
		//false, false, false, false, true, false, false
		final long[][] iq = new long[][] {
			{ 1,  0,  0,  0,  0,  0,  0},
			{ 0,  1,  0,  0,  0,  0,  0},
			{ 0,  0,  1,  0,  0,  0,  0},
			{ 0,  0,  0,  1,  0,  0,  0},
			{ 0,  0,  0,  0,  0,  1,  0},
			{ 0,  0,  0,  0,  0,  0,  1},
		};
    	internalTestFra(null, eq, iq);
    }
	/**
	 * The sample of the "binary approach" paper, using an equality and an
	 * inequality matrix. Only a convex basis is computed.
     */
    public void testBinaryApproachFraFra() throws Exception {
		final BigFraction[][] eq = new BigFraction[][] {
			{ BigFraction.valueOf(1), BigFraction.valueOf(-1), BigFraction.valueOf(-1), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 1), BigFraction.valueOf( 0), BigFraction.valueOf(-1), BigFraction.valueOf(-1), BigFraction.valueOf(-1), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf(-1), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf(-1)}
		};
		//reversibliliies:
		//false), BigFraction.valueOf(false), BigFraction.valueOf(false), BigFraction.valueOf(false), BigFraction.valueOf(true), BigFraction.valueOf(false), BigFraction.valueOf(false
		final BigFraction[][] iq = new BigFraction[][] {
			{ BigFraction.valueOf(1), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 1), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 1), BigFraction.valueOf( 0)},
			{ BigFraction.valueOf(0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 0), BigFraction.valueOf( 1)},
		};
    	internalTestFra(null, eq, iq);
    }
    
	/**
	 * The sample of the "binary approach" paper, using an equality and an
	 * inequality matrix. Only a convex basis is computed.
     */
    public void testBinaryApproachIntDblWithOpts() throws Exception {
		final int[][] eq = new int[][] {
			{ 1, -1, -1,  0,  0,  0,  0},
			{ 0,  1,  0, -1, -1, -1,  0},
			{ 0,  0,  1,  0,  1, -1,  0},
			{ 0,  0,  0,  0,  0,  1, -1}
		};
		//reversibliliies:
		//false, false, false, false, true, false, false
		final int[][] iq = new int[][] {
			{ 1,  0,  0,  0,  0,  0,  0},
			{ 0,  1,  0,  0,  0,  0,  0},
			{ 0,  0,  1,  0,  0,  0,  0},
			{ 0,  0,  0,  1,  0,  0,  0},
			{ 0,  0,  0,  0,  0,  1,  0},
			{ 0,  0,  0,  0,  0,  0,  1},
		};
		final Options opts = new Options();
		opts.setLoglevel(Level.FINER);
		opts.setLogFormat(Options.LogFormat.plain);
		opts.setLogFile(new File(System.getProperty("java.io.tmpdir"), getClass().getSimpleName() + ".log"));
    	internalTestDbl(opts, eq, iq);
    }
    

    protected void internalTestDbl(Options opts, double[][] eq, double[][] iq) throws Exception {
    	final PolcoAdapter polco = opts == null ? new PolcoAdapter() : new PolcoAdapter(opts);
    	final double[][] rays = polco.getDoubleRays(eq, iq);
    	
    	final int maxOut = 100;
    	for (int i = 0; i < Math.min(maxOut, rays.length); i++) {
			Loggers.getRootLogger().info("ray[" + i + "] = " + Arrays.toString(rays[i]));
		}
    	if (rays.length > maxOut) {
			Loggers.getRootLogger().info("...");
    	}
    }
    protected void internalTestDbl(Options opts, int[][] eq, int[][] iq) throws Exception {
    	final PolcoAdapter polco = opts == null ? new PolcoAdapter() : new PolcoAdapter(opts);
    	final double[][] rays = polco.getDoubleRays(eq, iq);
    	
    	final int maxOut = 100;
    	for (int i = 0; i < Math.min(maxOut, rays.length); i++) {
			Loggers.getRootLogger().info("ray[" + i + "] = " + Arrays.toString(rays[i]));
		}
    	if (rays.length > maxOut) {
			Loggers.getRootLogger().info("...");
    	}
    }
    protected void internalTestDbl(Options opts, long[][] eq, long[][] iq) throws Exception {
    	final PolcoAdapter polco = opts == null ? new PolcoAdapter() : new PolcoAdapter(opts);
    	final double[][] rays = polco.getDoubleRays(eq, iq);
    	
    	final int maxOut = 100;
    	for (int i = 0; i < Math.min(maxOut, rays.length); i++) {
			Loggers.getRootLogger().info("ray[" + i + "] = " + Arrays.toString(rays[i]));
		}
    	if (rays.length > maxOut) {
			Loggers.getRootLogger().info("...");
    	}
    }
    protected void internalTestDbl(Options opts, BigFraction[][] eq, BigFraction[][] iq) throws Exception {
    	final PolcoAdapter polco = opts == null ? new PolcoAdapter() : new PolcoAdapter(opts);
    	final double[][] rays = polco.getDoubleRays(eq, iq);
    	
    	final int maxOut = 100;
    	for (int i = 0; i < Math.min(maxOut, rays.length); i++) {
			Loggers.getRootLogger().info("ray[" + i + "] = " + Arrays.toString(rays[i]));
		}
    	if (rays.length > maxOut) {
			Loggers.getRootLogger().info("...");
    	}
    }
    protected void internalTestInt(Options opts, double[][] eq, double[][] iq) throws Exception {
    	final PolcoAdapter polco = opts == null ? new PolcoAdapter() : new PolcoAdapter(opts);
    	final BigInteger[][] rays = polco.getBigIntegerRays(eq, iq);
    	
    	final int maxOut = 100;
    	for (int i = 0; i < Math.min(maxOut, rays.length); i++) {
			Loggers.getRootLogger().info("ray[" + i + "] = " + Arrays.toString(rays[i]));
		}
    	if (rays.length > maxOut) {
			Loggers.getRootLogger().info("...");
    	}
    }
    protected void internalTestInt(Options opts, int[][] eq, int[][] iq) throws Exception {
    	final PolcoAdapter polco = opts == null ? new PolcoAdapter() : new PolcoAdapter(opts);
    	final BigInteger[][] rays = polco.getBigIntegerRays(eq, iq);
    	
    	final int maxOut = 100;
    	for (int i = 0; i < Math.min(maxOut, rays.length); i++) {
			Loggers.getRootLogger().info("ray[" + i + "] = " + Arrays.toString(rays[i]));
		}
    	if (rays.length > maxOut) {
			Loggers.getRootLogger().info("...");
    	}
    }
    protected void internalTestInt(Options opts, long[][] eq, long[][] iq) throws Exception {
    	final PolcoAdapter polco = opts == null ? new PolcoAdapter() : new PolcoAdapter(opts);
    	final BigInteger[][] rays = polco.getBigIntegerRays(eq, iq);
    	
    	final int maxOut = 100;
    	for (int i = 0; i < Math.min(maxOut, rays.length); i++) {
			Loggers.getRootLogger().info("ray[" + i + "] = " + Arrays.toString(rays[i]));
		}
    	if (rays.length > maxOut) {
			Loggers.getRootLogger().info("...");
    	}
    }
    protected void internalTestInt(Options opts, BigFraction[][] eq, BigFraction[][] iq) throws Exception {
    	final PolcoAdapter polco = opts == null ? new PolcoAdapter() : new PolcoAdapter(opts);
    	final BigInteger[][] rays = polco.getBigIntegerRays(eq, iq);
    	
    	final int maxOut = 100;
    	for (int i = 0; i < Math.min(maxOut, rays.length); i++) {
			Loggers.getRootLogger().info("ray[" + i + "] = " + Arrays.toString(rays[i]));
		}
    	if (rays.length > maxOut) {
			Loggers.getRootLogger().info("...");
    	}
    }
    protected void internalTestFra(Options opts, double[][] eq, double[][] iq) throws Exception {
    	final PolcoAdapter polco = opts == null ? new PolcoAdapter() : new PolcoAdapter(opts);
    	final BigFraction[][] rays = polco.getBigFractionRays(eq, iq);
    	
    	final int maxOut = 100;
    	for (int i = 0; i < Math.min(maxOut, rays.length); i++) {
			Loggers.getRootLogger().info("ray[" + i + "] = " + Arrays.toString(rays[i]));
		}
    	if (rays.length > maxOut) {
			Loggers.getRootLogger().info("...");
    	}
    }
    protected void internalTestFra(Options opts, int[][] eq, int[][] iq) throws Exception {
    	final PolcoAdapter polco = opts == null ? new PolcoAdapter() : new PolcoAdapter(opts);
    	final BigFraction[][] rays = polco.getBigFractionRays(eq, iq);
    	
    	final int maxOut = 100;
    	for (int i = 0; i < Math.min(maxOut, rays.length); i++) {
			Loggers.getRootLogger().info("ray[" + i + "] = " + Arrays.toString(rays[i]));
		}
    	if (rays.length > maxOut) {
			Loggers.getRootLogger().info("...");
    	}
    }
    protected void internalTestFra(Options opts, long[][] eq, long[][] iq) throws Exception {
    	final PolcoAdapter polco = opts == null ? new PolcoAdapter() : new PolcoAdapter(opts);
    	final BigFraction[][] rays = polco.getBigFractionRays(eq, iq);
    	
    	final int maxOut = 100;
    	for (int i = 0; i < Math.min(maxOut, rays.length); i++) {
			Loggers.getRootLogger().info("ray[" + i + "] = " + Arrays.toString(rays[i]));
		}
    	if (rays.length > maxOut) {
			Loggers.getRootLogger().info("...");
    	}
    }
    protected void internalTestFra(Options opts, BigFraction[][] eq, BigFraction[][] iq) throws Exception {
    	final PolcoAdapter polco = opts == null ? new PolcoAdapter() : new PolcoAdapter(opts);
    	final BigFraction[][] rays = polco.getBigFractionRays(eq, iq);
    	
    	final int maxOut = 100;
    	for (int i = 0; i < Math.min(maxOut, rays.length); i++) {
			Loggers.getRootLogger().info("ray[" + i + "] = " + Arrays.toString(rays[i]));
		}
    	if (rays.length > maxOut) {
			Loggers.getRootLogger().info("...");
    	}
    }
}
