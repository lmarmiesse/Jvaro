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

import view.popup.ReactionFrame;


//Add a metabolite in a reaction
public class AddMetaboliteReactionListener implements ActionListener {
	private ReactionFrame frame;
	private String type;

	public AddMetaboliteReactionListener(ReactionFrame frame, String type) {
		this.frame = frame;
		this.type = type;
	}


	public void actionPerformed(ActionEvent e) {
		if (type.equals("reactant")) {
			if (frame.getReactants().containsKey(frame.getMetabolite())) {
				System.out.println(frame.getMetabolite());

			} else {
				frame.addReactant();
			}
		}
		if (type.equals("product")) {
			if (frame.getProducts().containsKey(frame.getMetabolite())) {

			} else {
				frame.addProduct();
			}
		}
	}

}
