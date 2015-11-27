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

import java.io.File;

import javax.swing.UIManager;

import view.GeneralFrame;
import controler.MainControler;

public class Main {

	static GeneralFrame frame;

	public static void main(String[] args) {

		// Changing the default appearance
		String system_lf = UIManager.getSystemLookAndFeelClassName()
				.toLowerCase();
		if (system_lf.contains("metal")) {
			try {
				UIManager
						.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch (Exception e) {
			}
		} else {
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e) {
			}
		}

		// Main
		MainControler controler = new MainControler();
		frame = GeneralFrame.getInstance(controler);
		controler.addObserver(frame);

		String files;
		File folder = new File("./res/genFiles/");
		File[] listOfFiles = folder.listFiles();

		// delete generated files of the previous session
		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();
				listOfFiles[i].deleteOnExit();
			}
		}

		folder = new File("./res/");
		listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();
				listOfFiles[i].deleteOnExit();
			}
		}

	}

}
