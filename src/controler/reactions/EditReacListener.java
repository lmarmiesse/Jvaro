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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import controler.MainControler;

import model.Metabolite;

import view.GeneralFrame;
import view.ReactionsPanel;
import view.popup.ReactionFrame;

public class EditReacListener implements ActionListener {
	private MainControler controler;
	private JTable table;

	public EditReacListener(MainControler controler,
			ReactionsPanel reactionsPanel) {
		this.controler = controler;
		this.table = reactionsPanel.getTable();
	}

	public void actionPerformed(ActionEvent e) {
		String enzName = (String) table.getValueAt(table.getSelectedRow(), 0);
		boolean isReversible = (Boolean) table.getValueAt(
				table.getSelectedRow(), 1);
		ReactionFrame f = new ReactionFrame(
				GeneralFrame.getInstance(controler), controler, enzName,
				isReversible);

		if (f.okPressed()) {
			boolean reversible = f.getReversible();
			Map<Metabolite, Double> r = f.getReactants();
			Map<Metabolite, Double> p = f.getProducts();
			controler.delMetabolitesReaction(enzName);
			for (Entry<Metabolite, Double> entry : r.entrySet()) {
				controler
				.addReactant(enzName, entry.getKey(), entry.getValue());
			}
			for (Entry<Metabolite, Double> entry : p.entrySet()) {
				controler.addProduct(enzName, entry.getKey(), entry.getValue());
			}
			System.out.println(f.getName());
			System.out.println(enzName);
			if (!controler.getNetwork().hasName(f.getName()) || (f.getName().toLowerCase().equals(enzName.toLowerCase()))){
				controler.setReactionReversible(enzName, reversible);
				controler.setReactionName(enzName, f.getName());
			}
			else {
				JOptionPane.showMessageDialog(null,
						"Another reaction or metabolite has the same name", "Error",
						JOptionPane.ERROR_MESSAGE);
			}

		}
		controler.notifyObservers();
	}

}
