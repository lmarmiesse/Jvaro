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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Metabolite;
import model.Reaction;

import controler.MainControler;

// display boolean in ckeckboxes in tables
class MyTableModel extends DefaultTableModel {
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int iRowIndex, int iColumnIndex) {

		if (getColumnName(iColumnIndex) == "visible") {
			return true;
		}

		return false;
	}
}

public class VisuChoiceFrame extends PopUpAbs {

	private MainControler controler;

	private List<JRadioButton> layouts;
	private ButtonGroup bg;
	private JTable metaTable;
	private JTable reacTable;

	Dimension d = new Dimension(300, 300);

	public VisuChoiceFrame(JFrame parent, MainControler controler) {
		super(parent);
		this.controler = controler;
		setLayout(new BorderLayout());

		JPanel centerPanel = new JPanel();

		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
		JPanel tables = new JPanel(new FlowLayout());
		JPanel leftTablePanel = new JPanel();
		JPanel rightTablePanel = new JPanel();
		leftTablePanel.setLayout(new BoxLayout(leftTablePanel,
				BoxLayout.PAGE_AXIS));
		rightTablePanel.setLayout(new BoxLayout(rightTablePanel,
				BoxLayout.PAGE_AXIS));
		tables.add(leftTablePanel);
		tables.add(rightTablePanel);
		centerPanel.add(tables);
		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.PAGE_AXIS));
		JPanel tulipLayoutPanel = new JPanel(new FlowLayout());
		JPanel bioinfoLayoutPanel = new JPanel(new FlowLayout());
		layoutPanel.add(tulipLayoutPanel);
		tulipLayoutPanel.add(new JLabel("Tulip layouts : "));
		layoutPanel.add(bioinfoLayoutPanel);
		bioinfoLayoutPanel.add(new JLabel("Bioinfo layouts : "));
		centerPanel.add(layoutPanel);

		JPanel bottomPanel = new JPanel();

		leftTablePanel.add(new JLabel("Metabolites"));
		rightTablePanel.add(new JLabel("Reactions"));

		MyTableModel model1 = new MyTableModel();
		model1.addColumn("name");
		model1.addColumn("visible");
		model1.addColumn("number of links");
		MyTableModel model2 = new MyTableModel();
		model2.addColumn("name");
		model2.addColumn("visible");

		metaTable = new JTable(model1);
		metaTable.setPreferredScrollableViewportSize(d);
		metaTable.setAutoCreateRowSorter(true);
		metaTable.getTableHeader().setReorderingAllowed(false);
		reacTable = new JTable(model2);
		reacTable.setPreferredScrollableViewportSize(d);
		reacTable.setAutoCreateRowSorter(true);
		reacTable.getTableHeader().setReorderingAllowed(false);
		leftTablePanel.add(new JScrollPane(metaTable));
		rightTablePanel.add(new JScrollPane(reacTable));

		remplirMetTable(controler.getListMetabos());
		remplirReacTable(controler.getListReactions());

		layouts = new ArrayList<JRadioButton>();
		// addition of tulip layouts
		layouts.add(new JRadioButton("Random layout"));
		layouts.add(new JRadioButton("Circular"));

		// this algorithm is not implemented on Windows
		String OS = System.getProperty("os.name").toLowerCase();
		if (!(OS.indexOf("win") >= 0)) {
			layouts.add(new JRadioButton("Balloon (OGDF)"));
			layouts.add(new JRadioButton("GEM Frick (OGDF)"));
		}
		bg = new ButtonGroup();
		for (JRadioButton jrb : layouts) {
			bg.add(jrb);
			tulipLayoutPanel.add(jrb);
		}
		// addition of bioinfo layouts
		JRadioButton linearLayout = new JRadioButton("Linear layout");
		layouts.add(linearLayout);
		bg.add(linearLayout);
		bioinfoLayoutPanel.add(linearLayout);

		JRadioButton squareLayout = new JRadioButton("Square layout");
		layouts.add(squareLayout);
		bg.add(squareLayout);
		bioinfoLayoutPanel.add(squareLayout);

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
					dispose();
				}
			}
		});

		bottomPanel.add(okButton);
		bottomPanel.add(cancelButton);

		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);
		add(new JLabel("Choose what you want to see in the visualization"),
				BorderLayout.NORTH);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void remplirMetTable(List<Metabolite> list) {
		for (Metabolite meta : list) {

			int nbLinks = 0;
			for (Reaction reac : controler.getNetwork().getReactions()) {
				for (Metabolite m : reac.getListReactants()) {
					if (meta.equals(m)) {
						nbLinks++;
					}
				}
			}
			for (Reaction reac : controler.getNetwork().getReactions()) {
				for (Metabolite m : reac.getListProducts()) {
					if (meta.equals(m)) {
						nbLinks++;
					}
				}

			}

			((MyTableModel) metaTable.getModel()).addRow(new Object[] {
					meta.getName(), true, nbLinks });
		}

	}

	public void remplirReacTable(List<Reaction> list) {
		for (Reaction reac : list) {
			((MyTableModel) reacTable.getModel()).addRow(new Object[] {
					reac.getName(), true });
		}

	}

	public String getLayoutAlgorithm() {
		for (JRadioButton jrb : layouts) {
			if (jrb.isSelected()) {
				return jrb.getText();
			}
		}

		return "";
	}

	public List<Metabolite> getSelectedMetabolites() {
		List<Metabolite> list = new ArrayList<Metabolite>();
		int size = metaTable.getRowCount();
		for (int i = 0; i < size; i++) {
			if ((Boolean) metaTable.getValueAt(i, 1)) {
				list.add(controler.getNetwork().getMetabolite(
						(String) metaTable.getValueAt(i, 0)));
			}
		}

		return list;

	}

	public List<Reaction> getSelectedReactions() {
		List<Reaction> list = new ArrayList<Reaction>();
		int size = reacTable.getRowCount();
		for (int i = 0; i < size; i++) {
			if ((Boolean) reacTable.getValueAt(i, 1)) {
				list.add(controler.getNetwork().getReaction(
						(String) reacTable.getValueAt(i, 0)));
			}
		}
		return list;

	}

	public boolean noErrors() {
		if (getSelectedReactions().size() == 0) {
			JOptionPane.showMessageDialog(null,
					"You must choose at least one reaction", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (getSelectedMetabolites().size() == 0) {
			JOptionPane.showMessageDialog(null,
					"You must choose at least one metabolite", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (getLayoutAlgorithm() == "") {
			JOptionPane.showMessageDialog(null,
					"You must choose a layout algorithm", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

}
