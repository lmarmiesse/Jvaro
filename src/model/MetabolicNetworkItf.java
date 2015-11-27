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

import java.util.List;
import java.util.Map;

public interface MetabolicNetworkItf {
	public void AddMetabolite(String name, String description, boolean internal);

	public void addReaction(String name, Map<Metabolite, Double> reactants,
			Map<Metabolite, Double> products, boolean reversible);

	public void delReaction(String name);

	public List<Reaction> getReactions();

	public List<Metabolite> getMetabolites();

	public Metabolite getMetabolite(String name);

	public Reaction getReaction(String name);

	public void setReactionReversible(String name, boolean r);

	public void setMetaboInternal(String name, boolean i);

	public void delMetabolite(String name);

	public void setMetabolite(String oldName, String newName, String description);

	public void setReactionName(String name, String newName);

	public void addRule(String rule);

	public void delRule(String rule);

	public List<String> getRules();

	public void editRule(String oldRule, String newRule);

	public boolean hasName(String name);

	public String formatName(String name);
}
