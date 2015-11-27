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

import controler.MainControler;

import view.GeneralFrame;
import view.popup.MetaboliteFrame;

//Add metabolite when add button is clicked
public class AddMetabListener implements ActionListener {

	private MainControler controler;

	public AddMetabListener(MainControler controler) {
		this.controler = controler;

	}

	public void actionPerformed(ActionEvent arg0) {
		MetaboliteFrame f = new MetaboliteFrame(
				GeneralFrame.getInstance(controler), true, "", "");
		
		//if ok is pressed and metabolite name is not empty
		//metabolite is added
		if (f.okPressed() && !f.getName().equals("")) {
			String name = controler.getNetwork().formatName(f.getName());
			if (!controler.getNetwork().hasName(name)) {
				controler.addMetabolite(name, f.getDescription(),
						f.isInternal());
				controler.notifyObservers();
			} else {
				JOptionPane.showMessageDialog(null,
						"Another reaction or metabolite has the same name", "Error",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}
}
