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
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.xml.stream.XMLStreamException;

import model.MetabolicNetworkItf;
import model.Metabolite;
import model.Reaction;
import model.data.SBMLFormat;

import org.sbml.jsbml.SBMLException;

import view.ReactionsPanel;
import controler.MainControler;

public class AddDbListener implements ActionListener {

	private MainControler controler;
	private JTable table;

	public AddDbListener(MainControler controler, ReactionsPanel reactionsPanel) {
		this.controler = controler;
		this.table = reactionsPanel.getTable();
	}

	public void actionPerformed(ActionEvent e) {

		MetabolicNetworkItf dbNetwork = controler.getDataBaseNetwork();

		int[] selectedIndices = table.getSelectedRows();
		String selectedReacs[] = new String[selectedIndices.length];

		for (int i = 0; i < selectedIndices.length; i++) {
			selectedReacs[i] = (String) table.getValueAt(selectedIndices[i], 0);
		}

		for (int i = 0; i < selectedReacs.length; i++) {

			Reaction r = controler.getReaction(selectedReacs[i]);

			for (Metabolite m  : r.getListMetabolite()){
				dbNetwork.AddMetabolite(m.getName(), m.getDescription(), m.isInternal());
			}

			dbNetwork.addReaction(r.getName(), r.getReactants(),
					r.getProducts(), r.isReversible());

		}

		try {
			new SBMLFormat().save(new File("dataBase.xml"), dbNetwork, false);
			
			
			JOptionPane.showMessageDialog(null, "Reactions added to the data base !", "",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (SBMLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (XMLStreamException e1) {
			e1.printStackTrace();
		}
		
	}

}
