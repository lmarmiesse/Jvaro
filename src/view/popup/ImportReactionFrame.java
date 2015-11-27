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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.MetabolicNetworkItf;
import model.Metabolite;
import model.Reaction;
import controler.MainControler;
import controler.Var;

public class ImportReactionFrame extends PopUpAbs {

	private MyTableModel modelImport, modelChoice;

	private List<Reaction> dbList, choiceList;

	public ImportReactionFrame(JFrame parent, final MainControler controler) {
		super(parent);
		dbList=new ArrayList<Reaction>();
		choiceList=new ArrayList<Reaction>();
		this.setLayout(new BorderLayout());

		MetabolicNetworkItf dbNetwork = controler.getDataBaseNetwork();
		
		String colName1 = "Enzyme name";
		String colName2 = "Reversible";
		String colName3 = "Reaction";

		final JTable importReaction = new JTable();
		modelImport = new MyTableModel();
		modelImport.addColumn(colName1);
		modelImport.addColumn(colName2);
		modelImport.addColumn(colName3);
		importReaction.setAutoCreateRowSorter(true);
		importReaction.getTableHeader().setReorderingAllowed(false);

		List<Reaction> reactions = dbNetwork.getReactions();

		for (Reaction reac : reactions) {
			dbList.add(reac);
		}

		importReaction.setModel(modelImport);

		final JTable choiceReaction = new JTable();
		modelChoice = new MyTableModel();
		modelChoice.addColumn(colName1);
		modelChoice.addColumn(colName2);
		modelChoice.addColumn(colName3);
		choiceReaction.setModel(modelChoice);
		choiceReaction.setAutoCreateRowSorter(true);
		choiceReaction.getTableHeader().setReorderingAllowed(false);

		reactions = controler.getNetwork().getReactions();
		for (Reaction reac : reactions) {
			choiceList.add(reac);
		}
		
		update();

		JButton selectButton = new JButton(Var.iconright);
		selectButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				for (int i : importReaction.getSelectedRows()) {
					boolean doIt = true;
					for (int j = 0; j < choiceReaction.getRowCount(); j++) {

						if (modelImport.getValueAt(i, 0) == modelChoice
								.getValueAt(j, 0)) {
							doIt = false;
							JOptionPane.showMessageDialog(null, "Reaction "
									+ modelImport.getValueAt(i, 0)
									+ " is already in your network", "",
									JOptionPane.ERROR_MESSAGE);
						}
					}

					if (doIt) {
						choiceList.add(dbList.get(i));

					}
					update();
				}
			}

		});

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new FlowLayout());
		
		
		JPanel dataBasePanel = new JPanel();
		dataBasePanel.setLayout(new BoxLayout(dataBasePanel, BoxLayout.PAGE_AXIS));
		dataBasePanel.add(new JLabel("Database",JLabel.CENTER));
		dataBasePanel.add(new JScrollPane(importReaction));
		
		JPanel networkPanel = new JPanel();
		networkPanel.setLayout(new BoxLayout(networkPanel, BoxLayout.PAGE_AXIS));
		networkPanel.add(new JLabel("Network",JLabel.CENTER));
		networkPanel.add(new JScrollPane(choiceReaction));
		
		
		centerPanel.add(dataBasePanel);
		centerPanel.add(selectButton);
		centerPanel.add(networkPanel);

		
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout());
		JButton okButton = new JButton("ok");
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (noErrors()) {
					okPressed = true;
					
					for (Reaction reac : choiceList){
						controler.getNetwork().addReaction(reac.getName(), reac.getReactants(), reac.getProducts(), reac.isReversible());
						for (Metabolite m : reac.getListProducts()) {
						    controler.getNetwork().AddMetabolite(m.getName(), m.getDescription(), m.isInternal());
						} 
						for (Metabolite m : reac.getListReactants()) {
						    controler.getNetwork().AddMetabolite(m.getName(), m.getDescription(), m.isInternal());
						} 
					}
					
					controler.notifyObservers();
					dispose();
				}
			}

		});
		JButton cancelButton = new JButton("cancel");
		southPanel.add(okButton);
		southPanel.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		this.add(centerPanel, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.PAGE_END);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public boolean noErrors() {

		return true;
	}

	public void update() {

		while(modelImport.getRowCount()>0){
			modelImport.removeRow(0);
		}
		while(modelChoice.getRowCount()>0){
			modelChoice.removeRow(0);
		}
		
		for (Reaction reac : dbList) {
			modelImport.addRow(new Object[] { reac.getName(),
					reac.isReversible(), reac.getStringReaction() });

		}

		for (Reaction reac : choiceList) {
			modelChoice.addRow(new Object[] { reac.getName(),
					reac.isReversible(), reac.getStringReaction() });

		}
	}

	// Display checkbox in tables
	class MyTableModel extends DefaultTableModel {
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int iRowIndex, int iColumnIndex) {
			return false;
		}
	}

}
