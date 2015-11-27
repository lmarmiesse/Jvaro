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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import controler.MainControler;

import view.GeneralFrame;
import view.MetabosPanel;
import view.popup.MetaboliteFrame;


//edit metabolite by edit button
public class EditMetabListener implements ActionListener {

	private JTable table;
	private MainControler controler;

	public EditMetabListener(MainControler controler, MetabosPanel panel) {
		this.controler = controler;
		this.table = panel.getTable();

	}

	public void actionPerformed(ActionEvent arg0) {
		//get the name of the metabolite and the name
		// to pre fill the fields
		String metName = (String) table.getValueAt(table.getSelectedRow(), 0);
		String metDescription = (String) table.getValueAt(
				table.getSelectedRow(), 2);
		//show edit frame
		MetaboliteFrame f = new MetaboliteFrame(
				GeneralFrame.getInstance(controler),
				(Boolean) table.getValueAt(table.getSelectedRow(), 1), metName,
				metDescription);
		//replace old values
		if (f.okPressed() && !f.getName().equals("")) {
			if (!controler.getNetwork().hasName(f.getName()) || (f.getName().toLowerCase().equals(metName.toLowerCase()))){
			controler.setMetaboInternal(metName, f.isInternal());
			controler.setMetabo(metName, f.getName(), f.getDescription());
			}
			else {
				JOptionPane.showMessageDialog(null,
						"Another reaction or metabolite has the same name", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			controler.notifyObservers();
		}
	}
}
