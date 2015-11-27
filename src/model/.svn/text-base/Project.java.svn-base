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

package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Project implements Serializable {

	private MetabolicNetworkItf network;
	private String name;
	private String path;
	private Set<String> imagePaths = new HashSet<String>();
	private Map<String,String[]> resultsPaths = new HashMap<String,String[]>();
	

	public Project(String name) {
		this.name = name;
		this.network = new MetabolicNetwork();
	}

	public Project(String name, String path) {
		this.name = name;
		this.path = path;
		this.network = new MetabolicNetwork();
	}

	public boolean hasPath() {

		return path != null;
	}

	public void setNetwork(MetabolicNetworkItf network) {
		this.network = network;
	}

	public MetabolicNetworkItf getNetwork() {
		return network;
	}

	public String getName() {
		return name;
	}

	public String getPath() {

		return path;
	}

	public void setNameAndPath(String name, String path) {
		this.name = name;
		this.path = path;
	}

	public void addImagePath(String path) {
		imagePaths.add(path);
	}
	
	public void addResultsPath(String path,String[] command) {
		resultsPaths.put(path,command);
	}
	
	public void removeResultsPath(String path) {
		resultsPaths.remove(path);
	}
	
	public void removeImagePath(String path) {
		imagePaths.remove(path);
	}

	public Set<String> getImagePaths() {
		return imagePaths;
	}
	
	public Map<String,String[]> getResultsPaths() {
		return resultsPaths;
	}
}
