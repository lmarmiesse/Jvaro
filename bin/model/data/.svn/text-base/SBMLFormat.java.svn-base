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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;
import javax.xml.stream.XMLStreamException;

import model.MetabolicNetwork;
import model.MetabolicNetworkItf;
import model.Metabolite;

import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;


public class SBMLFormat implements Format {

	public MetabolicNetworkItf load(File[] file) {

		MetabolicNetworkItf network = new MetabolicNetwork();

		System.out.println("loading SBML file : " + file[0].getAbsolutePath());

		SBMLDocument document = null;
		try {
			document = SBMLReader.read(file[0]);
		} catch (Exception e) {
			System.out.println("Could not load file");
			JOptionPane.showMessageDialog(null, "Could not load the file "
					+ file[0].getName(), "Loading error",
					JOptionPane.ERROR_MESSAGE);

		}

		// If the file was parsed successfully.
		if (document != null) {

			Model m = document.getModel();

			// First collect the metabolites
			for (Species s : m.getListOfSpecies()) {

				network.AddMetabolite(s.getId(), s.toString(),
						!s.getBoundaryCondition());

			}

			// then create the reactions
			for (Reaction r : m.getListOfReactions()) {

				Map<Metabolite, Double> reactants = new HashMap<Metabolite, Double>();
				for (SpeciesReference sr : r.getListOfReactants()) {
					// we look for each reactants in the meatbolites list
					Metabolite metab = network.getMetabolite(sr.getSpecies());

					reactants.put(metab, sr.getStoichiometry());
				}

				Map<Metabolite, Double> products = new HashMap<Metabolite, Double>();
				for (SpeciesReference sr : r.getListOfProducts()) {

					Metabolite metab = network.getMetabolite(sr.getSpecies());

					products.put(metab, sr.getStoichiometry());
				}

				network.addReaction(r.getId(), reactants, products,
						r.isReversible());

			}

		}
		// if the file couldn't be parsed successfully
		else {
			return null;
		}
		return network;
	}

	public void save(File file, MetabolicNetworkItf network, boolean message)
			throws IOException, SBMLException, XMLStreamException {

		// Create a new SBMLDocument object, using SBML Level 2 Version 4.
		SBMLDocument doc = new SBMLDocument(2, 1);
		Model model = doc.createModel("model");

		Compartment internal = new Compartment("internal");
		Compartment external = new Compartment("external");

		model.addCompartment(new Compartment(internal));
		model.addCompartment(new Compartment(external));

		for (Metabolite m : network.getMetabolites()) {

			Species s = new Species(m.getName());
			s.setName(m.getDescription());
			s.setBoundaryCondition(!m.isInternal());
			if (m.isInternal()) {
				s.setCompartment(internal);
			} else {
				s.setCompartment(external);
			}

			model.addSpecies(s);

		}

		for (model.Reaction r : network.getReactions()) {
			org.sbml.jsbml.Reaction jr = new org.sbml.jsbml.Reaction(
					r.getName());

			jr.setReversible(r.isReversible());

			for (Entry<Metabolite, Double> entry : r.getReactants().entrySet()) {

				Species s = model.getSpecies(entry.getKey().getName());

				SpeciesReference sr = jr.createReactant(s);
				sr.setStoichiometry(entry.getValue());

			}

			for (Entry<Metabolite, Double> entry : r.getProducts().entrySet()) {

				Species s = model.getSpecies(entry.getKey().getName());

				SpeciesReference sr = jr.createProduct(s);
				sr.setStoichiometry(entry.getValue());

			}

			model.addReaction(jr);
		}

		// Write the SBML document to a file.

		String outputPath = file.getAbsolutePath();
		String outputName = file.getName();

		if (!outputPath.endsWith(".xml")) {
			outputPath += ".xml";
			outputName += ".xml";
		}

		SBMLWriter.write(doc, outputPath, "JSBMLexample", "1.0");

		System.out.println("saving a SBML file : " + outputPath);
		if (message) {
			JOptionPane.showMessageDialog(null, "Successfully saved SBML file "
					+ outputName + " !", "Succes",
					JOptionPane.INFORMATION_MESSAGE);
		}

	}
}
