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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.SBMLException;

import model.MetabolicNetwork;
import model.MetabolicNetworkItf;
import model.Metabolite;
import model.Reaction;

public class RegEfmToolFormat implements Format {

	private File mFile;
	private File rFile;
	private File rvFile;
	private File sFile;
	private File grFile;

	public MetabolicNetworkItf load(File[] file) throws FileNotFoundException,
			IOException, ClassNotFoundException {

		System.out.println("loading RegEfmtool files ");
		MetabolicNetwork network = new MetabolicNetwork();

		// searching the correct files according to their extension
		for (File f : file) {
			if (getFileExtension(f).equals("mfile")) {
				mFile = f;
			} else if (getFileExtension(f).equals("rfile")) {
				rFile = f;
			} else if (getFileExtension(f).equals("rvfile")) {
				rvFile = f;
			} else if (getFileExtension(f).equals("sfile")) {
				sFile = f;
			} else if (getFileExtension(f).equals("grfile")) {
				grFile = f;
			}
		}

		// check if all the files were selected
		if ((mFile == null) || rFile == null || rvFile == null || sFile == null) {
			JOptionPane.showMessageDialog(null,
					"Could not load the files, missing file(s) ",
					"Loading error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		FileReader mfr = new FileReader(mFile);
		BufferedReader mbr = new BufferedReader(mfr);
		String s = mbr.readLine();
		String[] metabos = s.split(" ");
		for (String m : metabos) {
			String metName = m.replace("\"", "");
			network.AddMetabolite(metName, "", true);
		}

		// names of reactions
		FileReader rfr = new FileReader(rFile);
		BufferedReader rbr = new BufferedReader(rfr);
		String rs = rbr.readLine();
		String[] reactions = rs.split(" ");

		// reversible or not
		FileReader rvfr = new FileReader(rvFile);
		BufferedReader rvbr = new BufferedReader(rvfr);
		String rvs = rvbr.readLine();
		String[] reversible = rvs.split(" ");

		// add reactions
		for (int a = 0; a < reactions.length; a++) {
			Map<Metabolite, Double> reactants = new HashMap<Metabolite, Double>();
			Map<Metabolite, Double> products = new HashMap<Metabolite, Double>();
			String reacName = reactions[a].replace("\"", "");
			int rev = Integer.parseInt(reversible[a].replace("\"", ""));
			boolean r = (rev != 0);
			network.addReaction(reacName, reactants, products, r);
		}

		// run through matrix
		FileReader sfr = new FileReader(sFile);
		BufferedReader sbr = new BufferedReader(sfr);
		String stoech;
		int i = 0;
		while ((stoech = sbr.readLine()) != null) {

			String sto = stoech.replace("  ", " "); // if several spaces
			String verif = "";

			while (verif != sto) {
				verif = sto;
				sto = sto.replace("  ", " ");// if several spaces
				sto = sto.replace("	", " "); // if tabulation
			}

			if (sto.startsWith(" ")) {
				sto = sto.replaceFirst(" ", "");
			}

			String[] tabLine = sto.split(" ");
			for (int j = 0; j < tabLine.length; j++) {

				double n = Double.parseDouble(tabLine[j]);
				if (n < 0) {
					network.getMetabolite(metabos[i].replace("\"", ""));
					network.getReaction(reactions[j].replace("\"", ""))
							.addReactant(
									network.getMetabolite(metabos[i].replace(
											"\"", "")), -n);

				}
				if (n > 0) {
					network.getMetabolite(metabos[i].replace("\"", ""));
					network.getReaction(reactions[j].replace("\"", ""))
							.addProduct(
									network.getMetabolite(metabos[i].replace(
											"\"", "")), n);
				}
			}

			i++;
		}

		// rules
		if (grFile != null) {
			FileReader grfr = new FileReader(grFile);
			BufferedReader grbr = new BufferedReader(grfr);

			String gr;

			while ((gr = grbr.readLine()) != null) {
				if (!gr.startsWith("#")) {
					network.addRule(gr);
				}
			}

			grfr.close();

		}

		sfr.close();
		rvfr.close();
		rfr.close();
		mfr.close();

		return network;
	}

	public String getFileExtension(File f) {
		String name = f.getName();
		int pos = name.lastIndexOf('.');
		String ext = name.substring(pos + 1);
		return ext;

	}

	public void save(File file, MetabolicNetworkItf network, boolean message)
			throws IOException, SBMLException, XMLStreamException {
		System.out.println("saving a regEfmtool file : " + file.getName());
		File mfile = new File(file + ".mfile");
		FileWriter fwM = new FileWriter(mfile);

		File rfile = new File(file + ".rfile");
		FileWriter fwR = new FileWriter(rfile);

		File rvfile = new File(file + ".rvfile");
		FileWriter fwRv = new FileWriter(rvfile);

		File sfile = new File(file + ".sfile");
		FileWriter fwS = new FileWriter(sfile);

		File grfile = new File(file + ".grfile");
		FileWriter fwGr = new FileWriter(grfile);

		for (Metabolite metabolite : network.getMetabolites()) {

			if (metabolite.isInternal()) {
				fwM.write("\"" + metabolite + "\"" + " ");
				for (Reaction reaction : network.getReactions()) {
					if (reaction.getProducts().containsKey(metabolite)) {
						fwS.write(reaction.getProducts().get(metabolite)
								.toString()
								+ " ");
					} else if (reaction.getReactants().containsKey(metabolite)) {
						fwS.write("-"
								+ reaction.getReactants().get(metabolite)
										.toString() + " ");
					} else {
						fwS.write(0 + " ");
					}

				}
				fwS.write("\n");
			}
		}
		fwM.close();

		for (Reaction reaction : network.getReactions()) {
			fwR.write("\"" + reaction.getName() + "\"" + " ");
			if (reaction.isReversible()) {
				fwRv.write(1 + " ");
			} else
				fwRv.write(0 + " ");
		}

		for (String rule : network.getRules()) {
			// if rules created with interface
			if (rule.startsWith("IF")) {
				String regRule = getRegEfmToolRule(rule);
				fwGr.write(regRule);

			}
			// if already existing rules
			else {
				fwGr.write(rule);
			}
			fwGr.write("\n");
		}

		fwR.close();
		fwRv.close();
		fwS.close();
		fwGr.close();

		if (message) {
			JOptionPane.showMessageDialog(null,
					"Successfully saved regEfmtool files !", "Succes",
					JOptionPane.INFORMATION_MESSAGE);
		}

	}

	// conversion string interface -> regEfMTool
	public String getRegEfmToolRule(String rule) {
		String regRule = ("");
		rule = rule.replace("  ", " ");
		rule = rule.replace("   ", " ");
		String[] tabRule = rule.split(" ");

		String outputFlux = tabRule[tabRule.length - 1];
		String outputReaction = tabRule[tabRule.length - 3];

		regRule += outputReaction;
		regRule += (" = ");

		// equal case
		if (tabRule.length == 8 && tabRule[3].equals(tabRule[7])) {
			regRule += ("(");
			regRule += tabRule[3];
			regRule += tabRule[1];
			regRule += (" | ");
			regRule += tabRule[3];
			regRule += tabRule[1];
			regRule += (")");
		}

		else {
			// For closing the parenthesis in the end
			int nbParenthesis = 0;
			// number of operators to know the number of parenthesis
			int nbOperator = 0;
			regRule += ("(");
			nbParenthesis++;

			for (int i = 0; i < tabRule.length - 1; i++) {
				if ((tabRule[i].equals("AND") || tabRule[i].equals("OR"))) {
					nbOperator++;
				}
			}
			if ((outputFlux.equals("0")) && (tabRule.length != 8)) {
				regRule += ("!");
				while (nbOperator > 0) {
					regRule += ("(");
					nbParenthesis++;
					nbOperator--;
				}
			}
			if (!(outputFlux.equals("0")) && (tabRule.length != 8)) {
				while (nbOperator > 1) {
					regRule += ("(");
					nbParenthesis++;
					nbOperator--;
				}
			}
			for (int i = 1; i < tabRule.length - 4; i += 4) {

				if (!outputFlux.equals(tabRule[i + 2])
						&& ((tabRule.length == 8))) {
					regRule += ("!");
				}

				if (tabRule[i + 2].equals("0") && (!(tabRule.length == 8))) {
					regRule += ("(!");
					nbParenthesis++;
				}
				regRule += tabRule[i + 2]; // flux
				regRule += tabRule[i]; // metabolite name
				if (tabRule[i + 2].equals("0") && tabRule.length >= 12) {
					regRule += (")");
					nbParenthesis--;
				}
				if (tabRule[i + 2].equals("0") && (!(tabRule.length == 8))
						&& i != 1) {
					regRule += (")");
					nbParenthesis--;
				}
				if (tabRule[i + 2].equals("1") && (tabRule.length > 16)) {
					regRule += (")");
					nbParenthesis--;
				}

				if (tabRule[i + 3].equals("AND")) {
					regRule += (" & ");
				}
				if (tabRule[i + 3].equals("OR")) {
					regRule += ("|");
				}

			}
			while (nbParenthesis > 0) {
				regRule += (")");
				nbParenthesis--;
			}
		}

		return regRule;
	}

}
