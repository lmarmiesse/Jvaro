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

package controler.save;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.xml.stream.XMLStreamException;

import model.data.MetatoolFormat;
import model.data.ProjectFormat;
import model.data.RegEfmToolFormat;
import model.data.SBMLFormat;
import model.data.TlpFormat;

import org.sbml.jsbml.SBMLException;

import controler.MainControler;

public class MenuSaveListener implements ActionListener {

	private MainControler controler;
	private JMenuItem saveProject;
	private JMenuItem saveSBML;
	private JMenuItem saveMetatool;
	private JMenuItem saveRegEfmTool;
	private JMenuItem saveTlp;
	private JMenuItem save;
	private JFileChooser fc = new JFileChooser();

	public MenuSaveListener(MainControler controler, JMenuItem saveProject,
			JMenuItem saveSBML, JMenuItem saveMetatool,
			JMenuItem saveRegEfmTool, JMenuItem saveTlp, JMenuItem save) {
		this.controler = controler;
		this.saveProject = saveProject;
		this.saveSBML = saveSBML;
		this.save = save;
		this.saveMetatool = saveMetatool;
		this.saveRegEfmTool = saveRegEfmTool;
		this.saveTlp = saveTlp;
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == save) {
			try {
				controler.saveProject();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		// The loader is defined by the clicked button
		if (e.getSource() == saveProject) {
			controler.setSaver(new ProjectFormat(controler));
			UIManager.put("FileChooser.saveDialogTitleText",
					"Save as a project file");
		}
		if (e.getSource() == saveSBML) {
			controler.setSaver(new SBMLFormat());
			UIManager.put("FileChooser.saveDialogTitleText",
					"Save as a SBML file");
		}
		if (e.getSource() == saveMetatool) {
			controler.setSaver(new MetatoolFormat());
			UIManager.put("FileChooser.saveDialogTitleText",
					"Save as a Metatoolfile");
		}
		if (e.getSource() == saveRegEfmTool) {
			controler.setSaver(new RegEfmToolFormat());
			UIManager.put("FileChooser.saveDialogTitleText",
					"Save as a RegEfmToll files");
		}
		if (e.getSource() == saveTlp) {
			controler.setSaver(new TlpFormat());
			UIManager
					.put("FileChooser.saveDialogTitleText", "Save as tlp file");
		}
		SwingUtilities.updateComponentTreeUI(fc);
		int returnVal = fc.showSaveDialog(fc);
		// if the extension of the file is right
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();

			try {
				controler.saveNetworkAs(file);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (SBMLException e1) {
				e1.printStackTrace();
			} catch (XMLStreamException e1) {
				e1.printStackTrace();
			}

		}

	}

}
