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
import java.io.FileInputStream;
import java.util.logging.Level;

import junit.framework.TestCase;
import ch.javasoft.metabolic.Norm;
import ch.javasoft.metabolic.compress.CompressionMethod;
import ch.javasoft.metabolic.efm.adj.incore.tree.search.PatternTreeMinZerosAdjacencyEnumerator;
import ch.javasoft.metabolic.efm.config.Arithmetic;
import ch.javasoft.metabolic.efm.config.Config;
import ch.javasoft.metabolic.efm.progress.ProgressType;
import ch.javasoft.polco.callback.NullCallback;
import ch.javasoft.polco.config.ArithmeticCallback;
import ch.javasoft.polco.config.PolcoConfig;
import ch.javasoft.polco.metabolic.EfmExtremeRayEnumerator;
import ch.javasoft.polco.parse.CddParser;
import ch.javasoft.polco.xenum.ExtremeRayCallback;
import ch.javasoft.polco.xenum.ExtremeRayEnumerator;
import ch.javasoft.util.logging.Loggers;
import ch.javasoft.util.numeric.Zero;

public class CddExtTest extends TestCase {
	
    private static final File FOLDER_EXT = new File("../cdd-data/ext");

    private static final Arithmetic arithmetic;
	
    static {
//		arithmetic = Arithmetic.fractional;
//		arithmetic = Arithmetic.bigint;
		arithmetic = Arithmetic.varint;
//		arithmetic = Arithmetic.double_;
//		final CompressionMethod[] compression = CompressionMethod.STANDARD_NO_DUPLICATE;
		final CompressionMethod[] compression = CompressionMethod.NONE;
		if (Config.initForJUnitTest(PatternTreeMinZerosAdjacencyEnumerator.NAME, "MostZerosOrAbsLexMin", compression, false, arithmetic, Norm.min, 4, ProgressType.None)) {
//		if (Config.initForJUnitTest(PatternTreeMinZerosAdjacencyEnumerator.NAME, "FewestNegPos", compression, false, arithmetic, Norm.min, 4, ProgressType.None)) {
//		if (Config.initForJUnitTest(ModIntPrimeInCoreAdjEnum.NAME, "FewestNegPos", compression, false, arithmetic, Norm.min, 4, ProgressType.None)) {
//		if (Config.initForJUnitTest(ModIntPrimePatternTreeRankUpdateAdjacencyEnumerator.NAME, "FewestNegPosOrMostZeros", compression, false, arithmetic, Norm.min, 0)) {
//		if (Config.initForJUnitTest(ModIntPrimePatternTreeRankUpdateAdjacencyEnumerator.NAME, "FewestNegPosOrMostZeros", compression, false, arithmetic)) {
			Loggers.getRootLogger().setLevel(Level.FINE);			
//	        ElementaryFluxModes.setImpl(new SequentialDoubleDescriptionImpl(Config.getConfig(), new NullspaceEfmModelFactory(), new OutOfCoreMemoryFactory()));       		
//	        ElementaryFluxModes.setImpl(new SequentialDoubleDescriptionImpl(Config.getConfig(), new NullspaceEfmModelFactory(), new InCoreMemoryFactory()));       		
//	        ElementaryFluxModes.setImpl(new BornDieDoubleDescriptionImpl(Config.getConfig(), new BornDieEfmModelFactory(), new InCoreMemoryFactory()));       		
		}
	}

    public void testCcp4() throws Exception {
        internalTestExt("ccp4.ext");
    }
    public void testCcp5() throws Exception {
        internalTestExt("ccp5.ext");
    }
    public void testCcp6() throws Exception {
        internalTestExt("ccp6.ext");
    }
    public void testCcp7() throws Exception {
        internalTestExt("ccp7.ext");
    }
    public void testCcp8() throws Exception {
        internalTestExt("ccp8.ext");
    }
    public void testCutPolytope4() throws Exception {
        internalTestExt("cutPolytope4.ext");
    }
    public void testCutPolytope5() throws Exception {
        internalTestExt("cutPolytope5.ext");
    }
    public void testCutPolytope6() throws Exception {
        internalTestExt("cutPolytope6.ext");
    }
    public void testCutPolytope7() throws Exception {
        internalTestExt("cutPolytope7.ext");
    }
    public void testCutPolytope8() throws Exception {
        internalTestExt("cutPolytope8.ext");
    }
    public void testCcc4() throws Exception {
        internalTestExt("ccc4.ext");
    }
    public void testCcc5() throws Exception {
        internalTestExt("ccc5.ext");
    }
    public void testCcc6() throws Exception {
        internalTestExt("ccc6.ext");
    }
    public void testCcc7() throws Exception {
        internalTestExt("ccc7.ext");
    }
    public void testCube6() throws Exception {
    	internalTestExt("cube6.ext");
    }
    public void testCube8() throws Exception {
    	internalTestExt("cube8.ext");
    }
    public void testCube10() throws Exception {
    	internalTestExt("cube10.ext");
    }
    public void testCross6() throws Exception {
    	internalTestExt("cross6.ext");
    }
    public void testCross8() throws Exception {
    	internalTestExt("cross8.ext");
    }
    public void testCross10() throws Exception {
    	internalTestExt("cross10.ext");
    }
    /**
     * 3-dim cyclic polytope with 10 vertices
     * Number of facets (outputs) =16
     */
    public void testCyclic10_4() throws Exception {
    	internalTestExt("cyclic10-4.ext");
    }
    /**
     * 5-dim cyclic polytope with12 vertices
     * Number of facets (outputs) =72
     */
    public void testCyclic12_6() throws Exception {
    	internalTestExt("cyclic12-6.ext");
    }
    /**
     * 7-dim cyclic polytope with 14 vertices
     * Number of facets (outputs) =240
     */
    public void testCyclic14_8() throws Exception {
    	internalTestExt("cyclic14-8.ext");
    }
    /**
     * 9-dim cyclic polytope with 16 vertices
     * Number of facets (outputs) =660
     * This must be computed with exact arithmetic
     */
    public void testCyclic16_10() throws Exception {
    	internalTestExt("cyclic16-10.ext");
    }
    /**
     * 11-dim cyclic polytope with 18 vertices
     * Number of facets (outputs) =1584
     * This must be computed with exact arithmetic
     */
    public void testCyclic18_12() throws Exception {
    	internalTestExt("cyclic18-12.ext");
    }
    /**
     * 13-dim cyclic polytope with 20 vertices
     * Number of facets (outputs) =3432
     * This must be computed with exact arithmetic
     */
    public void testCyclic20_14() throws Exception {
    	internalTestExt("cyclic20-14.ext");
    }
    /**
     * 15-dim cyclic polytope with 30 vertices
     * Number of facets (outputs) =341088
     * This must be computed with exact arithmetic
     */
    public void testCyclic30_16() throws Exception {
    	internalTestExt("cyclic30-16.ext");
    }
    public void testProdmT62() throws Exception {
    	internalTestExt("prodmT62.ext");
    }
    /** 432 facets   */
    public void testTc7_30() throws Exception {
    	internalTestExt("tc7-30.ext");
    }
    /** 1675 facets   */
    public void testTc8_38() throws Exception {
    	internalTestExt("tc8-38.ext");
    }
    /** 6875 facets   */
    public void testTc9_48() throws Exception {
    	internalTestExt("tc9-48.ext");
    }
    /** 41591 facets   */
    public void testTc10_83() throws Exception {
    	internalTestExt("tc10-83.ext");
    }
    /** 250279 facets   */
    public void testTc11_106() throws Exception {
    	internalTestExt("tc11-106.ext");
    }
    /** 1975935 facets (? 1966766 ?)  */
    public void testTc12_152() throws Exception {
    	internalTestExt("tc12-152.ext");
    }
    /** 17464356 facets   */
    public void testTc13_254() throws Exception {
    	internalTestExt("tc13-254.ext");
    }

    public void internalTestExt(String fileName) throws Exception {
		final File file = new File(FOLDER_EXT, fileName);
		final String testName = getClass().getSimpleName() + "{" + file.getAbsolutePath() + "}";
		TestHelper.getPolcoArithmetic(arithmetic).callback(new ArithmeticCallback<Void>() {
			public <N extends Number, A> Void callback(ch.javasoft.polco.config.Arithmetic<N, A> arithmetic) throws Exception {
				final Zero zero = arithmetic.getDefaultZero();
				final PolyhedralCone<N, A> cone = new CddParser().parse(null, arithmetic, zero, new FileInputStream(file));
				final ExtremeRayEnumerator xray = new EfmExtremeRayEnumerator(zero, true);
				final ExtremeRayCallback<N, A> callback = NullCallback.instance();			
				PolcoConfig.traceJunitConfig(arithmetic, zero, testName);
				xray.enumerateExtremeRays(cone, callback, arithmetic.getLinAlgOperations(zero));
				return null;
			}
		});
	}
    

}
