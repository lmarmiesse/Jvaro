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

public class RemoveMetaboliteReactionListener implements ActionListener {

	private ReactionFrame frame;
	private String type;

	public RemoveMetaboliteReactionListener(ReactionFrame frame, String type) {
		this.frame = frame;
		this.type = type;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (type.equals("reactant")) {
			frame.delReactant();
		}
		if (type.equals("product")) {
			frame.delProduct();
		}

	}

}
