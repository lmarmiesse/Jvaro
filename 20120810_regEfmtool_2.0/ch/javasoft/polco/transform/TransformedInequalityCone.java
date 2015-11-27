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
package ch.javasoft.polco.transform;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

import ch.javasoft.math.array.ArrayOperations;
import ch.javasoft.math.linalg.LinAlgOperations;
import ch.javasoft.polco.InequalityPolyhedralCone;
import ch.javasoft.polco.PolyhedralCone;
import ch.javasoft.polco.impl.AbstractInequalityCone;
import ch.javasoft.util.logging.LogPrintWriter;
import ch.javasoft.util.logging.Loggers;

/**
 * The <code>TransformedInequalityCone</code> is an 
 * {@link InequalityPolyhedralCone} derived from a general 
 * {@link PolyhedralCone}. Extreme rays of this cone can be transformed back to
 * extreme rays of the original cone.
 * <p>
 * More formally, a polyhedral cone <i>P</i> in general form, defined as the 
 * intersection of hyperplanes and halfspaces, is transformed into a polyhedral
 * cone <i>P'</i> in inequality form, defined only by halfspaces, where
 * <pre>
 * P  = { A x = 0 , B x &ge; 0 }
 * P' = { B' x2 &ge; 0 }
 * </pre>
 * <p>
 * <b>Transformation:</b><br>
 * <pre>
 * choose linearly independent columns I from A 
 *                 with |I| = rank(A) = #rows(A)
 *                 J = not I
 *
 * let A1 = A<sub>*I</sub> , B1 = B<sub>*I</sub> , x1 = x<sub>I</sub>  
 *     A2 = A<sub>*J</sub> , B2 = B<sub>*J</sub> , x2 = x<sub>J</sub>
 *
 * and  T = inv(A1)  i.e   T A1 = 1
 * 
 * then
 * 
 *   P = {   A x = 0 , B x &ge; 0 }
 *     = { T A x = 0 , B x &ge; 0 }
 *     = { T A1 x1 + T A2 x2 = 0  ,  B1 x1 + B2 x2 &ge; 0 }
 *     = { x1 = -T A2 x2  ,   ( -B1 T A2 + B2 ) x2 &ge; 0 }
 * </pre>
 * <p>
 * <b>References:</b><br> 
 * <ul>
 *   <li>Peter Malkin, "<a href="http://edoc.bib.ucl.ac.be:81/ETD-db/collection/available/BelnUcetd-06222007-144602/">Computing Markov bases, Gr&ouml;bner bases, and extreme rays</a>",
 *   PhD Thesis, 2007, Universit&eacute; catholique de Louvain, Belgium
 *   </li>
 * </ul>
 * 
 * @type Num	the number type of a single number
 * @type Arr	the number type of an array of numbers
 */
public class TransformedInequalityCone<Num extends Number, Arr> extends AbstractInequalityCone<Num, Arr> implements TransformedPolyhedralCone<Num, Arr> {
	
	private final PolyhedralCone<Num, Arr>	parent;
	private final AtomicReference<Arr[]> 	mxTtoOriginal = new AtomicReference<Arr[]>();
	
	private Arr[] mx_B1_negTA1_B2;	// -B1 T A2 + B2
	private Arr[] mx_T;				// T(J,:) = [ 1 ]  ,  T(I,:) = -T A2 
//	private Arr[] mx_negTA2; 		// -T A2
	private int[] ixI;				// I
	private int[] ixJ;				// J
	
	public TransformedInequalityCone(PolyhedralCone<Num, Arr> toTransform) {
		super(toTransform.getLinAlgOperations());
		this.parent = toTransform;
		transform();
	}

	public PolyhedralCone<Num, Arr> getParentCone() {
		return parent;
	}
	
	public PolyhedralCone<Num, Arr> getOriginalCone() {
		return TransformHelper.getOriginalCone(this);
	}

	public Arr[] getB() {
		return mx_B1_negTA1_B2;
	}

	public Num getB(int row, int col) {
		return getLinAlgOperations().get(mx_B1_negTA1_B2, row, col);
	}

	public int getRowCountB() {
		return getArrayOperations().getRowCount(mx_B1_negTA1_B2);
	}

	public int getDimensions() {
		return getArrayOperations().getColumnCount(mx_B1_negTA1_B2);
	}

	public ArrayOperations<Arr> getArrayOperations() {
		return getLinAlgOperations().getArrayOperations();
	}

//	/**
//	 * Returns the back-transformed ray <code>x</code>, that is, 
//	 * <code>x2</code>, a ray of this transformed cone, is transformed back to a 
//	 * ray of the original cone as follows (I and J are defined in the 
//	 * {@link TransformedInequalityCone class comment}):
//	 * <pre>
//	 * x<sub>J</sub> = x2
//	 * x<sub>I</sub> = -T A2 x2<sub>J</sub>
//	 * </pre>
//	 * 
//	 * @param x2 a ray of this transformed cone
//	 * @return the ray of the original cone
//	 */
//	public Arr transformToParent(Arr x2) {
//		final LinAlgOperations<Num, Arr> linalg = getLinAlgOperations();
//		final ArrayOperations<Arr> aops = getArrayOperations();
//		final Arr x1 = linalg.multiply(mx_negTA2, x2);
//		final Arr x = aops.newVector(ixI.length + ixJ.length);
//		for (int i = 0; i < ixI.length; i++) {
//			aops.copyVectorElements(x1, i, x, ixI[i], 1);
//		}
//		for (int j = 0; j < ixJ.length; j++) {
//			aops.copyVectorElements(x2, j, x, ixJ[j], 1);
//		}
//		return normalize(x);
//	}
	
	private Arr[] createT(Arr[] mxNegTA2, int[] ixI, int[] ixJ) {
		final int cols = ixJ.length;
		final Arr[] mxT = arrayOps.newMatrix(ixI.length + ixJ.length, ixJ.length);
		for (int i = 0; i < ixI.length; i++) {
			arrayOps.copyMatrixRowElements(mxNegTA2, i, 0, mxT, ixI[i], 0, cols);
		}
		for (int i = 0; i < ixJ.length; i++) {
			for (int j = 0; j < cols; j++) {
				numberArrayOps.setAll(mxT[ixJ[i]], numberOps.zero());
			}
			numberArrayOps.set(mxT[ixJ[i]], i, numberOps.one());
		}
		return mxT;
	}
	
	/**
	 * Returns the matrix {@code T = -T A2}, that is, the back-transformed ray 
	 * <code>x</code> is derived from a ray <code>x2</code> of this cone as
	 * follows: 
	 * <pre>
	 *   x<sub>J</sub> = x2
	 *   x<sub>I</sub> = -T A2 x2
	 * </pre>
	 * The returned matrix {@code T} performs the whole back-transformation, 
	 * that is,
	 * <pre>
	 * x = T x2
	 * </pre>
	 * This is achieved by setting
	 * <pre>
	 *   uI(I(i)) = i;
	 *   uJ(J(i)) = i;
	 *   T<sub>uJ</sub> = 1
	 *   T<sub>uI</sub> = -T A2
	 * </pre>
	 * 
	 * @return the transformation matrix {@code T}
	 */
	public Arr[] getTransformationMatrixToParent() {
		return mx_T;
	}
	
	public Arr transformToOriginal(Arr x2) {
		return TransformHelper.transformToOriginal(this, mxTtoOriginal, x2);
	}
	
	/**
	 * See {@link TransformedInequalityCone class comments} for transformation
	 * details
	 */
	private void transform() {
		final LinAlgOperations<Num, Arr> linalg = getLinAlgOperations();
		final ArrayOperations<Arr> aops = getArrayOperations();
		final Arr[] mxA = parent.getA();
		final Arr[] mxB = parent.getB();
		final int rows	= aops.getRowCount(mxA);
		final int cols	= aops.getColumnCount(mxA);
		final int[] rowmap 	= new int[rows];
		final int[] colmap 	= new int[cols];
		final int[] ptrRank	= new int[1];
		final Arr[] mxT  = linalg.invertMaximalSubmatrix(mxA, rowmap, colmap, ptrRank);
		final int rank = ptrRank[0];
		final int[] ixR = rank == rows ? rowmap : getIndexSet(rowmap, rank);
		ixI = getIndexSet(colmap, rank);
		ixJ = getComplementSet(colmap, rank);
		//final Arr[] mxA1 = ops.subMatrix(mxA, rowmap, ixI);
		final Arr[] mxA2 = aops.copyOfSubMatrix(mxA, ixR, ixJ);
		final Arr[] mxB1 = aops.copyOfColumnSubMatrix(mxB, ixI);
		final Arr[] mxB2 = aops.copyOfColumnSubMatrix(mxB, ixJ);
		
		//some tracing
		if (Loggers.isLoggable(LogPkg.LOGGER, Level.FINE)) {
			LogPkg.LOGGER.fine("matrix " + aops.getMatrixSignatureString("A", mxA) + " has rank " + rank);
			if (rank < rows) {
				LogPkg.LOGGER.fine("using non-redundant rows of A: " + Arrays.toString(ixR));
			}
			LogPkg.LOGGER.fine("I: " + Arrays.toString(ixI));
			LogPkg.LOGGER.fine("J: " + Arrays.toString(ixJ));

			if (Loggers.isLoggable(LogPkg.LOGGER, Level.FINEST)) {
				final Arr[] mxA1 = aops.copyOfSubMatrix(mxA, rowmap, ixI);
				final LogPrintWriter finestWriter = new LogPrintWriter(LogPkg.LOGGER, Level.FINEST);
				aops.printMatrix(finestWriter, "T", mxT);
				aops.printMatrix(finestWriter, "A1", mxA1);
				aops.printMatrix(finestWriter, "A2", mxA2);
				aops.printMatrix(finestWriter, "B1", mxB1);
				aops.printMatrix(finestWriter, "B2", mxB2);					
				finestWriter.flush();
			}
		}
		
		final Arr[] mx_negT = linalg.negate(mxT);
		
		// -T A2
		final Arr[] mx_negTA2 = normalize(linalg.multiply(mx_negT, mxA2));
		mx_T = normalize(createT(mx_negTA2, ixI, ixJ));
		
		// -B1 T A2 + B2
		final Arr[] mx_negTA1 = linalg.multiply(linalg.multiply(mxB1, mx_negT), mxA2);
		mx_B1_negTA1_B2 = normalize(linalg.add(mx_negTA1, mxB2));
		
		//some more tracing
		if (Loggers.isLoggable(LogPkg.LOGGER, Level.FINE)) {
			final int r_negTA2 = aops.getRowCount(mx_negTA2);
			final int c_negTA2 = aops.getColumnCount(mx_negTA2);
			final int r_B1_negTA1_B2 = aops.getRowCount(mx_B1_negTA1_B2);
			final int c_B1_negTA1_B2 = aops.getColumnCount(mx_B1_negTA1_B2);

			LogPkg.LOGGER.fine("transformed cone: P = { x1 = -T A2 x2  ,  ( -B1 T A2 + B2 ) x2 >= 0  ;  (-T A2):" + r_negTA2 + "x" + c_negTA2 + " , (-B1 T A2 + B2):" + r_B1_negTA1_B2 + "x" + c_B1_negTA1_B2 + " }");

			if (Loggers.isLoggable(LogPkg.LOGGER, Level.FINER)) {
				final LogPrintWriter finerWriter = new LogPrintWriter(LogPkg.LOGGER, Level.FINER);
				aops.printMatrix(finerWriter, "(-T A2)", mx_negTA2);
				aops.printMatrix(finerWriter, "(-B1 T A2 + B2)", mx_B1_negTA1_B2);
				finerWriter.flush();
			}
		}
	}
	/**
	 * Returns the first {@code rank} elements of {@code set} as new array
	 */
	private static int[] getIndexSet(int[] set, int rank) {
		final int[] ind = new int[rank];
		System.arraycopy(set, 0, ind, 0, rank);
		return ind;
	}
	/**
	 * Returns the last {@code |set|-rank} elements of {@code set} as new array
	 */
	private static int[] getComplementSet(int[] set, int rank) {
		final int[] ind = new int[set.length - rank];
		System.arraycopy(set, rank, ind, 0, ind.length);
		return ind;
	}
}
