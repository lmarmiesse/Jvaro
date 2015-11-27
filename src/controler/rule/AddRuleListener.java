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

package controler.rule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import controler.MainControler;

import view.GeneralFrame;
import view.popup.AddRuleFrame;

public class AddRuleListener implements ActionListener {
	private MainControler controler;

	public AddRuleListener(MainControler controler) {
		this.controler = controler;
	}



	public void actionPerformed(ActionEvent e) {
		String rule = "";
		if (!(controler.getListReactions().size()<2)){
			AddRuleFrame f = new AddRuleFrame(GeneralFrame.getInstance(controler),
					controler,rule);
			if (f.okPressed()) {
				rule =f.getRule();
				controler.addRule(rule);
				controler.notifyObservers();
			}
		}
		else {
			JOptionPane.showMessageDialog(null,
					"You have a least two reactions in your network", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

}
