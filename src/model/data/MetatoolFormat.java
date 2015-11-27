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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import model.MetabolicNetwork;
import model.MetabolicNetworkItf;
import model.Metabolite;
import model.Reaction;

public class MetatoolFormat implements Format {
	public MetabolicNetworkItf load(File[] file) {

		System.out.println("loading Metatool file : "
				+ file[0].getAbsolutePath());
		
		
		MetabolicNetwork network = new MetabolicNetwork();
		
		
		ArrayList<String> reactionEquations = new ArrayList<String>();
		String enzRevData = "", enzIrevData = "", metIntData = "",
				metExtData = "";

		try {
		BufferedReader in = new BufferedReader(new FileReader(file[0]));
		String input;
		input = in.readLine();


		String mode = "";

		while (input != null) {
			input = input.trim();
			if (input.equals("-ENZREV")) {
				mode = "ENZREV";
			} else if (input.equals("-ENZIRREV")) {
				mode = "ENZIRREV";
			} else if (input.equals("-METINT")) {
				mode = "METINT";
			} else if (input.equals("-METEXT")) {
				mode = "METEXT";
			} else if (input.equals("-CAT")) {
				mode = "CAT";
			} else {
				input += " ";
				if (mode.equals("ENZREV")) {
					enzRevData += input;
				} else if (mode.equals("ENZIRREV")) {
					enzIrevData += input;
				} else if (mode.equals("METINT")) {
					metIntData += input;
				} else if (mode.equals("METEXT")) {
					metExtData += input;
				} else if (mode.equals("CAT")) {
					String equation = input.trim();
					if (equation.length() > 0) {
						reactionEquations.add(equation);
					}
				}
			}

				input = in.readLine();
			} 
		}catch (IOException e) {
			e.printStackTrace();
		}

		metIntData = metIntData.trim();
		metExtData = metExtData.trim();
		enzRevData = enzRevData.trim();
		enzIrevData = enzIrevData.trim();

		
		

		// if the file is correct
		if (isMetatoolInputFile(file[0])) {

			// recover metabolites
			for (String metabo : extractItems(metIntData)) {
				network.AddMetabolite(metabo, "", true);
			}

			for (String metabo : extractItems(metExtData)) {

				network.AddMetabolite(metabo, "", false);
			}

			// create reactions
			for (String equation : reactionEquations) {

				network.addReaction(getEnzyme(equation),
						getReactants(network, equation),
						getProducts(network, equation),
						isReversible(equation, enzRevData));

			}

		}
		// if the file could not be parsed
		else {
			System.out.println("Could not load file");
			JOptionPane.showMessageDialog(null, "Could not load the file "
					+ file[0].getName(), "Loading error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return network;
	}

	public void save(File file, MetabolicNetworkItf network, boolean message) {

		String outputName = file.getName();
		String outputPath = file.getAbsolutePath();

		if (!outputPath.endsWith(".dat")) {
			outputPath += ".dat";
			outputName += ".dat";
		}

		System.out.println("saving a Metatool file : " + outputName);

		FileWriter fw;
		try {
			fw = new FileWriter(outputPath);
			fw.write("#Generated by J-Varo \n");

			fw.write("-METINT" + "\n");
			String w = "";
			for (Metabolite metabolite : network.getMetabolites()) {
				if (metabolite.isInternal()) {
					w += metabolite + " ";
				}
			}
			fw.write(w + "\n");
			fw.write("-METEXT" + "\n");
			w = "";
			for (Metabolite metabolite : network.getMetabolites()) {
				if (!metabolite.isInternal()) {
					w += metabolite + " ";
				}
			}
			fw.write(w + "\n");
			fw.write("-ENZREV" + "\n");
			w = "";
			for (Reaction reaction : network.getReactions()) {
				if (reaction.isReversible()) {
					w += reaction.getName() + " ";
				}
			}
			fw.write(w + "\n");
			fw.write("-ENZIRREV" + "\n");
			w = "";
			for (Reaction reaction : network.getReactions()) {
				if (!reaction.isReversible()) {
					w += reaction.getName() + " ";
				}
			}
			fw.write(w + "\n");
			fw.write("-CAT" + "\n");
			for (Reaction reaction : network.getReactions()) {
				fw.write(reaction.getName() + " : "
						+ reaction.getStringReaction().replace(",", ".") + "."
						+ "\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		if (message) {
			JOptionPane.showMessageDialog(null,
					"Successfully saved METATOOL file " + outputName + " !",
					"Succes", JOptionPane.INFORMATION_MESSAGE);
		}

	}
	
	private Set<String> extractItems(String text) {
		Set<String> data = new HashSet<String>();
		StringTokenizer token = new StringTokenizer(text, " ", false);
		while (token.hasMoreTokens()) {
			String item = token.nextToken().trim();
			data.add(item);
		}
		return data;
	}
	
	public HashMap<Metabolite, Double> getProducts(MetabolicNetwork network,
			String equation) {
		HashMap<Metabolite, Double> products = new HashMap<Metabolite, Double>();
		equation = equation.replaceAll("\\s+", " ");
		
		if (!equation.contains("= .")){
			//String[] p = equation.split(" : ")[1].split(" = ")[1].trim().split(" ");
			String[] p = equation.substring(equation.indexOf(" = ")+3, equation.indexOf(" .")).trim().split(" ");
			for (int i = 0; i < p.length; i++) {
				if (isNumeric(p[i])) {
					products.put(network.getMetabolite(p[i + 1].trim()),
							Double.parseDouble(p[i].trim()));
					i += 2;
				} else {
					products.put(network.getMetabolite(p[i].trim()), 1.0);
					i++;
				}
			}
		}
		return products;
	}

	public HashMap<Metabolite, Double> getReactants(MetabolicNetwork network,
			String equation) {
		HashMap<Metabolite, Double> reactants = new HashMap<Metabolite, Double>();
		equation = equation.replaceAll("\\s+", " ");
		if (!equation.contains(": =")){
			//String[] p = equation.split(" : ")[1].split("=")[0].trim().split(" ");
			String[] p = equation.substring(equation.indexOf(" : ")+3, equation.indexOf(" = ")).trim().split(" ");
			for (int i = 0; i < p.length; i++) {

				if (isNumeric(p[i])) {
					reactants.put(network.getMetabolite(p[i + 1].trim()),
							Double.parseDouble(p[i]));
					i += 2;
				} else {
					reactants.put(network.getMetabolite(p[i].trim()), 1.0);
					i++;
				}
			}
		}
		return reactants;
	}

	public String getEnzyme(String equation) {
		return equation.split(":")[0].trim();
	}
	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	public boolean isReversible(String equation, String enzRevData) {
		String enzyme = getEnzyme(equation);
		if (enzRevData.contains(enzyme)) {
			return true;
		}
		return false;
	}
	
	public static boolean isMetatoolInputFile(File file) {
		boolean retval = true;
		StringBuffer content = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String input = in.readLine();
			while (input != null) {
				content.append(input);
				input = in.readLine();
			}
		} catch (IOException ex) {
			retval = false;
		}

		String all = content.toString();
		retval = (all.indexOf("-ENZREV") >= 0)
				&& (all.indexOf("-ENZIRREV") >= 0)
				&& (all.indexOf("-METINT") >= 0)
				&& (all.indexOf("-METEXT") >= 0) && (all.indexOf("-CAT") >= 0);
		return retval;
	}

}
