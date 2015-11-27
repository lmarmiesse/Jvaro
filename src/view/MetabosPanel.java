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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;



import controler.MainControler;
import controler.MyDocumentListener;
import controler.Var;
import controler.metabolites.AddMetabListener;
import controler.metabolites.DelMetabListener;
import controler.metabolites.EditMetabListener;
import controler.metabolites.MetabosTableCellEditorListener;
import controler.metabolites.MetabosTableSelectionListener;

public class MetabosPanel extends JPanel implements Observer {

	

	private JButton buttonajoutmet = new JButton(Var.iconajout);
	private JButton buttonmodifmet = new JButton(Var.iconmodif);
	private JButton buttonsuppmet = new JButton(Var.iconsupp);
	private JTextField searchField = new JTextField(8);

	private MetabosTable table;

	private MainControler controler;

	public MetabosPanel(MainControler controler) {
		this.controler = controler;

		String tableCol1 = "Name";
		String tableCol2 = "Internal";
		String tableCol3 = "Description";

		table = MetabosTable.getInstance(controler, tableCol1, tableCol2,
				tableCol3);

		buttonmodifmet.setEnabled(false);
		buttonsuppmet.setEnabled(false);
		buttonajoutmet.setEnabled(false);
		searchField.setEnabled(false);

		buttonajoutmet.setToolTipText("Add a metabolite");
		buttonajoutmet.addActionListener(new AddMetabListener(controler));
		buttonmodifmet.setToolTipText("Edit");
		buttonmodifmet
				.addActionListener(new EditMetabListener(controler, this));
		buttonsuppmet.setToolTipText("Remove");
		buttonsuppmet.addActionListener(new DelMetabListener(controler, this));
		
		

		table.getSelectionModel().addListSelectionListener(
				new MetabosTableSelectionListener(this, tableCol2, controler));
		table.getDefaultEditor(Boolean.class).addCellEditorListener(
				new MetabosTableCellEditorListener(controler, this));

		// Filter of tab rows for research
		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
				table.getModel());
		table.setRowSorter(sorter);
		
		//key deletelistener
		table.addKeyListener(new DelMetabListener(controler, this));

		controler.addObserver(this);

		JToolBar toolbarmet = new JToolBar();
		toolbarmet.setFloatable(false);
		toolbarmet.setLayout(new BoxLayout(toolbarmet, BoxLayout.LINE_AXIS));
		toolbarmet.add(buttonajoutmet);
		toolbarmet.add(buttonmodifmet);
		toolbarmet.add(buttonsuppmet);
		searchField.setMaximumSize(new Dimension(20, 30));
		toolbarmet.add(Box.createHorizontalGlue());

		toolbarmet.add(searchField);
		toolbarmet.add(new JLabel(Var.iconsearch));

		setLayout(new BorderLayout());
		add(toolbarmet, BorderLayout.PAGE_START);
		add(new JScrollPane(table), BorderLayout.CENTER);

		searchField.getDocument().addDocumentListener(
				new MyDocumentListener(searchField, sorter,0,2));
	}

	public MetabosTable getTable() {
		return table;
	}

	public void setButtons() {
		buttonmodifmet.setEnabled(table.getSelectedRowCount() == 1);
		buttonsuppmet.setEnabled(table.getSelectedRowCount() > 0);
	}

	public void update(Observable arg0, Object arg1) {
		table.update();
		buttonajoutmet.setEnabled(controler.hasLoadedProject());
		searchField.setEnabled(controler.hasLoadedProject());

		// Buttons are activated only if a line is selected
		buttonmodifmet.setEnabled(table.getSelectedRowCount() > 0);
		buttonsuppmet.setEnabled(table.getSelectedRowCount() > 0);
	}

}
