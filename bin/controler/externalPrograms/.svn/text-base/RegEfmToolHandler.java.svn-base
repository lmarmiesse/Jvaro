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

package controler.externalPrograms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.xml.stream.XMLStreamException;

import model.data.RegEfmToolFormat;
import model.data.SBMLFormat;

import org.sbml.jsbml.SBMLException;

import view.GeneralFrame;
import view.ResultsPanel;
import view.RightPanel;
import view.popup.LaunchRegEfmToolFrame;
import controler.MainControler;

public class RegEfmToolHandler extends Thread implements
		ExternalProgramsHandler {

	private MainControler controler;
	private String[] command;
	private String resultsName;
	private Process p;

	private boolean stopThread = false;

	
	public RegEfmToolHandler(MainControler controler) {
		this.controler = controler;
		//the analysis can't be running without metabolites
		// and reactions in the network
		if (controler.getListMetabos().size() == 0
				&& controler.getListReactions().size() == 0) {
			JOptionPane.showMessageDialog(null, "Your network is empty",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;

		}
		
		//show regEfmtool options frame
		LaunchRegEfmToolFrame efm = new LaunchRegEfmToolFrame(
				GeneralFrame.getInstance(controler));
		
		//if ok is selected by the user
		if (efm.okPressed()) {

			int indice = 0;
			while (true) {

				// until we find a non-existent file
				if (!new File("res/saved/" + controler.getProjectName()
						+ indice + ".res").isFile()
						&& !new File("res/" + controler.getProjectName()
								+ indice + ".res").isFile()) {
					break;
				}
				indice++;
			}
			String resultsName = controler.getProjectName();
			resultsName += indice;

			// generate regEfmTool files and SBML file
			try {
				new RegEfmToolFormat().save(new File("res/genFiles/"
						+ controler.getProjectName()), controler.getProject()
						.getNetwork(), false);

				new SBMLFormat().save(
						new File("res/genFiles/" + controler.getProjectName()),
						controler.getProject().getNetwork(), false);
			} catch (SBMLException | IOException | XMLStreamException e) {
				e.printStackTrace();
			}

			// collect the parameters
			Map<String, String> params = efm.getParameters();

			// lauch analysis

			String s;
			List<String> command = new ArrayList<String>();
			command.add("java");
			command.add("-Xmx"+params.get("ram")+"G");
			command.add("-jar");
			command.add("20120810_regEfmtool_2.0/regEfmtool.jar");
			command.add("-log");
			command.add("console");

			command.add("-level");
			command.add(params.get("level"));
			command.add("-format");
			command.add(params.get("format"));
			command.add("-kind");
			command.add(params.get("kind"));
			if (params.get("kind") == "stoichiometry") {
				command.add("-stoich");
				command.add("res/genFiles/" + controler.getProjectName()
						+ ".sfile");
				command.add("-rev");
				command.add("res/genFiles/" + controler.getProjectName()
						+ ".rvfile");
				command.add("-meta");
				command.add("res/genFiles/" + controler.getProjectName()
						+ ".mfile");
				command.add("-reac");
				command.add("res/genFiles/" + controler.getProjectName()
						+ ".rfile");
			} else if (params.get("kind") == "sbml") {
				command.add("-in");

				String xmlFileName = controler.getProjectName();
				if (!xmlFileName.endsWith(".xml")) {
					xmlFileName += ".xml";
				}

				command.add("res/genFiles/" + xmlFileName);
				command.add("-ext");
				command.add("external");
			}
			if (efm.geneRule()) {
				command.add("-generule");
				command.add("res/genFiles/" + controler.getProjectName()
						+ ".grfile");
			}
			command.add("-compression");
			command.add(params.get("comp"));
			command.add("-out");
			command.add(params.get("out"));
			command.add("res/" + resultsName + ".res");
			command.add("-adjacency-method");
			command.add(params.get("adj"));
			command.add("-arithmetic");
			command.add(params.get("ari"));
			command.add("-precision");
			command.add(params.get("pre"));
			command.add("-normalize");
			command.add(params.get("norm"));
			command.add("-maxthreads");
			command.add(params.get("maxthreads"));

			System.out.println(command);

			String[] commandArray = new String[command.size()];
			for (int i = 0; i < command.size(); i++) {
				commandArray[i] = command.get(i);
			}

			this.command = commandArray;
			this.resultsName = resultsName;

			this.start();

		}

	}

	public void run() {
		// initialize the frame of loading
		final JFrame running = new JFrame();

		running.setLayout(new BorderLayout());
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		running.setTitle("regEfmtool process is running");

		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));

		JTextArea logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setBackground(new Color(0, 0, 0));
		logArea.setForeground(new Color(255, 255, 255));

		center.add(new JScrollPane(logArea));

		running.add(progressBar, BorderLayout.PAGE_START);
		running.add(center, BorderLayout.CENTER);
		JButton cancelAnalysis = new JButton("cancel analysis");

		cancelAnalysis.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// we kill the process
				p.destroy();
				stop();
				running.dispose();
				JOptionPane.showMessageDialog(null, "Analysis canceled", "",
						JOptionPane.INFORMATION_MESSAGE);
			}

		});

		running.add(cancelAnalysis, BorderLayout.PAGE_END);
		running.setSize(600, 600);
		running.setLocationRelativeTo(null);
		running.setVisible(true);

		String s;

		try {

			String logContent = "";

			p = Runtime.getRuntime().exec(command);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			// read the output

			while ((s = stdInput.readLine()) != null) {
				logContent += s + "\n";
				logArea.setText(logContent);
				logArea.setCaretPosition(logArea.getText().length());
			}
			// read any errors

			while ((s = stdError.readLine()) != null) {
				logContent += s + "\n";
				logArea.setText(logContent);
				logArea.setCaretPosition(logArea.getText().length());
			}

			// we wait for the process to be over
			p.waitFor();

			// we save the log content

			BufferedWriter out = new BufferedWriter(new FileWriter(new File(
					"res/" + resultsName + ".log"), true));

			out.write(logContent);
			out.close();

			display();
			running.dispose();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void display() {
		ResultsPanel results = new ResultsPanel(controler, command, resultsName);

		GeneralFrame frame = GeneralFrame.getInstance(controler);

		JTabbedPane jtp = frame.getRightPanel();
		((RightPanel) jtp).addResPane(results, "Results");
		jtp.setSelectedIndex(jtp.getComponentCount() - 2);
	}

}
