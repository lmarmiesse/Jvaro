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

package model.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import view.GeneralFrame;

import model.MetabolicNetworkItf;
import model.Project;
import controler.MainControler;
import controler.Var;

public class ProjectFormat implements Format {

	Project project;
	MainControler controler;

	public ProjectFormat(MainControler controler) {
		this.controler = controler;
	}

	public MetabolicNetworkItf load(File[] file) throws IOException,
			ClassNotFoundException {

		System.out.println("loading Project file : "
				+ file[0].getAbsolutePath());

		try {
			FileInputStream fis = new FileInputStream(file[0].getAbsolutePath());

			ObjectInputStream ois = new ObjectInputStream(fis);

			project = (Project) ois.readObject();

			ois.close();
			fis.close();
		} catch (Exception e) {
			System.out.println("Could not load file");
			JOptionPane.showMessageDialog(null, "Could not load the file "
					+ file[0].getName(), "Loading error",
					JOptionPane.ERROR_MESSAGE);
		}

		// on change le nom du projet en enlevant l'extenssion

		if (project != null) {
			controler.createProject(project);

		}
		else{
			System.out.println("error");
		}

		GeneralFrame.getInstance(controler).getRightPanel().resetPanes();
		
		return null;
	}

	public void save(File file, MetabolicNetworkItf network, boolean message)
			throws IOException {

		String outputPath = file.getAbsolutePath();
		String outputName = file.getName();

		if (!file.getAbsolutePath().endsWith(Var.extension)) {
			outputPath += Var.extension;
			outputName += Var.extension;
		}

		// update project name
		controler.setProjectNameAndPath(
				outputName.substring(0, outputName.length() - 4), outputPath);

		FileOutputStream fos = new FileOutputStream(outputPath);

		ObjectOutputStream oos = new ObjectOutputStream(fos);

		oos.writeObject(controler.getProject());
		oos.flush();

		oos.close();
		fos.close();

		controler.notifyObservers();

		System.out.println("saving a Project file : " + outputPath);

		if (message) {
			JOptionPane.showMessageDialog(null,
					"Successfully saved project file " + outputName + " !",
					"Succes", JOptionPane.INFORMATION_MESSAGE);
		}

	}
}
