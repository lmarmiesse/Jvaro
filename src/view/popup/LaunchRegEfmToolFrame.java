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
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class LaunchRegEfmToolFrame extends PopUpAbs {

	JRadioButton defaultAdvancedButton;
	JPanel centerPanel;

	// components which contains parameters
	JComboBox kind;
	JComboBox out;
	JComboBox level;
	JComboBox format;
	JComboBox comp;
	JComboBox adj;
	JTextField maxthread;
	JTextField ram;
	JComboBox arithmetic;
	JComboBox precision;
	JComboBox norm;
	ButtonGroup bg;
	JRadioButton yes;

	public LaunchRegEfmToolFrame(JFrame parent) {
		super(parent);
		setLayout(new BorderLayout());
		setTitle("regEfmtool options");
		

		JPanel bottomPanel = new JPanel();
		JButton okButton = new JButton("Run !");
		okButton.addActionListener(new ActionListener()

		{
			public void actionPerformed(ActionEvent arg0) {
				if (noErrors()) {
					okPressed = true;
					dispose();
				}

			}
		});
		JButton cancelButton = new JButton("cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		bottomPanel.add(okButton);
		bottomPanel.add(cancelButton);

		centerPanel = new JPanel();

		centerPanel.setLayout(new GridLayout(0, 2));
		centerPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		bg = new ButtonGroup();
		yes = new JRadioButton("yes");
		JRadioButton no = new JRadioButton("no");
		bg.add(yes);
		bg.add(no);
		yes.setSelected(true);
		centerPanel.add(new JLabel("Use gene rules :"));
		centerPanel.add(new JLabel(""));
		centerPanel.add(yes);
		centerPanel.add(no);

		
		centerPanel.add(new JLabel("Allocated memory (GB)"));
		
		System.out.println(Runtime.getRuntime().freeMemory());
		
		int totalMemory =(int) Math.ceil(Runtime.getRuntime().totalMemory()/1073741824.0);
				
		ram = new JTextField(String.valueOf(totalMemory));
		centerPanel.add(ram);
		
		
		centerPanel.add(new JLabel("kind :"));
		Object[] kindElements = new Object[] { "stoichiometry", "sbml" };
		kind = new JComboBox(kindElements);

		centerPanel.add(kind);

		centerPanel.add(new JLabel("out :"));
		Object[] outElements = new Object[] { "text-doubles", "text-boolean",
				"binary-boolean", "binary-doubles", "count", "null", "matlab",
				"matlab-directions" };
		out = new JComboBox(outElements);

		centerPanel.add(out);

		centerPanel.add(new JLabel("level :"));
		Object[] levelElements = new Object[] { "FINEST", "FINER", "FINE",
				"WARNING", "CONFIG", "INFO" };
		level = new JComboBox(levelElements);

		centerPanel.add(level);

		centerPanel.add(new JLabel("format :"));
		Object[] formatElements = new Object[] { "plain", "default" };
		format = new JComboBox(formatElements);

		centerPanel.add(format);

		centerPanel.add(new JLabel("compression :"));
		Object[] compElements = new Object[] { "default", "off", "unique",
				"nullspace", "unique-no-recursion", "nullspace-no-recursion" };
		comp = new JComboBox(compElements);

		centerPanel.add(comp);

		centerPanel.add(new JLabel("adjacency-method :"));
		Object[] adjElements = new Object[] { "default",
				"pattern-tree-minzero", "pattern-tree-rank" };
		adj = new JComboBox(adjElements);

		centerPanel.add(adj);

		centerPanel.add(new JLabel("maxthreads :"));
		maxthread = new JTextField(String.valueOf(Runtime.getRuntime().availableProcessors()));
		centerPanel.add(maxthread);
		
		centerPanel.add(new JLabel("arithmetic :"));
		Object[] ariElements = new Object[] { "double", "fractional" };
		arithmetic = new JComboBox(ariElements);

		centerPanel.add(arithmetic);
		
		centerPanel.add(new JLabel("precision :"));
		Object[] preElements = new Object[] { "-1", "128","256" };
		precision = new JComboBox(preElements);

		centerPanel.add(precision);

		centerPanel.add(new JLabel("normalize :"));
		Object[] normElements = new Object[] { "none","max", "min", "norm2", "squared"};
		norm = new JComboBox(normElements);

		centerPanel.add(norm);
		
		
		defaultAdvancedButton = new JRadioButton("Advanced setup");
		defaultAdvancedButton.setSelected(false);
		defaultAdvancedButton.addActionListener(new ActionListener()

		{
			public void actionPerformed(ActionEvent arg0) {
				if (noErrors()) {
					update();
				}

			}
		});
		add(defaultAdvancedButton, BorderLayout.PAGE_START);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);
		pack();
		setLocationRelativeTo(null);
		update();
		setVisible(true);

	}

	private boolean noErrors() {

		try {
			double d = Integer.parseInt(maxthread.getText());
		} catch (NumberFormatException nfe) {

			JOptionPane.showMessageDialog(null,
					"maxthread must be an integer", "Error",
					JOptionPane.ERROR_MESSAGE);

			return false;
		}
		
		try {
			double d = Integer.parseInt(ram.getText());
		} catch (NumberFormatException nfe) {

			JOptionPane.showMessageDialog(null,
					"Alocated ram must be an integer", "Error",
					JOptionPane.ERROR_MESSAGE);

			return false;
		}

		return true;
	}

	public Map<String, String> getParameters() {
		Map<String, String> params = new HashMap<String, String>();

		params.put("ram",ram.getText());
		params.put("kind", kind.getSelectedItem().toString());
		params.put("out", out.getSelectedItem().toString());
		params.put("level", level.getSelectedItem().toString());
		params.put("format", format.getSelectedItem().toString());
		params.put("comp", comp.getSelectedItem().toString());
		params.put("adj", adj.getSelectedItem().toString());
		params.put("maxthreads", maxthread.getText());
		params.put("ari", arithmetic.getSelectedItem().toString());
		params.put("pre", precision.getSelectedItem().toString());
		params.put("norm", norm.getSelectedItem().toString());
		

		return params;
	}
	
	public boolean geneRule(){
		return (yes.isSelected());
	}

	public void update() {

		for (Component c : centerPanel.getComponents()) {
			c.setEnabled(defaultAdvancedButton.isSelected());
		}
	}
}
