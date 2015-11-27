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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MetabolicNetwork implements MetabolicNetworkItf, Serializable {

	private List<Metabolite> metabolites;
	private List<Reaction> reactions;
	private List<String> rules;

	public MetabolicNetwork() {
		metabolites = new ArrayList<Metabolite>();
		reactions = new ArrayList<Reaction>();
		rules = new ArrayList<String>();
	}

	public void addReaction(String name, Map<Metabolite, Double> reactants,
			Map<Metabolite, Double> products, boolean reversible) {
		name = formatName(name);
		if (!this.hasName(name)) {
			reactions.add(new Reaction(name, reactants, products, reversible));
		}
	}

	public void delReaction(String name) {
		name = formatName(name);
		Reaction toRemove = null;
		for (Reaction reac : reactions) {
			if (reac.getName().equals(name)) {
				toRemove = reac;
			}
		}

		if (toRemove != null) {
			System.out.println("Reaction " + toRemove.getName() + " deleted");
			reactions.remove(toRemove);

		}
	}

	public void delMetabolite(String name) {
		name = formatName(name);
		Metabolite toRemove = null;
		for (Metabolite met : metabolites) {
			if (met.getName().equals(name)) {
				toRemove = met;
			}
		}

		if (toRemove != null) {
			System.out.println("Metabolite " + toRemove.getName() + " deleted");
			metabolites.remove(toRemove);
		}

	}

	public boolean getReversible(String name) {
		name = formatName(name);
		return getReaction(name).isReversible();
	}

	public List<Reaction> getReactions() {

		return Collections.unmodifiableList(reactions);

	}

	public List<Metabolite> getMetabolites() {

		return Collections.unmodifiableList(metabolites);

	}

	public void AddMetabolite(String name, String description, boolean internal) {
		name = formatName(name);
		if (!this.hasName(name)) {
			metabolites.add(new Metabolite(name, description, internal));
		}
	}

	public void addRule(String rule) {
		rules.add(rule);
	}

	public void delRule(String rule) {
		rules.remove(rule);
	}

	public Reaction getReaction(String name) {
		name = formatName(name);
		for (Reaction reac : reactions) {
			if (reac.getName().equals(name)) {
				return reac;
			}
		}
		return null;
	}

	public Metabolite getMetabolite(String name) {
		name = formatName(name);
		for (Metabolite metab : metabolites) {
			if (metab.getName().equals(name)) {
				return metab;
			}
		}
		return null;
	}

	public void setReactionReversible(String name, boolean reversible) {
		for (Reaction reac : reactions) {
			if (reac.getName() == name) {
				reac.setReversible(reversible);
			}
		}
	}

	public void setReactionName(String name, String newName) {
		for (Reaction reac : reactions) {
			if (reac.getName() == name) {
				reac.setName(name, newName);
			}
		}
	}

	public void setMetaboInternal(String name, boolean i) {
		for (Metabolite metab : metabolites) {
			if (metab.getName() == name) {
				metab.setInternal(i);
			}
		}
	}

	public void setMetabolite(String oldName, String newName, String description) {
		for (Metabolite metab : metabolites) {
			if (metab.getName() == oldName) {
				metab.setName(newName);
				metab.setDescription(description);
			}
		}

	}

	public List<String> getRules() {
		return rules;
	}

	public void editRule(String oldRule, String newRule) {
		for (int i = 0; i < rules.size(); i++) {
			if (rules.get(i) == oldRule) {
				rules.set(i, newRule);
			}
		}

	}

	public boolean hasName(String name) {
		name = formatName(name);
		for (Reaction r : getReactions()) {
			if (r.getName().toLowerCase().equals(name.toLowerCase())) {
				return true;
			}
		}
		for (Metabolite m : getMetabolites()) {
			if (m.getName().toLowerCase().equals(name.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public String formatName(String name) {
		name = name.replaceAll("[^a-zA-Z 0-9]+", "_");
		name = name.replaceAll(" ", "_");
		if (Character.isDigit(name.charAt(0))) {
			name = "_" + name;
		}
		return name;
	}

}
