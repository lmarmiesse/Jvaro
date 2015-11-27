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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import controler.MainControler;

import model.Metabolite;
import model.Reaction;
import view.MetabosPanel;

//del metabolites selected 
//delete button or delete key
public class DelMetabListener implements ActionListener, KeyListener  {

	private JTable table;
	private MainControler controler;

	public DelMetabListener(MainControler controler, MetabosPanel panel) {
		this.controler = controler;
		this.table = panel.getTable();

	}

	public void actionPerformed(ActionEvent arg0) {
		delMetabolites();
	}

	
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode()==127 && table.hasFocus()){
			delMetabolites();
		}

	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void delMetabolites(){

		int[] selectedIndices = table.getSelectedRows();
		String selectedMets [] = new String[selectedIndices.length];
		
		// list of deleted reactions when a metabolite is deleted
		List<String> concernedReactions = new ArrayList<String>();

		for (int i = 0; i < selectedIndices.length; i++) {
			selectedMets[i]=(String) table.getValueAt(selectedIndices[i], 0);
		}


		for (int i = 0; i < selectedMets.length; i++) {

			

			for (Reaction reac : controler.getListReactions()) {
				for (Metabolite met : reac.getListMetabolite()) {
					if (met.getName() == selectedMets[i]) {
						concernedReactions.add(reac.getName());
					}
				}
			}

		}
		String warningMessage = "Are you sure you want to delete "
				+ selectedMets.length + " metabolite(s) ? ";

		if (concernedReactions.size() != 0) {
			warningMessage += "\nIt would also remove the reactions :"
					+ reacs(concernedReactions);

		}

			if (JOptionPane.showConfirmDialog(null, warningMessage, "",
					JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

				for (String reac : concernedReactions) {
					controler.delReaction(reac);
				}
				
				for (String m : selectedMets){
					controler.delMetabolite(m);
					
				}
				controler.notifyObservers();
			}
		}
	
	public String reacs(List<String> liste) {
		int i=0;
		String result = "";
		for (String reacName : liste) {
			result += "" + reacName + " ";
			if (i%10==0){
				result +="\n";
			}
			i+=1;
		}

		return result;

	}

}
