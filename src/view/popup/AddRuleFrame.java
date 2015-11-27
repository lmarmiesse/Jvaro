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

package view.popup;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Reaction;

import controler.MainControler;
import controler.Var;
import controler.rule.AddPanelRulePoPupListener;

public class AddRuleFrame extends PopUpAbs {

	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JPanel bottomPanel;
	private JComboBox<Reaction> listReaction1;

	public AddRuleFrame(JFrame generalFrame, MainControler controler,
			String rule) {

		super(generalFrame);
		okPressed = false;

		// List of the reactions
		ComboBoxModel<Reaction> model1 = new DefaultComboBoxModel<Reaction>();
		for (Reaction r : controler.getListReactions()) {
			((DefaultComboBoxModel<Reaction>) model1).addElement(r);
		}
		listReaction1 = new JComboBox<Reaction>(model1);

		ComboBoxModel<Reaction> model2 = new DefaultComboBoxModel<Reaction>();
		for (Reaction r : controler.getListReactions()) {
			((DefaultComboBoxModel<Reaction>) model2).addElement(r);
		}
		JComboBox<Reaction> listReaction2 = new JComboBox<Reaction>(model2);

		setLayout(new BorderLayout());
		JLabel StringIf = new JLabel("IF");
		JLabel StringEqual = new JLabel("=");
		JComboBox<String> operator = new JComboBox<String>(new String[] {
				"","AND", "OR" });

		JComboBox<String> flux = new JComboBox<String>(
				new String[] { "0", "1" });

		JComboBox<String> fluxEnd = new JComboBox<String>(
				new String[] { "0", "1" });

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		JPanel firstPanel = new JPanel();
		firstPanel.setLayout(new FlowLayout());
		firstPanel.add(StringIf);
		firstPanel.add(listReaction1);
		firstPanel.add(StringEqual);
		firstPanel.add(flux);
		firstPanel.add(operator);

		JPanel secondPanel = new JPanel();
		secondPanel.add(new JLabel("THEN"));
		secondPanel.add(listReaction2);
		secondPanel.add(new JLabel("="));
		secondPanel.add(fluxEnd);

		JButton okButton = new JButton("ok");
		JButton cancelButton = new JButton("cancel");

		// ok
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		// cancel
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (noErrors()) {
					okPressed = true;
					dispose();
				}
			}
		});

		operator.addActionListener(new AddPanelRulePoPupListener(panel,
				operator, controler,this));
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.PAGE_AXIS));

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		panel.add(firstPanel);

		bottomPanel.add(secondPanel);
		bottomPanel.add(buttonPanel);
		add(panel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);

		if (!rule.equals("")) { // edit rule
			System.out.println(rule);
			ImageIcon delPanel =Var.suplittle;

			rule = rule.replace("   ", " ");
			rule = rule.replace("  "," ");
			String[] tab = rule.split(" ");

			//values of IF panel
			listReaction1.setSelectedItem(controler.getReaction(tab[1]));
			flux.setSelectedItem(tab[3]);
			operator.setSelectedItem(tab[4]);
			if (tab.length>8){
				panel.remove(1);
			}
			//values of THEN panel
			listReaction2.setSelectedItem(controler.getReaction(tab[tab.length-3]));
			fluxEnd.setSelectedItem(tab[tab.length-1]);

			//add panels OR / AND
			for (int j=4;j<tab.length-4;j+=4){
				System.out.println(j+tab[j]);
				JPanel nextPanel = new JPanel();

				// The model is created for each combobox
				ComboBoxModel<Reaction> model = new DefaultComboBoxModel<Reaction>();

				for (Reaction r : controler.getListReactions()) {
					((DefaultComboBoxModel<Reaction>) model).addElement(r);
				}

				JComboBox listReaction = new JComboBox<Reaction>(model);
				listReaction.setSelectedItem(controler.getReaction(tab[j+1]));
				JComboBox<String> nextoperator = new JComboBox<String>(
						new String[] {"","AND", "OR"});
				nextoperator.setSelectedItem(tab[j+4]);

				JComboBox<String> flux2 = new JComboBox<String>(new String[] { "0", "1" });
				System.out.println("j+3"+tab[j+3]);
				flux2.setSelectedItem(tab[j+3]);

				nextPanel.add(new JComboBox(model));
				nextPanel.add(new JLabel("="));
				nextPanel.add(flux2);
				nextPanel.add(nextoperator);
				nextoperator.addActionListener(new AddPanelRulePoPupListener(panel,
						nextoperator, controler,this));

				final JButton del = new JButton(delPanel);
				del.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						panel.remove(del.getParent());
						panel.revalidate();
						panel.repaint();
						pack();
					}

				});
				nextPanel.add(del);

				panel.add(nextPanel);
				panel.revalidate();
				panel.repaint();
				pack();
			}
		}


		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Add a rule");
		this.pack();
		setLocationRelativeTo(null);
		this.setVisible(true);

	}

	// collect all the components of the frame
	public List<Component> getListComponents() {
		List<Component> listComponent = new ArrayList<Component>();
		for (Component c : panel.getComponents()) {
			if (c instanceof JPanel) {
				for (Component comp : ((JPanel) c).getComponents()) {
					listComponent.add(comp);
				}
			}
		}
		for (Component c : bottomPanel.getComponents()) {
			if (c instanceof JPanel) {
				for (Component comp : ((JPanel) c).getComponents()) {
					listComponent.add(comp);
				}
			}
		}
		return listComponent;
	}

	// return rule in string
	public String getRule() {
		String rule = "";
		for (Component comp : getListComponents()) {
			if (comp instanceof JComboBox) {
				rule += ((JComboBox<?>) comp).getSelectedItem();
			} else if (comp instanceof JLabel) {
				rule += ((JLabel) comp).getText();
			} else if (comp instanceof JTextField) {
				rule += ((JTextField) comp).getText();
			}
			rule += " ";
		}
		rule = rule.replace("  "," ");
		rule = rule.replace("   "," ");
		return rule;
	}

	public boolean noErrors() {
		String str = getRule().replace("   "," "); 
		str = str.replace("  "," ");
		String[] tab = str.split(" ");
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(tab));
		String OutputReaction = listReaction1.getSelectedItem().toString();
		//output reaction can't be an input reaction
		if (Collections.frequency(list, OutputReaction) > 1) {
					JOptionPane.showMessageDialog(null, "The output reaction must never be an input reaction.", " ",
							JOptionPane.ERROR_MESSAGE);
			return false;
		}
		//operator before then must be empty
		else if ((!tab[tab.length-5].equals("0")) && (!tab[tab.length-5].equals("1"))){
				JOptionPane.showMessageDialog(null, "No operator before THEN ", " ",
						JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

}
