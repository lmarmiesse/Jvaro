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
import ch.javasoft.polco.parse.MpsParser;
import ch.javasoft.polco.xenum.ExtremeRayCallback;
import ch.javasoft.polco.xenum.ExtremeRayEnumerator;
import ch.javasoft.util.logging.Loggers;
import ch.javasoft.util.numeric.Zero;

public class MpsTest extends TestCase {
	
    private static final File FOLDER_INE = new File("../mps-data");
    
    private static final Arithmetic arithmetic;
	
    static {
		arithmetic = Arithmetic.rawint;
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
    
    /**  ??? facets */
    public void testStein9() throws Exception {
        internalTestPolymake("stein9.mps");
    }

    protected void internalTestPolymake(String fileName) throws Exception {
		final File file = new File(FOLDER_INE, fileName);
		TestHelper.getPolcoArithmetic(arithmetic).callback(new ArithmeticCallback<Void>() {
			public <N extends Number, A> Void callback(ch.javasoft.polco.config.Arithmetic<N, A> arithmetic) throws Exception {
				final Zero zero = Config.getConfig().zero();
				final PolyhedralCone<N, A> cone = new MpsParser().parse(null, arithmetic, zero, new FileInputStream(file));
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
