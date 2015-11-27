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

import javax.swing.JOptionPane;

import controler.MainControler;

import model.Metabolite;

import view.GeneralFrame;
import view.popup.ReactionFrame;

//Add reaction
public class AddReacListener implements ActionListener {

	private MainControler controler;

	public AddReacListener(MainControler controler) {
		this.controler = controler;
	}

	public void actionPerformed(ActionEvent e) {

		if (!(controler.getListMetabos().size()==0)){
			ReactionFrame f = new ReactionFrame(
					GeneralFrame.getInstance(controler), controler, null, false);
			if (f.okPressed()) {
				String enzyme = controler.getNetwork().formatName(f.getName());
				if (!controler.getNetwork().hasName(enzyme)){
				boolean reversible = f.getReversible();
				Map<Metabolite, Double> r = f.getReactants();
				Map<Metabolite, Double> p = f.getProducts();
				controler.addReaction(enzyme, r, p, reversible);
				controler.notifyObservers();
				}
				else {
					JOptionPane.showMessageDialog(null,
							"Another reaction or metabolite has the same name", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				
			}
		}
		else {
			JOptionPane.showMessageDialog(null,
					"You must have at least one metabolite in your network", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

}
