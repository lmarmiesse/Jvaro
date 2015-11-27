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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;



import controler.MainControler;
import controler.MyDocumentListener;
import controler.Var;
import controler.metabolites.DelMetabListener;
import controler.reactions.AddDbListener;
import controler.reactions.AddReacListener;
import controler.reactions.DelReacListener;
import controler.reactions.EditReacListener;
import controler.reactions.ReactionsTableCellEditorListener;
import controler.reactions.ReactionsTableSelectionListener;

public class ReactionsPanel extends JPanel implements Observer {



	private JButton buttonajoutreac = new JButton(Var.iconajout);
	private JButton buttonmodifreac = new JButton(Var.iconmodif);
	private JButton buttonsuppreac = new JButton(Var.iconsupp);
	private JButton buttonadddb = new JButton(Var.upload);
	private JTextField searchField = new JTextField(8);

	private ReactionsTable table;

	private MainControler controler;

	public ReactionsPanel(MainControler controler) {

		this.controler = controler;

		String tableCol1 = "Enzyme name";
		String tableCol2 = "Reversible";
		String tableCol3 = "Reaction";

		table = ReactionsTable.getInstance(controler, tableCol1, tableCol2,
				tableCol3);

		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
				table.getModel());
		table.setRowSorter(sorter);

		buttonmodifreac.setEnabled(false);
		buttonsuppreac.setEnabled(false);
		buttonajoutreac.setEnabled(false);
		buttonadddb.setEnabled(false);
		searchField.setEnabled(false);

		buttonajoutreac.setToolTipText("Add a reaction");
		buttonmodifreac.setToolTipText("Edit");
		buttonsuppreac.setToolTipText("Remove");
		buttonadddb.setToolTipText("Add the reaction(s) to the data base");

		buttonsuppreac.addActionListener(new DelReacListener(controler, this));
		buttonajoutreac.addActionListener(new AddReacListener(controler));
		buttonmodifreac
				.addActionListener(new EditReacListener(controler, this));
		buttonadddb.addActionListener(new AddDbListener(controler,this));

		table.getSelectionModel()
				.addListSelectionListener(
						new ReactionsTableSelectionListener(this, tableCol2,
								controler));
		table.getDefaultEditor(Boolean.class).addCellEditorListener(
				new ReactionsTableCellEditorListener(controler, this));

		controler.addObserver(this);
		JToolBar toolbarreaction = new JToolBar();
		toolbarreaction.setFloatable(false);
		toolbarreaction.setLayout(new BoxLayout(toolbarreaction,
				BoxLayout.LINE_AXIS));
		toolbarreaction.add(buttonajoutreac);
		toolbarreaction.add(buttonmodifreac);
		toolbarreaction.add(buttonsuppreac);
		toolbarreaction.add(buttonadddb);

		searchField.setMaximumSize(new Dimension(20, 30));
		toolbarreaction.add(Box.createHorizontalGlue());
		toolbarreaction.add(searchField);
		toolbarreaction.add(new JLabel(Var.iconsearch));

		setLayout(new BorderLayout());
		add(toolbarreaction, BorderLayout.PAGE_START);

		// call the class which manage reaction's display
		add(new JScrollPane(table), BorderLayout.CENTER);

		searchField.getDocument().addDocumentListener(
				new MyDocumentListener(searchField, sorter,0,2));
		
		//key delete listener
		table.addKeyListener(new DelReacListener(controler, this));

	}

	public ReactionsTable getTable() {
		return table;
	}

	public void update(Observable arg0, Object arg1) {

		table.update();
		buttonajoutreac.setEnabled(controler.hasLoadedProject());
		searchField.setEnabled(controler.hasLoadedProject());

		// Buttons are clickables only if a row is selected
		buttonmodifreac.setEnabled(table.getSelectedRowCount() > 0);
		buttonsuppreac.setEnabled(table.getSelectedRowCount() > 0);
		buttonadddb.setEnabled(table.getSelectedRowCount() > 0);

	}
	public void setButtons() {
		buttonmodifreac.setEnabled(table.getSelectedRowCount() == 1);
		buttonsuppreac.setEnabled(table.getSelectedRowCount() > 0);
		buttonadddb.setEnabled(table.getSelectedRowCount() > 0);
	}

}
