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

package controler;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import model.ElementaryMode;
import model.Reaction;

public class DownloadContentThread extends Thread {

	private List<ElementaryMode> modes;
	private MainControler controler;
	private String[] command;
	private String logContent;

	public DownloadContentThread(List<ElementaryMode> modes,
			MainControler controler, String[] command, String log) {
		this.modes = modes;
		this.controler = controler;
		this.command = command;
		this.logContent = log;
	}

	public void run() {
		String content = "";

		content += "Project name : " + controler.getProjectName();
		content += "\n";
		content += "\n";

		String stringCommand = "";
		for (int i = 0; i < command.length; i++) {
			stringCommand += command[i] + " ";
		}

		content += "command : " + stringCommand;
		content += "\n";
		content += "\n";
		content += "log : ";
		content += "\n";
		content += logContent;
		content += "\n";
		content += "\n";
		content += "Modes :";
		content += "\n";

		final JFrame running = new JFrame();

		running.setLayout(new BorderLayout());
		JProgressBar progressBar = new JProgressBar(0, modes.size());
		progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(200, 30));
		running.setTitle("Constructing file");
		JButton cancel = new JButton("cancel");

		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				running.dispose();
				stop();
			}
		});

		running.add(new JLabel("File beeing constructed"),
				BorderLayout.PAGE_START);

		running.add(progressBar, BorderLayout.CENTER);
		running.add(cancel, BorderLayout.SOUTH);

		running.pack();
		running.setLocationRelativeTo(null);
		running.setVisible(true);

		// long time1 = 0;
		// long time2 = System.currentTimeMillis();
		for (int i = 0; i < modes.size(); i++) {

			int updateFrequency = 100;

			if (i % updateFrequency == 0) {

				// long remainingLoops=(modes.size()-i)/updateFrequency;

				progressBar.setValue(i);
				// time1=time2;
				// time2=System.currentTimeMillis();
				// long time = (time2-time1)*remainingLoops;
				// timeLeft.setText(String.valueOf(time/1000));
			}

			String mode = "";
			mode += "mode number " + (i + 1);
			mode += "\n";
			Map<Reaction, Double> map = modes.get(i).getContent();
			for (Reaction key : map.keySet()) {

				mode += map.get(key) + " " + key.getName() + " "
						+ key.getStringReaction();
				mode += "\n";

			}
			mode += "\n";
			content += mode;
		}
		running.dispose();

		new Download(content, "Download the results", ".txt");

	}

}
