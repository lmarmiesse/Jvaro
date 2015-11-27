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

package view.popup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.ElementaryMode;
import model.Metabolite;
import model.Reaction;
import controler.Filter;
import controler.MainControler;
import controler.Var;

public class FilterFrame extends PopUpAbs {

	private List<ElementaryMode> modes;
	private List<Integer> indices = new ArrayList<Integer>();
	private List<ElementaryMode> newModes = new ArrayList<ElementaryMode>();
	private Filter filter;
	private JList<String> filterReacsList = new JList<String>();
	private JList<String> filterMetabsList = new JList<String>();

	public FilterFrame(JFrame parent, List<ElementaryMode> modes,
			Filter filter, final MainControler controler) {
		super(parent);
		this.modes = modes;
		this.filter = filter;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Set filter");

		Dimension d = new Dimension(300, 500);
		filterReacsList.setModel(new DefaultListModel<String>());
		filterReacsList.setMaximumSize(d);
		filterMetabsList.setModel(new DefaultListModel<String>());
		filterMetabsList.setMaximumSize(d);
		
		JPanel centerPanel = new JPanel();

		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));

		JPanel reactionsPanel = new JPanel(new FlowLayout());

		JPanel reactionsListPanel = new JPanel();
		reactionsListPanel.setLayout(new BoxLayout(reactionsListPanel,
				BoxLayout.PAGE_AXIS));
		reactionsListPanel.add(new JLabel("Your network:", JLabel.CENTER));
		reactionsListPanel.add(new JLabel("Reactions", JLabel.CENTER));
		final JList<Reaction> reactionsList = new JList<Reaction>();
		reactionsList.setModel(new DefaultListModel<Reaction>());
		reactionsList.setMaximumSize(d);
		for (Reaction r : controler.getNetwork().getReactions()) {
			((DefaultListModel<Reaction>) reactionsList.getModel())
					.addElement(r);

		}
		reactionsListPanel.add(new JScrollPane(reactionsList));
		reactionsPanel.add(reactionsListPanel);
		JButton addReac = new JButton(Var.iconright);

		addReac.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				List<Reaction> selectedReacs = reactionsList
						.getSelectedValuesList();

				for (Reaction r : selectedReacs) {
					getFilter().addReaction(r);
				}

				update();
			}

		});

		reactionsPanel.add(addReac);

		JPanel reactionsFilterPanel = new JPanel();
		reactionsFilterPanel.setLayout(new BoxLayout(reactionsFilterPanel,
				BoxLayout.PAGE_AXIS));
		reactionsFilterPanel.add(new JLabel("The filter:", JLabel.CENTER));
		reactionsFilterPanel.add(new JLabel("Reactions", JLabel.CENTER));
		reactionsFilterPanel.add(new JScrollPane(filterReacsList));
		reactionsPanel.add(reactionsFilterPanel);

		JPanel metabolitesPanel = new JPanel(new FlowLayout());

		JPanel metabolitesListPanel = new JPanel();
		metabolitesListPanel.setLayout(new BoxLayout(metabolitesListPanel,
				BoxLayout.PAGE_AXIS));
		metabolitesListPanel.add(new JLabel("Metabolites", JLabel.CENTER));
		final JList<Metabolite> metabolitesList = new JList<Metabolite>();
		metabolitesList.setModel(new DefaultListModel<Metabolite>());
		metabolitesList.setMaximumSize(d);
		for (Metabolite m : controler.getNetwork().getMetabolites()) {
			((DefaultListModel<Metabolite>) metabolitesList.getModel())
					.addElement(m);

		}
		metabolitesListPanel.add(new JScrollPane(metabolitesList));
		metabolitesPanel.add(metabolitesListPanel);
		JButton addMetab = new JButton(Var.iconright);

		addMetab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				List<Metabolite> selectedMetabs = metabolitesList
						.getSelectedValuesList();

				for (Metabolite m : selectedMetabs) {
					getFilter().addMetabolite(m);
				}

				update();
			}

		});

		metabolitesPanel.add(addMetab);

		JPanel metabolitesFilterPanel = new JPanel();
		metabolitesFilterPanel.setLayout(new BoxLayout(metabolitesFilterPanel,
				BoxLayout.PAGE_AXIS));
		metabolitesFilterPanel.add(new JLabel("Metabolites", JLabel.CENTER));
		metabolitesFilterPanel.add(new JScrollPane(filterMetabsList));

		metabolitesPanel.add(metabolitesFilterPanel);

		centerPanel.add(reactionsPanel);
		centerPanel.add(metabolitesPanel);

		JButton cancelButton = new JButton("cancel");
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JButton okButton = new JButton("ok");
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (noErrors()) {
					okPressed = true;
					setModes();
					dispose();
				}
			}
		});

		JButton resetButton = new JButton("Reset filter");
		resetButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				getFilter().reset();
				update();
			}
		});

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(okButton);
		bottomPanel.add(cancelButton);
		bottomPanel.add(resetButton);

		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);

		update();

		this.pack();
		setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private boolean noErrors() {
		return true;
	}

	public void update() {

		((DefaultListModel<String>) filterReacsList.getModel())
				.removeAllElements();
		((DefaultListModel<String>) filterMetabsList.getModel())
				.removeAllElements();

		for (Reaction r : filter.getReactions()) {
			((DefaultListModel<String>) filterReacsList.getModel())
					.addElement(r.getName());
		}
		for (Metabolite m : filter.getMetabolites()) {
			((DefaultListModel<String>) filterMetabsList.getModel())
					.addElement(m.getName());
		}
	}

	public void setModes() {

		indices.clear();
		newModes.clear();

		for (int i = 0;i<modes.size();i++) {
			if (filter.accepts(modes.get(i))) {
				newModes.add(modes.get(i));
				indices.add(i+1);
			}
		}
	}

	public List<ElementaryMode> getNewModes() {
		return newModes;
	}

	public List<Integer> getIndices() {
		return indices;
	}
	
	public Filter getFilter() {
		return filter;
	}
}
