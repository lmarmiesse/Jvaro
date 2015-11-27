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

package controler.load;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import model.data.MetatoolFormat;
import model.data.ProjectFormat;
import model.data.RegEfmToolFormat;
import model.data.SBMLFormat;
import controler.ExtensionFilter;
import controler.MainControler;
import controler.Var;

//load file depending on the type 
public class MenuOpenListener implements ActionListener {

	private MainControler controler;
	private JMenuItem openProject;
	private JMenuItem openSBML;
	private JMenuItem openMetatool;
	private JMenuItem openRegefmtool;
	private JButton button;
	private JFileChooser fc;
	List<String> extensions = new ArrayList<String>();
	private String type = "";

	public MenuOpenListener(MainControler controler, JMenuItem openProject,
			JMenuItem openSBML, JMenuItem openMetatool, JMenuItem openRegefmtool) {
		this.controler = controler;
		this.openProject = openProject;
		this.openSBML = openSBML;
		this.openMetatool = openMetatool;
		this.openRegefmtool = openRegefmtool;
		fc = new JFileChooser();
	}

	public MenuOpenListener(MainControler controler, JButton button) {
		this.controler = controler;
		this.button = button;
		fc = new JFileChooser();
		
	}

	public void actionPerformed(ActionEvent e) {
		fc.setMultiSelectionEnabled(true);

		//The loader is defined by the clicked button
		if (e.getSource() == openProject || e.getSource() == button) {
			type="prj";
			extensions.add(Var.extension);

			fc.setFileFilter(new ExtensionFilter(extensions, "Project file"));

			controler.setLoader(new ProjectFormat(controler));
			UIManager.put("FileChooser.openDialogTitleText",
					"Open a project file");
		}
		if (e.getSource() == openSBML) {
			type="sbml";
			extensions.add(".xml");

			fc.setFileFilter(new ExtensionFilter(extensions, "SBML files"));

			controler.setLoader(new SBMLFormat());
			UIManager
					.put("FileChooser.openDialogTitleText", "Open a SBML file");
		}
		if (e.getSource() == openMetatool) {
			type="met";
			extensions.add(".dat");
			extensions.add(".txt");

			fc.setFileFilter(new ExtensionFilter(extensions,
					"Metatool files (.dat or .txt)"));

			controler.setLoader(new MetatoolFormat());
			UIManager.put("FileChooser.openDialogTitleText",
					"Open a Metatool file");
		}
		
		if (e.getSource() == openRegefmtool) {

			extensions.add(".rfile");
			extensions.add(".sfile");
			extensions.add(".rvfile");
			extensions.add(".mfile");
			extensions.add(".grfile");

			fc.setFileFilter(new ExtensionFilter(extensions,
					"regEfmTool Files"));
			
			
			controler.setLoader(new RegEfmToolFormat());
			UIManager.put("FileChooser.openDialogTitleText",
					"Open regEfmTool files");
		}

		SwingUtilities.updateComponentTreeUI(fc);
		int returnVal = fc.showOpenDialog(null);

		// if the file has the right extension
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] file = fc.getSelectedFiles();
			
			System.out.println(file);

			try {
				controler.loadNetwork(file);
				
				if (type!=""){
					
					//first we open the file to get the first 4 lines
					BufferedReader in = new BufferedReader(new FileReader(new File(
							"recent.txt")));
					String input = in.readLine();
					String content="";
					
					int nbligne=1;
					while (input != null) {
					
						//we only get the first 4 lines
						if (nbligne>4){
							break;
						}
						
						content+=input+"\n";
						
						nbligne++;
						input = in.readLine();
					}
					
					BufferedWriter out = new BufferedWriter(new FileWriter(new File(
							"recent.txt"),false));
					
					out.write(file[0].getAbsolutePath()+" "+type+"\n");
					out.write(content);
					
					out.close();
				}	
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		fc = new JFileChooser();
		extensions.clear();
	}

}
