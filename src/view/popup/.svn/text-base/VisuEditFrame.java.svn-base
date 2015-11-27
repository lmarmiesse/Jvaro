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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class VisuEditFrame extends PopUpAbs {

	private Color reacRevColor;
	private Color reacIrrevColor;
	private Color metabIntColor;
	private Color metabExtColor;
	private Color textColor;
	private JTextField textSizeField;
	private JButton okButton, cancelButton;
	private JPanel reacRevColorImage, reacIrrevColorImage, metabIntColorImage,
			metabExtColorImage, textColorImage;
	private int textSize;

	public VisuEditFrame(JFrame parent, Color reacRevColor,
			Color reacIrrevColor, Color metabIntColor, Color metabExtColor,
			Color textColor, int textSize) {
		super(parent);
		this.setTitle("Edit the image");
		this.textSize = textSize;
		this.reacRevColor = reacRevColor;
		this.reacIrrevColor = reacIrrevColor;
		this.metabIntColor = metabIntColor;
		this.metabExtColor = metabExtColor;
		this.textColor = textColor;

		cancelButton = new JButton("cancel");
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		okButton = new JButton("ok");
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

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));

		JPanel reactionRevPanel = new JPanel(new FlowLayout());
		JPanel reactionIrrevPanel = new JPanel(new FlowLayout());
		JPanel metabosIntPanel = new JPanel(new FlowLayout());
		JPanel metabosExtPanel = new JPanel(new FlowLayout());
		JPanel textColorPanel = new JPanel(new FlowLayout());
		JPanel textSizePanel = new JPanel(new FlowLayout());

		reactionRevPanel
				.add(new JLabel("Color of the reversible reactions : "));
		reacRevColorImage = new JPanel();
		reacRevColorImage.setBackground(reacRevColor);
		reacRevColorImage.setSize(70, 70);
		reactionRevPanel.add(reacRevColorImage);
		JButton editReacRevColor = new JButton("edit");
		reactionRevPanel.add(editReacRevColor);
		editReacRevColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color c = editColor(getReacRevColor());
				if (c != null)
					setReacRevColor(c);
				update();
			}
		});

		reactionIrrevPanel.add(new JLabel(
				"Color of the irreversible reactions : "));
		reacIrrevColorImage = new JPanel();
		reacIrrevColorImage.setBackground(reacIrrevColor);
		reacIrrevColorImage.setSize(70, 70);
		reactionIrrevPanel.add(reacIrrevColorImage);
		JButton editReacIrrevColor = new JButton("edit");
		reactionIrrevPanel.add(editReacIrrevColor);
		editReacIrrevColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color c = editColor(getReacIrrevColor());
				if (c != null)
					setReacIrrevColor(c);
				update();
			}
		});

		metabosIntPanel.add(new JLabel("Color of the internal metabolites : "));
		metabIntColorImage = new JPanel();
		metabIntColorImage.setBackground(metabIntColor);
		metabIntColorImage.setSize(70, 70);
		metabosIntPanel.add(metabIntColorImage);
		JButton editMetabIntColor = new JButton("edit");
		metabosIntPanel.add(editMetabIntColor);
		editMetabIntColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color c = editColor(getMetabIntColor());
				if (c != null)
					setMetabIntColor(c);
				update();
			}
		});

		metabosExtPanel.add(new JLabel("Color of the external metabolites : "));
		metabExtColorImage = new JPanel();
		metabExtColorImage.setBackground(metabExtColor);
		metabExtColorImage.setSize(70, 70);
		metabosExtPanel.add(metabExtColorImage);
		JButton editMetabExtColor = new JButton("edit");
		metabosExtPanel.add(editMetabExtColor);
		editMetabExtColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color c = editColor(getMetabExtColor());
				if (c != null)
					setMetabExtColor(c);
				update();
			}
		});

		textColorPanel.add(new JLabel("Color of the text : "));
		textColorImage = new JPanel();
		textColorImage.setBackground(textColor);
		textColorImage.setSize(70, 70);
		textColorPanel.add(textColorImage);
		JButton editTextColor = new JButton("edit");
		textColorPanel.add(editTextColor);
		editTextColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color c = editColor(getTextColor());
				if (c != null)
					setTextColor(c);
				update();
			}
		});

		textSizePanel.add(new JLabel("Size of the text : "));

		textSizeField = new JTextField(String.valueOf(textSize));
		textSizeField.setColumns(3);
		textSizePanel.add(textSizeField);

		centerPanel.add(reactionRevPanel);
		centerPanel.add(reactionIrrevPanel);
		centerPanel.add(metabosIntPanel);
		centerPanel.add(metabosExtPanel);
		centerPanel.add(textColorPanel);
		centerPanel.add(textSizePanel);

		add(bottomPanel, BorderLayout.PAGE_END);
		add(centerPanel, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public Color editColor(Color color) {
		Color newColor = JColorChooser
				.showDialog(this, "Choose a color", color);

		return newColor;

	}

	public void update() {
		reacRevColorImage.setBackground(reacRevColor);
		reacIrrevColorImage.setBackground(reacIrrevColor);
		metabIntColorImage.setBackground(metabIntColor);
		metabExtColorImage.setBackground(metabExtColor);
		textColorImage.setBackground(textColor);
	}

	private boolean noErrors() {
		try {
			Integer.parseInt(textSizeField.getText().trim());
			return true;
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(null, "Text size must be an integer",
					"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

	}

	public Color getReacRevColor() {
		return reacRevColor;
	}

	public Color getReacIrrevColor() {
		return reacIrrevColor;
	}

	public Color getMetabIntColor() {
		return metabIntColor;
	}

	public Color getMetabExtColor() {
		return metabExtColor;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setReacRevColor(Color c) {
		reacRevColor = c;
	}

	public void setReacIrrevColor(Color c) {
		reacIrrevColor = c;
	}

	public void setMetabIntColor(Color c) {
		metabIntColor = c;
	}

	public void setMetabExtColor(Color c) {
		metabExtColor = c;
	}

	public void setTextColor(Color c) {
		textColor = c;
	}

	public int getTextSize() {
		return Integer.parseInt(textSizeField.getText().trim());
	}
}
