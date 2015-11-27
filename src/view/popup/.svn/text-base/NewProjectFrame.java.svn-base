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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewProjectFrame extends PopUpAbs {

	private JTextField nameField;
	private JTextField pathField;

	public NewProjectFrame(JFrame generalFrame) {

		super(generalFrame);
		okPressed = false;

		JPanel centerPanel = new JPanel();

		setLayout(new BorderLayout());
		centerPanel.add(new JLabel("Project name:"));
		nameField = new JTextField(10);
		pathField = new JTextField(System.getProperty("user.home"));
		centerPanel.add(nameField);

		centerPanel.add(new JLabel("Save directory:"));
		centerPanel.add(pathField);

		JButton chooser = new JButton("choose directory");
		chooser.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					pathField.setText(file.getAbsolutePath());
				}

			}
		});

		centerPanel.add(chooser);
		JButton okButton = new JButton("ok");
		JButton cancelButton = new JButton("cancel");
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (noErrors()) {
					okPressed = true;
					dispose();
				}
			}
		});

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(okButton);
		bottomPanel.add(cancelButton);

		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setTitle("New project");
		this.pack();
		setLocationRelativeTo(null);
		this.setVisible(true);

	}

	public String getName() {
		return nameField.getText();
	}

	public String getPath() {
		return pathField.getText();
	}

	public boolean noErrors() {

		File f = new File(pathField.getText());

		if (nameField.getText().length() == 0) {
			JOptionPane.showMessageDialog(null, "Project name is empty",
					"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!f.isDirectory()) {
			JOptionPane.showMessageDialog(null,
					"You must give an existing directory", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

}
