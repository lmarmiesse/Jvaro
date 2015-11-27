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

package controler;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.xml.stream.XMLStreamException;

import model.MetabolicNetwork;
import model.MetabolicNetworkItf;
import model.Metabolite;
import model.Project;
import model.Reaction;
import model.data.Format;
import model.data.ProjectFormat;
import model.data.SBMLFormat;

import org.sbml.jsbml.SBMLException;

import view.GeneralFrame;

public class MainControler extends Observable {

	private List<Observer> observers;
	private Project project;
	private Format networkLoader;
	private Format networkSaver;

	private MetabolicNetworkItf dataBaseNetwork;

	public MainControler() {

		observers = new ArrayList<Observer>();
		cleanSvg();
		dataBaseNetwork = loadDataBaseNetwork();

	}

	public void notifyObservers() {
		// Shows updates of the view
		System.out.println("UPDATE");

		for (Observer obs : observers) {
			obs.update(this, "");
		}

	}

	public void addObserver(Observer obs) {
		observers.add(obs);
	}

	public void setLoader(Format loader) {
		this.networkLoader = loader;
	}

	public void setSaver(Format saver) {
		this.networkSaver = saver;
	}

	public boolean hasLoadedProject() {
		return project != null;
	}

	public String getProjectName() {
		return project.getName();
	}

	public String getProjectPath() {
		return project.getPath();
	}

	public void setProjectNameAndPath(String name, String path) {

		if (!this.hasLoadedProject()) {
			project = new Project(name, path);
		} else {

			project.setNameAndPath(name, path);
		}

	}

	public List<Object[]> getReactions() {

		// Controller collects reactions of the model
		List<Object[]> listeObj = new ArrayList<Object[]>();
		List<Reaction> reactions = project.getNetwork().getReactions();
		for (Reaction reac : reactions) {
			listeObj.add(new Object[] { reac.getName(), reac.isReversible(),
					reac.getStringReaction() });

		}
		// return list of corresponding reactions
		return listeObj;

	}

	public List<String> getRules() {
		return project.getNetwork().getRules();
	}

	public List<Object[]> getProducts(String reacName) {
		return project.getNetwork().getReaction(reacName).getProductsTable();
	}

	public List<Object[]> getReactants(String reacName) {
		return project.getNetwork().getReaction(reacName)
				.getObjetReactantsTable();
	}

	public List<Object[]> getMetabos() {
		// Controller collects metabolites of the model
		List<Object[]> listeObj = new ArrayList<Object[]>();
		List<Metabolite> metabolites = project.getNetwork().getMetabolites();
		for (Metabolite metab : metabolites) {
			listeObj.add(new Object[] { metab.getName(), metab.isInternal(),
					metab.getDescription() });

		}

		// return list of corresponding metabolites
		return listeObj;
	}

	public List<Metabolite> getListMetabos() {
		List<Metabolite> metabolites = project.getNetwork().getMetabolites();
		return metabolites;
	}

	public boolean getReversible(String name) {
		return project.getNetwork().getReaction(name).isReversible();
	}

	public void delReaction(String reacName) {
		project.getNetwork().delReaction(reacName);
	}

	public Metabolite getMetabolite(String name) {
		return project.getNetwork().getMetabolite(name);
	}

	public void delMetabolite(String metName) {
		project.getNetwork().delMetabolite(metName);

	}

	public void addProduct(String name, Metabolite met, Double n) {
		project.getNetwork().getReaction(name).addProduct(met, n);
	}

	public void addReactant(String name, Metabolite met, Double n) {
		project.getNetwork().getReaction(name).addReactant(met, n);
	}

	public void delMetabolitesReaction(String name) {
		project.getNetwork().getReaction(name).delMetabolites();
	}

	public void addMetabolite(String name, String description, boolean internal) {
		project.getNetwork().AddMetabolite(name, description, internal);
	}

	public void addRule(String rule) {
		project.getNetwork().addRule(rule);
	}

	public void editRule(String oldRule, String newRule) {
		project.getNetwork().editRule(oldRule, newRule);
	}

	public void delRule(String rule) {
		project.getNetwork().delRule(rule);
	}

	public void setReactionReversible(String name, boolean reversible) {
		project.getNetwork().setReactionReversible(name, reversible);

	}

	public void setReactionName(String name, String newName) {
		project.getNetwork().setReactionName(name, newName);

	}

	public Reaction getReaction(String name) {
		return project.getNetwork().getReaction(name);
	}

	public void setMetaboInternal(String name, boolean internal) {
		project.getNetwork().setMetaboInternal(name, internal);

	}

	public void setMetabo(String oldName, String newName, String description) {
		project.getNetwork().setMetabolite(oldName, newName, description);

	}

	public void addReaction(String enzyme, Map<Metabolite, Double> r,
			Map<Metabolite, Double> p, boolean reversible) {
		project.getNetwork().addReaction(enzyme, r, p, reversible);
	}

	public void loadNetwork(File[] file) throws ClassNotFoundException,
			IOException {

		// we load the file
		MetabolicNetworkItf network = networkLoader.load(file);

		if (network != null) {

			// if no project is loaded it creats one
			if (!this.hasLoadedProject()) {
				this.createProject(file[0].getName(), file[0].getAbsolutePath());
			}

			this.setNetwork(network);

		}

		new Thread() {
			public void run() {
				JFrame running = new JFrame();

				running.setLayout(new BorderLayout());
				JProgressBar progressBar = new JProgressBar();
				progressBar.setIndeterminate(true);

				running.add(new JLabel("Loading file ..."),
						BorderLayout.PAGE_START);
				running.add(progressBar, BorderLayout.CENTER);
				running.pack();
				running.setLocationRelativeTo(null);
				running.setVisible(true);

				try {
					notifyObservers();
					running.dispose();
				} catch (NullPointerException e) {
					running.dispose();
				}
			}
		}.start();

	}

	public void setNetwork(MetabolicNetworkItf network) {
		project.setNetwork(network);
	}

	public void saveNetworkAs(File file) throws IOException, SBMLException,
			XMLStreamException {

		networkSaver.save(file, project.getNetwork(), true);
	}

	public void saveProject() throws IOException {

		File f = new File(getProjectPath());

		System.out.println(f.getAbsolutePath());

		new ProjectFormat(this).save(f, project.getNetwork(), false);
		
		GeneralFrame.getInstance(this).disableSave();
	}

	public MetabolicNetworkItf getNetwork() {
		return project.getNetwork();
	}

	public boolean hasLoadedProjectWithPath() {
		return project.hasPath();
	}

	public void createProject(String name, String path) {
		project = new Project(name, path);
	}

	public void createProject(Project project) {
		this.project = project;
	}

	public Project getProject() {
		return project;
	}

	public List<Reaction> getListReactions() {

		return project.getNetwork().getReactions();
	}

	public void cleanSvg() {
		// svg files unsaved are deleted
		File svgfolder = new File("tulip/svg");
		for (final File file : svgfolder.listFiles()) {
			if (file.isFile()) {
				file.delete();
			}
		}

		// and tlp files
		File tlpfolder = new File("tulip/tlp");
		for (final File file : tlpfolder.listFiles()) {
			if (file.isFile()) {
				file.delete();
			}
		}
	}

	private MetabolicNetworkItf loadDataBaseNetwork() {

		File dataBase = new File("dataBase.xml");
		if (!dataBase.isFile()) {
			try {
				new SBMLFormat().save(dataBase, new MetabolicNetwork(), false);
			} catch (SBMLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}

		File[] f = new File[1];
		f[0] = dataBase;
		return new SBMLFormat().load(f);
	}

	public MetabolicNetworkItf getDataBaseNetwork() {
		return dataBaseNetwork;
	}

}
