/*
    This file is part of J-Varo.
    Authors: Elodie Cassan, Christophe Djemiel, Thomas Faux, Aurélie Lelièvre, Lucas Marmiesse

    J-Varo is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    J-Varo is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with J-Varo.  If not, see <http://www.gnu.org/licenses/>.
 */

package model.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.Metabolite;
import model.Reaction;
import controler.Var;

public class ExportTlp {

	public ExportTlp(String outName, List<Metabolite> listMet,
			List<Reaction> listReac) throws IOException {

		File file = new File(outName);
		FileWriter fw = new FileWriter(file);
		PrintWriter pw = new PrintWriter(fw);
		pw.println("(tlp \"2.3\"");

		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Date date = new Date();

		pw.println("(date \"" + dateFormat.format(date) + "\")");
		pw.println("(comment \"Generated by bioinfos\")");

		// map to link a name to the ID of his node in the graph
		Map<String, Integer> nodeId = new HashMap<String, Integer>();
		int id = 0;

		int nbNodes = 0;
		for (Reaction reac : listReac) {
			nodeId.put(reac.getName(), id);
			id++;
			nbNodes++;
		}
		for (Metabolite metab : listMet) {
			nodeId.put(metab.getName(), id);
			id++;
			nbNodes++;
		}

		pw.println("(nb_nodes " + nbNodes + ")");
		pw.println("(nodes 0.." + (nbNodes - 1) + ")");

		int nbEdges = 0;
		for (Reaction reac : listReac) {
			for (Metabolite met : reac.getListMetabolite()) {
				// if the metabolite is in the list
				if (nodeId.containsKey(met.getName())) {
					nbEdges++;
				}
			}
		}
		pw.println("(nb_edges " + nbEdges + ")");

		int idEdge = 0;
		for (Reaction reac : listReac) {

			for (Metabolite reactant : reac.getListReactants()) {

				// if the metabolite is in the list
				if (nodeId.containsKey(reactant.getName())) {
					pw.println("(edge " + idEdge + " "
							+ nodeId.get(reactant.getName()) + " "
							+ nodeId.get(reac.getName()) + ")");
					idEdge++;
				}
			}
			for (Metabolite product : reac.getListProducts()) {
				// if the metabolite is in the list
				if (nodeId.containsKey(product.getName())) {
					pw.println("(edge " + idEdge + " "
							+ nodeId.get(reac.getName()) + " "
							+ nodeId.get(product.getName()) + ")");
					idEdge++;
				}
			}
		}

		// Add labels on nodes
		pw.println("(property  0 string \"viewLabel\"");
		pw.println("(default \"\" \"\")");
		for (Entry<String, Integer> entry : nodeId.entrySet()) {
			pw.println("(node " + entry.getValue() + " \"" + entry.getKey()
					+ "\")");

		}
		pw.println(")");
		// sizes of nodes and edges
		pw.println("(property  0 size \"viewSize\"");
		pw.println("(default \"(13,13,5)\" \"(0.125,0.125,0)\")");
		pw.println(")");

		// size of labels
		pw.println("(property  0 int \"viewFontSize\"");
		pw.println("(default \"5\" \"5\")");
		pw.println(")");

		// Position of labels
		pw.println("(property  0 int \"viewLabelPosition\"");
		pw.println("(default \"1\" \"1\")");
		pw.println(")");

		// shape nodes
		pw.println("(property  0 int \"viewShape\"");
		pw.println("(default \"2\" \"0\")");

		// reactions in square shape
		for (Reaction reac : listReac) {
			pw.println("(node " + nodeId.get(reac.getName()) + " " + "\"4\")");

		}
		pw.println(")");
		//
		// different color for reactions and metabolites
		pw.println("(property  0 color \"viewColor\"");
		pw.println("(default \"(102,178,255,255)\" \"(0,0,0,255)\")");
		// reactions color
		for (Reaction reac : listReac) {
			if (reac.isReversible()) {
				pw.println("(node " + nodeId.get(reac.getName()) + " " + "\"("
						+ Var.reacRevColor.getRed() + ","
						+ Var.reacRevColor.getGreen() + ","
						+ Var.reacRevColor.getBlue() + ","
						+ Var.reacRevColor.getAlpha() + ")\")");
			} else {
				pw.println("(node " + nodeId.get(reac.getName()) + " " + "\"("
						+ Var.reacIrrevColor.getRed() + ","
						+ Var.reacIrrevColor.getGreen() + ","
						+ Var.reacIrrevColor.getBlue() + ","
						+ Var.reacIrrevColor.getAlpha() + ")\")");
			}

		}
		// metab colors
		for (Metabolite metab : listMet) {
			if (metab.isInternal()) {
				pw.println("(node " + nodeId.get(metab.getName()) + " " + "\"("
						+ Var.metabIntColor.getRed() + ","
						+ Var.metabIntColor.getGreen() + ","
						+ Var.metabIntColor.getBlue() + ","
						+ Var.metabIntColor.getAlpha() + ")\")");
			} else {
				pw.println("(node " + nodeId.get(metab.getName()) + " " + "\"("
						+ Var.metabExtColor.getRed() + ","
						+ Var.metabExtColor.getGreen() + ","
						+ Var.metabExtColor.getBlue() + ","
						+ Var.metabExtColor.getAlpha() + ")\")");
			}
		}
		pw.println(")");
		//
		pw.println(")");
		pw.close();
	}
}
