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
import ch.javasoft.polco.callback.NullCallback;
import ch.javasoft.polco.config.ArithmeticCallback;
import ch.javasoft.polco.metabolic.EfmExtremeRayEnumerator;
import ch.javasoft.polco.parse.PolymakeParser;
import ch.javasoft.polco.xenum.ExtremeRayCallback;
import ch.javasoft.polco.xenum.ExtremeRayEnumerator;
import ch.javasoft.util.logging.Loggers;
import ch.javasoft.util.numeric.Zero;

public class PolymakeTest extends TestCase {
	
    private static final File FOLDER_INE = new File("../polymake-data");
    
    private static final Arithmetic arithmetic;
	
    static {
//		arithmetic = Arithmetic.rawint;
//		arithmetic = Arithmetic.bigint;
		arithmetic = Arithmetic.varint;
//		arithmetic = Arithmetic.bigint;
//		arithmetic = Arithmetic.double_;
		//LogFormatter.setDefaultFormat(LogFormatter.FORMAT_PLAIN);
		final CompressionMethod[] compression = CompressionMethod.NONE;
//		final CompressionMethod[] compression = CompressionMethod.methods(CompressionMethod.UniqueFlows, CompressionMethod.DeadEnd, CompressionMethod.Recursive);
//		final CompressionMethod[] compression = CompressionMethod.methods(CompressionMethod.UniqueFlows, CompressionMethod.Recursive);
//		final CompressionMethod[] compression = CompressionMethod.NONE;
//		if (Config.initForJUnitTest(PatternTreeMinZerosAdjacencyEnumerator.NAME, "MostZerosOrAbsLexMin", compression, false, arithmetic)) {
		if (Config.initForJUnitTest(PatternTreeMinZerosAdjacencyEnumerator.NAME, "FewestNegPos", compression, false, arithmetic, Norm.min)) {
//		if (Config.initForJUnitTest(ModIntPrimePatternTreeRankUpdateAdjacencyEnumerator.NAME, "FewestNegPos", compression, false, arithmetic, Norm.min)) {
			Loggers.getRootLogger().setLevel(Level.FINE);
//	        ElementaryFluxModes.setImpl(new BornDieDoubleDescriptionImpl(Config.getConfig(), new BornDieEfmModelFactory(), new InCoreMemoryFactory()));       		
		}
	}
    
    /** 17 facets */
    public void testMJ_16_17() throws Exception {
        internalTestPolymake("MJ:16-17.poly");
    }
    /** 33 facets */
    public void testMJ_32_33() throws Exception {
        internalTestPolymake("MJ:32-33.poly");
    }
    /** 368 facets */
    public void testCUT6() throws Exception {
        internalTestPolymake("CUT6:15-32.poly");
    }
    /** 116,764 facets */
    public void testCUT7() throws Exception {
        internalTestPolymake("CUT7:21-64.poly");
    }
    /** ??? facets */
    public void testCUT8() throws Exception {
        internalTestPolymake("CUT8:28-128.poly");
    }
    /** 524 facets */
    public void testOA_8_25() throws Exception {
        internalTestPolymake("OA:8-25.poly");
    }
    /** 1870 facets */
    public void testOA_9_33() throws Exception {
        internalTestPolymake("OA:9-33.poly");
    }
    /** 9708 facets */
    public void testOA_10_44() throws Exception {
        internalTestPolymake("OA:10-44.poly");
    }
    /** 41,591 facets */
    public void testTC_10_83() throws Exception {
        internalTestPolymake("TC:10-83.poly");
    }
    /** 250,279 facets */
    public void testTC_11_106() throws Exception {
        internalTestPolymake("TC:11-106.poly");
    }
    /** 1975935 facets (? 1966766 ?) vertices */
    public void testTC_12_152() throws Exception {
        internalTestPolymake("TC:12-152.poly");
    }
    /**  17464356 facets */
    public void testTC_13_254() throws Exception {
        internalTestPolymake("TC:13-254.poly");
    }

    protected void internalTestPolymake(String fileName) throws Exception {
		final File file = new File(FOLDER_INE, fileName);
		TestHelper.getPolcoArithmetic(arithmetic).callback(new ArithmeticCallback<Void>() {
			public <N extends Number, A> Void callback(ch.javasoft.polco.config.Arithmetic<N, A> arithmetic) throws Exception {
				final Zero zero = Config.getConfig().zero();
				final PolyhedralCone<N, A> cone = new PolymakeParser().parse(null, arithmetic, zero, new FileInputStream(file));
				final ExtremeRayEnumerator xray = new EfmExtremeRayEnumerator(zero, true);
				final ExtremeRayCallback<N, A> callback = NullCallback.instance();
//				final ExtremeRayCallback<N, A> callback = new BDDCallback<N, A>(new FileOutputStream("/tmp/bdd-efm.txt"));
//				final ExtremeRayCallback<N, A> callback = new TextCallback<N, A>(new FileOutputStream("/tmp/efm.txt"));
				xray.enumerateExtremeRays(cone, callback, arithmetic.getLinAlgOperations(zero));
				return null;
			}
		});
	}

}
