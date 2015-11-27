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

package controler;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import view.ButtonTabComponent;

//Add close buttons JTabbedPane when a change occurs on the jtabbedpane
public class ChangeTabListener implements ChangeListener {

	private JTabbedPane tabbedPane;

	
	public ChangeTabListener(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}
	
	//two first panels (reactions and rules) 
	//are not closable
	public void stateChanged(ChangeEvent e) {
		for (int i = 2; i < tabbedPane.getTabCount(); i++) {
			tabbedPane.setTabComponentAt(i, new ButtonTabComponent(tabbedPane));
		}

	}

}
