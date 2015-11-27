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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class MetaboliteFrame extends PopUpAbs {

	JTextField name, description;
	JRadioButton intern, extern;

	public MetaboliteFrame(JFrame parent, boolean internal, String metName,
			String metDescription) {
		super(parent);

		setTitle("Metabolite");

		JLabel n = new JLabel("Name",JLabel.CENTER);
		JLabel d = new JLabel("Description",JLabel.CENTER);

		name = new JTextField(15);
		description = new JTextField(15);

		ButtonGroup bg = new ButtonGroup();
		intern = new JRadioButton("intern");
		extern = new JRadioButton("extern");

		JButton ok = new JButton("ok");
		JButton cancel = new JButton("cancel");

		description.setText(metDescription);
		name.setText(metName);

		bg.add(intern);
		bg.add(extern);
		intern.setSelected(internal);
		extern.setSelected(!internal);

		setLayout(new GridLayout(4, 2));


		add(n);
		add(name);

		add(d);
		add(description);

		add(intern);
		add(extern);
		
		add(ok);
		add(cancel);
		

		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				dispose();
			}
		});
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (noErrors()) {
					okPressed = true;
					dispose();
				} else {
					JOptionPane.showMessageDialog(null,
							"Metabolite name is not valid", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	public String getName() {
		return name.getText();
	}

	public String getDescription() {
		return description.getText();
	}

	public boolean isInternal() {
		if (intern.isSelected()) {
			return true;
		}
		return false;
	}

	public boolean noErrors() {
		if (name.getText().equals("")) {
			return false;
		}
		return true;

	}
}
