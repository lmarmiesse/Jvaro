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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.logging.Level;

import junit.framework.TestCase;
import ch.javasoft.metabolic.compress.CompressionMethod;
import ch.javasoft.metabolic.efm.adj.incore.tree.search.PatternTreeMinZerosAdjacencyEnumerator;
import ch.javasoft.metabolic.efm.config.Arithmetic;
import ch.javasoft.metabolic.efm.config.Config;
import ch.javasoft.polco.callback.matlab.MatlabCallback;
import ch.javasoft.polco.config.ArithmeticCallback;
import ch.javasoft.polco.metabolic.EfmExtremeRayEnumerator;
import ch.javasoft.polco.parse.TextParser;
import ch.javasoft.polco.xenum.ExtremeRayCallback;
import ch.javasoft.polco.xenum.ExtremeRayEnumerator;
import ch.javasoft.util.logging.Loggers;
import ch.javasoft.util.numeric.Zero;

/**
 * The <code>TextTest</code> ... TODO javadoc-TextTest-type
 * 
 */
public class TextTest extends TestCase {

	private static final File FOLDER = new File("../metabolic-data/text-matrix");
    
	private static final Arithmetic arithmetic;
	
    static {
		arithmetic = Arithmetic.fractional;
//		arithmetic = Arithmetic.fractional;
		//LogFormatter.setDefaultFormat(LogFormatter.FORMAT_PLAIN);
//		final CompressionMethod[] compression = CompressionMethod.STANDARD_NO_DUPLICATE;
//		final CompressionMethod[] compression = CompressionMethod.methods(CompressionMethod.UniqueFlows, CompressionMethod.DeadEnd, CompressionMethod.Recursive);
//		final CompressionMethod[] compression = CompressionMethod.methods(CompressionMethod.UniqueFlows, CompressionMethod.Recursive);
//		final CompressionMethod[] compression = CompressionMethod.NONE;
		final CompressionMethod[] compression = CompressionMethod.STANDARD_NO_DUPLICATE;
		if (Config.initForJUnitTest(PatternTreeMinZerosAdjacencyEnumerator.NAME, "FewestNegPos", compression, false, arithmetic)) {
//		if (Config.initForJUnitTest(ModIntPrimePatternTreeRankUpdateAdjacencyEnumerator.NAME, "FewestNegPos", compression, false, arithmetic, Norm.min)) {
			Loggers.getRootLogger().setLevel(Level.FINE);
//	        ElementaryFluxModes.setImpl(new BornDieDoubleDescriptionImpl(Config.getConfig(), new BornDieEfmModelFactory(), new InCoreMemoryFactory()));       		
		}
	}

    /**
     * Tests the tricky case with inequalities such that the transformed cone
     * contains no constraints
     */
    public void testZeroConstraints() throws Exception {
    	final String[] iq = {
    		"1 0 1 0 0",
    		"0 1 0 1 0",
    		"0 0 1 1 0",
    		"0 0 1 0 1",
    		"0 0 0 1 1",
    	};
    	internalTestText(null, iq);
    }
    
    public void testColiAll() throws Exception {
    	internalTestText("coli-nature-all");
    }
    public void testColiExpandedAll() throws Exception {
    	internalTestText("coli-nature-expanded-all");
    }
    public void testColiExpandedGlX() throws Exception {
    	internalTestText("coli-nature-expanded-glx");
    }
    /**
     * Contains an additional hyperplane 
     * mue >= c*Glc_PTS_up + c*Glc_ATP_up
     * <p>
     * c=0.1038810742 is a constant ensuring that the biomass yield is at least 
     * 98% of the best possible. The biomass yield is computed as
     * 4.79*mue/(24*Glc_PTS_up + 24*Glc_ATP_up). The top yield is 0.0211560521.
     */
    public void testColiExpandedGlXMueCut() throws Exception {
    	internalTestText("coli-nature-expanded-glx-muecut");
    }

    protected void internalTestText(String[] eq, String[] iq) throws Exception {
    	InputStream seq = str2InputStream(eq); 
    	InputStream sine = str2InputStream(iq);
    	internalTestText(seq, sine);
    }
	private static InputStream str2InputStream(String[] mat) {
		if (mat == null) return null;
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(out);
		for (int i = 0; i < mat.length; i++) {
			ps.println(mat[i]);
		}
		ps.flush();
		return new ByteArrayInputStream(out.toByteArray());
	}

	protected void internalTestText(final String fileName) throws Exception {
		final File eq = new File(FOLDER, fileName + ".eq");
		final File iq = new File(FOLDER, fileName + ".iq");
		internalTestText(eq, iq);
	}
	protected void internalTestText(final File eq, final File iq) throws Exception {
		final InputStream seq = eq == null ? null : new FileInputStream(eq);
		final InputStream siq = iq == null ? null : new FileInputStream(iq);
		internalTestText(seq, siq);
	}
	protected void internalTestText(final InputStream eq, final InputStream iq) throws Exception {
		TestHelper.getPolcoArithmetic(arithmetic).callback(new ArithmeticCallback<Void>() {
			public <N extends Number, A> Void callback(ch.javasoft.polco.config.Arithmetic<N, A> arithmetic) throws Exception {
				final Zero zero = Config.getConfig().zero();
				final PolyhedralCone<N, A> cone = new TextParser().parse(null, arithmetic, zero, eq, iq);
				final ExtremeRayEnumerator xray = new EfmExtremeRayEnumerator(zero, true);
//				final ExtremeRayCallback<N, A> callback = NullCallback.instance();
//				final ExtremeRayCallback<N, A> callback = new TextCallback<N, A>(System.out);
				final ExtremeRayCallback<N, A> callback = new MatlabCallback<N, A>(new File("/tmp/efms.mat"));
				xray.enumerateExtremeRays(cone, callback, arithmetic.getLinAlgOperations(zero));
				return null;
			}
		});
	}
}
