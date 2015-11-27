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
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.apache.batik.dom.util.DOMUtilities;
import org.w3c.dom.Element;

import view.popup.VisuEditFrame;
import controler.Download;
import controler.MainControler;
import controler.Var;
import controler.visu.MySvgCanvas;

public class VisualizationPanel extends JPanel {

	private File svgFile;

	private JPanel svgPanel = new JPanel();
	// The SVG canvas.
	private MySvgCanvas svgCanvas;
	private MainControler controler;

	// Buttons
	private JButton undo = new JButton(Var.undo);
	private JButton redo = new JButton(Var.redo);
	private JButton download = new JButton(Var.download);;
	private JButton addToProject = new JButton(Var.iconaddtoproject);
	private JButton removeFromProject = new JButton(Var.iconremoveproject);
	private JButton editImageButton = new JButton(Var.iconmodif);
	private JButton saveSvg = new JButton(Var.savelittle);

	private JToolBar toolbar = new JToolBar();

	// list of svgCanvas for undo/redo
	private List<MySvgCanvas> svgList = new ArrayList<MySvgCanvas>();

	// colors
	private Color reacRevColor = new Color(Var.reacRevColor.getRed(),
			Var.reacRevColor.getGreen(), Var.reacRevColor.getBlue(),
			Var.reacRevColor.getAlpha());
	private Color reacIrrevColor = new Color(Var.reacIrrevColor.getRed(),
			Var.reacIrrevColor.getGreen(), Var.reacIrrevColor.getBlue(),
			Var.reacIrrevColor.getAlpha());
	private Color metabIntColor = new Color(Var.metabIntColor.getRed(),
			Var.metabIntColor.getGreen(), Var.metabIntColor.getBlue(),
			Var.metabIntColor.getAlpha());
	private Color metabExtColor = new Color(Var.metabExtColor.getRed(),
			Var.metabExtColor.getGreen(), Var.metabExtColor.getBlue(),
			Var.metabExtColor.getAlpha());
	private Color textColor = new Color(Var.textColor.getRed(),
			Var.textColor.getGreen(), Var.textColor.getBlue(),
			Var.textColor.getAlpha());

	public VisualizationPanel(File svgFile, MainControler controler) {

		toolbar.setFloatable(false);
		this.controler = controler;
		this.svgFile = svgFile;
		this.setLayout(new BorderLayout());
		svgCanvas = new MySvgCanvas(svgFile, controler, this);
		svgCanvas.setSize(getSize());

		// initialisation of the list for undo/redo
		svgList.add(svgCanvas);

		addToProject.setToolTipText("Attach this image to the project");
		removeFromProject.setToolTipText("Remove this image from the project");
		undo.setToolTipText("undo");
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				undo();
			}
		});
		redo.setToolTipText("redo");
		redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				redo();
			}
		});

		saveSvg.setToolTipText("Save changes");
		saveSvg.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				svgChange();
				saveImage();
			}
		});

		editImageButton.setToolTipText("Edit image");

		editImageButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				openVisuEditFrame();
			}
		});

		download.setToolTipText("Download image (SVG)");
		download.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String downloadContent = getSvgString();
				new Download(downloadContent, "Download the svg file", ".svg");

			}
		});

		setToolBar();

		// List<Interactor> interactors = svgCanvas.getInteractors();
		// interactors.add(new SVGInteractor());

		// add eventListeners

		add(toolbar, BorderLayout.PAGE_START);

		add(new JScrollPane(svgPanel), BorderLayout.CENTER);
		repaint();
	}

	public void addImageToProject() {
		// file are in saved repertory
		File copy = new File("tulip/svg/saved/" + svgFile.getName());

		// move tlp file
		new File("tulip/tlp/"
				+ svgFile.getName()
						.substring(0, svgFile.getName().length() - 4))
				.renameTo(new File("tulip/tlp/saved/"
						+ svgFile.getName().substring(0,
								svgFile.getName().length() - 4)));

		if (svgFile.renameTo(copy))

			// save it in the project
			svgFile = copy;
		controler.getProject().addImagePath(copy.getAbsolutePath());
		for (String path : controler.getProject().getImagePaths()) {

			System.out.println(path);
		}
		
		try {
			controler.saveProject();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void removeImageFromProject() {
		// remove the file of the project
		controler.getProject().removeImagePath(svgFile.getAbsolutePath());
		// and remove from saved repertory
		File copy = new File("tulip/svg/" + svgFile.getName());

		svgFile.renameTo(copy);
		svgFile = copy;

		new File("tulip/tlp/saved/"
				+ svgFile.getName()
						.substring(0, svgFile.getName().length() - 4))
				.renameTo(new File("tulip/tlp/"
						+ svgFile.getName().substring(0,
								svgFile.getName().length() - 4)));
		
		try {
			controler.saveProject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public JPanel panel() {
		return this;
	}

	public void setToolBar() {

		toolbar.removeAll();
		toolbar.add(undo);
		// undo is possible only if there is svgs before
		undo.setEnabled(svgList.indexOf(svgCanvas) > 0);
		toolbar.add(redo);
		// undo is possible only if there is svgs after
		redo.setEnabled(svgList.indexOf(svgCanvas) < svgList.size() - 1);

		toolbar.add(saveSvg);
		toolbar.add(editImageButton);

		// if the controler doesn't contain this picture
		if (!controler.getProject().getImagePaths()
				.contains(svgFile.getAbsolutePath())) {
			addToProject.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					addImageToProject();
					controler.notifyObservers();
				}
			});
			toolbar.add(addToProject);
		} else {
			removeFromProject.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					removeImageFromProject();
					controler.notifyObservers();
				}
			});
			toolbar.add(removeFromProject);
		}

		toolbar.add(download);

		svgPanel.removeAll();
		svgPanel.add(svgCanvas);
		svgPanel.revalidate();
		this.repaint();

	}

	public void update() {
		setToolBar();
	}

	public void saveImage() {
		Writer writer = null;
		try {
			writer = new PrintWriter(new FileOutputStream(
					svgFile.getAbsoluteFile()));
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		try {

			DOMUtilities.writeDocument(svgCanvas.getSVGDocument(), writer);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void openVisuEditFrame() {

		
		// we collect size of the text
		
		Element textElement = (Element) svgCanvas.getSVGDocument()
				.getElementsByTagName("text").item(0);

		String style = textElement.getAttribute("style");

		style = style.substring(style.indexOf("fill:rgb"));

		
		style = textElement.getAttribute("style");

		style = style.split(";")[0];

		int textSize = Integer.parseInt(style.replace("font-size:", ""));

		VisuEditFrame vef = new VisuEditFrame(
				GeneralFrame.getInstance(controler), reacRevColor, reacIrrevColor,metabIntColor,metabExtColor,
				textColor, textSize);

		// if "ok" is pressed, picture is updated
		if (vef.okPressed()) {

			svgChange();
			svgCanvas.reDraw(vef.getReacRevColor(), vef.getReacIrrevColor(),vef.getMetabIntColor(),vef.getMetabExtColor(),
					vef.getTextColor(), vef.getTextSize());
			
			this.reacRevColor=vef.getReacRevColor();
			this.reacIrrevColor=vef.getReacIrrevColor();
			this.metabIntColor=vef.getMetabIntColor();
			this.metabExtColor=vef.getMetabExtColor();
			this.textColor=vef.getTextColor();
		}
	}

	// save the current state of the document
	public void svgChange() {

		// if last position of the list

		if (svgList.indexOf(svgCanvas) == svgList.size() - 1) {

			svgList.set(svgList.size() - 1, svgCanvas.getClone());
			svgList.add(svgCanvas);
		}

		// if middle of the list
		else {
			System.out.println(":/");
			int index = svgList.indexOf(svgCanvas);
			svgList.set(index, svgCanvas.getClone());
			// remove elements after
			while (svgList.size() > index + 1) {
				svgList.remove(svgList.size() - 1);
			}

			svgList.add(svgCanvas);

		}

		setToolBar();
	}

	public void undo() {

		int index = svgList.indexOf(svgCanvas);
		svgCanvas = svgList.get(index - 1);
		svgCanvas.initListeners();
		svgCanvas.revalidate();
		svgCanvas.repaint();

		setToolBar();

	}

	public void redo() {

		int index = svgList.indexOf(svgCanvas);
		svgCanvas = svgList.get(index + 1);
		System.out.println(svgCanvas.getSVGDocument());
		svgCanvas.initListeners();
		svgCanvas.revalidate();
		svgCanvas.repaint();

		setToolBar();
	}

	protected String getSvgString() {

		// we write the SVG content in a string
		StringWriter writer = new StringWriter();
		String content = "";
		try {
			DOMUtilities.writeDocument(svgCanvas.getSVGDocument(), writer);
			content = writer.toString();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return content;
	}
}
