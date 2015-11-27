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
import ch.javasoft.metabolic.compress.CompressionMethod;
import ch.javasoft.metabolic.efm.adj.incore.tree.search.PatternTreeMinZerosAdjacencyEnumerator;
import ch.javasoft.metabolic.efm.config.Arithmetic;
import ch.javasoft.metabolic.efm.config.Config;
import ch.javasoft.polco.callback.NullCallback;
import ch.javasoft.polco.config.ArithmeticCallback;
import ch.javasoft.polco.metabolic.EfmExtremeRayEnumerator;
import ch.javasoft.polco.parse.CddParser;
import ch.javasoft.polco.xenum.ExtremeRayCallback;
import ch.javasoft.polco.xenum.ExtremeRayEnumerator;
import ch.javasoft.util.logging.Loggers;
import ch.javasoft.util.numeric.Zero;

public class CddIneTest extends TestCase {
	
    private static final File FOLDER_INE = new File("../cdd-data/ine");
    
    private static final Arithmetic arithmetic;
	
    static {
		arithmetic = Arithmetic.bigint;
//		arithmetic = Arithmetic.double_;
		//LogFormatter.setDefaultFormat(LogFormatter.FORMAT_PLAIN);
		final CompressionMethod[] compression = CompressionMethod.NONE;
//		final CompressionMethod[] compression = CompressionMethod.methods(CompressionMethod.UniqueFlows, CompressionMethod.DeadEnd, CompressionMethod.Recursive);
//		final CompressionMethod[] compression = CompressionMethod.methods(CompressionMethod.UniqueFlows, CompressionMethod.Recursive);
//		final CompressionMethod[] compression = CompressionMethod.NONE;
		if (Config.initForJUnitTest(PatternTreeMinZerosAdjacencyEnumerator.NAME, "MostZerosOrAbsLexMin", compression, false, arithmetic)) {
//		if (Config.initForJUnitTest(PatternTreeMinZerosAdjacencyEnumerator.NAME, "FewestNegPos", compression, false, arithmetic)) {
//		if (Config.initForJUnitTest(ModIntPrimePatternTreeRankUpdateAdjacencyEnumerator.NAME, "FewestNegPos", compression, false, arithmetic, Norm.min)) {
			Loggers.getRootLogger().setLevel(Level.FINE);
//	        ElementaryFluxModes.setImpl(new BornDieDoubleDescriptionImpl(Config.getConfig(), new BornDieEfmModelFactory(), new InCoreMemoryFactory()));       		
		}
	}

    public void testCube2() throws Exception {
        internalTestIne("cube2.ine");
    }
    public void testCube3() throws Exception {
        internalTestIne("cube3.ine");
    }
    public void testCube4() throws Exception {
        internalTestIne("cube4.ine");
    }
    public void testCube6() throws Exception {
        internalTestIne("cube6.ine");
    }
    public void testCube8() throws Exception {
        internalTestIne("cube8.ine");
    }
    public void testCube10() throws Exception {
        internalTestIne("cube10.ine");
    }
    
    public void testCube14() throws Exception {
        internalTestIne("cube14.ine");
    }
    
    public void testCube16() throws Exception {
        internalTestIne("cube16.ine");
    }
    public void testCube18() throws Exception {
        internalTestIne("cube18.ine");
    }
    public void testCube20() throws Exception {
        internalTestIne("cube20.ine");
    }
    public void testCube22() throws Exception {
        internalTestIne("cube22.ine");
    }
    
    public void testCross6() throws Exception {
        internalTestIne("cross6.ine");
    }
    
    public void testCross8() throws Exception {
        internalTestIne("cross8.ine");
    }
    
    public void testCross10() throws Exception {
        internalTestIne("cross10.ine");
    }
    
    public void testCross12() throws Exception {
        internalTestIne("cross12.ine");
    }
    
    public void testCcp4rev() throws Exception {
        internalTestIne("ccp4.ine");
    }
//    public void testCcp4() throws Exception {
//        internalTestIneFile(FOLDER_EXT, "ccp4.ext");
//    }
    public void testCcp5rev() throws Exception {
        internalTestIne("ccp5.ine");
    }
//    public void testCcp5() throws Exception {
//        internalTestIneFile(FOLDER_EXT, "ccp5.ext");
//    }
    public void testCcp6rev() throws Exception {
        internalTestIne("ccp6.ine");
    }
//    public void testCcp6() throws Exception {
//        internalTestIneFile(FOLDER_EXT, "ccp6.ext");
//    }
    public void testCcp7rev() throws Exception {
        internalTestIne("ccp7.ine");
    }
//    public void testCcp7() throws Exception {
//        internalTestIneFile(FOLDER_EXT, "ccp7.ext");
//    }
//    public void testCcc6() throws Exception {
//        internalTestIneFile(FOLDER_EXT, "ccc6.ext");
//    }
//    public void testCcc7() throws Exception {
//        internalTestIneFile(FOLDER_EXT, "ccc7.ext");
//    }
    public void testReg600_5() throws Exception {
        internalTestIne("reg600-5.ine");
    }
    public void testProdmT5() throws Exception {
        internalTestIne("prodmt5.ine");
    }
    public void testProdmT62() throws Exception {
        internalTestIne("prodst62.ine");
    }
    /** 18553 vertices */
    public void testMit31_20() throws Exception {
        internalTestIne("mit31-20.ine");
    }
    /** 29108 vertices */
    public void testMit41_16() throws Exception {
        internalTestIne("mit41-16.ine");
    }
    /** 3149579 vertices */
    public void testMit71_61() throws Exception {
        internalTestIne("mit71-61.ine");
    }
    /** 323188 ? vertices */
    public void testMit90_86() throws Exception {
        internalTestIne("mit90-86.ine");
    }
    /** 4862 vertices */
    public void testMit729_9() throws Exception {
        internalTestIne("mit729-9.ine");
    }    
    /** 18 vertices */
    public void testKkd11_3() throws Exception {
        internalTestIne("kkd11_3.ine");
    }
    /** 56 vertices */
    public void testKkd18_4() throws Exception {
        internalTestIne("kkd18_4.ine");
    }
    /** 130 vertices */
    public void testKkd27_5() throws Exception {
        internalTestIne("kkd27_5.ine");
    }
    /** 252 vertices */
    public void testKkd38_6() throws Exception {
        internalTestIne("kkd38_6.ine");
    }
    /** 434 vertices */
    public void testKkd51_7() throws Exception {
        internalTestIne("kkd51_7.ine");
    }
    /** 688 vertices */
    public void testKkd66_8() throws Exception {
        internalTestIne("kkd66_8.ine");
    }
    /** 1026 vertices */
    public void testKkd83_9() throws Exception {
        internalTestIne("kkd83_9.ine");
    }
    /** 1460 vertices */
    public void testKkd102_10() throws Exception {
        internalTestIne("kkd102_10.ine");
    }
    /** 2002 vertices */
    public void testKkd123_11() throws Exception {
        internalTestIne("kkd123_11.ine");
    }
    /** 2664 vertices */
    public void testKkd146_12() throws Exception {
        internalTestIne("kkd146_12.ine");
    }
    
    /** 367,525 vertices */
    public void testStein27() throws Exception {
        internalTestIne("stein27.ine");
    }

    protected void internalTestIne(String fileName) throws Exception {
		final File file = new File(FOLDER_INE, fileName);
		TestHelper.getPolcoArithmetic(arithmetic).callback(new ArithmeticCallback<Void>() {
			public <N extends Number, A> Void callback(ch.javasoft.polco.config.Arithmetic<N, A> arithmetic) throws Exception {
				final Zero zero = Config.getConfig().zero();
				final PolyhedralCone<N, A> cone = new CddParser().parse(null, arithmetic, zero, new FileInputStream(file));
				final ExtremeRayEnumerator xray = new EfmExtremeRayEnumerator(zero, true);
				final ExtremeRayCallback<N, A> callback = NullCallback.instance();
				xray.enumerateExtremeRays(cone, callback, arithmetic.getLinAlgOperations(zero));
				return null;
			}
		});
	}

}
