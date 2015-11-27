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

package controler.reactions;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import controler.MainControler;

import view.GeneralFrame;
import view.ReactionsPanel;

public class ReactionsTableCellEditorListener implements CellEditorListener {

	private ReactionsPanel panel;
	private JTable table;
	private MainControler controler;

	public ReactionsTableCellEditorListener(MainControler controler,
			ReactionsPanel reactionsPanel) {
		this.panel = reactionsPanel;
		this.controler = controler;
		this.table = reactionsPanel.getTable();
	}

	public void editingCanceled(ChangeEvent arg0) {

	}

	public void editingStopped(ChangeEvent e) {

		String reacName = (String) table.getValueAt(table.getSelectedRow(), 0);

		controler.setReactionReversible(
				reacName,
				(Boolean) table.getValueAt(table.getSelectedRow(),
						table.getSelectedColumn()));

		table.clearSelection();
		panel.setButtons();
		GeneralFrame.getInstance(controler).enableSave();

	}

}
