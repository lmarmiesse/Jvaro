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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

//download class : 
//download svg files and result files
public class Download {

	private JFileChooser fc = new JFileChooser();

	public Download(String downloadContent, String message,String extension) {
		
		//open file chooser
		UIManager.put("FileChooser.saveDialogTitleText", message);
		SwingUtilities.updateComponentTreeUI(fc);
		int returnVal = fc.showSaveDialog(fc);
		
		//if user select "ok"
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			
			boolean doIt = true;
			
			//if the file already exists
			if (file.isFile()) {
				if (JOptionPane.showConfirmDialog(null,
						"This file already exists, are you sure you want to delete it ?", "",
						JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
					doIt=false;
				}

			}
			//write the file
			if (doIt) {
				BufferedWriter out;
				try {
					
					String outName=file.getAbsolutePath();
					if (!outName.endsWith(extension)){
						outName+=extension;
					}
					
					out = new BufferedWriter(new FileWriter(new File(outName), false));

					out.write(downloadContent);

					out.close();

					JOptionPane.showMessageDialog(null, "Successfully saved "
							+ outName + " !", "Succes",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "Download cancelled", "Cancelled",
						JOptionPane.INFORMATION_MESSAGE);
				
			}
		}

	}
}
