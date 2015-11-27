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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import controler.MainControler;
import controler.Var;
import controler.reactions.AddMetaboliteReactionListener;
import controler.reactions.RemoveMetaboliteReactionListener;

import model.Metabolite;

public class ReactionFrame extends PopUpAbs {

	private MainControler controler;
	private JTextField stochiometry, name;
	private JCheckBox reversible;
	private JList metabolites;
	private JTable tableReactant, tableProduct;
	private JButton okButton, cancelButton;
	DefaultTableModel ModelR, ModelP;
	private String enzName;

	public ReactionFrame(JFrame parent, MainControler controler,
			String enzName, boolean isReversible) {
		super(parent);

		this.controler = controler;
		this.enzName = enzName;
		setTitle("Reaction");

		Dimension d = new Dimension(200, 200);

		// columns names
		String col1 = "Name";
		String col2 = "Stoichiometry";

		// enzyme name and reversible
		name = new JTextField();
		reversible = new JCheckBox();

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		JPanel panelNorth = new JPanel();
		JPanel panelSouth = new JPanel();
		JPanel panelReactant = new JPanel();
		JPanel panelProduct = new JPanel();

		panelNorth.setLayout(new GridLayout(2, 2));

		panelNorth.add(new JLabel("Enzyme name", JLabel.CENTER));
		panelNorth.add(name);
		panelNorth.add(new JLabel("Reversible", JLabel.CENTER));
		panelNorth.add(reversible);

		// Boutons ok cancel
		panelSouth.setLayout(new FlowLayout());

		okButton = new JButton("ok");
		cancelButton = new JButton("cancel");
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (noErrors()) {
					okPressed = true;
					dispose();
				} else if (name.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Invalid name.", " ",
							JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null,
							"Invalid stoichiometry", " ",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		panelSouth.add(okButton);
		panelSouth.add(cancelButton);

		// Reactants
		panelReactant.setLayout(new BoxLayout(panelReactant,
				BoxLayout.PAGE_AXIS));

		JToolBar toolbarR = new JToolBar();

		JButton buttonSuppR = new JButton(Var.iconsupp);
		buttonSuppR.addActionListener(new RemoveMetaboliteReactionListener(
				this, "reactant"));

		toolbarR.add(buttonSuppR);
		toolbarR.setFloatable(false);

		MyTableModel modelR = new MyTableModel();

		tableReactant = new JTable();
		tableReactant.setModel(modelR);
		tableReactant.setPreferredScrollableViewportSize(d);
		modelR.addColumn(col1);
		modelR.addColumn(col2);

		panelReactant.add(new JLabel("<html><b>Reactants</b></html>"));
		panelReactant.add(toolbarR);
		panelReactant.add(new JScrollPane(tableReactant));

		// Products
		panelProduct
				.setLayout(new BoxLayout(panelProduct, BoxLayout.PAGE_AXIS));

		JToolBar toolbarP = new JToolBar();

		JButton buttonSuppP = new JButton(Var.iconsupp);
		buttonSuppP.addActionListener(new RemoveMetaboliteReactionListener(
				this, "product"));

		toolbarP.add(buttonSuppP);
		toolbarP.setFloatable(false);

		MyTableModel modelP = new MyTableModel();
		tableProduct = new JTable();
		tableProduct.setModel(modelP);
		tableProduct.setPreferredScrollableViewportSize(d);

		modelP.addColumn(col1);
		modelP.addColumn(col2);

		panelProduct.add(new JLabel("<html><b>Products</b></html>"));
		panelProduct.add(toolbarP);
		panelProduct.add(new JScrollPane(tableProduct));

		JButton buttonRight = new JButton(Var.iconright);
		JButton buttonLeft = new JButton(Var.iconleft);
		buttonLeft.addActionListener(new AddMetaboliteReactionListener(this,
				"reactant"));
		buttonRight.addActionListener(new AddMetaboliteReactionListener(this,
				"product"));

		//

		// Jlist metabo
		DefaultListModel ListModel = new DefaultListModel();

		for (Metabolite m : controler.getListMetabos()) {
			ListModel.addElement(m);
		}

		metabolites = new JList(ListModel);
		metabolites.setSelectedIndex(0);
		metabolites.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		stochiometry = new JTextField(3);

		JPanel panelMetabo = new JPanel();
		panelMetabo.setLayout(new BoxLayout(panelMetabo, BoxLayout.PAGE_AXIS));
		panelMetabo.add(new JScrollPane(metabolites));
		panelMetabo.add(new JLabel("Stochiometry"));
		panelMetabo.add(stochiometry);

		JPanel panelReaction = new JPanel();

		panelReaction.setLayout(new FlowLayout());
		panelReaction.add(panelReactant);
		panelReaction.add(buttonLeft);
		panelReaction.add(panelMetabo);
		panelReaction.add(buttonRight);
		panelReaction.add(panelProduct);

		add(panelNorth);
		add(new JSeparator());
		add(panelReaction);
		add(new JSeparator());
		add(panelSouth);

		tableProduct.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 127 && tableProduct.hasFocus()) {
					delProduct();
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}

		});
		
		tableReactant.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==127 && tableReactant.hasFocus()) {
					delReactant();
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}

		});

		// edit : fields are already filled
		if (enzName != null) {
			name.setText(enzName);
			reversible.setSelected(isReversible);
			reversible.setSelected(controler.getReversible(enzName));
			List<Object[]> listeReactant = controler.getReactants(enzName);
			for (Object[] r : listeReactant) {
				((DefaultTableModel) tableReactant.getModel()).addRow(r);
			}
			List<Object[]> listeProduct = controler.getProducts(enzName);
			for (Object[] p : listeProduct) {
				((DefaultTableModel) tableProduct.getModel()).addRow(p);
			}

		}

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public String getStochiometry() {
		if (!stochiometry.getText().equals("")) {
			return stochiometry.getText();
		}
		return "1";
	}

	public String getName() {
		return name.getText();
	}

	public boolean getReversible() {
		return reversible.isSelected();
	}

	public Metabolite getMetabolite() {
		return (Metabolite) metabolites.getSelectedValue();
	}

	public void addReactant() {
		if (getStochiometry().equals("")) {
			((DefaultTableModel) tableReactant.getModel()).addRow(new Object[] {
					getMetabolite(), "1" });
		} else {
			((DefaultTableModel) tableReactant.getModel()).addRow(new Object[] {
					getMetabolite(), getStochiometry() });
		}
		setStochiometry();
	}

	public void addProduct() {
		if (getStochiometry().equals("")) {
			((DefaultTableModel) tableProduct.getModel()).addRow(new Object[] {
					getMetabolite(), "1" });
		} else {
			((DefaultTableModel) tableProduct.getModel()).addRow(new Object[] {
					getMetabolite(), getStochiometry() });
		}
		setStochiometry();
	}

	public void delReactant() {
		if (tableReactant.getSelectedRow()>=0){
		((DefaultTableModel) tableReactant.getModel()).removeRow(tableReactant
				.getSelectedRow());
		}
	}

	public void delProduct() {
		if (tableProduct.getSelectedRow()>=0){
			((DefaultTableModel) tableProduct.getModel()).removeRow(tableProduct
				.getSelectedRow());
		}
	}

	public Map<Metabolite, Double> getReactants() {
		HashMap<Metabolite, Double> reactant = new HashMap<Metabolite, Double>();
		for (int i = 0; i < tableReactant.getModel().getRowCount(); i++) {
			String s = (String) tableReactant.getModel().getValueAt(i, 1);
			Metabolite metab = (Metabolite) tableReactant.getModel()
					.getValueAt(i, 0);
			Double stochio = Double.parseDouble(s);
			reactant.put(metab, stochio);
		}
		return reactant;

	}

	public Map<Metabolite, Double> getProducts() {
		HashMap<Metabolite, Double> product = new HashMap<Metabolite, Double>();
		for (int i = 0; i < tableProduct.getModel().getRowCount(); i++) {
			String s = (String) tableProduct.getModel().getValueAt(i, 1);
			Metabolite metab = (Metabolite) tableProduct.getModel().getValueAt(
					i, 0);
			Double stochio = Double.parseDouble(s);
			product.put(metab, stochio);
		}
		return product;

	}

	public void setStochiometry() {
		stochiometry.setText("");
	}

	private boolean noErrors() {
		if (name.getText().equals("")) {
			return false;
		}
		for (int i = 0; i < tableProduct.getModel().getRowCount(); i++) {
			try {
				Double stochio = Double.parseDouble((String) tableProduct
						.getModel().getValueAt(i, 1));
			} catch (NumberFormatException ex) {
				return false;
			}
		}
		for (int i = 0; i < tableReactant.getModel().getRowCount(); i++) {
			try {
				Double stochio = Double.parseDouble((String) tableReactant
						.getModel().getValueAt(i, 1));
			} catch (NumberFormatException ex) {
				return false;

			}
		}

		return true;
	}

	// Stochiometry case can be modified by hand
	class MyTableModel extends DefaultTableModel {
		private String[] columnNames = { "Name", "Stochiometry" };

		public boolean isCellEditable(int iRowIndex, int iColumnIndex) {

			if (iColumnIndex == 1) {
				return true;
			} else {
				return false;
			}

		}

	}

}
