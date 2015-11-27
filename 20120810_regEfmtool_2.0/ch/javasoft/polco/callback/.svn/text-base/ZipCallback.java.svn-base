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
package ch.javasoft.polco.callback;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import ch.javasoft.math.array.NumberArrayOperations;
import ch.javasoft.polco.EqualityPolyhedralCone;
import ch.javasoft.polco.InequalityPolyhedralCone;
import ch.javasoft.polco.PolyhedralCone;
import ch.javasoft.polco.xenum.ExtremeRayCallback;
import ch.javasoft.polco.xenum.ExtremeRayEvent;

/**
 * The <code>ZipCallback</code> writes the output to a zip file. The zip file 
 * contains text file entries with a description and the matrices {@code A},
 * {@code B} and <code>R<sup>T</sup></code>. The matrix text files contain one
 * row per line, values are separated by tab.
 * 
 * @type Num	the number type of a single number
 * @type Arr	the number type of an array of numbers
 */
public class ZipCallback<Num extends Number, Arr> implements ExtremeRayCallback<Num, Arr> {

	private final ZipOutputStream zipStream;
	
	private PrintStream rayPrinter;
	
	/**
	 * Constructor with output stream to write to
	 */
	public ZipCallback(OutputStream out) {
		zipStream = new ZipOutputStream(out);
	}
	
	public boolean initialize(ExtremeRayEvent<Num, Arr> event) throws IOException {
		final PolyhedralCone<Num, Arr> cone = event.getPolyhedralCone();
		final NumberArrayOperations<Num, Arr> naops = cone.getLinAlgOperations().getNumberArrayOperations();
		
		
		openEntry("description");
		PrintStream print = new PrintStream(zipStream);
		print.println(cone.toString());
		print.println("P = { x = R' c , R:" + event.getRayCount() + "x" + cone.getDimensions() + "   for some c >= 0 }");
		print.flush();
		closeEntry();
		
		if (!(cone instanceof InequalityPolyhedralCone)) {
			openEntry("A");
			writeMatrix(naops, cone.getA());
			closeEntry();
		}
		if (!(cone instanceof EqualityPolyhedralCone)) {
			openEntry("B");
			writeMatrix(naops, cone.getB());
			closeEntry();
		}
		openEntry("R");
		rayPrinter = new PrintStream(zipStream);
		return true;//ray output desired
	}

	public void outputExtremeRay(ExtremeRayEvent<Num, Arr> event, long index, Arr extremeRay) throws IOException {
		final NumberArrayOperations<Num, Arr> naops = event.getPolyhedralCone().getLinAlgOperations().getNumberArrayOperations();
		final int len = naops.getArrayOperations().getLength(extremeRay);
		writeVector(rayPrinter, naops, extremeRay, len);
	}

	public void terminate(ExtremeRayEvent<Num, Arr> event) throws IOException {
		rayPrinter.flush();
		closeEntry();
		zipStream.close();
		rayPrinter = null;
	}
	
	private void writeMatrix(NumberArrayOperations<Num, Arr> naops, Arr[] matrix) throws IOException {
		final int rows = naops.getArrayOperations().getRowCount(matrix);
		final int cols = naops.getArrayOperations().getColumnCount(matrix);
		final PrintStream print = new PrintStream(zipStream);
		for (int r = 0; r < rows; r++) {
			writeVector(print, naops, matrix[r], cols);
		}
		print.flush();
	}
	private void writeVector(PrintStream print, NumberArrayOperations<Num, Arr> naops, Arr vector, int length) throws IOException {
		for (int i = 0; i < length; i++) {
			if (i > 0) print.print('\t');
			print.print(naops.get(vector, i));
		}
		print.println();
	}
	private void openEntry(String name) throws IOException {
		zipStream.putNextEntry(new ZipEntry(name));
	}
	private void closeEntry() throws IOException {
		zipStream.closeEntry();
	}

}
