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
package ch.javasoft.polco.callback.hdf5;

import java.io.File;
import java.io.IOException;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;
import ch.javasoft.math.array.ArrayOperations;
import ch.javasoft.math.array.Converter;
import ch.javasoft.math.array.NumberArrayOperations;
import ch.javasoft.math.operator.impl.DoubleOperators;
import ch.javasoft.polco.EqualityPolyhedralCone;
import ch.javasoft.polco.InequalityPolyhedralCone;
import ch.javasoft.polco.PolyhedralCone;
import ch.javasoft.polco.xenum.ExtremeRayCallback;
import ch.javasoft.polco.xenum.ExtremeRayEvent;
import ch.javasoft.util.ExceptionUtil;
import ch.javasoft.util.numeric.Zero;

/**
 * The <code>Hdf5Callback</code> writes the output to a hdf5 file. The file 
 * contains the rays in a data set {@code "R"}, each ray is a column of the
 * matrix.
 * <p>
 * See
 * <a href="http://www.hdfgroup.org">http://www.hdfgroup.org</a>
 * 
 * @type Num	the number type of a single number
 * @type Arr	the number type of an array of numbers
 */
public class Hdf5Callback<Num extends Number, Arr> implements ExtremeRayCallback<Num, Arr> {

	private final H5File file;
	private final int gzipLevel;
	private final int rowsPerChunk;
	
	private Dataset 								dataset;
	private Converter<Num, Arr, Double, double[]>	converter;	
	private double[]								buffer;
	
	/**
	 * Constructor with hdf5 file to be created. Existing files are deleted.
	 */
	public Hdf5Callback(File file, int gzipLevel, int rowsPerChunk) throws IOException {
		if (file.exists()) {
			file.delete();
		}
		
		this.file = new H5File(file.getAbsolutePath(), HDF5Constants.H5F_ACC_TRUNC);
		this.gzipLevel 		= gzipLevel;
		this.rowsPerChunk	= rowsPerChunk;
	}
	
	public boolean initialize(ExtremeRayEvent<Num, Arr> event) throws IOException {
		final PolyhedralCone<Num, Arr> cone = event.getPolyhedralCone();
        final NumberArrayOperations<Num, Arr> naops = cone.getLinAlgOperations().getNumberArrayOperations();
		try {
			final H5Datatype typeUInt32	= new H5Datatype(H5.H5Tcopy(HDF5Constants.H5T_STD_U32LE));
			final H5Datatype typeMatrix	= getH5MatrixType(naops.numberClass());
			converter = getDoubleConverter(naops);
			
			final int dim 		= cone.getDimensions();
			final long rays		= event.getRayCount();
			final long[] dim1	= new long[]{1};
			final long[] rDims	= new long[]{rays, dim};

			//compute rows in buffer and create buffer
			buffer = new double[getBufferRows(cone, rays) * dim];

			final long[] rChunk	= getChunk(rays, dim);

			final String desc = cone.toString() + " = { x = R' c , R:" + rDims[0] + "x" + rDims[1] + "   for some c >= 0 }";
			final H5Datatype typeString	= new H5Datatype(H5Datatype.CLASS_STRING, desc.length(), H5Datatype.ORDER_LE, H5Datatype.SIGN_NONE);
			file.createScalarDS("description", null, typeString, dim1, null, null, 0, new String[] {desc});
			file.createScalarDS("dim", null, typeUInt32, dim1, null, null, 0, new int[] {cone.getDimensions()});
			if (!(cone instanceof InequalityPolyhedralCone)) {
				writeMatrix(typeMatrix, "A", cone.getA());
			}
			if (!(cone instanceof EqualityPolyhedralCone)) {
				writeMatrix(typeMatrix, "B", cone.getB());
			}
			dataset = file.createScalarDS("R", null, typeMatrix, rDims, rDims, rChunk, gzipLevel, null);
			dataset.init();
		} 
		catch (Exception e) {
			throw ExceptionUtil.toRuntimeExceptionOr(IOException.class, e);
		}
		return true;//ray output desired
	}
	
	private int getBufferRows(PolyhedralCone<Num, Arr> cone, long rayCount) {
		long bufferRows = rayCount;
		if (!(cone instanceof InequalityPolyhedralCone)) {
			bufferRows = Math.max(bufferRows, cone.getRowCountA());
		}
		if (!(cone instanceof EqualityPolyhedralCone)) {
			bufferRows = Math.max(bufferRows, cone.getRowCountB());
		}
		return (int)Math.min(bufferRows, rowsPerChunk);
	}
	private long[] getChunk(long rows, long cols) {
		return new long[] {Math.min(rows, rowsPerChunk), cols};
	}

	private H5Datatype getH5MatrixType(Class<Num> numberClass) throws HDF5LibraryException {
//		if (BigInteger.class.equals(numberClass)) {
//			return new H5Datatype(H5.H5Tcopy(HDF5Constants.H5T_STD_U64LE));
//		}
		//default: double
		return new H5Datatype(H5.H5Tcopy(HDF5Constants.H5T_IEEE_F64LE));
	}

	private Converter<Num, Arr, Double, double[]> getDoubleConverter(final NumberArrayOperations<Num, Arr> naops) {
		return new DoubleOperators(new Zero()).getNumberArrayOperations().getConverterFrom(naops);
	}
	
	private void writeConvertedBuffered(Dataset ds, Arr vec, long index, long count, int length) throws Exception {
		final int offset = (int)((index % rowsPerChunk) * length);
		converter.convertVector(vec, 0, buffer, offset, length);

		final long rowsToWrite;
		if (index + 1 == count) {
			rowsToWrite = count % rowsPerChunk;
		}
		else if (offset + length < buffer.length) {
			rowsToWrite = 0;
		}
		else {
			rowsToWrite = rowsPerChunk;
		}

		if (rowsToWrite > 0) {
			// start, stride and sizes will determined the selected subset
			final long[] start = ds.getStartDims();
	        final long[] stride = ds.getStride();
	        final long[] sizes = ds.getSelectedDims();
	        start[0] = rowsPerChunk * (index / rowsPerChunk);
	        start[1] = 0;
	        sizes[0] = rowsToWrite;
	        sizes[1] = length;
	        stride[0] = 1;
	        stride[1] = 1;

	        ds.write(buffer);
		}
	}

	private void writeMatrix(H5Datatype dataType, String varName, Arr[] matrix) throws Exception {
        final ArrayOperations<Arr> aops = converter.getNumberArrayOperationsInput().getArrayOperations();
		final int rows = aops.getRowCount(matrix);
		final int cols = aops.getColumnCount(matrix);
		
		final long[] dims = new long[] {rows, cols};
		final long[] rChunk	= getChunk(rows, cols);
		
		final Dataset ds = file.createScalarDS(varName, null, dataType, dims, dims, rChunk, gzipLevel, null);

		ds.init();
		for (int r = 0; r < rows; r++) {
			writeConvertedBuffered(ds, matrix[r], r, rows, cols);
		}
	}
	
	public void outputExtremeRay(ExtremeRayEvent<Num, Arr> event, long index, Arr extremeRay) throws IOException {
		try {
			writeConvertedBuffered(dataset, extremeRay, index, event.getRayCount(), event.getPolyhedralCone().getDimensions());
		} 
		catch (Exception e) {
			throw ExceptionUtil.toRuntimeExceptionOr(IOException.class, e);
		}
	}

	public void terminate(ExtremeRayEvent<Num, Arr> event) throws IOException {
		try {
			file.close();
			buffer	= null;
			dataset = null;
		} 
		catch (HDF5Exception e) {
			throw ExceptionUtil.toRuntimeExceptionOr(IOException.class, e);
		}
	}

}
