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
import ch.javasoft.math.array.NumberArrayOperations;
import ch.javasoft.math.array.NumberOperators;
import ch.javasoft.math.linalg.LinAlgOperations;
import ch.javasoft.math.operator.AggregatingUnaryOperator;
import ch.javasoft.math.operator.BinaryOperator;
import ch.javasoft.math.operator.BooleanUnaryOperator;
import ch.javasoft.math.operator.UnaryOperator;
import ch.javasoft.polco.EqualityPolyhedralCone;
import ch.javasoft.polco.InequalityPolyhedralCone;
import ch.javasoft.polco.PolyhedralCone;
import ch.javasoft.polco.impl.AbstractEqualityCone;
import ch.javasoft.util.logging.LogPrintWriter;
import ch.javasoft.util.logging.Loggers;

/**
 * The <code>TransformedEqualityCone</code> is an {@link EqualityPolyhedralCone} 
 * derived from a general {@link PolyhedralCone}. Extreme rays of this cone can 
 * be transformed back to extreme rays of the original cone.
 * <p>
 * More formally, a polyhedral cone <i>P</i> in inequality form, defined as the 
 * intersection of halfspaces, is transformed into a polyhedral
 * cone <i>P'</i> in equality form, defined only by hyperplanes and nonzero 
 * variables, where
 * <pre>
 * P  = { B x &ge; 0 }
 * P' = { A x2 = 0 , x2 &ge; 0 }
 * </pre>
 * An {@link TransformedInequalityCone inequality transformation} is applied if
 * necessary to get the inequality form from a general polyhedral cone 
 * <i>P''</i>, defined as the intersection of hyperplanes and halfspaces, that 
 * is,
 * <pre>
 * P'' = { A'' x1 = 0 , B'' x1 &ge; 0 }
 * </pre>
 * <p>
 * <b>Transformation:</b><br>
 * <pre>
 * choose      T   = [ T1 T2 ], T1 square
 * such that   T B = [ I ; 0 ]
 * 
 * then
 * 
 *   P = {   B x &ge; 0 }
 *     = {   B x - I x2 = 0  ,  x2 &ge 0 }
 *     = { T B x - T x2 = 0  ,  x2 &ge 0 }
 *     = { x = T1 x2  ,  T2 x2 = 0 ,  x2 &ge; 0 }
 * </pre>
 * <p>
 * <b>Note:</b> See {@link #isDiagMultipleOne(Object[], Object[], int, int, int, int[], Object) isDiagMultipleOne(..)}
 * 				for special treatment if integer arithmetic is used
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
public class TransformedEqualityCone<Num extends Number, Arr> extends AbstractEqualityCone<Num, Arr> implements TransformedPolyhedralCone<Num, Arr> {
	
	private final InequalityPolyhedralCone<Num, Arr>	parent;
	private final AtomicReference<Arr[]> 				mxTtoOriginal = new AtomicReference<Arr[]>();
	
	private Arr[] mxT1;
	private Arr[] mxT2;
	
	public TransformedEqualityCone(PolyhedralCone<Num, Arr> toTransform) {
		this(toTransform instanceof InequalityPolyhedralCone ? (InequalityPolyhedralCone<Num, Arr>)toTransform : new TransformedInequalityCone<Num, Arr>(toTransform));
	}
	public TransformedEqualityCone(InequalityPolyhedralCone<Num, Arr> toTransform) {
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

	public int getDimensions() {
		return parent.getRowCountB();
	}

	public ArrayOperations<Arr> getArrayOperations() {
		return getLinAlgOperations().getArrayOperations();
	}
	
	public Arr[] getA() {
		return mxT2;
	}
	
	public Num getA(int row, int col) {
		final LinAlgOperations<Num, Arr> linalg = getLinAlgOperations();
		return linalg.getNumberArrayOperations().get(mxT2, row, col);
	}
	
	public int getRowCountA() {
		final LinAlgOperations<Num, Arr> linalg = getLinAlgOperations();
		return linalg.getArrayOperations().getRowCount(mxT2);
	}

	/**
	 * Returns the matrix {@code T1}, that is, the back-transformed ray 
	 * <code>x</code> is derived from a ray <code>x2</code> of this cone as
	 * follows: 
	 * <pre>
	 * x = T1 x2
	 * </pre>
	 * 
	 * @return the transformation matrix {@code T1}
	 */
	public Arr[] getTransformationMatrixToParent() {
		return mxT1;
	}
	
	public Arr transformToOriginal(Arr x2) {
		return TransformHelper.transformToOriginal(this, mxTtoOriginal, x2);
	}
	
	/**
	 * See {@link TransformedEqualityCone class comments} for transformation
	 * details
	 */
	private void transform() {
		final boolean logFine 	= Loggers.isLoggable(LogPkg.LOGGER, Level.FINE);
		final boolean logFiner	= logFine && Loggers.isLoggable(LogPkg.LOGGER, Level.FINER);
		final boolean logFinest	= logFiner && Loggers.isLoggable(LogPkg.LOGGER, Level.FINEST);
		final LinAlgOperations<Num, Arr> linalg = getLinAlgOperations();
		final ArrayOperations<Arr> aops = linalg.getArrayOperations();
		final NumberOperators<Num, Arr> nops = linalg.getNumberOperators();
		final NumberArrayOperations<Num, Arr> naops = linalg.getNumberArrayOperations();
		final BinaryOperator<Num, Arr> mul = nops.binary(BinaryOperator.Id.multiply);
		final BinaryOperator<Num, Arr> div = nops.binary(BinaryOperator.Id.divide);
		final AggregatingUnaryOperator<Num, Arr> nrmDiv = nops.aggregatingUnary(AggregatingUnaryOperator.Id.normDivisor);
		final BooleanUnaryOperator<Num, Arr> isOne = nops.booleanUnary(BooleanUnaryOperator.Id.isOne);
		final BooleanUnaryOperator<Num, Arr> isZero = nops.booleanUnary(BooleanUnaryOperator.Id.isZero);
		
		final int dims  = parent.getDimensions();
		final int rows = parent.getRowCountB();
		final Arr[] mxB = parent.getB();
		// T B = [ I ; 0 ]
		//  mx = [ B 1 ]
		final Arr[] mx  = naops.newZeroMatrix(rows, rows + dims);		
		aops.copyMatrixElements(mxB, 0, 0, mx, 0, 0, rows, dims);
		for (int i = 0; i < rows; i++) {
			naops.set(mx, i, i + dims, naops.getNumberOperators().one());
		}
		// rref(mx) = [ 1  M ] = [ [ 1 ; 0 ] T ]   
		final int[] rowmap = new int[rows];
		final int[] colmap = new int[dims];
		final int[] ptRank = new int[1];
		final Arr[] rref = linalg.rowEchelon(mx, true /*reduced*/, rowmap, colmap, ptRank);
		final int rank = ptRank[0]; 
		if (logFine) {
			LogPkg.LOGGER.fine("rank: " + rank);
			LogPkg.LOGGER.fine("rowmap: " + Arrays.toString(rowmap));
			LogPkg.LOGGER.fine("colmap: " + Arrays.toString(colmap));
			if (logFinest) {
				final LogPrintWriter finestWriter = new LogPrintWriter(LogPkg.LOGGER, Level.FINEST);
				aops.printMatrix(finestWriter, "rref", rref);
				finestWriter.flush();
			}
		}
		if (rank < dims) {
			LogPkg.LOGGER.info("cone is not pointed, B has not full column rank: " + rank + "<" + dims);
			LogPkg.LOGGER.warning("the " + (dims - rank) + " linealities of this unpointed cone are not reported");
//			throw new RuntimeException("cone is not pointed since matrix has not full column rank, " + ptRank[0] + 
//					" < " + dims);
//			final Arr[] lineality = getLinealityAndFixColMap(colmap, rank);
//			if (logFinest) {
//				final LogPrintWriter finestWriter = new LogPrintWriter(LogPkg.LOGGER, Level.FINEST);
//				aops.printMatrix(finestWriter, "lineality", lineality);
//				finestWriter.flush();
//				LogPkg.LOGGER.finest("fixed colmap: " + Arrays.toString(colmap));
//			}
		}
		// T1 = rref(rmap(1:rank),     cmap(rank+1:end))   
		// T2 = rref(rmap(rank+1:end), cmap(rank+1:end))   
		final Arr[] mxT1 = aops.newMatrix(dims, rows);
		final Arr[] mxT2 = aops.newMatrix(rows - rank, rows);
		if (logFine) {
			LogPkg.LOGGER.fine("transformed cone: P = { x = T1 x2  ,  T2 x2 = 0 , x2 >= 0  ;  T1:" + dims + "x" + rows + " , T2:" + (rows-rank) + "x" + rows + " }");
		}
		
		final Arr ptrDiagMul = aops.newVector(1);
		final Arr vecDiag	 = isDiagMultipleOne(rref, mxB, rank, 0, dims, colmap, ptrDiagMul);//T*B
		final boolean diagIsIdent = isOne.booleanOperate(ptrDiagMul, 0);
		for (int c = 0; c < rows; c++) {
			//matrix T1
			for (int r = 0; r < rank; r++) {
				aops.copyMatrixElement(rref, r, c + dims, mxT1, colmap[r], rowmap[c]);				
				if (!diagIsIdent) {
					// T B = [ I ; 0 ] is now [ D ; 0 ]
					// thus, we compute
					// T1' = inv(D) T1
					mul.operate(mxT1[colmap[r]], rowmap[c], ptrDiagMul, 0, mxT1[colmap[r]], rowmap[c]);
					div.operate(mxT1[colmap[r]], rowmap[c], vecDiag, r, mxT1[colmap[r]], rowmap[c]);
				}
			}
			//matrix T1: linealities lead to zero rows
			for (int r = rank; r < dims; r++) {
				naops.set(mxT1, colmap[r], rowmap[c], nops.zero());
			}
			
			//matrix T2
			for (int r = rank; r < rows; r++) {
				//NOTE colmap is too short, ERROR for instance with ccc7 !! 
				//aops.copyMatrixElement(rref, r, c + dims, mxT2, colmap[r - rank], rowmap[c]);
				aops.copyMatrixElement(rref, r, c + dims, mxT2, r - rank, rowmap[c]);
			}
		}
		
		//normalize matrices T1 and T2
		if (nops.getDivisionSupport().isExact()) {
			//normalize T1: divide by matrix-global norm divisor
			final Arr ptrTmp = naops.newZeroVector(1);
			nrmDiv.operate(mxT1, 0, 0, dims, rows, ptrTmp, 0);	
			if (!isOne.booleanOperate(ptrTmp, 0) && !isZero.booleanOperate(ptrTmp, 0)) {
				for (int r = 0; r < rank; r++) {
					for (int c = 0; c < rows; c++) {
						div.operate(mxT1[r], c, ptrTmp, 0, mxT1[r], c);					
					}
				}
			}
			//normalize T2: divide row elements by row norm divisor 
			for (int r = 0; r < rows - rank; r++) {
				nrmDiv.operate(mxT2[r], 0, rows, ptrTmp, 0);	
				if (!isOne.booleanOperate(ptrTmp, 0) && !isZero.booleanOperate(ptrTmp, 0)) {
					for (int c = 0; c < rows; c++) {
						div.operate(mxT2[r], c, ptrTmp, 0, mxT2[r], c);					
					}					
				}
			}
		}
		normalize(mxT1);//reduce fractions or round almost-zeros
		normalize(mxT2);//reduce fractions or round almost-zeros
		
		//some tracing
		if (logFiner) {
			final LogPrintWriter finerWriter = new LogPrintWriter(LogPkg.LOGGER, Level.FINER);
			if (logFinest) {
				final LogPrintWriter finestWriter = new LogPrintWriter(LogPkg.LOGGER, Level.FINEST);
				aops.printVector(finestWriter, "diag D", vecDiag, true);
				finestWriter.flush();
			}
			aops.printMatrix(finerWriter, "T1", mxT1);
			aops.printMatrix(finerWriter, "T2", mxT2);
			finerWriter.flush();
		}
		this.mxT1 = mxT1;
		this.mxT2 = mxT2;
	}
	
	/**
	 * If integer arithmetic is used, the matrix product {@code T B} does not 
	 * contain an identity matrix, but a positive diagonal matrix {@code D} 
	 * instead. More formally, instead of 
	 * <pre>
     *      T   = [ T1 T2 ], T1 square
     *      T B = [ I ; 0 ]
	 * </pre>
	 * we have
	 * <pre>
     *      T B = [ D ; 0 ], D(i,i) > 0 and D(i,j) = 0 for i &ne; j
	 * </pre>
	 * The back-transformation uses the equality
	 * <pre>
 	 *      T B x - T x2 = 0
	 * </pre>
	 * and instead of
	 * <pre>
	 *    x = T1 x2
	 * </pre>
	 * we get
	 * <pre>
	 *  D x = T1 x2
	 *    x = D<sup>-1</sup> T1 x2
	 * </pre>
	 * The method returns the diagonal of the matrix {@code D} and places the 
	 * least common multiple of the diagonal elements in {@code ptrDiagMul}, 
	 * that is, {@code ptrDiagMul[0] = LCM(diag(D))}. This product is used to 
	 * scale up elements of the matrix {@code T1} before dividing by elements
	 * of the diagonal (to compute the product <code>D<sup>-1</sup> T1</code>).
	 * This ensures that integer division is always exact. 
	 * 
	 * @param mxIdentT		the matrix containing {@code T} at the offsets
	 * 						specified below, contains an identity matrix
	 * 						at {@code (0,0)} if {@code D} is an identity matrix,
	 * 						and a positive diagonal matrix otherwise
	 * @param mxB			the matrix {@code B}
	 * @param rank			the rank of matrix B
	 * @param rowOffsetT	the row offset in {@code mxIdentT} where matrix 
	 * 						{@code T} begins
	 * @param colOffsetT	the column offset in {@code mxIdentT} where matrix 
	 * 						{@code T} begins
	 * @param colmap		the column mapping, has been used to compute 
	 * 						{@code T} and must be applied to matrix {@code B}
	 * @param ptrDiagMul	the out parameter, an array with 1 element, is set
	 * 						to the least common multiple of the diagonal 
	 * 						elements of {@code D}, that is, 
	 * 						{@code ptrDiagMul[0] = LCM(diag(D))}
	 * @return the diagonal {@code diag(D)} of the diagonal matrix {@code D}
	 */
	private Arr isDiagMultipleOne(Arr[] mxIdentT, Arr[] mxB, int rank, int rowOffsetT, int colOffsetT, int[] colmap, Arr ptrDiagMul) {
		final LinAlgOperations<Num, Arr> linalg = getLinAlgOperations();
		final NumberOperators<Num, Arr> nops = linalg.getNumberOperators();
		final ArrayOperations<Arr> aops = linalg.getArrayOperations();
		final NumberArrayOperations<Num, Arr> naops = linalg.getNumberArrayOperations();
		final BinaryOperator<Num, Arr> add = nops.binary(BinaryOperator.Id.add);
		final BinaryOperator<Num, Arr> mul = nops.binary(BinaryOperator.Id.multiply);
		final BinaryOperator<Num, Arr> div = nops.binary(BinaryOperator.Id.divide);
		final UnaryOperator<Num, Arr> nor = nops.unary(UnaryOperator.Id.normalize);
		final AggregatingUnaryOperator<Num, Arr> gcd = nops.aggregatingUnary(AggregatingUnaryOperator.Id.normDivisor);
		final BooleanUnaryOperator<Num, Arr> isOne = nops.booleanUnary(BooleanUnaryOperator.Id.isOne);

//		final int ilen = aops.getColumnCount(mxB);
		final int jlen = aops.getRowCount(mxB);
		
		//check if we have an identity matrix at mxIdentT(0,0)
		boolean allOne = true;
		for (int i = 0; i < rank && allOne; i++) {
			allOne &= isOne.booleanOperate(mxIdentT[i], i);
		}
		if (allOne) {
			naops.set(ptrDiagMul, 0, nops.one());
			return naops.newOneVector(rank);
		}
		
		//compute diagonal of matrix product T B
		final Arr ptrDiag = aops.newVector(rank); 
		final Arr ptrTmp = aops.newVector(2);
		naops.set(ptrTmp, 0, nops.one());
		for (int i = 0; i < rank; i++) {
			naops.set(ptrTmp, 1, nops.zero());
			for (int j = 0; j < jlen; j++) {
				mul.operate(mxIdentT[rowOffsetT + i], colOffsetT + j, mxB[j], colmap[i], ptrDiag, i);
				add.operate(ptrTmp, 1, ptrDiag, i, ptrTmp, 1);
				nor.operate(ptrTmp, 1, ptrTmp, 1);
			}
			aops.copyVectorElement(ptrTmp, 1, ptrDiag, i);
			gcd.operate(ptrTmp, 0, 2, ptrTmp, 1);
			div.operate(ptrDiag, i, ptrTmp, 1, ptrTmp, 1);
			mul.operate(ptrTmp, 0, ptrTmp, 1, ptrTmp, 0);
		}
		aops.copyVectorElement(ptrTmp, 0, ptrDiagMul, 0);
		if (isOne.booleanOperate(ptrDiagMul, 0)) {
			naops.set(ptrDiagMul, 0, nops.one());
		}
		return ptrDiag;
	}
	
}
