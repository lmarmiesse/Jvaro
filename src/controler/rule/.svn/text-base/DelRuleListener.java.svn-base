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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import view.RulesPanel;
import controler.MainControler;

public class DelRuleListener implements ActionListener, KeyListener {
	private RulesPanel panel;
	private MainControler controler;

	public DelRuleListener(MainControler controler, RulesPanel panel) {
		this.controler = controler;
		this.panel = panel;
		
	}


	public void actionPerformed(ActionEvent e) {
		String rule = panel.getSelected();
		controler.delRule(rule);
		controler.notifyObservers();
	}


	@Override
	public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 127) {
				String rule = panel.getSelected();
				controler.delRule(rule);
				controler.notifyObservers();
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

}
