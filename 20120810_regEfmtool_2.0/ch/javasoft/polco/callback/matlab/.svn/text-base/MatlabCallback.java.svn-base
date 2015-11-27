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
package ch.javasoft.polco.callback.matlab;

import java.io.File;
import java.io.IOException;

import ch.javasoft.jsmat.MatFileWriter;
import ch.javasoft.jsmat.ReservedMatrixWriter;
import ch.javasoft.jsmat.variable.MatCharMatrix;
import ch.javasoft.jsmat.variable.MatDoubleMatrix;
import ch.javasoft.jsmat.variable.MatReservedMatrix;
import ch.javasoft.math.array.Converter;
import ch.javasoft.math.array.NumberArrayOperations;
import ch.javasoft.math.operator.impl.DoubleOperators;
import ch.javasoft.polco.EqualityPolyhedralCone;
import ch.javasoft.polco.InequalityPolyhedralCone;
import ch.javasoft.polco.PolyhedralCone;
import ch.javasoft.polco.xenum.ExtremeRayCallback;
import ch.javasoft.polco.xenum.ExtremeRayEvent;
import ch.javasoft.util.numeric.Zero;

/**
 * The <code>MatlabCallback</code> writes the output to a matlab .mat file. The 
 * file contains a description string and the matrices {@code A}, {@code B} and 
 * <code>R<sup>T</sup></code>.
 * 
 * @type Num	the number type of a single number
 * @type Arr	the number type of an array of numbers
 */
public class MatlabCallback<Num extends Number, Arr> implements ExtremeRayCallback<Num, Arr> {

	private final MatFileWriter matFileWriter;

	private Converter<Num, Arr, Double, double[]>	converter;	
	private ReservedMatrixWriter<double[]> 			rayWriter;
	
	/**
	 * Constructor with file to write to
	 * @throws IOException 
	 */
	public MatlabCallback(File file) throws IOException {
		matFileWriter = new MatFileWriter(file);
	}
	
	public boolean initialize(ExtremeRayEvent<Num, Arr> event) throws IOException {
		final PolyhedralCone<Num, Arr> cone = event.getPolyhedralCone();
		final NumberArrayOperations<Num, Arr> naops = cone.getLinAlgOperations().getNumberArrayOperations();
		final int dims = cone.getDimensions();
		
		converter = getDoubleConverter(naops);

		long bytes = 0;
		
		//write description
		final MatCharMatrix desc = new MatCharMatrix(cone.toString() + " = { x = R c , R:" + cone.getDimensions() + "x" + event.getRayCount() + "   for some c >= 0 }");
		matFileWriter.write("description", desc);
		bytes += desc.getRawDataSize();
		
		//write A if necessary
		if (!(cone instanceof InequalityPolyhedralCone)) {
			final MatDoubleMatrix mat = new MatDoubleMatrix(converter.convertMatrix(cone.getA()));
			matFileWriter.write("A", mat);
			bytes += mat.getRawDataSize();
		}
		//write B if necessary
		if (!(cone instanceof EqualityPolyhedralCone)) {
			final MatDoubleMatrix mat = new MatDoubleMatrix(converter.convertMatrix(cone.getB()));
			matFileWriter.write("B", mat);
			bytes += mat.getRawDataSize();
		}
		
		//prepare ray writer
		bytes += event.getRayCount() * dims + 8 + 2;
		bytes += 1<<16;/*leave some extra*/
		if (bytes > (2L * Integer.MAX_VALUE)) {
			throw new IOException("too many bytes (" + bytes + " > 4G), use hdf5 instead");
		}
		
		final MatReservedMatrix<double[]> mat = MatReservedMatrix.createDoubleMatrix(dims, /*FIXME*/(int)event.getRayCount());
		rayWriter = matFileWriter.createReservedWriter("R", mat);  
		
		return true;//ray output desired
	}

	public void outputExtremeRay(ExtremeRayEvent<Num, Arr> event, long index, Arr extremeRay) throws IOException {
		final NumberArrayOperations<Num, Arr> naops = event.getPolyhedralCone().getLinAlgOperations().getNumberArrayOperations();
		writeVector(rayWriter, naops, extremeRay);
	}

	public void terminate(ExtremeRayEvent<Num, Arr> event) throws IOException {
		rayWriter.close();
		rayWriter = null;
		matFileWriter.close();
	}
	
	private void writeVector(ReservedMatrixWriter<double[]> writer, NumberArrayOperations<Num, Arr> naops, Arr vector) throws IOException {
		final double[] ray = converter.convertVector(vector);
		writer.append(ray);
	}

	private Converter<Num, Arr, Double, double[]> getDoubleConverter(final NumberArrayOperations<Num, Arr> naops) {
		return new DoubleOperators(new Zero()).getNumberArrayOperations().getConverterFrom(naops);
	}
}
