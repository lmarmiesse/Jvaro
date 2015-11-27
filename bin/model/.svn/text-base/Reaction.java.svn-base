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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Reaction implements Serializable {
	private String name;
	private Map<Metabolite, Double> reactants;// metabolite +  stoechimoetrie
	private Map<Metabolite, Double> products;
	private boolean reversible;

	public Reaction(String name, Map<Metabolite, Double> reactants,
			Map<Metabolite, Double> products, boolean reversible) {

		this.name = name;
		this.reactants = reactants;
		this.products = products;
		this.reversible = reversible;

	}

	public String getName() {
		return name;
	}

	public void setReversible(boolean r) {
		reversible = r;
	}

	public boolean isReversible() {
		return reversible;
	}

	public List<Object[]> getObjetReactantsTable() {
		DecimalFormat df = new DecimalFormat("0.###");
		List<Object[]> listeObj = new ArrayList<Object[]>();
		for (Entry<Metabolite, Double> entry : reactants.entrySet()) {
			listeObj.add(new Object[] { entry.getKey(),
					df.format(entry.getValue()).toString().replace(",", ".") });
		}
		return listeObj;
	}

	public List<Object[]> getProductsTable() {
		DecimalFormat df = new DecimalFormat("0.###");
		List<Object[]> listeObj = new ArrayList<Object[]>();
		for (Entry<Metabolite, Double> entry : products.entrySet()) {
			listeObj.add(new Object[] { entry.getKey(),
					df.format(entry.getValue()).toString().replace(",", ".") });
		}
		return listeObj;
	}

	public Map<Metabolite, Double> getReactants() {
		return reactants;
	}

	public Map<Metabolite, Double> getProducts() {
		return products;
	}

	public void addProduct(Metabolite m, Double d) {
		products.put(m, d);
	}

	public void addReactant(Metabolite m, Double d) {
		reactants.put(m, d);
	}

	public void delMetabolites() {
		reactants.clear();
		products.clear();
	}

	public String getStringReaction() {
	
		String stringReaction = "";
		// display only int when float number
		DecimalFormat df = new DecimalFormat("0.###");

		int indice = 0;
		for (Entry<Metabolite, Double> entry : reactants.entrySet()) {
			indice++;
			stringReaction += df.format(entry.getValue()) + " ";
			stringReaction += entry.getKey().getName() + " ";
			if (indice != reactants.size())
				stringReaction += "+ ";
		}

		stringReaction += " = ";

		indice = 0;
		for (Entry<Metabolite, Double> entry : products.entrySet()) {
			indice++;
			stringReaction += df.format(entry.getValue()) + " ";
			stringReaction += entry.getKey().getName() + " ";
			if (indice != products.size())
				stringReaction += "+ ";
		}
		return stringReaction;
	}

	public List<Metabolite> getListMetabolite() {
		List<Metabolite> list = new ArrayList<Metabolite>();

		for (Entry<Metabolite, Double> entry : reactants.entrySet()) {
			list.add(entry.getKey());
		}
		for (Entry<Metabolite, Double> entry : products.entrySet()) {
			list.add(entry.getKey());
		}

		return list;
	}

	public List<Metabolite> getListReactants() {
		List<Metabolite> list = new ArrayList<Metabolite>();

		for (Entry<Metabolite, Double> entry : reactants.entrySet()) {
			list.add(entry.getKey());
		}

		return list;
	}

	public List<Metabolite> getListProducts() {
		List<Metabolite> list = new ArrayList<Metabolite>();

		for (Entry<Metabolite, Double> entry : products.entrySet()) {
			list.add(entry.getKey());
		}

		return list;
	}

	public void setName(String name, String newName) {
		this.name = newName;

	}
	
	public String toString(){
		return this.getName();
	}
	

}
