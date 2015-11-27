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

public class Metabolite implements Serializable {
	private String name;
	private String description;
	private boolean internal;

	public Metabolite(String name, String description, boolean internal) {
		this.description = description;
		this.name = name;
		this.internal = internal;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isInternal() {
		return internal;
	}

	public boolean equals(Object o) {
		return name.equals(((Metabolite) o).getName());
	}

	public void setInternal(boolean i) {
		this.internal = i;

	}

	public String toString() {
		return name;
	}

	public void setName(String newName) {
		this.name = newName;

	}

	public void setDescription(String description) {
		this.description = description;
	}

}
