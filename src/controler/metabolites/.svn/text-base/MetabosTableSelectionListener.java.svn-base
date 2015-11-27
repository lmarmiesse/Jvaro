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

package controler.metabolites;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controler.MainControler;

import view.MetabosPanel;

//set the panel buttons depending on the selection of JTables
public class MetabosTableSelectionListener implements ListSelectionListener {
	private MetabosPanel panel;
	private JTable table;

	public MetabosTableSelectionListener(MetabosPanel metabosPanel,
			String col2, MainControler controler) {
		this.panel = metabosPanel;
		this.table = metabosPanel.getTable();
	}

	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
			panel.setButtons();
		}

	}
}
