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

package controler.save;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import controler.MainControler;

//Save the network
public class ButtonSaveListener implements ActionListener {

	private MainControler controler;

	public ButtonSaveListener(MainControler controler) {
		this.controler = controler;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			controler.saveProject();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
