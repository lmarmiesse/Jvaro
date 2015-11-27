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

package view;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;

import controler.MainControler;
import controler.RulesListSelectionListener;
import controler.Var;
import controler.rule.AddRuleListener;
import controler.rule.DelRuleListener;
import controler.rule.EditRuleListener;

public class RulesPanel extends JPanel implements Observer {



	private JButton buttonajoutrule = new JButton(Var.iconajout);
	private JButton buttonmodifrule = new JButton(Var.iconmodif);
	private JButton buttonsupprule = new JButton(Var.iconsupp);

	private MainControler controler;

	private JList<String> listRule;
	private DefaultListModel<String> model;

	public RulesPanel(MainControler controler) {

		this.controler = controler;

		buttonajoutrule.setEnabled(false);
		buttonmodifrule.setEnabled(false);
		buttonsupprule.setEnabled(false);

		buttonajoutrule.setToolTipText("Add a rule");
		buttonmodifrule.setToolTipText("Edit");
		buttonsupprule.setToolTipText("Remove");

		buttonsupprule.addActionListener(new DelRuleListener(controler, this));
		buttonajoutrule.addActionListener(new AddRuleListener(controler));
		buttonmodifrule
				.addActionListener(new EditRuleListener(controler, this));

		JToolBar toolbarrule = new JToolBar();
		toolbarrule.setFloatable(false);
		model = new DefaultListModel<String>();
		listRule = new JList<String>(model);
		listRule.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listRule.addListSelectionListener(new RulesListSelectionListener(this,
				listRule));
		
		//delete key delete rule
		listRule.addKeyListener(new DelRuleListener(controler, this));

		toolbarrule.add(buttonajoutrule);
		toolbarrule.add(buttonmodifrule);
		toolbarrule.add(buttonsupprule);
		setLayout(new BorderLayout());
		add(toolbarrule, BorderLayout.PAGE_START);
		add(new JScrollPane(listRule));
		controler.addObserver(this);

	}

	public void setEnableButtons(boolean enabled) {
		buttonmodifrule.setEnabled(enabled);
		buttonsupprule.setEnabled(enabled);

	}

	public void update(Observable o, Object arg) {

		buttonajoutrule.setEnabled(controler.hasLoadedProject());

		if (!model.isEmpty()) {
			model.removeAllElements();
		}
		List<String> listRules = controler.getRules();
		for (String rule : listRules) {
			model.addElement(rule);
		}
		buttonmodifrule.setEnabled(listRule.getSelectedValue() != null);
		buttonsupprule.setEnabled(listRule.getSelectedValue() != null);

	}

	public JList<String> getList() {
		return listRule;
	}

	public String getSelected() {
		return listRule.getSelectedValue();
	}

}
