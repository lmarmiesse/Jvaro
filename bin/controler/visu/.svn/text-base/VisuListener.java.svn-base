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

package controler.visu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import model.data.ExportTlp;
import view.GeneralFrame;
import view.RightPanel;
import view.VisualizationPanel;
import view.popup.VisuChoiceFrame;
import controler.MainControler;

public class VisuListener implements ActionListener {

	private MainControler controler;

	public VisuListener(MainControler controler) {

		this.controler = controler;
	}

	public void actionPerformed(ActionEvent arg0) {
		
		if (controler.getListMetabos().size()==0 && controler.getListReactions().size()==0){
			JOptionPane.showMessageDialog(null,
					"Your network is empty", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
			
		}
		
		VisuChoiceFrame vcf = new VisuChoiceFrame(
				GeneralFrame.getInstance(controler), controler);
		
		if (vcf.okPressed()) {

			System.out.println(vcf.getLayoutAlgorithm());

			// generate tlp file

			int i = 0;
			while (true) {

				// until we find a tlp file non-existent
				if (!new File("tulip/tlp/saved/" + controler.getProjectName()
						+ i + ".tlp").isFile()
						&& !new File("tulip/tlp/" + controler.getProjectName()
								+ i + ".tlp").isFile()) {
					break;
				}
				i++;
			}
			//we create the name of the file
			String tlpName = controler.getProjectName();
			tlpName += i;
			tlpName += ".tlp";

			//the tlp file is created
			try {
				ExportTlp et = new ExportTlp("tulip/tlp/" + tlpName,
						vcf.getSelectedMetabolites(),
						vcf.getSelectedReactions());
				System.out.println(tlpName + " file created");
			} catch (IOException e) {
				e.printStackTrace();
			}

			String s;
			String outName = tlpName + ".svg";

			String[] command = new String[5];
			command[0] = "python";
			command[1] = "tulip/svgCreator.py";
			command[2] = tlpName;
			command[3] = vcf.getLayoutAlgorithm();
			command[4] = outName;
			try {

				ProcessBuilder pb = new ProcessBuilder(command);
				String OS = System.getProperty("os.name").toLowerCase();
				Map<String, String> env = pb.environment();

				if (OS.indexOf("mac") >= 0) {
					System.out.println("Mac");
					env.put("DYLD_LIBRARY_PATH", ":tulip/libMac/python/");
					System.out.println(env.get("DYLD_LIBRARY_PATH"));

					command[1] = "tulip/svgCreatorMac.py";
					pb.command(command);
				}

				if (OS.indexOf("win") >= 0) {
					System.out.println("Windows");
					env.put("Path", env.get("Path") + ";tulip\\bin\\");

					command[0] = "C:\\Python27\\python";
					command[1] = "tulip/svgCreatorWin.py";

					env = pb.environment();
					System.out.println(env.get("Path"));
					pb.command(command);
				}

				System.out.println("command");
				for (String a : command) {
					System.out.println(a);
				}

				Process p = pb.start();

				BufferedReader stdInput = new BufferedReader(
						new InputStreamReader(p.getInputStream()));
				BufferedReader stdError = new BufferedReader(
						new InputStreamReader(p.getErrorStream()));

				// read the output

				while ((s = stdInput.readLine()) != null) {
					System.out.println(s);
				}
				// read any errors

				while ((s = stdError.readLine()) != null) {
					System.out.println(s);
				}

				// add panel with svg
				VisualizationPanel visuPanel = new VisualizationPanel(new File(
						"tulip/svg/" + outName), controler);
				JTabbedPane jtp = GeneralFrame.getInstance(controler)
						.getRightPanel();
				((RightPanel) jtp).addVisuPane(visuPanel, "Visualization");
				jtp.setSelectedIndex(jtp.getComponentCount() - 2);

			} catch (IOException e) {
				JOptionPane
						.showMessageDialog(
								null,
								"Error with python, check your installation. Make sure you have python 2.7 installed.",
								"Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}

		}
	}
}
