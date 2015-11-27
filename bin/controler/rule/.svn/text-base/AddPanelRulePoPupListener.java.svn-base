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

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controler.MainControler;
import controler.Var;

import model.Reaction;

import view.popup.AddRuleFrame;

public class AddPanelRulePoPupListener implements ActionListener {

	JPanel panel;
	JComboBox<String> operator;
	MainControler controler;
	AddRuleFrame f;
	// row number of JPanel in the frame
	int i;

	public AddPanelRulePoPupListener(JPanel panel, JComboBox<String> operator,
			MainControler controler, AddRuleFrame f) {
		this.panel = panel;
		this.operator = operator;
		this.controler = controler;
		this.f =f; //ruleFrame

	}

	public void actionPerformed(ActionEvent arg0) {
		
		ImageIcon delPanel = Var.suplittle;
		if (this.operator.getSelectedItem()!=""){

			JPanel nextPanel = new JPanel();

			// The model is created for each combobox
			ComboBoxModel<Reaction> model = new DefaultComboBoxModel<Reaction>();

			for (Reaction r : controler.getListReactions()) {
				((DefaultComboBoxModel<Reaction>) model).addElement(r);
			}

			JComboBox listReaction = new JComboBox<Reaction>(model);

			nextPanel.add(new JComboBox(model));
			nextPanel.add(new JLabel("="));
			nextPanel.add(new JComboBox<String>(new String[] { "0", "1" }));
			JComboBox<String> nextoperator = new JComboBox<String>(
					new String[] {"","AND", "OR"});
			nextPanel.add(nextoperator);
			nextoperator.addActionListener(new AddPanelRulePoPupListener(panel,
					nextoperator, controler,f));
			final JButton del = new JButton(delPanel);
			del.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					panel.remove(del.getParent());
					panel.revalidate();
					panel.repaint();
					f.pack();
				}

			});
			nextPanel.add(del);

			panel.add(nextPanel);
			panel.revalidate();
			panel.repaint();
			f.pack();
		}

	}

}
