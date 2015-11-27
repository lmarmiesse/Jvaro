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

import java.io.File;
import java.util.List;

import javax.swing.filechooser.FileFilter;


//test the extension of files
public class ExtensionFilter extends FileFilter {

	private List<String> extension;
	private String description;

	public ExtensionFilter(List<String> extension, String description) {
		this.extension = extension;
		this.description = description;

	}

	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		String nomFichier = f.getName().toLowerCase();

		for (String ext : extension) {
			if (nomFichier.endsWith(ext))
				return true;
		}
		return false;
	}

	public String getDescription() {
		return description;
	}

}
