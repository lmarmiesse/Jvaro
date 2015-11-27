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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import controler.MainControler;

import view.ReactionsPanel;

public class DelReacListener implements ActionListener, KeyListener  {

	private JTable table;
	private ReactionsPanel panel;
	private MainControler controler;

	public DelReacListener(MainControler controler, ReactionsPanel panel) {
		this.controler = controler;
		this.panel = panel;
		this.table = panel.getTable();

	}

	public void actionPerformed(ActionEvent arg0) {
		delReactions();
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==127 && table.hasFocus()){
			delReactions();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void delReactions(){
		int[] selectedIndices = table.getSelectedRows();
		String selectedReacs[] = new String[selectedIndices.length];

		for (int i = 0; i < selectedIndices.length; i++) {
			selectedReacs[i] = (String) table.getValueAt(selectedIndices[i], 0);
		}


		if (JOptionPane.showConfirmDialog(null,
				"Are you sure you want to delete "+ selectedReacs.length+" reaction(s) "
						+ "", "", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			for (int i = 0; i < selectedReacs.length; i++) {
				controler.delReaction(selectedReacs[i]);
				controler.notifyObservers();
				panel.update(controler, "");
			}
		}
	}

}

