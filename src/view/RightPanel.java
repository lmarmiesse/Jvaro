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

package view;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;


import controler.ChangeTabListener;
import controler.MainControler;

public class RightPanel extends JTabbedPane implements Observer {

	private MainControler controler;
	private List<VisualizationPanel> listVisuPanels = new ArrayList<VisualizationPanel>();
	private List<ResultsPanel> listResPanels = new ArrayList<ResultsPanel>();

	public RightPanel(MainControler controler) {
		this.controler = controler;

		controler.addObserver(this);

		add("Reactions", new ReactionsPanel(controler));

		add("Rules", new RulesPanel(controler));

		setPreferredSize(new Dimension(600, 400));

		this.addChangeListener(new ChangeTabListener(this));

		this.initCloseTab();

	}

	public void removeVisuPane(JPanel panel) {
		listVisuPanels.remove((VisualizationPanel) panel);
		this.remove(panel);
	}

	public void removeResPane(JPanel panel) {
		listResPanels.remove((ResultsPanel) panel);
		this.remove(panel);
	}
	
	public void resetPanes(){
		while (listVisuPanels.size()>0){
			this.remove(listVisuPanels.get(0));
			listVisuPanels.remove(0);
		}
		while (listResPanels.size()>0){
			this.remove(listResPanels.get(0));
			listResPanels.remove(0);
			
		}
		
	}

	public void addVisuPane(JPanel panel, String name) {

		listVisuPanels.add((VisualizationPanel) panel);
		add(name, panel);
		initCloseTab();

	}

	public void addResPane(JPanel panel, String name) {
		listResPanels.add((ResultsPanel) panel);
		add(name, panel);
		initCloseTab();

	}

	public void update(Observable arg0, Object arg1) {

		if (listVisuPanels.size() == 0 && listResPanels.size() == 0) {
			if (controler.hasLoadedProject()) {
				List<String> pathsToRemove = new ArrayList<String>();
				for (String path : controler.getProject().getImagePaths()) {
					VisualizationPanel panel;

					try {
						panel = new VisualizationPanel(new File(path),
								controler);
						addVisuPane(panel, "Visualization");
					} catch (NullPointerException e) {
						pathsToRemove.add(path);

					}

				}

				Map<String, String[]> map = controler.getProject()
						.getResultsPaths();
				for (String key : map.keySet()) {
					ResultsPanel panel;


					panel = new ResultsPanel(controler, map.get(key), "saved/"
							+ key);
					addResPane(panel, "Results");
				}

				// delete files not attached to the project
				for (String path : pathsToRemove) {
					controler.getProject().removeImagePath(path);
				}
			}
		}

		// otherwise, visualisations are updated
		else {
			for (VisualizationPanel vp : listVisuPanels) {
				vp.update();
			}

		}

	}

	// add close button to all the Tab
	public void initCloseTab() {
		for (int i = 2; i < this.getTabCount(); i++) {
			setTabComponentAt(i, new ButtonTabComponent(this));
		}
	}
}
